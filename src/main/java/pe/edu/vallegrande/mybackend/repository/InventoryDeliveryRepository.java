package pe.edu.vallegrande.mybackend.repository;

import pe.edu.vallegrande.mybackend.model.InventoryDelivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InventoryDeliveryRepository extends JpaRepository<InventoryDelivery, Long> {

    List<InventoryDelivery> findByProducerId(Long producerId);

    List<InventoryDelivery> findByAgrochemicalId(Long agrochemicalId);

    // US-04: Real-time balance per producer per product
    @Query("""
        SELECT d.agrochemical.id, SUM(d.quantityDelivered)
        FROM InventoryDelivery d
        WHERE d.producer.id = :producerId
        GROUP BY d.agrochemical.id
        """)
    List<Object[]> sumDeliveredByProducer(@Param("producerId") Long producerId);

    // US-04: Total delivered per product (central stock)
    @Query("""
        SELECT d.agrochemical.id, SUM(d.quantityDelivered)
        FROM InventoryDelivery d
        GROUP BY d.agrochemical.id
        """)
    List<Object[]> sumAllDeliveries();
}
