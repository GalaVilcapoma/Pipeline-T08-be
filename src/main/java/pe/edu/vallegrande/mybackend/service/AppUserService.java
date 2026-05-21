package pe.edu.vallegrande.mybackend.service;

import pe.edu.vallegrande.mybackend.model.AppUser;
import pe.edu.vallegrande.mybackend.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * US-08: Gestión de roles y permisos de usuarios
 */
@Service
public class AppUserService {

    @Autowired private AppUserRepository repo;

    public List<AppUser> findAll() {
        return repo.findAll();
    }

    public Optional<AppUser> findById(Long id) {
        return repo.findById(id);
    }

    public AppUser save(AppUser user) {
        if (repo.existsByUsername(user.getUsername()))
            throw new IllegalArgumentException("Username already exists: " + user.getUsername());
        return repo.save(user);
    }

    public AppUser update(Long id, AppUser user) {
        user.setId(id);
        user.setUpdatedAt(LocalDateTime.now());
        return repo.save(user);
    }

    public AppUser toggleActive(Long id) {
        AppUser u = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found: " + id));
        u.setActive(!u.getActive());
        u.setUpdatedAt(LocalDateTime.now());
        if (!u.getActive()) u.setDeletedAt(LocalDateTime.now());
        return repo.save(u);
    }

    public AppUser changeRole(Long id, AppUser.UserRole role) {
        AppUser u = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found: " + id));
        u.setRole(role);
        u.setUpdatedAt(LocalDateTime.now());
        return repo.save(u);
    }

    // Simple login check (no JWT — demo purposes)
    public Optional<AppUser> login(String username, String password) {
        return repo.findByUsername(username)
                .filter(u -> u.getActive() && u.getPasswordHash().equals(password));
    }
}
