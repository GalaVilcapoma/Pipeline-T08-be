package pe.edu.vallegrande.mybackend.service.impl;

import pe.edu.vallegrande.mybackend.model.Producto;
import pe.edu.vallegrande.mybackend.repository.ProductoRepository;
import pe.edu.vallegrande.mybackend.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public Page<Producto> listarConFiltros(String nombre, String categoria, Boolean estado, Pageable pageable) {
        // Si nombre o categoria son vacíos, tratarlos como null para que el query los ignore
        String nombreFiltro = (nombre != null && !nombre.trim().isEmpty()) ? nombre.trim() : null;
        String categoriaFiltro = (categoria != null && !categoria.trim().isEmpty()) ? categoria.trim() : null;
        return productoRepository.buscarConFiltros(nombreFiltro, categoriaFiltro, estado, pageable);
    }

    @Override
    public List<Producto> listar() {
        return productoRepository.findAll();
    }

    @Override
    public Optional<Producto> listarPorId(Long id) {
        return productoRepository.findById(id);
    }

    @Override
    public List<Producto> listarPorEstado(Boolean estado) {
        return productoRepository.findByEstado(estado);
    }

    @Override
    public Producto crear(Producto producto) {
        producto.setId(null);           // forzar INSERT
        producto.setCreatedAt(LocalDateTime.now());
        producto.setUpdatedAt(null);
        producto.setDeletedAt(null);
        producto.setRestoredAt(null);
        if (producto.getEstado() == null) {
            producto.setEstado(true);
        }
        return productoRepository.save(producto);
    }

    @Override
    public Producto editar(Producto producto) {
        Producto existing = productoRepository.findById(producto.getId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + producto.getId()));

        producto.setCreatedAt(existing.getCreatedAt());
        producto.setDeletedAt(existing.getDeletedAt());
        producto.setRestoredAt(existing.getRestoredAt());
        producto.setUpdatedAt(LocalDateTime.now());

        if (producto.getEstado() == null) {
            producto.setEstado(existing.getEstado());
        }

        return productoRepository.save(producto);
    }

    @Override
    public Producto eliminar(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));

        producto.setEstado(false);
        producto.setDeletedAt(LocalDateTime.now());
        return productoRepository.save(producto);
    }

    @Override
    public Producto restaurar(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));

        producto.setEstado(true);
        producto.setRestoredAt(LocalDateTime.now());
        return productoRepository.save(producto);
    }

    @Override
    public List<String> obtenerCategorias() {
        return productoRepository.findDistinctCategorias();
    }
}
