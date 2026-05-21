package pe.edu.vallegrande.mybackend.rest;

import pe.edu.vallegrande.mybackend.model.Field;
import pe.edu.vallegrande.mybackend.model.Producer;
import pe.edu.vallegrande.mybackend.service.ProducerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * US-02: Gestión de productores y campos asociados
 * US-10: QR view endpoint (public)
 */
@RestController
@RequestMapping("/v1/api/producers")
@CrossOrigin(origins = "*")
@Tag(name = "Producers API", description = "US-02 — Producer and field management")
public class ProducerRest {

    @Autowired private ProducerService service;

    // ── Producers ──────────────────────────────────────────────────────────

    @GetMapping
    @Operation(summary = "Get all producers")
    public List<Producer> findAll() {
        return service.findAll();
    }

    @GetMapping("/active")
    @Operation(summary = "Get active producers")
    public List<Producer> findActive() {
        return service.findAllActive();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get producer by ID")
    public ResponseEntity<Producer> findById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create producer")
    public ResponseEntity<Producer> save(@RequestBody Producer producer) {
        return ResponseEntity.ok(service.save(producer));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update producer")
    public ResponseEntity<Producer> update(@PathVariable Long id, @RequestBody Producer producer) {
        return ResponseEntity.ok(service.update(id, producer));
    }

    @PatchMapping("/{id}/toggle")
    @Operation(summary = "Toggle producer active status")
    public ResponseEntity<Producer> toggle(@PathVariable Long id) {
        return ResponseEntity.ok(service.toggleActive(id));
    }

    // ── Fields ─────────────────────────────────────────────────────────────

    @GetMapping("/{producerId}/fields")
    @Operation(summary = "Get fields by producer")
    public List<Field> getFields(@PathVariable Long producerId) {
        return service.findFieldsByProducer(producerId);
    }

    @PostMapping("/{producerId}/fields")
    @Operation(summary = "Create field for producer")
    public ResponseEntity<Field> saveField(@PathVariable Long producerId, @RequestBody Field field) {
        field.setProducer(new Producer());
        field.getProducer().setId(producerId);
        return ResponseEntity.ok(service.saveField(field));
    }

    @PutMapping("/fields/{fieldId}")
    @Operation(summary = "Update field")
    public ResponseEntity<Field> updateField(@PathVariable Long fieldId, @RequestBody Field field) {
        return ResponseEntity.ok(service.updateField(fieldId, field));
    }

    // ── US-10: Public QR endpoint (no login required) ──────────────────────

    @GetMapping("/qr/{token}")
    @Operation(summary = "US-10 — Public QR view (no auth required)")
    public ResponseEntity<?> getByQrToken(@PathVariable String token) {
        return service.findByQrToken(token)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/fields/qr/{token}")
    @Operation(summary = "US-10 — Field QR view (no auth required)")
    public ResponseEntity<?> getFieldByQrToken(@PathVariable String token) {
        return service.findFieldByQrToken(token)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
