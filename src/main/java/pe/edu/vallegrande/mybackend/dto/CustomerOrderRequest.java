package pe.edu.vallegrande.mybackend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO REQUEST — Cabecera + Detalle del Pedido de Cliente
 * Se recibe en una sola petición POST para crear la transacción completa.
 *
 * Campos autocalculados en el backend (NO se envían desde el frontend):
 *   - numeroPedido, fechaPedido, subtotal, igv, total
 *   - precioUnitario y subtotal de cada detalle
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerOrderRequest {

    /** ID del cliente (tabla maestra Customer) */
    private Long customerId;

    /** Fecha estimada de entrega (opcional) */
    private LocalDate fechaEntregaEstimada;

    /** Observaciones generales del pedido */
    private String observaciones;

    /** Lista de productos a pedir (mínimo 1 ítem) */
    private List<CustomerOrderDetailRequest> detalles;
}
