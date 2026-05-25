package pe.edu.vallegrande.mybackend.service.impl;

import pe.edu.vallegrande.mybackend.model.Alert;
import pe.edu.vallegrande.mybackend.model.Alert.AlertStatus;
import pe.edu.vallegrande.mybackend.repository.AlertRepository;
import pe.edu.vallegrande.mybackend.service.AlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AlertServiceImpl implements AlertService {

    @Autowired private AlertRepository repo;

    @Override
    public List<Alert> findAll() {
        return repo.findAll();
    }

    @Override
    public List<Alert> findPending() {
        return repo.findByStatusOrderByCreatedAtDesc(AlertStatus.PENDING);
    }

    @Override
    public long countPending() {
        return repo.countPending();
    }

    @Override
    public Optional<Alert> findById(Long id) {
        return repo.findById(id);
    }

    @Override
    public Alert save(Alert alert) {
        return repo.save(alert);
    }

    @Override
    public Alert markReviewed(Long id, String reviewedBy, String observation) {
        Alert alert = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Alert not found: " + id));
        alert.setStatus(AlertStatus.REVIEWED);
        alert.setReviewedBy(reviewedBy);
        alert.setReviewObservation(observation);
        alert.setReviewedAt(LocalDateTime.now());
        return repo.save(alert);
    }

    @Override
    public Alert dismiss(Long id) {
        Alert alert = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Alert not found: " + id));
        alert.setStatus(AlertStatus.DISMISSED);
        return repo.save(alert);
    }
}
