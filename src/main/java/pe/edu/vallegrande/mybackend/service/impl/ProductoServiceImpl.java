package pe.edu.vallegrande.mybackend.service.impl;

import pe.edu.vallegrande.mybackend.model.Producto;
import pe.edu.vallegrande.mybackend.repository.ProductoRepository;
import pe.edu.vallegrande.mybackend.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

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
        return productoRepository.save(producto);
    }

    @Override
    public Producto editar(Producto producto) {
        return productoRepository.save(producto);
    }

    @Override
    public void eliminar(Long id) {
        productoRepository.findById(id).ifPresent(p -> {
            p.setEstado(false);
            productoRepository.save(p);
        });
    }

    @Override
    public void restaurar(Long id) {
        productoRepository.findById(id).ifPresent(p -> {
            p.setEstado(true);
            productoRepository.save(p);
        });
    }
}