package pe.edu.vallegrande.mybackend.rest;

import pe.edu.vallegrande.mybackend.model.InventoryDelivery;
import pe.edu.vallegrande.mybackend.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * US-04: Control de inventario de insumos por productor
 */
@RestController
@RequestMapping("/v1/api/inventory")
@CrossOrigin(origins = "*")
@Tag(name = "Inventory API", description = "US-04 — Input inventory control per producer")
public class InventoryRest {

    @Autowired private InventoryService service;

    @GetMapping("/deliveries")
    @Operation(summary = "Get all deliveries")
    public List<InventoryDelivery> findAll() {
        return service.findAll();
    }

    @GetMapping("/deliveries/producer/{producerId}")
    @Operation(summary = "US-04 — Get deliveries by producer")
    public List<InventoryDelivery> findByProducer(@PathVariable Long producerId) {
        return service.findByProducer(producerId);
    }

    @PostMapping("/deliveries")
    @Operation(summary = "US-04 — Register delivery to producer")
    public ResponseEntity<InventoryDelivery> registerDelivery(@RequestBody InventoryDelivery delivery) {
        return ResponseEntity.ok(service.registerDelivery(delivery));
    }

    @GetMapping("/producer-stock/{producerId}")
    @Operation(summary = "US-04 — Real-time stock balance per producer")
    public ResponseEntity<List<Object[]>> getProducerStock(@PathVariable Long producerId) {
        return ResponseEntity.ok(service.getProducerStock(producerId));
    }

    @GetMapping("/central-stock")
    @Operation(summary = "US-04 — Central warehouse stock")
    public ResponseEntity<List<Object[]>> getCentralStock() {
        return ResponseEntity.ok(service.getCentralStock());
    }
}
