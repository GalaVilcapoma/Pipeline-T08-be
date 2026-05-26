package pe.edu.vallegrande.mybackend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import pe.edu.vallegrande.mybackend.model.CustomerOrder.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO RESPONSE — Cabecera + Detalle del Pedido de Cliente
 * Incluye datos del cliente y todos los detalles con sus productos
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerOrderResponse {

    // Cabecera
    private Long id;
    private String numeroPedido;

    // Datos del cliente
    private Long customerId;
    private String customerRazonSocial;
    private String customerNumeroDocumento;
    private String customerPais;

    // Fechas
    private LocalDate fechaPedido;
    private LocalDate fechaEntregaEstimada;

    // Estado
    private OrderStatus estado;
    private String observaciones;

    // Totales autocalculados
    private BigDecimal subtotal;
    private BigDecimal igv;
    private BigDecimal total;

    // Detalle de líneas
    private List<CustomerOrderDetailResponse> detalles;

    // Audit
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean active;
}
