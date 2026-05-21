package pe.edu.vallegrande.mybackend.rest;

import pe.edu.vallegrande.mybackend.dto.ApiResponse;
import pe.edu.vallegrande.mybackend.model.Customer;
import pe.edu.vallegrande.mybackend.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/api/clientes")
@CrossOrigin(origins = "*")
@Tag(name = "Clientes — Master", description = "API para la gestión del catálogo de clientes")
public class CustomerRest {

    private final CustomerService customerService;

    @Autowired
    public CustomerRest(CustomerService customerService) {
        this.customerService = customerService;
    }

    // GET /v1/api/clientes — Listar filtrado y paginado
    @GetMapping
    @Operation(summary = "Listar clientes con filtros y paginación")
    public ResponseEntity<Page<Customer>> findAll(
            @RequestParam(required = false) String razonSocial,
            @RequestParam(required = false) String pais,
            @RequestParam(required = false) String tipoCliente,
            @RequestParam(required = false) Boolean activo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<Customer> result = customerService.findAllConFiltros(razonSocial, pais, tipoCliente, activo, pageable);
        return ResponseEntity.ok(result);
    }

    // GET /v1/api/clientes/{id}
    @GetMapping("/{id}")
    @Operation(summary = "Obtener cliente por ID")
    public ResponseEntity<ApiResponse<Customer>> findById(@PathVariable Long id) {
        return customerService.findById(id)
                .map(c -> ResponseEntity.ok(new ApiResponse<>(c, "Cliente encontrado", true)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(null, "Cliente no encontrado con ID: " + id, false)));
    }

    // POST /v1/api/clientes
    @PostMapping
    @Operation(summary = "Crear nuevo cliente")
    public ResponseEntity<ApiResponse<Customer>> save(@RequestBody Customer customer) {
        Customer creado = customerService.save(customer);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(creado, "Cliente registrado exitosamente", true));
    }

    // PUT /v1/api/clientes/{id}
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar cliente existente")
    public ResponseEntity<ApiResponse<Customer>> update(@PathVariable Long id, @RequestBody Customer customer) {
        customer.setId(id);
        Customer editado = customerService.update(customer);
        return ResponseEntity.ok(new ApiResponse<>(editado, "Cliente actualizado exitosamente", true));
    }

    // DELETE /v1/api/clientes/{id} — Eliminar lógico
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar lógicamente un cliente")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        customerService.delete(id);
        return ResponseEntity.ok(new ApiResponse<>(null, "Cliente eliminado lógicamente", true));
    }

    // GET /v1/api/clientes/select — Lista simplificada de selección
    @GetMapping("/select")
    @Operation(summary = "Obtener lista simplificada para comboboxes")
    public ResponseEntity<List<CustomerSelectDto>> getSelectList() {
        List<CustomerSelectDto> selectList = customerService.findByActive(true).stream()
                .map(c -> new CustomerSelectDto(c.getId(), c.getRazonSocial()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(selectList);
    }

    // Helper DTO class for Select box list
    public static class CustomerSelectDto {
        private Long id;
        private String razonSocial;

        public CustomerSelectDto(Long id, String razonSocial) {
            this.id = id;
            this.razonSocial = razonSocial;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getRazonSocial() {
            return razonSocial;
        }

        public void setRazonSocial(String razonSocial) {
            this.razonSocial = razonSocial;
        }
    }
}
