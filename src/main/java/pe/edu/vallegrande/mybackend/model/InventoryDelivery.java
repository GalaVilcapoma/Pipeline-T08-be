package pe.edu.vallegrande.mybackend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * US-04: Control de inventario de insumos por productor
 * Registra entrega de agroquímicos a productores
 */
@Entity
@Data
@Table(name = "inventory_deliveries")
public class InventoryDelivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producer_id", nullable = false)
    private Producer producer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agrochemical_id", nullable = false)
    private Agrochemical agrochemical;

    @Column(name = "quantity_delivered", nullable = false)
    private Double quantityDelivered;

    @Column(name = "unit", nullable = false, length = 30)
    private String unit;

    @Column(name = "delivery_date", nullable = false)
    private LocalDate deliveryDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsible_id", nullable = false)
    private AppUser responsible;

    @Column(name = "observations", length = 500)
    private String observations;

    // Audit
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
