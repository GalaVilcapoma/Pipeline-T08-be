package pe.edu.vallegrande.mybackend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * US-08: Gestión de roles y permisos de usuarios
 * Roles: ADMIN, SUPERVISOR, TECHNICIAN, LOGISTICS, CERTIFICACIONES
 */
@Entity
@Data
@Table(name = "app_users")
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(name = "full_name", nullable = false, length = 200)
    private String fullName;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "role", nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(name = "active", nullable = false)
    private Boolean active = true;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    // Audit
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "restored_at")
    private LocalDateTime restoredAt;

    public enum UserRole {
        ROLE_ADMIN,
        ROLE_SUPERVISOR,
        ROLE_TECHNICIAN,
        ROLE_LOGISTICS,
        ROLE_CERTIFICACIONES
    }

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
