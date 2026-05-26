package pe.edu.vallegrande.mybackend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * TRANSACCIÓN: Pedido de Cliente (Cabecera)
 * Relaciona: Customer (tabla maestra) + CustomerOrderDetail (detalle)
 * Lógica: valida cliente activo, descuenta stock de productos, calcula totales
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customer_orders")
public class CustomerOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Número de pedido autogenerado */
    @Column(name = "numero_pedido", nullable = false, unique = true, length = 50)
    private String numeroPedido;

    /** FK → tabla maestra Customer */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    /** Fecha del pedido (autocalculada al crear) */
    @Column(name = "fecha_pedido", nullable = false)
    private LocalDate fechaPedido;

    /** Fecha estimada de entrega */
    @Column(name = "fecha_entrega_estimada")
    private LocalDate fechaEntregaEstimada;

    /** Estado del pedido */
    @Column(name = "estado", nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private OrderStatus estado = OrderStatus.PENDIENTE;

    /** Observaciones */
    @Column(name = "observaciones", length = 500)
    private String observaciones;

    /** Subtotal antes de IGV (autocalculado) */
    @Column(name = "subtotal", nullable = false, precision = 12, scale = 2)
    private BigDecimal subtotal = BigDecimal.ZERO;

    /** IGV 18% (autocalculado) */
    @Column(name = "igv", nullable = false, precision = 12, scale = 2)
    private BigDecimal igv = BigDecimal.ZERO;

    /** Total con IGV (autocalculado) */
    @Column(name = "total", nullable = false, precision = 12, scale = 2)
    private BigDecimal total = BigDecimal.ZERO;

    /** Detalle del pedido (cascade: se guarda junto con la cabecera) */
    @OneToMany(mappedBy = "customerOrder", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CustomerOrderDetail> detalles = new ArrayList<>();

    // ── Audit Columns
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "active", nullable = false)
    private Boolean active = true;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (active == null) active = true;
        if (estado == null) estado = OrderStatus.PENDIENTE;
        if (fechaPedido == null) fechaPedido = LocalDate.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum OrderStatus {
        PENDIENTE, CONFIRMADO, EN_PROCESO, ENTREGADO, CANCELADO
    }
}
