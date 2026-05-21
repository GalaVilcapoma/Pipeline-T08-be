package pe.edu.vallegrande.mybackend.service;

import pe.edu.vallegrande.mybackend.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface CustomerService {
    Page<Customer> findAllConFiltros(String razonSocial, String pais, String tipoCliente, Boolean active, Pageable pageable);
    List<Customer> findAll();
    List<Customer> findByActive(Boolean active);
    Optional<Customer> findById(Long id);
    Customer save(Customer customer);
    Customer update(Customer customer);
    Customer delete(Long id);
    Customer restore(Long id);
}
