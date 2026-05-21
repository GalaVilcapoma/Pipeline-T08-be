package pe.edu.vallegrande.mybackend.service;

import pe.edu.vallegrande.mybackend.model.Field;
import pe.edu.vallegrande.mybackend.model.Producer;
import pe.edu.vallegrande.mybackend.repository.FieldRepository;
import pe.edu.vallegrande.mybackend.repository.ProducerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * US-02: Gestión de productores y campos asociados
 */
@Service
public class ProducerService {

    @Autowired private ProducerRepository producerRepo;
    @Autowired private FieldRepository fieldRepo;

    // ── Producers ──────────────────────────────────────────────────────────

    public List<Producer> findAll() {
        return producerRepo.findAll();
    }

    public List<Producer> findByActive(Boolean active) {
        return producerRepo.findByActive(active);
    }

    public Optional<Producer> findById(Long id) {
        return producerRepo.findById(id);
    }

    public Producer save(Producer producer) {
        producer.setQrToken(UUID.randomUUID().toString());
        return producerRepo.save(producer);
    }

    public Producer update(Long id, Producer producer) {
        producer.setId(id);
        producer.setUpdatedAt(LocalDateTime.now());
        return producerRepo.save(producer);
    }

    public Producer toggleActive(Long id) {
        Producer p = producerRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Producer not found: " + id));
        p.setActive(!p.getActive());
        p.setUpdatedAt(LocalDateTime.now());
        if (!p.getActive()) p.setDeletedAt(LocalDateTime.now());
        return producerRepo.save(p);
    }

    public List<Producer> findAllActive() {
        return producerRepo.findAllActive();
    }

    // ── Fields ─────────────────────────────────────────────────────────────

    public List<Field> findFieldsByProducer(Long producerId) {
        return fieldRepo.findByProducerId(producerId);
    }

    public Field saveField(Field field) {
        field.setQrToken(UUID.randomUUID().toString());
        return fieldRepo.save(field);
    }

    public Field updateField(Long id, Field field) {
        field.setId(id);
        return fieldRepo.save(field);
    }

    // US-10: Find by QR token (public, no login required)
    public Optional<Producer> findByQrToken(String token) {
        return producerRepo.findByQrToken(token);
    }

    public Optional<Field> findFieldByQrToken(String token) {
        return fieldRepo.findByQrToken(token);
    }
}
