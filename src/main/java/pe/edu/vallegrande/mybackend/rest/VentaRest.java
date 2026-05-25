package pe.edu.vallegrande.mybackend.rest;

import pe.edu.vallegrande.mybackend.dto.ApiResponse;
import pe.edu.vallegrande.mybackend.dto.VentaRequest;
import pe.edu.vallegrande.mybackend.dto.VentaResponse;
import pe.edu.vallegrande.mybackend.service.VentaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/v1/api/ventas")
@CrossOrigin(origins = "*")
@Tag(name = "Ventas — Transaccion", description = "API para transacciones con cabecera y detalle")
public class VentaRest {

    @Autowired private VentaService ventaService;

    @PostMapping
    @Operation(summary = "Registrar transaccion con detalle en una sola accion")
    public ResponseEntity<ApiResponse<VentaResponse>> crear(@RequestBody VentaRequest request) {
        VentaResponse response = ventaService.crearVenta(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(response, "Transaccion registrada con exito", true));
    }

    @GetMapping
    @Operation(summary = "Listado de transacciones")
    public ResponseEntity<List<VentaResponse>> listar() {
        return ResponseEntity.ok(ventaService.listarVentas());
    }
}
