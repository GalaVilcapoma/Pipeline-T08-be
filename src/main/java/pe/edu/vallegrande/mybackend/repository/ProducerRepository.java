package pe.edu.vallegrande.mybackend.repository;

import pe.edu.vallegrande.mybackend.model.Producer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProducerRepository extends JpaRepository<Producer, Long> {

    List<Producer> findByActive(Boolean active);

    List<Producer> findByFullNameContainingIgnoreCase(String name);

    Optional<Producer> findByDni(String dni);

    Optional<Producer> findByQrToken(String qrToken);

    @Query("SELECT p FROM Producer p WHERE p.active = true ORDER BY p.fullName")
    List<Producer> findAllActive();
}
