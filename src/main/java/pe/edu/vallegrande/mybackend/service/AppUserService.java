package pe.edu.vallegrande.mybackend.service;

import pe.edu.vallegrande.mybackend.model.AppUser;
import java.util.List;
import java.util.Optional;

/**
 * US-08: Gestión de roles y permisos de usuarios
 */
public interface AppUserService {
    List<AppUser> findAll();
    Optional<AppUser> findById(Long id);
    AppUser save(AppUser user);
    AppUser update(Long id, AppUser user);
    AppUser toggleActive(Long id);
    AppUser delete(Long id);
    AppUser restore(Long id);
    AppUser changeRole(Long id, AppUser.UserRole role);
    Optional<AppUser> login(String username, String password);
}
