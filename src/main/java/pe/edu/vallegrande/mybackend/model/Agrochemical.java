package pe.edu.vallegrande.mybackend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "agrochemical")
public class Agrochemical {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "commercial_name", nullable = false, length = 200)
    private String commercialName;

    @Column(name = "active_ingredient", nullable = false, length = 200)
    private String activeIngredient;

    @Column(name = "category", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private AgrochemicalCategory category;

    @Column(name = "senasa_registration_number", length = 50)
    private String senasaRegistrationNumber;

    @Column(name = "registration_expiry", nullable = false)
    private LocalDate registrationExpiry;

    @Column(name = "max_dose", nullable = false, precision = 10, scale = 4)
    private BigDecimal maxDose;

    @Column(name = "waiting_period_days", nullable = false)
    private Integer waitingPeriodDays = 0;

    @Column(name = "manufacturer", length = 200)
    private String manufacturer;

    @Column(name = "active", nullable = false)
    private Boolean active = true;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "restored_at")
    private LocalDateTime restoredAt;

    public enum AgrochemicalCategory {
        INSECTICIDE, FUNGICIDE, HERBICIDE, FERTILIZER, BIOSTIMULANT, OTHER
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (active == null) active = true;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
