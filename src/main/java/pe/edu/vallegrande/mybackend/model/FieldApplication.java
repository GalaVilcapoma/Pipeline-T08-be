package pe.edu.vallegrande.mybackend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * US-01: Registro de aplicación de agroquímicos en campo
 * Vinculado al lote de producción activo del campo
 * Valida que el agroquímico esté autorizado por SENASA
 */
@Entity
@Data
@Table(name = "field_applications")
public class FieldApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producer_id", nullable = false)
    private Producer producer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "field_id", nullable = false)
    private Field field;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lot_id", nullable = false)
    private ProductionLot lot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agrochemical_id", nullable = false)
    private Agrochemical agrochemical;

    @Column(name = "dose", nullable = false)
    private Double dose;

    @Column(name = "dose_unit", nullable = false, length = 30)
    private String doseUnit;

    @Column(name = "application_date", nullable = false)
    private LocalDate applicationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "technician_id", nullable = false)
    private AppUser technician;

    @Column(name = "observations", length = 500)
    private String observations;

    // Validation flags (computed at save time)
    @Column(name = "senasa_valid")
    private Boolean senasaValid;

    @Column(name = "dose_exceeded")
    private Boolean doseExceeded;

    // Audit
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
