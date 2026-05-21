package pe.edu.vallegrande.mybackend.repository;

import pe.edu.vallegrande.mybackend.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByUsername(String username);

    Optional<AppUser> findByEmail(String email);

    List<AppUser> findByActive(Boolean active);

    List<AppUser> findByRole(AppUser.UserRole role);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
