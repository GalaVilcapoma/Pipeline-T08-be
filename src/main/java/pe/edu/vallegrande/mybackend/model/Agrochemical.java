package pe.edu.vallegrande.mybackend.model;

<<<<<<< HEAD
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
=======
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Maestro: Catálogo de Agroquímicos (SENASA)
 *
 * Campos del negocio (8 mínimo, 4 tipos de datos distintos):
 *  1.  id                        — Long        (BIGINT)
 *  2.  commercialName            — String      (NVARCHAR)
 *  3.  activeIngredient          — String      (NVARCHAR)
 *  4.  category                  — Enum/String (NVARCHAR)
 *  5.  senasaRegistrationNumber  — String      (NVARCHAR)
 *  6.  registrationExpiry        — LocalDate   (DATE)
 *  7.  maxDose                   — BigDecimal  (DECIMAL)
 *  8.  waitingPeriodDays         — Integer     (INT)
 *  9.  manufacturer              — String      (NVARCHAR)
 * 10.  active                    — Boolean     (BIT)  ← ESTADO (eliminación lógica)
 *
 * Campos de auditoría (4):
 * 11.  createdAt   — LocalDateTime
 * 12.  updatedAt   — LocalDateTime
 * 13.  deletedAt   — LocalDateTime
 * 14.  restoredAt  — LocalDateTime
 *
 * Total: 14 campos (≥ 12 requeridos)
 * Tipos de datos usados: Long, String, Enum, LocalDate, BigDecimal, Integer, Boolean, LocalDateTime
 */
@Entity
@Data
@Table(name = "agrochemicals")
public class Agrochemical {

    // ── Campos del negocio ────────────────────────────────────────────────

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                                                    // 1. ID (Long)

    @Column(name = "commercial_name", nullable = false, length = 200)
    private String commercialName;                                      // 2. String

    @Column(name = "active_ingredient", nullable = false, length = 200)
    private String activeIngredient;                                    // 3. String

    @Column(name = "category", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private AgrochemicalCategory category;                              // 4. Enum → String

    @Column(name = "senasa_registration_number", length = 50)
    private String senasaRegistrationNumber;                            // 5. String

    @Column(name = "registration_expiry", nullable = false)
    private LocalDate registrationExpiry;                               // 6. LocalDate (DATE)

    @Column(name = "max_dose", nullable = false, precision = 10, scale = 4)
    private BigDecimal maxDose;                                         // 7. BigDecimal (DECIMAL)

    @Column(name = "waiting_period_days", nullable = false)
    private Integer waitingPeriodDays = 0;                              // 8. Integer (INT)

    @Column(name = "manufacturer", length = 200)
    private String manufacturer;                                        // 9. String

    @Column(name = "active", nullable = false)
    private Boolean active = true;                                      // 10. Boolean (BIT) — ESTADO

    // ── Campos de auditoría (4) ───────────────────────────────────────────

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;                                    // 11. Auditoría: creación

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;                                    // 12. Auditoría: edición

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;                                    // 13. Auditoría: eliminación lógica

    @Column(name = "restored_at")
    private LocalDateTime restoredAt;                                   // 14. Auditoría: restauración lógica

    // ── Enumeraciones ─────────────────────────────────────────────────────

    public enum AgrochemicalCategory {
        INSECTICIDE, FUNGICIDE, HERBICIDE, FERTILIZER, BIOSTIMULANT, OTHER
    }

    // ── Callbacks JPA ─────────────────────────────────────────────────────

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (active == null) active = true;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
>>>>>>> c68b25f (feact(backend): general)
}
