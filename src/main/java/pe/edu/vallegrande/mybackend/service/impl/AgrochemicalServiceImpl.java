package pe.edu.vallegrande.mybackend.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.vallegrande.mybackend.model.Agrochemical;
import pe.edu.vallegrande.mybackend.repository.AgrochemicalRepository;
import pe.edu.vallegrande.mybackend.service.AgrochemicalService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AgrochemicalServiceImpl implements AgrochemicalService {

    @Autowired
    private AgrochemicalRepository agrochemicalRepository;

    @Override
    public List<Agrochemical> findAll() {
        return agrochemicalRepository.findAll();
    }

    @Override
    public List<Agrochemical> findByActive(Boolean active) {
        return agrochemicalRepository.findByActive(active);
    }

    @Override
    public Optional<Agrochemical> findById(Long id) {
        return agrochemicalRepository.findById(id);
    }

    @Override
    public Agrochemical save(Agrochemical agrochemical) {
        return agrochemicalRepository.save(agrochemical);
    }

    @Override
    public Agrochemical update(Long id, Agrochemical agrochemical) {
        agrochemical.setId(id);
        return agrochemicalRepository.save(agrochemical);
    }

    @Override
    public Agrochemical updateSenasaStatus(Long id, String status) {
        Agrochemical agro = agrochemicalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agrochemical not found: " + id));
        agro.setSenasaStatus(status);
        return agrochemicalRepository.save(agro);
    }

    @Override
    public List<Agrochemical> getActiveForSelection() {
        return agrochemicalRepository.findByActive(true).stream()
                .filter(a -> "ACTIVE".equals(a.getSenasaStatus()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Agrochemical> getExpiringSoon() {
        LocalDate threshold = LocalDate.now().plusDays(30);
        return agrochemicalRepository.findByActive(true).stream()
                .filter(a -> a.getRegistrationExpiry() != null && a.getRegistrationExpiry().isBefore(threshold))
                .collect(Collectors.toList());
    }
}
