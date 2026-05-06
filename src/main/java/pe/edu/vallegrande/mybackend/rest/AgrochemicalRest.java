package pe.edu.vallegrande.mybackend.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import pe.edu.vallegrande.mybackend.model.Agrochemical;
import pe.edu.vallegrande.mybackend.service.AgrochemicalService;

import java.util.List;
import java.util.Map;

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
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

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
    }
}
