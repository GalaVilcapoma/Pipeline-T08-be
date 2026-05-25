package pe.edu.vallegrande.mybackend.dto;

import lombok.Data;

@Data
public class VentaDetalleRequest {
    private Long productoId;
    private Integer cantidad;
}
