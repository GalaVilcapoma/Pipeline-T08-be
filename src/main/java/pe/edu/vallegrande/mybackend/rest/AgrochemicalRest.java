package pe.edu.vallegrande.mybackend.rest;

<<<<<<< HEAD
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import pe.edu.vallegrande.mybackend.model.Agrochemical;
import pe.edu.vallegrande.mybackend.service.AgrochemicalService;
=======
import pe.edu.vallegrande.mybackend.model.Agrochemical;
import pe.edu.vallegrande.mybackend.service.AgrochemicalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
>>>>>>> c68b25f (feact(backend): general)

import java.util.List;
import java.util.Map;

<<<<<<< HEAD
@RestController
@RequestMapping("/v1/api/agrochemicals")
@CrossOrigin(origins = "*")
@Tag(name = "Agrochemical API", description = "Agrochemical catalog with SENASA validation")
public class AgrochemicalRest {

    @Autowired
    private AgrochemicalService agrochemicalService;

    // GET /v1/api/agrochemicals
    @GetMapping
    public List<Agrochemical> findAll() {
        return agrochemicalService.findAll();
    }

    // GET /v1/api/agrochemicals/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Agrochemical> findById(@PathVariable Long id) {
        return agrochemicalService.findById(id)
=======
/**
 * REST Controller — Maestro: Catálogo de Agroquímicos
 *
 * Base URL: /v1/api/agrochemicals
 *
 * ✅ POST   /              → Crear
 * ✅ PUT    /{id}          → Editar
 * ✅ PATCH  /{id}/delete   → Eliminar lógico  (active=false, deletedAt=now)
 * ✅ PATCH  /{id}/restore  → Restaurar lógico (active=true,  restoredAt=now)
 * ✅ GET    /              → Listar todos
 * ✅ GET    /active        → Listar activos
 * ✅ GET    /inactive      → Listar inactivos
 * ✅ GET    /{id}          → Buscar por ID
 */
@RestController
@RequestMapping("/v1/api/agrochemicals")
@CrossOrigin(origins = "*")
@Tag(name = "Agrochemicals — Master", description = "")
public class AgrochemicalRest {

    @Autowired
    private AgrochemicalService service;

    // ── Consultas ─────────────────────────────────────────────────────────

    @GetMapping
    @Operation(
        summary = "Listar todos los agroquímicos",
        description = "Retorna todos los registros (activos e inactivos)"
    )
    public ResponseEntity<List<Agrochemical>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/active")
    @Operation(
        summary = "Listar agroquímicos activos",
        description = "Retorna solo los registros con active = true"
    )
    public ResponseEntity<List<Agrochemical>> findAllActive() {
        return ResponseEntity.ok(service.findAllActive());
    }

    @GetMapping("/inactive")
    @Operation(
        summary = "Listar agroquímicos inactivos (eliminados lógicamente)",
        description = "Retorna solo los registros con active = false"
    )
    public ResponseEntity<List<Agrochemical>> findAllInactive() {
        return ResponseEntity.ok(service.findAllInactive());
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Buscar agroquímico por ID",
        description = "Retorna el registro con el ID especificado"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Registro encontrado"),
        @ApiResponse(responseCode = "404", description = "Registro no encontrado", content = @Content)
    })
    public ResponseEntity<Agrochemical> findById(
            @Parameter(description = "ID del agroquímico", required = true)
            @PathVariable Long id) {
        return service.findById(id)
>>>>>>> c68b25f (feact(backend): general)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

<<<<<<< HEAD
    // GET /v1/api/agrochemicals/active-select
    @GetMapping("/active-select")
    public List<Agrochemical> getActiveForSelection() {
        return agrochemicalService.getActiveForSelection();
    }

    // GET /v1/api/agrochemicals/expiring-soon
    @GetMapping("/expiring-soon")
    public List<Agrochemical> getExpiringSoon() {
        return agrochemicalService.getExpiringSoon();
    }

    // POST /v1/api/agrochemicals
    @PostMapping
    public Agrochemical save(@RequestBody Agrochemical agrochemical) {
        return agrochemicalService.save(agrochemical);
    }

    // PUT /v1/api/agrochemicals/{id}
    @PutMapping("/{id}")
    public Agrochemical update(@PathVariable Long id, @RequestBody Agrochemical agrochemical) {
        return agrochemicalService.update(id, agrochemical);
    }

    // PATCH /v1/api/agrochemicals/{id}/senasa-status
    @PatchMapping("/{id}/senasa-status")
    public Agrochemical updateSenasaStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return agrochemicalService.updateSenasaStatus(id, body.get("status"));
=======
    // ── ✅ Crear (POST) ───────────────────────────────────────────────────

    @PostMapping
    @Operation(
        summary = "Crear agroquímico (POST)",
        description = "Crea un nuevo agroquímico. Se registra automáticamente createdAt con la fecha-hora actual."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Agroquímico creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content)
    })
    public ResponseEntity<Agrochemical> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Datos del agroquímico a crear",
                required = true,
                content = @Content(schema = @Schema(example = """
                    {
                      "commercialName": "Confidor 350 SC",
                      "activeIngredient": "Imidacloprid",
                      "category": "INSECTICIDE",
                      "senasaRegistrationNumber": "SENASA-001-2024",
                      "registrationExpiry": "2027-06-30",
                      "maxDose": 0.5,
                      "waitingPeriodDays": 7,
                      "manufacturer": "Bayer CropScience"
                    }
                    """))
            )
            @RequestBody Agrochemical agrochemical) {
        Agrochemical created = service.create(agrochemical);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // ── ✅ Editar (PUT) ───────────────────────────────────────────────────

    @PutMapping("/{id}")
    @Operation(
        summary = "Editar agroquímico (PUT)",
        description = "Actualiza un agroquímico existente. Se registra automáticamente updatedAt con la fecha-hora actual."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Agroquímico actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Registro no encontrado", content = @Content)
    })
    public ResponseEntity<?> update(
            @Parameter(description = "ID del agroquímico a editar", required = true)
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Nuevos datos del agroquímico",
                required = true,
                content = @Content(schema = @Schema(example = """
                    {
                      "commercialName": "Confidor 350 SC Actualizado",
                      "activeIngredient": "Imidacloprid",
                      "category": "INSECTICIDE",
                      "senasaRegistrationNumber": "SENASA-001-2025",
                      "registrationExpiry": "2028-06-30",
                      "maxDose": 0.6,
                      "waitingPeriodDays": 10,
                      "manufacturer": "Bayer CropScience"
                    }
                    """))
            )
            @RequestBody Agrochemical agrochemical) {
        try {
            return ResponseEntity.ok(service.update(id, agrochemical));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // ── ✅ Eliminar lógico (PATCH) ────────────────────────────────────────

    @PatchMapping("/{id}/delete")
    @Operation(
        summary = "Eliminar lógicamente (PATCH)",
        description = "Eliminación lógica: establece active=false y registra deletedAt con la fecha-hora actual. NO elimina el registro de la base de datos."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Agroquímico eliminado lógicamente"),
        @ApiResponse(responseCode = "404", description = "Registro no encontrado", content = @Content),
        @ApiResponse(responseCode = "400", description = "El registro ya está eliminado", content = @Content)
    })
    public ResponseEntity<?> delete(
            @Parameter(description = "ID del agroquímico a eliminar", required = true)
            @PathVariable Long id) {
        try {
            Agrochemical deleted = service.delete(id);
            return ResponseEntity.ok(Map.of(
                "message", "Agroquímico eliminado lógicamente",
                "id", deleted.getId(),
                "active", deleted.getActive(),
                "deletedAt", deleted.getDeletedAt().toString()
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // ── ✅ Restaurar lógico (PATCH) ───────────────────────────────────────

    @PatchMapping("/{id}/restore")
    @Operation(
        summary = "Restaurar lógicamente (PATCH)",
        description = "Restauración lógica: establece active=true y registra restoredAt con la fecha-hora actual."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Agroquímico restaurado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Registro no encontrado", content = @Content),
        @ApiResponse(responseCode = "400", description = "El registro ya está activo", content = @Content)
    })
    public ResponseEntity<?> restore(
            @Parameter(description = "ID del agroquímico a restaurar", required = true)
            @PathVariable Long id) {
        try {
            Agrochemical restored = service.restore(id);
            return ResponseEntity.ok(Map.of(
                "message", "Agroquímico restaurado exitosamente",
                "id", restored.getId(),
                "active", restored.getActive(),
                "restoredAt", restored.getRestoredAt().toString()
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
>>>>>>> c68b25f (feact(backend): general)
    }
}
