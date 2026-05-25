package pe.edu.vallegrande.mybackend.service;

import pe.edu.vallegrande.mybackend.model.Field;
import pe.edu.vallegrande.mybackend.model.Producer;
import java.util.List;
import java.util.Optional;

/**
 * US-02: Gestión de productores y campos asociados
 */
public interface ProducerService {

    // ── Producers ──────────────────────────────────────────────────────────
    List<Producer> findAll();
    List<Producer> findByActive(Boolean active);
    Optional<Producer> findById(Long id);
    Producer save(Producer producer);
    Producer update(Long id, Producer producer);
    Producer toggleActive(Long id);
    Producer delete(Long id);
    Producer restore(Long id);
    List<Producer> findAllActive();

    // ── Fields ─────────────────────────────────────────────────────────────
    List<Field> findFieldsByProducer(Long producerId);
    Field saveField(Field field);
    Field updateField(Long id, Field field);

    // US-10: Find by QR token (public, no login required)
    Optional<Producer> findByQrToken(String token);
    Optional<Field> findFieldByQrToken(String token);
}
