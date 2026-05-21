package pe.edu.vallegrande.mybackend.rest;

import pe.edu.vallegrande.mybackend.model.Alert;
import pe.edu.vallegrande.mybackend.service.AlertService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

/**
 * US-09: Alertas automáticas por incumplimiento de registro
 */
@RestController
@RequestMapping("/v1/api/alerts")
@CrossOrigin(origins = "*")
@Tag(name = "Alerts API", description = "US-09 — Automatic system alerts")
public class AlertRest {

    @Autowired private AlertService service;

    @GetMapping
    @Operation(summary = "Get all alerts")
    public List<Alert> findAll() {
        return service.findAll();
    }

    @GetMapping("/pending")
    @Operation(summary = "US-09 — Get pending alerts")
    public List<Alert> findPending() {
        return service.findPending();
    }

    @GetMapping("/pending-count")
    @Operation(summary = "US-06 — Count pending alerts for dashboard")
    public ResponseEntity<Map<String, Long>> countPending() {
        return ResponseEntity.ok(Map.of("count", service.countPending()));
    }

    @PatchMapping("/{id}/review")
    @Operation(summary = "US-09 — Mark alert as reviewed")
    public ResponseEntity<Alert> markReviewed(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(
            service.markReviewed(id, body.get("reviewedBy"), body.get("observation"))
        );
    }

    @PatchMapping("/{id}/dismiss")
    @Operation(summary = "Dismiss alert")
    public ResponseEntity<Alert> dismiss(@PathVariable Long id) {
        return ResponseEntity.ok(service.dismiss(id));
    }
}
