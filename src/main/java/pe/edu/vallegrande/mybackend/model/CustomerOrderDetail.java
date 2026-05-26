package pe.edu.vallegrande.mybackend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * TRANSACCIÓN: Detalle del Pedido de Cliente
 * Relaciona: CustomerOrder (cabecera) + Producto (tabla maestra)
 * Campos autocalculados: precioUnitario (tomado del producto), subtotal = cantidad * precioUnitario
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customer_order_details")
public class CustomerOrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** FK → cabecera del pedido */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private CustomerOrder customerOrder;

    /** FK → tabla maestra Producto */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    /** Cantidad solicitada */
    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    /** Precio unitario al momento del pedido (tomado del producto) */
    @Column(name = "precio_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitario;

    /** Subtotal = cantidad * precioUnitario (autocalculado) */
    @Column(name = "subtotal", nullable = false, precision = 12, scale = 2)
    private BigDecimal subtotal;

    /** Observación por línea */
    @Column(name = "observacion", length = 300)
    private String observacion;

    // ── Audit
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
