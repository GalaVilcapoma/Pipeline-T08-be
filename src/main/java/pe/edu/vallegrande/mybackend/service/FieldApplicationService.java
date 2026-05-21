package pe.edu.vallegrande.mybackend.service;

import pe.edu.vallegrande.mybackend.model.Agrochemical;
import pe.edu.vallegrande.mybackend.model.FieldApplication;
import pe.edu.vallegrande.mybackend.model.ProductionLot;
import pe.edu.vallegrande.mybackend.repository.AgrochemicalRepository;
import pe.edu.vallegrande.mybackend.repository.FieldApplicationRepository;
import pe.edu.vallegrande.mybackend.repository.ProductionLotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * US-01: Registro de aplicación de agroquímicos en campo
 * - Valida SENASA antes de guardar
 * - Vincula al lote activo del campo
 * - Detecta si la dosis supera el máximo
 */
@Service
public class FieldApplicationService {

    @Autowired private FieldApplicationRepository appRepo;
    @Autowired private AgrochemicalRepository agroRepo;
    @Autowired private ProductionLotRepository lotRepo;

    public List<FieldApplication> findAll() {
        return appRepo.findAll();
    }

    public List<FieldApplication> findByProducer(Long producerId) {
        return appRepo.findByProducerId(producerId);
    }

    public List<FieldApplication> findByField(Long fieldId) {
        return appRepo.findByFieldId(fieldId);
    }

    public List<FieldApplication> findByDateRange(LocalDate from, LocalDate to) {
        return appRepo.findByApplicationDateBetween(from, to);
    }

    public Optional<FieldApplication> findById(Long id) {
        return appRepo.findById(id);
    }

    /**
     * US-01: Save with dose check
     * Note: SENASA status field not present in model — skip that validation
     */
    public FieldApplication save(FieldApplication application) {
        Agrochemical agro = agroRepo.findById(application.getAgrochemical().getId())
                .orElseThrow(() -> new RuntimeException("Agrochemical not found"));

        // Mark SENASA valid if agrochemical is active and not expired
        boolean senasaValid = agro.getActive() &&
                agro.getRegistrationExpiry().isAfter(LocalDate.now());
        application.setSenasaValid(senasaValid);

        if (!senasaValid) {
            throw new IllegalStateException(
                "Agrochemical '" + agro.getCommercialName() + "' is not active or its SENASA registration has expired."
            );
        }

        // US-09: Check dose exceeded — compare Double vs BigDecimal safely
        boolean doseExceeded = application.getDose() != null &&
                agro.getMaxDose() != null &&
                application.getDose().compareTo(agro.getMaxDose().doubleValue()) > 0;
        application.setDoseExceeded(doseExceeded);

        return appRepo.save(application);
    }

    public FieldApplication update(Long id, FieldApplication application) {
        application.setId(id);
        return appRepo.save(application);
    }

    public void delete(Long id) {
        appRepo.deleteById(id);
    }

    // US-05: Traceability by lot
    public List<FieldApplication> findByLotForTraceability(Long lotId) {
        return appRepo.findByLotForTraceability(lotId);
    }

    // US-06: Count last 7 days
    public Long countLast7Days() {
        return appRepo.countApplicationsSince(LocalDate.now().minusDays(7));
    }

    public Long countToday() {
        return appRepo.countApplicationsSince(LocalDate.now());
    }
}
