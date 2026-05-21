package pe.edu.vallegrande.mybackend.service;

import pe.edu.vallegrande.mybackend.model.Agrochemical;
<<<<<<< HEAD
import java.util.List;
import java.util.Optional;

public interface AgrochemicalService {
    List<Agrochemical> findAll();
    List<Agrochemical> findByActive(Boolean active);
    Optional<Agrochemical> findById(Long id);
    Agrochemical save(Agrochemical agrochemical);
    Agrochemical update(Long id, Agrochemical agrochemical);
    Agrochemical updateSenasaStatus(Long id, String status);
    List<Agrochemical> getActiveForSelection();
    List<Agrochemical> getExpiringSoon();
=======
import pe.edu.vallegrande.mybackend.repository.AgrochemicalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Servicio CRUD completo para el maestro Agrochemical.
 *
 * Operaciones con auditoría de fecha-hora:
 *  ✅ Crear     (POST)  → createdAt  se asigna en @PrePersist
 *  ✅ Editar    (PUT)   → updatedAt  se asigna en @PreUpdate
 *  ✅ Eliminar  (PATCH) → active=false, deletedAt=now()
 *  ✅ Restaurar (PATCH) → active=true,  restoredAt=now()
 */
@Service
public class AgrochemicalService {

    @Autowired
    private AgrochemicalRepository repository;

    // ── Consultas ─────────────────────────────────────────────────────────

    /** Lista todos los registros (activos e inactivos). */
    public List<Agrochemical> findAll() {
        return repository.findAll();
    }

    /** Lista solo los registros activos. */
    public List<Agrochemical> findAllActive() {
        return repository.findByActiveTrue();
    }

    /** Lista solo los registros eliminados lógicamente. */
    public List<Agrochemical> findAllInactive() {
        return repository.findByActiveFalse();
    }

    /** Busca por ID. */
    public Optional<Agrochemical> findById(Long id) {
        return repository.findById(id);
    }

    // ── ✅ Crear (POST) ───────────────────────────────────────────────────

    /**
     * Crea un nuevo agroquímico.
     * createdAt se asigna automáticamente en @PrePersist.
     */
    public Agrochemical create(Agrochemical agrochemical) {
        agrochemical.setId(null);       // forzar INSERT — evita StaleObjectStateException si llega id=0
        agrochemical.setActive(true);
        agrochemical.setDeletedAt(null);
        agrochemical.setRestoredAt(null);
        return repository.save(agrochemical);
    }

    // ── ✅ Editar (PUT) ───────────────────────────────────────────────────

    /**
     * Actualiza un agroquímico existente.
     * updatedAt se asigna automáticamente en @PreUpdate.
     */
    public Agrochemical update(Long id, Agrochemical incoming) {
        Agrochemical existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agroquímico no encontrado con ID: " + id));

        // Actualizar solo los campos del negocio, preservar auditoría
        existing.setCommercialName(incoming.getCommercialName());
        existing.setActiveIngredient(incoming.getActiveIngredient());
        existing.setCategory(incoming.getCategory());
        existing.setSenasaRegistrationNumber(incoming.getSenasaRegistrationNumber());
        existing.setRegistrationExpiry(incoming.getRegistrationExpiry());
        existing.setMaxDose(incoming.getMaxDose());
        existing.setWaitingPeriodDays(incoming.getWaitingPeriodDays());
        existing.setManufacturer(incoming.getManufacturer());
        // updatedAt se setea en @PreUpdate automáticamente

        return repository.save(existing);
    }

    // ── ✅ Eliminar lógico (PATCH) ────────────────────────────────────────

    /**
     * Eliminación lógica: active=false, registra deletedAt con fecha-hora actual.
     */
    public Agrochemical delete(Long id) {
        Agrochemical agrochemical = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agroquímico no encontrado con ID: " + id));

        if (!agrochemical.getActive()) {
            throw new RuntimeException("El agroquímico ya está eliminado (ID: " + id + ")");
        }

        agrochemical.setActive(false);
        agrochemical.setDeletedAt(LocalDateTime.now());   // ✅ fecha-hora de eliminación
        return repository.save(agrochemical);
    }

    // ── ✅ Restaurar lógico (PATCH) ───────────────────────────────────────

    /**
     * Restauración lógica: active=true, registra restoredAt con fecha-hora actual.
     */
    public Agrochemical restore(Long id) {
        Agrochemical agrochemical = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agroquímico no encontrado con ID: " + id));

        if (agrochemical.getActive()) {
            throw new RuntimeException("El agroquímico ya está activo (ID: " + id + ")");
        }

        agrochemical.setActive(true);
        agrochemical.setRestoredAt(LocalDateTime.now());  // ✅ fecha-hora de restauración
        agrochemical.setDeletedAt(null);                  // limpiar fecha de eliminación
        return repository.save(agrochemical);
    }
>>>>>>> c68b25f (feact(backend): general)
}
