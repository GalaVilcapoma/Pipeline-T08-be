package pe.edu.vallegrande.mybackend.repository;

import pe.edu.vallegrande.mybackend.model.CustomerOrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerOrderDetailRepository extends JpaRepository<CustomerOrderDetail, Long> {

    /** Obtener todos los detalles de un pedido */
    List<CustomerOrderDetail> findByCustomerOrderId(Long orderId);
}
