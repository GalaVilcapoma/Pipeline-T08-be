package pe.edu.vallegrande.mybackend.rest;

import pe.edu.vallegrande.mybackend.model.FieldApplication;
import pe.edu.vallegrande.mybackend.service.FieldApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

/**
 * US-01: Registro de aplicación de agroquímicos en campo
 * US-05: Trazabilidad por lote
 */
@RestController
@RequestMapping("/v1/api/applications")
@CrossOrigin(origins = "*")
@Tag(name = "Field Applications API", description = "US-01 — Agrochemical application records")
public class FieldApplicationRest {

    @Autowired private FieldApplicationService service;

    @GetMapping
    @Operation(summary = "Get all applications")
    public List<FieldApplication> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get application by ID")
    public ResponseEntity<FieldApplication> findById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/producer/{producerId}")
    @Operation(summary = "US-01 — Get applications by producer")
    public List<FieldApplication> findByProducer(@PathVariable Long producerId) {
        return service.findByProducer(producerId);
    }

    @GetMapping("/field/{fieldId}")
    @Operation(summary = "US-01 — Get applications by field")
    public List<FieldApplication> findByField(@PathVariable Long fieldId) {
        return service.findByField(fieldId);
    }

    @GetMapping("/traceability/{lotId}")
    @Operation(summary = "US-05 — Traceability report by production lot")
    public List<FieldApplication> traceability(@PathVariable Long lotId) {
        return service.findByLotForTraceability(lotId);
    }

    @GetMapping("/range")
    @Operation(summary = "US-01 — Get applications by date range")
    public List<FieldApplication> findByRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return service.findByDateRange(from, to);
    }

    @PostMapping
    @Operation(summary = "US-01 — Register application (validates SENASA)")
    public ResponseEntity<?> save(@RequestBody FieldApplication application) {
        try {
            return ResponseEntity.ok(service.save(application));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update application")
    public ResponseEntity<FieldApplication> update(@PathVariable Long id, @RequestBody FieldApplication application) {
        return ResponseEntity.ok(service.update(id, application));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete application")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
