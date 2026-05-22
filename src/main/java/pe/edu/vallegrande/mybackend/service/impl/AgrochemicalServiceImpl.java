package pe.edu.vallegrande.mybackend.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.vallegrande.mybackend.model.Agrochemical;
import pe.edu.vallegrande.mybackend.repository.AgrochemicalRepository;
import pe.edu.vallegrande.mybackend.service.AgrochemicalService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AgrochemicalServiceImpl implements AgrochemicalService {

    @Autowired
    private AgrochemicalRepository agrochemicalRepository;

    @Override
    public List<Agrochemical> findAll() {
        return agrochemicalRepository.findAll();
    }

    @Override
    public List<Agrochemical> findAllActive() {
        return agrochemicalRepository.findByActiveTrue();
    }

    @Override
    public List<Agrochemical> findAllInactive() {
        return agrochemicalRepository.findByActiveFalse();
    }

    @Override
    public Optional<Agrochemical> findById(Long id) {
        return agrochemicalRepository.findById(id);
    }

    @Override
    public Agrochemical create(Agrochemical agrochemical) {
        agrochemical.setId(null);
        agrochemical.setActive(true);
        agrochemical.setDeletedAt(null);
        agrochemical.setRestoredAt(null);
        return agrochemicalRepository.save(agrochemical);
    }

    @Override
    public Agrochemical update(Long id, Agrochemical incoming) {
        Agrochemical existing = agrochemicalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agrochemical not found: " + id));

        existing.setCommercialName(incoming.getCommercialName());
        existing.setActiveIngredient(incoming.getActiveIngredient());
        existing.setCategory(incoming.getCategory());
        existing.setSenasaRegistrationNumber(incoming.getSenasaRegistrationNumber());
        existing.setRegistrationExpiry(incoming.getRegistrationExpiry());
        existing.setMaxDose(incoming.getMaxDose());
        existing.setWaitingPeriodDays(incoming.getWaitingPeriodDays());
        existing.setManufacturer(incoming.getManufacturer());

        return agrochemicalRepository.save(existing);
    }

    @Override
    public Agrochemical delete(Long id) {
        Agrochemical agrochemical = agrochemicalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agrochemical not found: " + id));

        if (!agrochemical.getActive()) {
            throw new RuntimeException("Agrochemical already inactive: " + id);
        }

        agrochemical.setActive(false);
        agrochemical.setDeletedAt(LocalDateTime.now());
        return agrochemicalRepository.save(agrochemical);
    }

    @Override
    public Agrochemical restore(Long id) {
        Agrochemical agrochemical = agrochemicalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agrochemical not found: " + id));

        if (agrochemical.getActive()) {
            throw new RuntimeException("Agrochemical already active: " + id);
        }

        agrochemical.setActive(true);
        agrochemical.setRestoredAt(LocalDateTime.now());
        agrochemical.setDeletedAt(null);
        return agrochemicalRepository.save(agrochemical);
    }
}
