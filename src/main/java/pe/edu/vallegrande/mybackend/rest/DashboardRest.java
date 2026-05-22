package pe.edu.vallegrande.mybackend.rest;

import pe.edu.vallegrande.mybackend.model.DashboardMetrics;
import pe.edu.vallegrande.mybackend.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api/dashboard")
@CrossOrigin(origins = "*")
@Tag(name = "Dashboard API", description = "Supervisor dashboard metrics")
public class DashboardRest {

    @Autowired
    private DashboardService service;

    @GetMapping("/metrics")
    @Operation(summary = "Get dashboard metrics")
    public ResponseEntity<DashboardMetrics> getMetrics() {
        return ResponseEntity.ok(service.getMetrics());
    }
}
