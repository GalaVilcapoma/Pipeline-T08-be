package pe.edu.vallegrande.mybackend.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.vallegrande.mybackend.dto.*;
import pe.edu.vallegrande.mybackend.model.*;
import pe.edu.vallegrande.mybackend.model.CustomerOrder.OrderStatus;
import pe.edu.vallegrande.mybackend.repository.*;
import pe.edu.vallegrande.mybackend.service.CustomerOrderService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CustomerOrderServiceImpl implements CustomerOrderService {

    private static final BigDecimal IGV_RATE = new BigDecimal("0.18");

    @Autowired private CustomerOrderRepository orderRepository;
    @Autowired private CustomerOrderDetailRepository detailRepository;
    @Autowired private CustomerRepository customerRepository;
    @Autowired private ProductoRepository productoRepository;

    // ─────────────────────────────────────────────────────────────────────────
    // CREAR PEDIDO — Transacción completa cabecera + detalle
    // ─────────────────────────────────────────────────────────────────────────
    @Override
    @Transactional
    public CustomerOrderResponse crearPedido(CustomerOrderRequest request) {
        log.info("Iniciando creación de pedido para customerId={}", request.getCustomerId());

        // 1. Validar que el request tenga detalles
        if (request.getDetalles() == null || request.getDetalles().isEmpty()) {
            throw new IllegalArgumentException("El pedido debe tener al menos un producto en el detalle.");
        }

        // 2. Validar cliente activo
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException(
                        "Cliente no encontrado con ID: " + request.getCustomerId()));

        if (!Boolean.TRUE.equals(customer.getActive())) {
            throw new IllegalStateException(
                    "El cliente '" + customer.getRazonSocial() + "' está inactivo. No se puede registrar el pedido.");
        }

        // 3. Construir cabecera
        CustomerOrder order = new CustomerOrder();
        order.setCustomer(customer);
        order.setNumeroPedido(generarNumeroPedido());
        order.setFechaPedido(LocalDate.now());
        order.setFechaEntregaEstimada(request.getFechaEntregaEstimada());
        order.setObservaciones(request.getObservaciones());
        order.setEstado(OrderStatus.CONFIRMADO);
        order.setActive(true);

        // 4. Construir detalles, validar stock y calcular totales
        List<CustomerOrderDetail> detalles = new ArrayList<>();
        BigDecimal subtotalTotal = BigDecimal.ZERO;

        for (CustomerOrderDetailRequest detalleReq : request.getDetalles()) {

            // 4a. Validar producto activo
            Producto producto = productoRepository.findById(detalleReq.getProductoId())
                    .orElseThrow(() -> new RuntimeException(
                            "Producto no encontrado con ID: " + detalleReq.getProductoId()));

            if (!Boolean.TRUE.equals(producto.getEstado())) {
                throw new IllegalStateException(
                        "El producto '" + producto.getNombre() + "' está inactivo.");
            }

            // 4b. Validar cantidad positiva
            if (detalleReq.getCantidad() == null || detalleReq.getCantidad() <= 0) {
                throw new IllegalArgumentException(
                        "La cantidad para el producto '" + producto.getNombre() + "' debe ser mayor a 0.");
            }

            // 4c. Validar stock disponible
            if (producto.getStockActual() < detalleReq.getCantidad()) {
                throw new IllegalStateException(
                        "Stock insuficiente para '" + producto.getNombre() +
                        "'. Stock disponible: " + producto.getStockActual() +
                        ", cantidad solicitada: " + detalleReq.getCantidad());
            }

            // 4d. Calcular subtotal de la línea
            BigDecimal precioUnitario = producto.getPrecioUnitario();
            BigDecimal subtotalLinea = precioUnitario
                    .multiply(BigDecimal.valueOf(detalleReq.getCantidad()))
                    .setScale(2, RoundingMode.HALF_UP);

            // 4e. Construir detalle
            CustomerOrderDetail detalle = new CustomerOrderDetail();
            detalle.setCustomerOrder(order);
            detalle.setProducto(producto);
            detalle.setCantidad(detalleReq.getCantidad());
            detalle.setPrecioUnitario(precioUnitario);
            detalle.setSubtotal(subtotalLinea);
            detalle.setObservacion(detalleReq.getObservacion());

            detalles.add(detalle);
            subtotalTotal = subtotalTotal.add(subtotalLinea);

            // 4f. Descontar stock del producto
            producto.setStockActual(producto.getStockActual() - detalleReq.getCantidad());
            productoRepository.save(producto);
            log.info("Stock descontado: producto='{}', nuevo stock={}", producto.getNombre(), producto.getStockActual());
        }

        // 5. Calcular IGV y total de la cabecera
        BigDecimal igv = subtotalTotal.multiply(IGV_RATE).setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = subtotalTotal.add(igv).setScale(2, RoundingMode.HALF_UP);

        order.setSubtotal(subtotalTotal);
        order.setIgv(igv);
        order.setTotal(total);
        order.setDetalles(detalles);

        // 6. Guardar cabecera + detalles en una sola operación (cascade)
        CustomerOrder savedOrder = orderRepository.save(order);
        log.info("Pedido creado exitosamente: numeroPedido={}, total={}", savedOrder.getNumeroPedido(), savedOrder.getTotal());

        return toResponse(savedOrder);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // LISTAR TODOS
    // ─────────────────────────────────────────────────────────────────────────
    @Override
    @Transactional(readOnly = true)
    public List<CustomerOrderResponse> listarTodos() {
        log.info("Listando todos los pedidos activos");
        return orderRepository.findByActiveTrue()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ─────────────────────────────────────────────────────────────────────────
    // LISTAR CON FILTROS Y PAGINACIÓN
    // ─────────────────────────────────────────────────────────────────────────
    @Override
    @Transactional(readOnly = true)
    public Page<CustomerOrderResponse> listarConFiltros(
            Long customerId, OrderStatus estado,
            LocalDate fechaDesde, LocalDate fechaHasta, Pageable pageable) {

        log.info("Listando pedidos con filtros: customerId={}, estado={}", customerId, estado);
        return orderRepository.buscarConFiltros(customerId, estado, fechaDesde, fechaHasta, pageable)
                .map(this::toResponse);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // BUSCAR POR ID
    // ─────────────────────────────────────────────────────────────────────────
    @Override
    @Transactional(readOnly = true)
    public Optional<CustomerOrderResponse> findById(Long id) {
        return orderRepository.findById(id).map(this::toResponse);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // BUSCAR POR NÚMERO DE PEDIDO
    // ─────────────────────────────────────────────────────────────────────────
    @Override
    @Transactional(readOnly = true)
    public Optional<CustomerOrderResponse> findByNumeroPedido(String numeroPedido) {
        return orderRepository.findByNumeroPedido(numeroPedido).map(this::toResponse);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // LISTAR POR CLIENTE
    // ─────────────────────────────────────────────────────────────────────────
    @Override
    @Transactional(readOnly = true)
    public List<CustomerOrderResponse> listarPorCliente(Long customerId) {
        log.info("Listando pedidos del cliente ID={}", customerId);
        return orderRepository.findByCustomerIdAndActiveTrue(customerId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ─────────────────────────────────────────────────────────────────────────
    // CANCELAR PEDIDO — Devuelve stock a los productos
    // ─────────────────────────────────────────────────────────────────────────
    @Override
    @Transactional
    public CustomerOrderResponse cancelarPedido(Long id) {
        log.info("Cancelando pedido ID={}", id);

        CustomerOrder order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con ID: " + id));

        if (order.getEstado() == OrderStatus.CANCELADO) {
            throw new IllegalStateException("El pedido ya está cancelado.");
        }

        if (order.getEstado() == OrderStatus.ENTREGADO) {
            throw new IllegalStateException("No se puede cancelar un pedido ya entregado.");
        }

        // Devolver stock a cada producto
        for (CustomerOrderDetail detalle : order.getDetalles()) {
            Producto producto = detalle.getProducto();
            producto.setStockActual(producto.getStockActual() + detalle.getCantidad());
            productoRepository.save(producto);
            log.info("Stock devuelto: producto='{}', nuevo stock={}", producto.getNombre(), producto.getStockActual());
        }

        order.setEstado(OrderStatus.CANCELADO);
        CustomerOrder saved = orderRepository.save(order);
        log.info("Pedido cancelado: numeroPedido={}", saved.getNumeroPedido());

        return toResponse(saved);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // HELPERS PRIVADOS
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Genera número de pedido con formato: PED-YYYYMMDD-NNN
     * Ejemplo: PED-20260522-001
     */
    private String generarNumeroPedido() {
        LocalDate hoy = LocalDate.now();
        String fecha = hoy.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        Long correlativo = orderRepository.countByFechaPedido(hoy) + 1;
        return String.format("PED-%s-%03d", fecha, correlativo);
    }

    /**
     * Mapea entidad CustomerOrder → CustomerOrderResponse (DTO)
     */
    private CustomerOrderResponse toResponse(CustomerOrder order) {
        CustomerOrderResponse response = new CustomerOrderResponse();
        response.setId(order.getId());
        response.setNumeroPedido(order.getNumeroPedido());

        // Datos del cliente
        Customer c = order.getCustomer();
        response.setCustomerId(c.getId());
        response.setCustomerRazonSocial(c.getRazonSocial());
        response.setCustomerNumeroDocumento(c.getNumeroDocumento());
        response.setCustomerPais(c.getPais());

        // Fechas y estado
        response.setFechaPedido(order.getFechaPedido());
        response.setFechaEntregaEstimada(order.getFechaEntregaEstimada());
        response.setEstado(order.getEstado());
        response.setObservaciones(order.getObservaciones());

        // Totales
        response.setSubtotal(order.getSubtotal());
        response.setIgv(order.getIgv());
        response.setTotal(order.getTotal());

        // Audit
        response.setCreatedAt(order.getCreatedAt());
        response.setUpdatedAt(order.getUpdatedAt());
        response.setActive(order.getActive());

        // Detalles
        List<CustomerOrderDetailResponse> detallesResponse = order.getDetalles()
                .stream()
                .map(this::toDetailResponse)
                .collect(Collectors.toList());
        response.setDetalles(detallesResponse);

        return response;
    }

    /**
     * Mapea entidad CustomerOrderDetail → CustomerOrderDetailResponse (DTO)
     */
    private CustomerOrderDetailResponse toDetailResponse(CustomerOrderDetail detalle) {
        CustomerOrderDetailResponse dr = new CustomerOrderDetailResponse();
        dr.setId(detalle.getId());

        Producto p = detalle.getProducto();
        dr.setProductoId(p.getId());
        dr.setProductoCodigo(p.getCodigo());
        dr.setProductoNombre(p.getNombre());
        dr.setProductoUnidadMedida(p.getUnidadMedida());

        dr.setCantidad(detalle.getCantidad());
        dr.setPrecioUnitario(detalle.getPrecioUnitario());
        dr.setSubtotal(detalle.getSubtotal());
        dr.setObservacion(detalle.getObservacion());
        dr.setCreatedAt(detalle.getCreatedAt());

        return dr;
    }
}
