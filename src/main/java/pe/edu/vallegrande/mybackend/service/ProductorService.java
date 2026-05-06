package pe.edu.vallegrande.mybackend.service;

import pe.edu.vallegrande.mybackend.model.Productor;
import java.util.List;
import java.util.Optional;

public interface ProductorService {
    List<Productor> buscarTodos();
    List<Productor> buscarActivos();
    Optional<Productor> buscarPorId(Long id);
    Productor crear(Productor productor);
    Productor actualizar(Long id, Productor productor);
    Productor eliminarLogico(Long id);
    Productor restaurarLogico(Long id);
}
