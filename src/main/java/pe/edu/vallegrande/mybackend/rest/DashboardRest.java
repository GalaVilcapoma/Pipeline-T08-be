package pe.edu.vallegrande.mybackend.rest;

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
    }
}
