package pe.edu.vallegrande.mybackend.repository;

import pe.edu.vallegrande.mybackend.model.ProductionLot;
import pe.edu.vallegrande.mybackend.model.ProductionLot.LotStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductionLotRepository extends JpaRepository<ProductionLot, Long> {

    // US-02: A field can only have ONE active lot at a time
    Optional<ProductionLot> findByFieldIdAndStatus(Long fieldId, LotStatus status);

    List<ProductionLot> findByFieldId(Long fieldId);

    boolean existsByFieldIdAndStatus(Long fieldId, LotStatus status);
}
