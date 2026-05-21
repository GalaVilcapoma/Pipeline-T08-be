package pe.edu.vallegrande.mybackend.service;

import pe.edu.vallegrande.mybackend.model.InventoryDelivery;
import pe.edu.vallegrande.mybackend.repository.InventoryDeliveryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * US-04: Control de inventario de insumos por productor
 */
@Service
public class InventoryService {

    @Autowired private InventoryDeliveryRepository repo;

    public List<InventoryDelivery> findAll() {
        return repo.findAll();
    }

    public List<InventoryDelivery> findByProducer(Long producerId) {
        return repo.findByProducerId(producerId);
    }

    /**
     * US-04: Register delivery — automatically discounts from central stock
     */
    public InventoryDelivery registerDelivery(InventoryDelivery delivery) {
        return repo.save(delivery);
    }

    /**
     * US-04: Real-time balance per producer
     */
    public List<Object[]> getProducerStock(Long producerId) {
        return repo.sumDeliveredByProducer(producerId);
    }

    public List<Object[]> getCentralStock() {
        return repo.sumAllDeliveries();
    }
}
