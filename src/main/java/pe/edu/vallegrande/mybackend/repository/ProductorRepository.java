package pe.edu.vallegrande.mybackend.repository;

import pe.edu.vallegrande.mybackend.model.Productor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductorRepository extends JpaRepository<Productor, Long> {
    List<Productor> findByEstado(Boolean estado);
}
