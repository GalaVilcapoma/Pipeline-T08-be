package pe.edu.vallegrande.mybackend.repository;

import pe.edu.vallegrande.mybackend.model.Alert;
import pe.edu.vallegrande.mybackend.model.Alert.AlertStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {

    List<Alert> findByStatus(AlertStatus status);

    List<Alert> findByProducerId(Long producerId);

    List<Alert> findByStatusOrderByCreatedAtDesc(AlertStatus status);

    long countByStatus(AlertStatus status);

    // US-06: Dashboard — pending alerts count
    @Query("SELECT COUNT(a) FROM Alert a WHERE a.status = 'PENDING'")
    long countPending();
}
