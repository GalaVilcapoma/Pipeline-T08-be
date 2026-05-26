package pe.edu.vallegrande.mybackend.repository;

import pe.edu.vallegrande.mybackend.model.CustomerOrder;
import pe.edu.vallegrande.mybackend.model.CustomerOrder.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long> {

    /** Buscar por número de pedido */
    Optional<CustomerOrder> findByNumeroPedido(String numeroPedido);

    /** Listar pedidos activos de un cliente */
    List<CustomerOrder> findByCustomerIdAndActiveTrue(Long customerId);

    /** Listar todos los pedidos activos */
    List<CustomerOrder> findByActiveTrue();

    /** Listar pedidos por estado */
    List<CustomerOrder> findByEstadoAndActiveTrue(OrderStatus estado);

    /** Contar pedidos del día para generar número correlativo */
    @Query("SELECT COUNT(o) FROM CustomerOrder o WHERE o.fechaPedido = :fecha")
    Long countByFechaPedido(@Param("fecha") LocalDate fecha);

    /** Búsqueda con filtros y paginación */
    @Query("SELECT o FROM CustomerOrder o " +
           "JOIN o.customer c " +
           "WHERE (:customerId IS NULL OR c.id = :customerId) " +
           "AND (:estado IS NULL OR o.estado = :estado) " +
           "AND (:fechaDesde IS NULL OR o.fechaPedido >= :fechaDesde) " +
           "AND (:fechaHasta IS NULL OR o.fechaPedido <= :fechaHasta) " +
           "AND o.active = true")
    Page<CustomerOrder> buscarConFiltros(
        @Param("customerId") Long customerId,
        @Param("estado") OrderStatus estado,
        @Param("fechaDesde") LocalDate fechaDesde,
        @Param("fechaHasta") LocalDate fechaHasta,
        Pageable pageable
    );
}
