package pe.edu.vallegrande.mybackend.service;

import pe.edu.vallegrande.mybackend.model.Agrochemical;
import java.util.List;
import java.util.Optional;

public interface AgrochemicalService {
    List<Agrochemical> findAll();
    List<Agrochemical> findAllActive();
    List<Agrochemical> findAllInactive();
    Optional<Agrochemical> findById(Long id);
    Agrochemical create(Agrochemical agrochemical);
    Agrochemical update(Long id, Agrochemical agrochemical);
    Agrochemical delete(Long id);
    Agrochemical restore(Long id);
}
