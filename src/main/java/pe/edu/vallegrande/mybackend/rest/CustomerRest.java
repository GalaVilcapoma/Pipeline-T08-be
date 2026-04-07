package pe.edu.vallegrande.mybackend.rest;

import pe.edu.vallegrande.mybackend.model.Customer;
import pe.edu.vallegrande.mybackend.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/api/customer")
@Tag(name = "Customer API", description = "API for Customer management")
public class CustomerRest {

    private final CustomerService customerService;

    @Autowired
    public CustomerRest(CustomerService customerService) {
        this.customerService = customerService;
    }
    
    // 🌐🔍 Listar Todos - GET
    @GetMapping
    @Operation(summary = "Get All Customer", description = "Get All Customer")
    public List<Customer> findAll() {
        return customerService.findAll();
    }

    // 🌐🔍 Listar por Estado Activo - GET
    @GetMapping("/activo/{activo}")
    @Operation(summary = "Get Customer By Active Status", description = "Get Customer By Active Status")
    public List<Customer> findByActive(@PathVariable("activo") Boolean active) {
        return customerService.findByActive(active);
    }

    // 🌐✅ Registrar - POST
    @PostMapping("/save")
    @Operation(summary = "Save Customer", description = "Save Customer")
    public Customer save(@RequestBody Customer customer) {
        return customerService.save(customer);
    }

    // 🌐✏️ Actualizar - PUT
    @PutMapping("/update/{id}")
    @Operation(summary = "Update Customer", description = "Update Customer")
    public Customer update(@PathVariable Long id, @RequestBody Customer customer) {
        return customerService.update(customer);
    }

    // 🌐❌ Eliminar lógico - PATCH
    @PatchMapping("/eliminar/{id}")
    @Operation(summary = "Logical Delete Customer", description = "Eliminar cliente lógico")
    public Customer delete(@PathVariable Long id) {
        return customerService.delete(id);
    }

    // 🌐♻️ Restaurar - PATCH
    @PatchMapping("/restore/{id}")
    @Operation(summary = "Logical Restore Customer", description = "Logical Restore Customer")
    public Customer restore(@PathVariable Long id) {
        return customerService.restore(id);
    }

    // 🌐🔍 Listar por ID - GET (solo acepta números)
    @GetMapping("/{id:\\d+}")
    @Operation(summary = "Get Customer By ID", description = "Get Customer By ID")
    public Optional<Customer> findById(@PathVariable Long id) {
        return customerService.findById(id);
    }

}
