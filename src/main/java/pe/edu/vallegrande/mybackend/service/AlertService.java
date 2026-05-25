package pe.edu.vallegrande.mybackend.service;

import pe.edu.vallegrande.mybackend.model.Alert;
import java.util.List;
import java.util.Optional;

/**
 * US-09: Alertas automáticas por incumplimiento de registro
 */
public interface AlertService {
    List<Alert> findAll();
    List<Alert> findPending();
    long countPending();
    Optional<Alert> findById(Long id);
    Alert save(Alert alert);
    
    /**
     * US-09: Supervisor marks alert as reviewed with observation
     */
    Alert markReviewed(Long id, String reviewedBy, String observation);
    Alert dismiss(Long id);
}
