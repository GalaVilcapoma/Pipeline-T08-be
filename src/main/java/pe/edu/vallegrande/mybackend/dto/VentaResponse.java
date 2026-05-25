package pe.edu.vallegrande.mybackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class VentaResponse {
    private Long id;
    private Long clienteId;
    private String clienteRazonSocial;
    private LocalDateTime fecha;
    private BigDecimal subtotal;
    private BigDecimal total;
    private List<VentaDetalleResponse> detalles;
}
