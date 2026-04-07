package pe.edu.vallegrande.mybackend.service;

import pe.edu.vallegrande.mybackend.model.Producto;
import java.util.List;
import java.util.Optional;

public interface ProductoService {
    List<Producto> listar();
    Optional<Producto> listarPorId(Long id);
    List<Producto> listarPorEstado(Boolean estado);
    Producto crear(Producto producto);
    Producto editar(Producto producto);
    void eliminar(Long id);
    void restaurar(Long id);
}