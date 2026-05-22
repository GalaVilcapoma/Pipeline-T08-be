package pe.edu.vallegrande.mybackend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

/**
 * US-02: Gestión de productores y campos asociados
 */
@Entity
@Data
@Table(name = "producers")
public class Producer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = false, length = 200)
    private String fullName;

    @Column(name = "dni", nullable = false, unique = true, length = 20)
    private String dni;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "location", nullable = false, length = 200)
    private String location;

    @Column(name = "district", length = 100)
    private String district;

    @Column(name = "province", length = 100)
    private String province;

    @Column(name = "region", length = 100)
    private String region;

    @Column(name = "active", nullable = false)
    private Boolean active = true;

    @Column(name = "qr_token", unique = true, length = 100)
    private String qrToken;

    // Audit
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "restored_at")
    private LocalDateTime restoredAt;

    @OneToMany(mappedBy = "producer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Field> fields;

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
