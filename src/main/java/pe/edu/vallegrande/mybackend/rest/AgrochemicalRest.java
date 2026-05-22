package pe.edu.vallegrande.mybackend.rest;

import pe.edu.vallegrande.mybackend.model.Agrochemical;
import pe.edu.vallegrande.mybackend.service.AgrochemicalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/api/agrochemicals")
@CrossOrigin(origins = "*")
@Tag(name = "Agrochemicals API", description = "Agrochemical catalog endpoints")
public class AgrochemicalRest {

    @Autowired
    private AgrochemicalService service;

    @GetMapping
    @Operation(summary = "List all agrochemicals")
    public ResponseEntity<List<Agrochemical>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/active")
    @Operation(summary = "List active agrochemicals")
    public ResponseEntity<List<Agrochemical>> findAllActive() {
        return ResponseEntity.ok(service.findAllActive());
    }

    @GetMapping("/inactive")
    @Operation(summary = "List inactive agrochemicals")
    public ResponseEntity<List<Agrochemical>> findAllInactive() {
        return ResponseEntity.ok(service.findAllInactive());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get agrochemical by ID")
    public ResponseEntity<Agrochemical> findById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create agrochemical")
    public ResponseEntity<Agrochemical> create(@RequestBody Agrochemical agrochemical) {
        Agrochemical created = service.create(agrochemical);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update agrochemical")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Agrochemical agrochemical) {
        try {
            return ResponseEntity.ok(service.update(id, agrochemical));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PatchMapping("/{id}/delete")
    @Operation(summary = "Logical delete (deactivate) agrochemical")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            Agrochemical deleted = service.delete(id);
            return ResponseEntity.ok(Map.of(
                "message", "Agrochemical deactivated",
                "id", deleted.getId(),
                "active", deleted.getActive(),
                "deletedAt", String.valueOf(deleted.getDeletedAt())
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PatchMapping("/{id}/restore")
    @Operation(summary = "Restore agrochemical")
    public ResponseEntity<?> restore(@PathVariable Long id) {
        try {
            Agrochemical restored = service.restore(id);
            return ResponseEntity.ok(Map.of(
                "message", "Agrochemical restored",
                "id", restored.getId(),
                "active", restored.getActive(),
                "restoredAt", String.valueOf(restored.getRestoredAt())
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
