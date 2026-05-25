package pe.edu.vallegrande.mybackend.service.impl;

import pe.edu.vallegrande.mybackend.model.Field;
import pe.edu.vallegrande.mybackend.model.Producer;
import pe.edu.vallegrande.mybackend.repository.FieldRepository;
import pe.edu.vallegrande.mybackend.repository.ProducerRepository;
import pe.edu.vallegrande.mybackend.service.ProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProducerServiceImpl implements ProducerService {

    @Autowired private ProducerRepository producerRepo;
    @Autowired private FieldRepository fieldRepo;

    // ── Producers ──────────────────────────────────────────────────────────

    @Override
    public List<Producer> findAll() {
        return producerRepo.findAll();
    }

    @Override
    public List<Producer> findByActive(Boolean active) {
        return producerRepo.findByActive(active);
    }

    @Override
    public Optional<Producer> findById(Long id) {
        return producerRepo.findById(id);
    }

    @Override
    public Producer save(Producer producer) {
        producer.setQrToken(UUID.randomUUID().toString());
        return producerRepo.save(producer);
    }

    @Override
    public Producer update(Long id, Producer producer) {
        Producer existing = producerRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Producer not found: " + id));

        existing.setFullName(producer.getFullName());
        existing.setDni(producer.getDni());
        existing.setPhone(producer.getPhone());
        existing.setEmail(producer.getEmail());
        existing.setLocation(producer.getLocation());
        existing.setDistrict(producer.getDistrict());
        existing.setProvince(producer.getProvince());
        existing.setRegion(producer.getRegion());
        existing.setUpdatedAt(LocalDateTime.now());

        return producerRepo.save(existing);
    }

    @Override
    public Producer toggleActive(Long id) {
        Producer p = producerRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Producer not found: " + id));
        p.setActive(!p.getActive());
        p.setUpdatedAt(LocalDateTime.now());
        if (!p.getActive()) {
            p.setDeletedAt(LocalDateTime.now());
        } else {
            p.setRestoredAt(LocalDateTime.now());
            p.setDeletedAt(null);
        }
        return producerRepo.save(p);
    }

    @Override
    public Producer delete(Long id) {
        Producer p = producerRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Producer not found: " + id));
        p.setActive(false);
        p.setDeletedAt(LocalDateTime.now());
        return producerRepo.save(p);
    }

    @Override
    public Producer restore(Long id) {
        Producer p = producerRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Producer not found: " + id));
        p.setActive(true);
        p.setRestoredAt(LocalDateTime.now());
        p.setDeletedAt(null);
        return producerRepo.save(p);
    }

    @Override
    public List<Producer> findAllActive() {
        return producerRepo.findAllActive();
    }

    // ── Fields ─────────────────────────────────────────────────────────────

    @Override
    public List<Field> findFieldsByProducer(Long producerId) {
        return fieldRepo.findByProducerId(producerId);
    }

    @Override
    public Field saveField(Field field) {
        field.setQrToken(UUID.randomUUID().toString());
        return fieldRepo.save(field);
    }

    @Override
    public Field updateField(Long id, Field field) {
        field.setId(id);
        return fieldRepo.save(field);
    }

    // US-10: Find by QR token (public, no login required)
    @Override
    public Optional<Producer> findByQrToken(String token) {
        return producerRepo.findByQrToken(token);
    }

    @Override
    public Optional<Field> findFieldByQrToken(String token) {
        return fieldRepo.findByQrToken(token);
    }
}
