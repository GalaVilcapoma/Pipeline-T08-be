package pe.edu.vallegrande.mybackend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO RESPONSE — Línea de detalle del pedido
 * Incluye datos del producto para que el frontend no necesite hacer otra llamada
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerOrderDetailResponse {

    private Long id;

    // Datos del producto
    private Long productoId;
    private String productoCodigo;
    private String productoNombre;
    private String productoUnidadMedida;

    // Datos de la línea
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;
    private String observacion;

    private LocalDateTime createdAt;
}
