package pe.edu.vallegrande.mybackend.dto;

import lombok.Data;
import java.util.List;

@Data
public class VentaRequest {
    private Long clienteId;
    private List<VentaDetalleRequest> detalles;
}
