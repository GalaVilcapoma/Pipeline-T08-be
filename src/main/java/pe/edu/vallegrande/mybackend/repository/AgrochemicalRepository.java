package pe.edu.vallegrande.mybackend.repository;

import pe.edu.vallegrande.mybackend.model.Agrochemical;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AgrochemicalRepository extends JpaRepository<Agrochemical, Long> {
    List<Agrochemical> findByActive(Boolean active);
    List<Agrochemical> findBySenasaStatus(String senasaStatus);
}
