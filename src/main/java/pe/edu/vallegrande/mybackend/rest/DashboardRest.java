package pe.edu.vallegrande.mybackend.rest;

<<<<<<< HEAD
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import pe.edu.vallegrande.mybackend.model.DashboardMetrics;
import pe.edu.vallegrande.mybackend.service.DashboardService;

@RestController
@RequestMapping("/v1/api/dashboard")
@CrossOrigin(origins = "*")
@Tag(name = "Dashboard API", description = "Supervisor dashboard metrics")
public class DashboardRest {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/metrics")
    public DashboardMetrics getMetrics() {
        return dashboardService.getMetrics();
=======
import pe.edu.vallegrande.mybackend.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

/**
 * US-06: Panel de control para supervisores
 */
@RestController
@RequestMapping("/v1/api/dashboard")
@CrossOrigin(origins = "*")
@Tag(name = "Dashboard API", description = "US-06 — Supervisor control panel metrics")
public class DashboardRest {

    @Autowired private DashboardService service;

    @GetMapping("/metrics")
    @Operation(summary = "US-06 — Get real-time dashboard metrics")
    public ResponseEntity<Map<String, Object>> getMetrics() {
        return ResponseEntity.ok(service.getMetrics());
>>>>>>> c68b25f (feact(backend): general)
    }
}
