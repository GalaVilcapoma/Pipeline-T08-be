package pe.edu.vallegrande.mybackend.service;

import pe.edu.vallegrande.mybackend.dto.CustomerOrderRequest;
import pe.edu.vallegrande.mybackend.dto.CustomerOrderResponse;
import pe.edu.vallegrande.mybackend.model.CustomerOrder.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CustomerOrderService {

    /**
     * Crea la transacción completa: cabecera + detalle en una sola operación.
     * Valida cliente activo, valida stock de productos, descuenta stock,
     * calcula subtotales, IGV y total automáticamente.
     */
    CustomerOrderResponse crearPedido(CustomerOrderRequest request);

    /** Listar todos los pedidos activos */
    List<CustomerOrderResponse> listarTodos();

    /** Listar pedidos con filtros y paginación */
    Page<CustomerOrderResponse> listarConFiltros(
        Long customerId,
        OrderStatus estado,
        LocalDate fechaDesde,
        LocalDate fechaHasta,
        Pageable pageable
    );

    /** Obtener pedido por ID */
    Optional<CustomerOrderResponse> findById(Long id);

    /** Obtener pedido por número de pedido */
    Optional<CustomerOrderResponse> findByNumeroPedido(String numeroPedido);

    /** Listar pedidos de un cliente específico */
    List<CustomerOrderResponse> listarPorCliente(Long customerId);

    /** Cancelar un pedido (devuelve stock) */
    CustomerOrderResponse cancelarPedido(Long id);
}
