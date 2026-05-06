package pe.edu.vallegrande.mybackend.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import pe.edu.vallegrande.mybackend.model.Productor;
import pe.edu.vallegrande.mybackend.service.ProductorService;

import java.util.List;

@RestController
@RequestMapping("/v1/api/productores")
@CrossOrigin(origins = "*")
@Tag(name = "API de Productores", description = "Operaciones CRUD para Productores con Auditoría")
public class ProductorRest {

    @Autowired
    private ProductorService productorService;

    @Operation(summary = "Listar todos los productores")
    @GetMapping
    public List<Productor> buscarTodos() {
        return productorService.buscarTodos();
    }

    @Operation(summary = "Listar productores activos")
    @GetMapping("/activos")
    public List<Productor> buscarActivos() {
        return productorService.buscarActivos();
    }

    @Operation(summary = "Buscar productor por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Productor> buscarPorId(@PathVariable Long id) {
        return productorService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear un nuevo productor (Registra fecha_creacion)")
    @PostMapping
    public Productor crear(@RequestBody Productor productor) {
        return productorService.crear(productor);
    }

    @Operation(summary = "Actualizar un productor (Registra fecha_actualizacion)")
    @PutMapping("/{id}")
    public Productor actualizar(@PathVariable Long id, @RequestBody Productor productor) {
        return productorService.actualizar(id, productor);
    }

    @Operation(summary = "Eliminación Lógica (estado=false y registra fecha_eliminacion)")
    @PatchMapping("/{id}/eliminar")
    public Productor eliminarLogico(@PathVariable Long id) {
        return productorService.eliminarLogico(id);
    }

    @Operation(summary = "Restauración Lógica (estado=true y registra fecha_restauracion)")
    @PatchMapping("/{id}/restaurar")
    public Productor restaurarLogico(@PathVariable Long id) {
        return productorService.restaurarLogico(id);
    }
}
