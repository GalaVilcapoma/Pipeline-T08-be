# ============================================================
# AVSA — Seed de Agroquímicos via API REST
# Ejecutar DESPUÉS de que el backend esté corriendo en :8085
# Uso: .\seed-agrochemicals.ps1
# ============================================================

$baseUrl = "http://localhost:8085/v1/api/agrochemicals"
$headers = @{ "Content-Type" = "application/json" }

# Verificar que el backend esté corriendo
try {
    $check = Invoke-RestMethod -Uri $baseUrl -Method GET -TimeoutSec 5
    Write-Host "✅ Backend conectado. Registros actuales: $($check.Count)"
    if ($check.Count -ge 5) {
        Write-Host "ℹ️  Ya existen $($check.Count) agroquímicos. Seed omitido."
        exit 0
    }
} catch {
    Write-Host "❌ No se puede conectar al backend en $baseUrl"
    Write-Host "   Asegúrate de que Docker y el backend estén corriendo."
    exit 1
}

$agrochemicals = @(
    @{
        commercialName           = "Confidor 350 SC"
        activeIngredient         = "Imidacloprid"
        category                 = "INSECTICIDE"
        senasaRegistrationNumber = "SENASA-001-2024"
        registrationExpiry       = "2027-06-30"
        maxDose                  = 0.5
        waitingPeriodDays        = 7
        manufacturer             = "Bayer CropScience"
    },
    @{
        commercialName           = "Mancozeb 80 WP"
        activeIngredient         = "Mancozeb"
        category                 = "FUNGICIDE"
        senasaRegistrationNumber = "SENASA-002-2024"
        registrationExpiry       = "2027-12-31"
        maxDose                  = 2.0
        waitingPeriodDays        = 14
        manufacturer             = "Dow AgroSciences"
    },
    @{
        commercialName           = "Roundup 480 SL"
        activeIngredient         = "Glifosato"
        category                 = "HERBICIDE"
        senasaRegistrationNumber = "SENASA-003-2024"
        registrationExpiry       = "2028-08-15"
        maxDose                  = 3.0
        waitingPeriodDays        = 0
        manufacturer             = "Monsanto"
    },
    @{
        commercialName           = "Karate 2.5 WG"
        activeIngredient         = "Lambda-cihalotrina"
        category                 = "INSECTICIDE"
        senasaRegistrationNumber = "SENASA-004-2024"
        registrationExpiry       = "2027-03-20"
        maxDose                  = 0.3
        waitingPeriodDays        = 7
        manufacturer             = "Syngenta"
    },
    @{
        commercialName           = "Nitrofoska Foliar"
        activeIngredient         = "NPK 12-4-6"
        category                 = "FERTILIZER"
        senasaRegistrationNumber = "SENASA-005-2024"
        registrationExpiry       = "2028-01-01"
        maxDose                  = 5.0
        waitingPeriodDays        = 0
        manufacturer             = "COMPO Expert"
    },
    @{
        commercialName           = "Score 250 EC"
        activeIngredient         = "Difenoconazol"
        category                 = "FUNGICIDE"
        senasaRegistrationNumber = "SENASA-006-2024"
        registrationExpiry       = "2026-06-05"
        maxDose                  = 0.5
        waitingPeriodDays        = 14
        manufacturer             = "Syngenta"
    },
    @{
        commercialName           = "Bioestimulante Stoller"
        activeIngredient         = "Aminoacidos + Micronutrientes"
        category                 = "BIOSTIMULANT"
        senasaRegistrationNumber = "SENASA-007-2024"
        registrationExpiry       = "2028-06-30"
        maxDose                  = 3.0
        waitingPeriodDays        = 0
        manufacturer             = "Stoller"
    },
    @{
        commercialName           = "Dithane M-45"
        activeIngredient         = "Mancozeb"
        category                 = "FUNGICIDE"
        senasaRegistrationNumber = "SENASA-008-2024"
        registrationExpiry       = "2027-09-30"
        maxDose                  = 2.5
        waitingPeriodDays        = 14
        manufacturer             = "Dow AgroSciences"
    },
    @{
        commercialName           = "Actara 25 WG"
        activeIngredient         = "Tiametoxam"
        category                 = "INSECTICIDE"
        senasaRegistrationNumber = "SENASA-009-2022"
        registrationExpiry       = "2025-12-31"
        maxDose                  = 0.2
        waitingPeriodDays        = 7
        manufacturer             = "Syngenta"
    },
    @{
        commercialName           = "Vydate 24 SL"
        activeIngredient         = "Oxamil"
        category                 = "INSECTICIDE"
        senasaRegistrationNumber = "SENASA-010-2021"
        registrationExpiry       = "2024-06-30"
        maxDose                  = 1.0
        waitingPeriodDays        = 21
        manufacturer             = "DuPont"
    }
)

$created = 0
$failed  = 0

foreach ($item in $agrochemicals) {
    $body = $item | ConvertTo-Json
    try {
        $result = Invoke-RestMethod -Uri $baseUrl -Method POST -Headers $headers -Body $body
        Write-Host "  ✅ Creado: $($result.commercialName) (ID: $($result.id))"
        $created++
    } catch {
        Write-Host "  ❌ Error creando $($item.commercialName): $_"
        $failed++
    }
}

# Eliminar lógicamente los últimos 2 (Actara y Vydate)
Write-Host ""
Write-Host "🗑️  Desactivando Actara 25 WG y Vydate 24 SL..."
$all = Invoke-RestMethod -Uri $baseUrl -Method GET
$toDeactivate = $all | Where-Object { $_.senasaRegistrationNumber -in @("SENASA-009-2022","SENASA-010-2021") }
foreach ($item in $toDeactivate) {
    try {
        Invoke-RestMethod -Uri "$baseUrl/$($item.id)/delete" -Method PATCH | Out-Null
        Write-Host "  ✅ Desactivado: $($item.commercialName)"
    } catch {
        Write-Host "  ⚠️  No se pudo desactivar $($item.commercialName): $_"
    }
}

Write-Host ""
Write-Host "============================================"
Write-Host "✅ Seed completado: $created creados, $failed errores"
Write-Host "   8 activos + 2 inactivos en la BD"
Write-Host "============================================"
