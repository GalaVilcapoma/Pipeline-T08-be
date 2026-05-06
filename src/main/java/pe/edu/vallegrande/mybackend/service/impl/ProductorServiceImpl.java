package pe.edu.vallegrande.mybackend.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.vallegrande.mybackend.model.Productor;
import pe.edu.vallegrande.mybackend.repository.ProductorRepository;
import pe.edu.vallegrande.mybackend.service.ProductorService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProductorServiceImpl implements ProductorService {

    @Autowired
    private ProductorRepository productorRepository;

    @Override
    public List<Productor> buscarTodos() {
        return productorRepository.findAll();
    }

    @Override
    public List<Productor> buscarActivos() {
        return productorRepository.findByEstado(true);
    }

    @Override
    public Optional<Productor> buscarPorId(Long id) {
        return productorRepository.findById(id);
    }

    @Override
    public Productor crear(Productor productor) {
        return productorRepository.save(productor);
    }

    @Override
    public Productor actualizar(Long id, Productor productor) {
        Productor existente = productorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Productor no encontrado con id: " + id));
        
        existente.setNombreCompleto(productor.getNombreCompleto());
        existente.setDni(productor.getDni());
        existente.setTelefono(productor.getTelefono());
        existente.setCorreo(productor.getCorreo());
        existente.setUbicacion(productor.getUbicacion());
        existente.setDistrito(productor.getDistrito());
        
        return productorRepository.save(existente);
    }

    @Override
    public Productor eliminarLogico(Long id) {
        Productor productor = productorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Productor no encontrado con id: " + id));
        productor.setEstado(false);
        productor.setFechaEliminacion(LocalDateTime.now());
        return productorRepository.save(productor);
    }

    @Override
    public Productor restaurarLogico(Long id) {
        Productor productor = productorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Productor no encontrado con id: " + id));
        productor.setEstado(true);
        productor.setFechaRestauracion(LocalDateTime.now());
        return productorRepository.save(productor);
    }
}
