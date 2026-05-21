package pe.edu.vallegrande.mybackend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * US-09: Alertas automáticas por incumplimiento de registro
 */
@Entity
@Data
@Table(name = "alerts")
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private AlertType type;

    @Column(name = "severity", nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private Severity severity;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "description", nullable = false, length = 500)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producer_id")
    private Producer producer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "field_id")
    private Field field;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agrochemical_id")
    private Agrochemical agrochemical;

    @Column(name = "status", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private AlertStatus status = AlertStatus.PENDING;

    @Column(name = "reviewed_by", length = 100)
    private String reviewedBy;

    @Column(name = "review_observation", length = 500)
    private String reviewObservation;

    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public enum AlertType {
        NO_APPLICATION_REGISTERED,
        DOSE_EXCEEDED,
        LOW_STOCK,
        SENASA_EXPIRING,
        SENASA_EXPIRED
    }

    public enum Severity {
        HIGH, MEDIUM, LOW
    }

    public enum AlertStatus {
        PENDING, REVIEWED, DISMISSED
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        status = AlertStatus.PENDING;
    }
}
