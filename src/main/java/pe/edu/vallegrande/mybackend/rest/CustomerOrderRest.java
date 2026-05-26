package pe.edu.vallegrande.mybackend.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.mybackend.dto.ApiResponse;
import pe.edu.vallegrande.mybackend.dto.CustomerOrderRequest;
import pe.edu.vallegrande.mybackend.dto.CustomerOrderResponse;
import pe.edu.vallegrande.mybackend.model.CustomerOrder.OrderStatus;
import pe.edu.vallegrande.mybackend.service.CustomerOrderService;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/api/pedidos-clientes")
@CrossOrigin(origins = "*")
@Tag(name = "Pedidos de Clientes — Transacción", description = "API para la gestión de pedidos: cabecera + detalle en una sola operación")
public class CustomerOrderRest {

    private final CustomerOrderService orderService;

    @Autowired
    public CustomerOrderRest(CustomerOrderService orderService) {
        this.orderService = orderService;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // POST /v1/api/pedidos-clientes — Crear pedido completo (cabecera + detalle)
    // ─────────────────────────────────────────────────────────────────────────
    @PostMapping
    @Operation(
        summary = "Crear pedido de cliente (transacción completa)",
        description = "Registra cabecera y detalle en una sola operación. " +
                      "Valida cliente activo, valida stock, descuenta stock, " +
                      "calcula subtotales, IGV (18%) y total automáticamente."
    )
    public ResponseEntity<ApiResponse<CustomerOrderResponse>> crearPedido(
            @RequestBody CustomerOrderRequest request) {
        try {
            CustomerOrderResponse response = orderService.crearPedido(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(response, "Pedido registrado exitosamente. Número: " + response.getNumeroPedido(), true));
        } catch (IllegalArgumentException | IllegalStateException e) {
            log.warn("Error de validación al crear pedido: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(null, e.getMessage(), false));
        } catch (RuntimeException e) {
            log.error("Error al crear pedido: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(null, e.getMessage(), false));
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // GET /v1/api/pedidos-clientes — Listar todos los pedidos activos
    // ─────────────────────────────────────────────────────────────────────────
    @GetMapping
    @Operation(summary = "Listar todos los pedidos activos")
    public ResponseEntity<ApiResponse<List<CustomerOrderResponse>>> listarTodos() {
        List<CustomerOrderResponse> lista = orderService.listarTodos();
        return ResponseEntity.ok(new ApiResponse<>(lista, "Pedidos listados exitosamente", true));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // GET /v1/api/pedidos-clientes/filtros — Listar con filtros y paginación
    // ─────────────────────────────────────────────────────────────────────────
    @GetMapping("/filtros")
    @Operation(summary = "Listar pedidos con filtros y paginación")
    public ResponseEntity<Page<CustomerOrderResponse>> listarConFiltros(
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) OrderStatus estado,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaDesde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaHasta,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<CustomerOrderResponse> resultado = orderService.listarConFiltros(
                customerId, estado, fechaDesde, fechaHasta, pageable);
        return ResponseEntity.ok(resultado);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // GET /v1/api/pedidos-clientes/{id} — Obtener pedido por ID
    // ─────────────────────────────────────────────────────────────────────────
    @GetMapping("/{id}")
    @Operation(summary = "Obtener pedido por ID")
    public ResponseEntity<ApiResponse<CustomerOrderResponse>> findById(@PathVariable Long id) {
        return orderService.findById(id)
                .map(o -> ResponseEntity.ok(new ApiResponse<>(o, "Pedido encontrado", true)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(null, "Pedido no encontrado con ID: " + id, false)));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // GET /v1/api/pedidos-clientes/numero/{numeroPedido} — Buscar por número
    // ─────────────────────────────────────────────────────────────────────────
    @GetMapping("/numero/{numeroPedido}")
    @Operation(summary = "Buscar pedido por número de pedido")
    public ResponseEntity<ApiResponse<CustomerOrderResponse>> findByNumeroPedido(
            @PathVariable String numeroPedido) {
        return orderService.findByNumeroPedido(numeroPedido)
                .map(o -> ResponseEntity.ok(new ApiResponse<>(o, "Pedido encontrado", true)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(null, "Pedido no encontrado: " + numeroPedido, false)));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // GET /v1/api/pedidos-clientes/cliente/{customerId} — Pedidos de un cliente
    // ─────────────────────────────────────────────────────────────────────────
    @GetMapping("/cliente/{customerId}")
    @Operation(summary = "Listar pedidos de un cliente específico")
    public ResponseEntity<ApiResponse<List<CustomerOrderResponse>>> listarPorCliente(
            @PathVariable Long customerId) {
        List<CustomerOrderResponse> lista = orderService.listarPorCliente(customerId);
        return ResponseEntity.ok(new ApiResponse<>(lista, "Pedidos del cliente listados exitosamente", true));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // PATCH /v1/api/pedidos-clientes/{id}/cancelar — Cancelar pedido
    // ─────────────────────────────────────────────────────────────────────────
    @PatchMapping("/{id}/cancelar")
    @Operation(
        summary = "Cancelar un pedido",
        description = "Cambia el estado a CANCELADO y devuelve el stock a los productos."
    )
    public ResponseEntity<ApiResponse<CustomerOrderResponse>> cancelarPedido(@PathVariable Long id) {
        try {
            CustomerOrderResponse response = orderService.cancelarPedido(id);
            return ResponseEntity.ok(new ApiResponse<>(response, "Pedido cancelado. Stock devuelto a productos.", true));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(null, e.getMessage(), false));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(null, e.getMessage(), false));
        }
    }
}
