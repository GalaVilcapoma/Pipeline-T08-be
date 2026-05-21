package pe.edu.vallegrande.mybackend.service;

import pe.edu.vallegrande.mybackend.model.Alert;
import pe.edu.vallegrande.mybackend.model.Alert.AlertStatus;
import pe.edu.vallegrande.mybackend.repository.AlertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * US-09: Alertas automáticas por incumplimiento de registro
 */
@Service
public class AlertService {

    @Autowired private AlertRepository repo;

    public List<Alert> findAll() {
        return repo.findAll();
    }

    public List<Alert> findPending() {
        return repo.findByStatusOrderByCreatedAtDesc(AlertStatus.PENDING);
    }

    public long countPending() {
        return repo.countPending();
    }

    public Optional<Alert> findById(Long id) {
        return repo.findById(id);
    }

    public Alert save(Alert alert) {
        return repo.save(alert);
    }

    /**
     * US-09: Supervisor marks alert as reviewed with observation
     */
    public Alert markReviewed(Long id, String reviewedBy, String observation) {
        Alert alert = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Alert not found: " + id));
        alert.setStatus(AlertStatus.REVIEWED);
        alert.setReviewedBy(reviewedBy);
        alert.setReviewObservation(observation);
        alert.setReviewedAt(LocalDateTime.now());
        return repo.save(alert);
    }

    public Alert dismiss(Long id) {
        Alert alert = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Alert not found: " + id));
        alert.setStatus(AlertStatus.DISMISSED);
        return repo.save(alert);
    }
}
