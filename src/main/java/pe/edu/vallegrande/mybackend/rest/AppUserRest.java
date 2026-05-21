package pe.edu.vallegrande.mybackend.rest;

import pe.edu.vallegrande.mybackend.model.AppUser;
import pe.edu.vallegrande.mybackend.service.AppUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

/**
 * US-08: Gestión de roles y permisos de usuarios
 * Auth: simple login endpoint (no JWT — extend as needed)
 */
@RestController
@CrossOrigin(origins = "*")
@Tag(name = "Users API", description = "US-08 — User and role management")
public class AppUserRest {

    @Autowired private AppUserService service;

    // ── Auth ───────────────────────────────────────────────────────────────

    @PostMapping("/v1/api/auth/login")
    @Operation(summary = "Login — returns user info")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        return service.login(body.get("username"), body.get("password"))
                .map(user -> {
                    // Build a simple token response (extend with JWT if needed)
                    Map<String, Object> response = Map.of(
                        "token",     "demo." + user.getId() + "." + user.getRole(),
                        "tokenType", "Bearer",
                        "expiresIn", 86400,
                        "user", Map.of(
                            "id",       user.getId(),
                            "username", user.getUsername(),
                            "fullName", user.getFullName(),
                            "email",    user.getEmail(),
                            "roles",    List.of(user.getRole().name())
                        )
                    );
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.status(401).build());
    }

    // ── Users ──────────────────────────────────────────────────────────────

    @GetMapping("/v1/api/users")
    @Operation(summary = "Get all users")
    public List<AppUser> findAll() {
        return service.findAll();
    }

    @GetMapping("/v1/api/users/{id}")
    @Operation(summary = "Get user by ID")
    public ResponseEntity<AppUser> findById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/v1/api/users")
    @Operation(summary = "US-08 — Create user")
    public ResponseEntity<?> save(@RequestBody AppUser user) {
        try {
            return ResponseEntity.ok(service.save(user));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/v1/api/users/{id}")
    @Operation(summary = "US-08 — Update user")
    public ResponseEntity<AppUser> update(@PathVariable Long id, @RequestBody AppUser user) {
        return ResponseEntity.ok(service.update(id, user));
    }

    @PatchMapping("/v1/api/users/{id}/toggle")
    @Operation(summary = "US-08 — Activate/deactivate user")
    public ResponseEntity<AppUser> toggle(@PathVariable Long id) {
        return ResponseEntity.ok(service.toggleActive(id));
    }

    @PatchMapping("/v1/api/users/{id}/role")
    @Operation(summary = "US-08 — Change user role")
    public ResponseEntity<AppUser> changeRole(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        AppUser.UserRole role = AppUser.UserRole.valueOf(body.get("role"));
        return ResponseEntity.ok(service.changeRole(id, role));
    }
}
