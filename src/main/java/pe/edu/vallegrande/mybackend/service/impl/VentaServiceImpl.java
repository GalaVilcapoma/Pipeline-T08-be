package pe.edu.vallegrande.mybackend.service.impl;

import pe.edu.vallegrande.mybackend.dto.VentaDetalleRequest;
import pe.edu.vallegrande.mybackend.dto.VentaDetalleResponse;
import pe.edu.vallegrande.mybackend.dto.VentaRequest;
import pe.edu.vallegrande.mybackend.dto.VentaResponse;
import pe.edu.vallegrande.mybackend.model.Customer;
import pe.edu.vallegrande.mybackend.model.Producto;
import pe.edu.vallegrande.mybackend.model.Venta;
import pe.edu.vallegrande.mybackend.model.VentaDetalle;
import pe.edu.vallegrande.mybackend.repository.CustomerRepository;
import pe.edu.vallegrande.mybackend.repository.ProductoRepository;
import pe.edu.vallegrande.mybackend.repository.VentaRepository;
import pe.edu.vallegrande.mybackend.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class VentaServiceImpl implements VentaService {

    @Autowired private VentaRepository ventaRepository;
    @Autowired private CustomerRepository customerRepository;
    @Autowired private ProductoRepository productoRepository;

    @Override
    @Transactional
    public VentaResponse crearVenta(VentaRequest request) {
        if (request == null || request.getClienteId() == null) {
            throw new RuntimeException("Cliente requerido para registrar la transaccion");
        }
        if (request.getDetalles() == null || request.getDetalles().isEmpty()) {
            throw new RuntimeException("Se requiere al menos un detalle para la transaccion");
        }

        Customer cliente = customerRepository.findById(request.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + request.getClienteId()));
        if (Boolean.FALSE.equals(cliente.getActive())) {
            throw new RuntimeException("Cliente inactivo: " + request.getClienteId());
        }

        Venta venta = new Venta();
        venta.setCliente(cliente);
        venta.setFecha(LocalDateTime.now());

        BigDecimal subtotal = BigDecimal.ZERO;
        List<VentaDetalle> detalles = new ArrayList<>();

        for (VentaDetalleRequest item : request.getDetalles()) {
            if (item.getProductoId() == null || item.getCantidad() == null) {
                throw new RuntimeException("Detalle incompleto: producto y cantidad son obligatorios");
            }
            if (item.getCantidad() <= 0) {
                throw new RuntimeException("Cantidad invalida para producto ID: " + item.getProductoId());
            }

            Producto producto = productoRepository.findById(item.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + item.getProductoId()));
            if (Boolean.FALSE.equals(producto.getEstado())) {
                throw new RuntimeException("Producto inactivo: " + item.getProductoId());
            }

            int stockActual = producto.getStockActual() == null ? 0 : producto.getStockActual();
            if (stockActual < item.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para producto ID: " + item.getProductoId());
            }

            BigDecimal precioUnitario = producto.getPrecioUnitario();
            BigDecimal lineSubtotal = precioUnitario.multiply(BigDecimal.valueOf(item.getCantidad()));

            VentaDetalle detalle = new VentaDetalle();
            detalle.setVenta(venta);
            detalle.setProducto(producto);
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioUnitario(precioUnitario);
            detalle.setSubtotal(lineSubtotal);

            detalles.add(detalle);
            subtotal = subtotal.add(lineSubtotal);

            producto.setStockActual(stockActual - item.getCantidad());
        }

        venta.setDetalles(detalles);
        venta.setSubtotal(subtotal);
        venta.setTotal(subtotal);

        Venta saved = ventaRepository.save(venta);
        return mapToResponse(saved);
    }

    @Override
    public List<VentaResponse> listarVentas() {
        List<Venta> ventas = ventaRepository.findAllWithDetails();
        List<VentaResponse> responses = new ArrayList<>();
        for (Venta venta : ventas) {
            responses.add(mapToResponse(venta));
        }
        return responses;
    }

    private VentaResponse mapToResponse(Venta venta) {
        List<VentaDetalleResponse> detalleResponses = new ArrayList<>();
        for (VentaDetalle detalle : venta.getDetalles()) {
            Producto producto = detalle.getProducto();
            detalleResponses.add(new VentaDetalleResponse(
                    detalle.getId(),
                    producto.getId(),
                    producto.getNombre(),
                    detalle.getCantidad(),
                    detalle.getPrecioUnitario(),
                    detalle.getSubtotal()
            ));
        }

        return new VentaResponse(
                venta.getId(),
                venta.getCliente().getId(),
                venta.getCliente().getRazonSocial(),
                venta.getFecha(),
                venta.getSubtotal(),
                venta.getTotal(),
                detalleResponses
        );
    }
}
