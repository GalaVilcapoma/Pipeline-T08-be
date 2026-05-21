-- ============================================================
-- AVSA Cañete — Seed Data: Catálogo de Agroquímicos
-- Columnas según modelo JPA: Agrochemical.java
-- Ejecutar DESPUÉS de que Spring Boot haya creado la tabla
-- ============================================================

USE avsa_db;
GO

-- Limpiar datos previos si existen (para re-ejecución segura)
DELETE FROM agrochemicals WHERE senasa_registration_number LIKE 'SENASA-00%-2024'
   OR senasa_registration_number LIKE 'SENASA-00%-2023'
   OR senasa_registration_number LIKE 'SENASA-00%-2022';
GO

-- ── 10 Agroquímicos de ejemplo ────────────────────────────────────────────
-- Columnas del modelo JPA:
--   commercial_name, active_ingredient, category,
--   senasa_registration_number, registration_expiry,
--   max_dose, waiting_period_days, manufacturer,
--   active, created_at, updated_at, deleted_at, restored_at

INSERT INTO agrochemicals
  (commercial_name, active_ingredient, category,
   senasa_registration_number, registration_expiry,
   max_dose, waiting_period_days, manufacturer,
   active, created_at)
VALUES
-- 1. Insecticida activo
('Confidor 350 SC',
 'Imidacloprid',
 'INSECTICIDE',
 'SENASA-001-2024', '2027-06-30',
 0.5000, 7, 'Bayer CropScience',
 1, GETDATE()),

-- 2. Fungicida activo
('Mancozeb 80 WP',
 'Mancozeb',
 'FUNGICIDE',
 'SENASA-002-2024', '2027-12-31',
 2.0000, 14, 'Dow AgroSciences',
 1, GETDATE()),

-- 3. Herbicida activo
('Roundup 480 SL',
 'Glifosato',
 'HERBICIDE',
 'SENASA-003-2024', '2028-08-15',
 3.0000, 0, 'Monsanto',
 1, GETDATE()),

-- 4. Insecticida activo
('Karate 2.5 WG',
 'Lambda-cihalotrina',
 'INSECTICIDE',
 'SENASA-004-2024', '2027-03-20',
 0.3000, 7, 'Syngenta',
 1, GETDATE()),

-- 5. Fertilizante activo
('Nitrofoska Foliar',
 'NPK 12-4-6',
 'FERTILIZER',
 'SENASA-005-2024', '2028-01-01',
 5.0000, 0, 'COMPO Expert',
 1, GETDATE()),

-- 6. Fungicida activo (vence pronto — dentro de 30 días)
('Score 250 EC',
 'Difenoconazol',
 'FUNGICIDE',
 'SENASA-006-2024', '2026-06-05',
 0.5000, 14, 'Syngenta',
 1, GETDATE()),

-- 7. Bioestimulante activo
('Bioestimulante Stoller',
 'Aminoácidos + Micronutrientes',
 'BIOSTIMULANT',
 'SENASA-007-2024', '2028-06-30',
 3.0000, 0, 'Stoller',
 1, GETDATE()),

-- 8. Fungicida activo
('Dithane M-45',
 'Mancozeb',
 'FUNGICIDE',
 'SENASA-008-2024', '2027-09-30',
 2.5000, 14, 'Dow AgroSciences',
 1, GETDATE()),

-- 9. Insecticida INACTIVO (eliminado lógicamente)
('Actara 25 WG',
 'Tiametoxam',
 'INSECTICIDE',
 'SENASA-009-2022', '2025-12-31',
 0.2000, 7, 'Syngenta',
 0, GETDATE()),

-- 10. Insecticida INACTIVO (registro vencido)
('Vydate 24 SL',
 'Oxamil',
 'INSECTICIDE',
 'SENASA-010-2021', '2024-06-30',
 1.0000, 21, 'DuPont',
 0, GETDATE());
GO

-- Registrar deleted_at para los inactivos
UPDATE agrochemicals
SET deleted_at = DATEADD(day, -30, GETDATE())
WHERE senasa_registration_number IN ('SENASA-009-2022', 'SENASA-010-2021');
GO

-- Verificar resultado
SELECT
  id,
  commercial_name,
  category,
  active,
  registration_expiry,
  created_at
FROM agrochemicals
ORDER BY id;
GO

PRINT '✅ 10 agroquímicos insertados correctamente (8 activos, 2 inactivos).';
GO
