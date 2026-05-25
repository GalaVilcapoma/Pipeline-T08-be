package pe.edu.vallegrande.mybackend.service.impl;

import pe.edu.vallegrande.mybackend.model.InventoryDelivery;
import pe.edu.vallegrande.mybackend.repository.InventoryDeliveryRepository;
import pe.edu.vallegrande.mybackend.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class InventoryServiceImpl implements InventoryService {

    @Autowired private InventoryDeliveryRepository repo;

    @Override
    public List<InventoryDelivery> findAll() {
        return repo.findAll();
    }

    @Override
    public List<InventoryDelivery> findByProducer(Long producerId) {
        return repo.findByProducerId(producerId);
    }

    @Override
    public InventoryDelivery registerDelivery(InventoryDelivery delivery) {
        return repo.save(delivery);
    }

    @Override
    public List<Object[]> getProducerStock(Long producerId) {
        return repo.sumDeliveredByProducer(producerId);
    }

    @Override
    public List<Object[]> getCentralStock() {
        return repo.sumAllDeliveries();
    }
}
