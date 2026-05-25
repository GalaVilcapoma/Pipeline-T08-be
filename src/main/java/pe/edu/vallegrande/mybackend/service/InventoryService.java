package pe.edu.vallegrande.mybackend.service;

import pe.edu.vallegrande.mybackend.model.InventoryDelivery;
import java.util.List;

/**
 * US-04: Control de inventario de insumos por productor
 */
public interface InventoryService {
    List<InventoryDelivery> findAll();
    List<InventoryDelivery> findByProducer(Long producerId);
    
    /**
     * US-04: Register delivery — automatically discounts from central stock
     */
    InventoryDelivery registerDelivery(InventoryDelivery delivery);
    
    /**
     * US-04: Real-time balance per producer
     */
    List<Object[]> getProducerStock(Long producerId);
    List<Object[]> getCentralStock();
}
