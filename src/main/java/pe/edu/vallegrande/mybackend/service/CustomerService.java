package pe.edu.vallegrande.mybackend.service;

import pe.edu.vallegrande.mybackend.model.Customer;
import java.util.List;
import java.util.Optional;

public interface CustomerService {

   
    List<Customer> findAll();

    
    List<Customer> findByActive(Boolean active);

  
    Optional<Customer> findById(Long id);

   
    Customer save(Customer customer);

    
    Customer update(Customer customer);

    
    Customer delete(Long id);

  
    Customer restore(Long id);
    
}
