package pe.edu.vallegrande.mybackend.repository;

import pe.edu.vallegrande.mybackend.model.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    
    @Query("SELECT p FROM Producto p WHERE " +
           "(:nombre IS NULL OR LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))) AND " +
           "(:categoria IS NULL OR LOWER(p.categoria) LIKE LOWER(CONCAT('%', :categoria, '%'))) AND " +
           "(:estado IS NULL OR p.estado = :estado)")
    Page<Producto> buscarConFiltros(
        @Param("nombre") String nombre,
        @Param("categoria") String categoria,
        @Param("estado") Boolean estado,
        Pageable pageable
    );

    List<Producto> findByEstado(Boolean estado);

    @Query("SELECT DISTINCT p.categoria FROM Producto p WHERE p.estado = true")
    List<String> findDistinctCategorias();
}



