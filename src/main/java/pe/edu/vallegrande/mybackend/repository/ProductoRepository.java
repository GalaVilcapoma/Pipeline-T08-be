package pe.edu.vallegrande.mybackend.repository;

import pe.edu.vallegrande.mybackend.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByEstado(Boolean estado);
}



