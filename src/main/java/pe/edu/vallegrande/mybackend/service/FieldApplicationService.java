package pe.edu.vallegrande.mybackend.service;

import pe.edu.vallegrande.mybackend.model.FieldApplication;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * US-01: Registro de aplicación de agroquímicos en campo
 * - Valida SENASA antes de guardar
 * - Vincula al lote activo del campo
 * - Detecta si la dosis supera el máximo
 */
public interface FieldApplicationService {
    List<FieldApplication> findAll();
    List<FieldApplication> findByProducer(Long producerId);
    List<FieldApplication> findByField(Long fieldId);
    List<FieldApplication> findByDateRange(LocalDate from, LocalDate to);
    Optional<FieldApplication> findById(Long id);
    
    /**
     * US-01: Save with dose check
     * Note: SENASA status field not present in model — skip that validation
     */
    FieldApplication save(FieldApplication application);
    FieldApplication update(Long id, FieldApplication application);
    void delete(Long id);
    
    // US-05: Traceability by lot
    List<FieldApplication> findByLotForTraceability(Long lotId);
    
    // US-06: Count last 7 days
    Long countLast7Days();
    Long countToday();
}
