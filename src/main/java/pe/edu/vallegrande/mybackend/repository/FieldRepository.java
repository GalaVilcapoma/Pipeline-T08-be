package pe.edu.vallegrande.mybackend.repository;

import pe.edu.vallegrande.mybackend.model.Field;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface FieldRepository extends JpaRepository<Field, Long> {

    List<Field> findByProducerId(Long producerId);

    List<Field> findByProducerIdAndActive(Long producerId, Boolean active);

    Optional<Field> findByQrToken(String qrToken);
}
