package pe.edu.vallegrande.mybackend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * DTO REQUEST — Línea de detalle del pedido
 * El frontend solo envía: productoId, cantidad y observación opcional
 * El precio y subtotal se calculan en el backend
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerOrderDetailRequest {

    /** ID del producto (tabla maestra Producto) */
    private Long productoId;

    /** Cantidad a pedir */
    private Integer cantidad;

    /** Observación opcional por línea */
    private String observacion;
}
