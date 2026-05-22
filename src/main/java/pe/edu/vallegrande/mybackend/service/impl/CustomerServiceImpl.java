package pe.edu.vallegrande.mybackend.service.impl;

import pe.edu.vallegrande.mybackend.model.Customer;
import pe.edu.vallegrande.mybackend.repository.CustomerRepository;
import pe.edu.vallegrande.mybackend.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Page<Customer> findAllConFiltros(String razonSocial, String pais, String tipoCliente, Boolean active, Pageable pageable) {
        log.info("Buscando clientes con filtros y paginación");
        String razonSocialFiltro = (razonSocial != null && !razonSocial.trim().isEmpty()) ? razonSocial.trim() : null;
        String paisFiltro = (pais != null && !pais.trim().isEmpty()) ? pais.trim() : null;
        String tipoClienteFiltro = (tipoCliente != null && !tipoCliente.trim().isEmpty()) ? tipoCliente.trim() : null;
        return customerRepository.buscarConFiltros(razonSocialFiltro, paisFiltro, tipoClienteFiltro, active, pageable);
    }

    @Override
    public List<Customer> findAll() {
        log.info("Listando Datos: ");
        return customerRepository.findAll();
    }

    @Override
    public List<Customer> findByActive(Boolean active) {
        log.info("Listando Datos por Estado Activo: " + active);
        return customerRepository.findByActive(active);
    }

    @Override
    public Optional<Customer> findById(Long id) {
        log.info("Listando Datos por ID: " + id);
        return customerRepository.findById(id);
    }

    @Override
    public Customer save(Customer customer) {
        log.info("Registrando Datos: " + customer.toString());
        customer.setId(null);           // forzar INSERT
        customer.setActive(true);
        customer.setCreatedAt(LocalDateTime.now());
        customer.setUpdatedAt(null);
        customer.setDeletedAt(null);
        customer.setRestoredAt(null);
        return customerRepository.save(customer);
    }

    @Override
    public Customer update(Customer customer) {
        log.info("Editando Datos: " + customer.toString());

        Customer existing = customerRepository.findById(customer.getId())
                .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + customer.getId()));

        customer.setCreatedAt(existing.getCreatedAt());
        customer.setDeletedAt(existing.getDeletedAt());
        customer.setRestoredAt(existing.getRestoredAt());
        customer.setActive(existing.getActive());
        customer.setUpdatedAt(LocalDateTime.now());
        return customerRepository.save(customer);
    }

    @Override
    public Customer delete(Long id) {
        log.info("Eliminando Datos: " + id);
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        customer.setActive(false);
        customer.setDeletedAt(LocalDateTime.now());
        return customerRepository.save(customer);
    }

    @Override
    public Customer restore(Long id) {
        log.info("Restaurando Datos: " + id);
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        customer.setActive(true);
        customer.setRestoredAt(LocalDateTime.now());
        return customerRepository.save(customer);
    }
}
