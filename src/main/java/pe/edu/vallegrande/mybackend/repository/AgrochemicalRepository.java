package pe.edu.vallegrande.mybackend.repository;

import pe.edu.vallegrande.mybackend.model.Agrochemical;
<<<<<<< HEAD
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
=======
import pe.edu.vallegrande.mybackend.model.Agrochemical.AgrochemicalCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
>>>>>>> c68b25f (feact(backend): general)
import java.util.List;

@Repository
public interface AgrochemicalRepository extends JpaRepository<Agrochemical, Long> {
<<<<<<< HEAD
    List<Agrochemical> findByActive(Boolean active);
    List<Agrochemical> findBySenasaStatus(String senasaStatus);
=======

    /** Todos los activos (active = true) */
    List<Agrochemical> findByActiveTrue();

    /** Todos los inactivos / eliminados lógicamente (active = false) */
    List<Agrochemical> findByActiveFalse();

    /** Filtrar por categoría */
    List<Agrochemical> findByCategoryAndActiveTrue(AgrochemicalCategory category);

    /** Activos cuya fecha de vencimiento es anterior a la fecha dada (expiring/expired) */
    List<Agrochemical> findByActiveTrueAndRegistrationExpiryBefore(LocalDate date);
>>>>>>> c68b25f (feact(backend): general)
}
