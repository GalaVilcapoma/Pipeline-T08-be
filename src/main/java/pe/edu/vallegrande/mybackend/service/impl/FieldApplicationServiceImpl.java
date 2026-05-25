package pe.edu.vallegrande.mybackend.service.impl;

import pe.edu.vallegrande.mybackend.model.Agrochemical;
import pe.edu.vallegrande.mybackend.model.FieldApplication;
import pe.edu.vallegrande.mybackend.repository.AgrochemicalRepository;
import pe.edu.vallegrande.mybackend.repository.FieldApplicationRepository;
import pe.edu.vallegrande.mybackend.repository.ProductionLotRepository;
import pe.edu.vallegrande.mybackend.service.FieldApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class FieldApplicationServiceImpl implements FieldApplicationService {

    @Autowired private FieldApplicationRepository appRepo;
    @Autowired private AgrochemicalRepository agroRepo;
    @Autowired private ProductionLotRepository lotRepo;

    @Override
    public List<FieldApplication> findAll() {
        return appRepo.findAll();
    }

    @Override
    public List<FieldApplication> findByProducer(Long producerId) {
        return appRepo.findByProducerId(producerId);
    }

    @Override
    public List<FieldApplication> findByField(Long fieldId) {
        return appRepo.findByFieldId(fieldId);
    }

    @Override
    public List<FieldApplication> findByDateRange(LocalDate from, LocalDate to) {
        return appRepo.findByApplicationDateBetween(from, to);
    }

    @Override
    public Optional<FieldApplication> findById(Long id) {
        return appRepo.findById(id);
    }

    @Override
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

    @Override
    public FieldApplication update(Long id, FieldApplication application) {
        application.setId(id);
        return appRepo.save(application);
    }

    @Override
    public void delete(Long id) {
        appRepo.deleteById(id);
    }

    @Override
    public List<FieldApplication> findByLotForTraceability(Long lotId) {
        return appRepo.findByLotForTraceability(lotId);
    }

    @Override
    public Long countLast7Days() {
        return appRepo.countApplicationsSince(LocalDate.now().minusDays(7));
    }

    @Override
    public Long countToday() {
        return appRepo.countApplicationsSince(LocalDate.now());
    }
}
