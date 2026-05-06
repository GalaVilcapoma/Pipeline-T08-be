package pe.edu.vallegrande.mybackend.model;

import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "agrochemical")
public class Agrochemical {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "commercial_name")
    private String commercialName;

    @Column(name = "active_ingredient")
    private String activeIngredient;

    private String category;          // INSECTICIDE, FUNGICIDE, HERBICIDE, FERTILIZER, BIOSTIMULANT, OTHER

    @Column(name = "senasa_status")
    private String senasaStatus;      // ACTIVE, SUSPENDED, CANCELLED, EXPIRED

    @Column(name = "senasa_registration_number")
    private String senasaRegistrationNumber;

    @Column(name = "registration_expiry")
    private LocalDate registrationExpiry;

    @Column(name = "max_dose")
    private Double maxDose;

    @Column(name = "dose_unit")
    private String doseUnit;

    @Column(name = "waiting_period_days")
    private Integer waitingPeriodDays;

    private String manufacturer;
    private Boolean active = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() { this.createdAt = LocalDateTime.now(); }

    @PreUpdate
    protected void onUpdate() { this.updatedAt = LocalDateTime.now(); }
}
