package pe.edu.vallegrande.mybackend.repository;

import pe.edu.vallegrande.mybackend.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface VentaRepository extends JpaRepository<Venta, Long> {

    @Query("SELECT DISTINCT v FROM Venta v " +
           "LEFT JOIN FETCH v.cliente " +
           "LEFT JOIN FETCH v.detalles d " +
           "LEFT JOIN FETCH d.producto " +
           "ORDER BY v.id DESC")
    List<Venta> findAllWithDetails();
}
