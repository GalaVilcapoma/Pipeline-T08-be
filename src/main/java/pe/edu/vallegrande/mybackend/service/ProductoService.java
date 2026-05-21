package pe.edu.vallegrande.mybackend.service;

import pe.edu.vallegrande.mybackend.model.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface ProductoService {
    Page<Producto> listarConFiltros(String nombre, String categoria, Boolean estado, Pageable pageable);
    List<Producto> listar();
    Optional<Producto> listarPorId(Long id);
    List<Producto> listarPorEstado(Boolean estado);
    Producto crear(Producto producto);
    Producto editar(Producto producto);
    Producto eliminar(Long id);
    Producto restaurar(Long id);
    List<String> obtenerCategorias();
}