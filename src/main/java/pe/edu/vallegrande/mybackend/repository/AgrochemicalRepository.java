package pe.edu.vallegrande.mybackend.repository;

import pe.edu.vallegrande.mybackend.model.Agrochemical;
import pe.edu.vallegrande.mybackend.model.Agrochemical.AgrochemicalCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface AgrochemicalRepository extends JpaRepository<Agrochemical, Long> {

    List<Agrochemical> findByActiveTrue();

    List<Agrochemical> findByActiveFalse();

    List<Agrochemical> findByCategoryAndActiveTrue(AgrochemicalCategory category);

    List<Agrochemical> findByActiveTrueAndRegistrationExpiryBefore(LocalDate date);
}
