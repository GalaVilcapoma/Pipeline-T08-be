package pe.edu.vallegrande.mybackend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

/**
 * US-02: Campo de cultivo asociado a un productor
 * Regla: un campo solo puede tener un lote de producción activo a la vez
 */
@Entity
@Data
@Table(name = "fields")
public class Field {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producer_id", nullable = false)
    private Producer producer;

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "area_hectares", nullable = false)
    private Double areaHectares;

    @Column(name = "active_crop", nullable = false, length = 100)
    private String activeCrop;

    @Column(name = "location", length = 200)
    private String location;

    @Column(name = "active", nullable = false)
    private Boolean active = true;

    @Column(name = "qr_token", unique = true, length = 100)
    private String qrToken;

    // Audit
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "field", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductionLot> lots;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        active = true;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
