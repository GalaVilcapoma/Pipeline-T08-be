package pe.edu.vallegrande.mybackend.service;

import pe.edu.vallegrande.mybackend.dto.VentaRequest;
import pe.edu.vallegrande.mybackend.dto.VentaResponse;
import java.util.List;

public interface VentaService {
    VentaResponse crearVenta(VentaRequest request);
    List<VentaResponse> listarVentas();
}
