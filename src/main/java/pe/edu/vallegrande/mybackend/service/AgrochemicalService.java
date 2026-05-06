package pe.edu.vallegrande.mybackend.service;

import pe.edu.vallegrande.mybackend.model.Agrochemical;
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
}
