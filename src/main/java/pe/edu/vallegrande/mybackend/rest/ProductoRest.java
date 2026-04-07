package pe.edu.vallegrande.mybackend.rest;

import pe.edu.vallegrande.mybackend.model.Producto;
import pe.edu.vallegrande.mybackend.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*")
public class ProductoRest {

    @Autowired
    private ProductoService productoService;

    // GET - Listar todos
    @GetMapping
    public ResponseEntity<List<Producto>> listar() {
        return ResponseEntity.ok(productoService.listar());
    }

    // GET - Listar por ID
    @GetMapping("/{id}")
    public ResponseEntity<Producto> listarPorId(@PathVariable Long id) {
        return productoService.listarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET - Listar por estado
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Producto>> listarPorEstado(@PathVariable Boolean estado) {
        return ResponseEntity.ok(productoService.listarPorEstado(estado));
    }

    // POST - Crear
    @PostMapping
    public ResponseEntity<Producto> crear(@RequestBody Producto producto) {
        return ResponseEntity.ok(productoService.crear(producto));
    }

    // PUT - Editar
    @PutMapping("/{id}")
    public ResponseEntity<Producto> editar(@PathVariable Long id, @RequestBody Producto producto) {
        producto.setId(id);
        return ResponseEntity.ok(productoService.editar(producto));
    }

    // PATCH - Eliminar lógico
    @PatchMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // PATCH - Restaurar lógico
    @PatchMapping("/restaurar/{id}")
    public ResponseEntity<Void> restaurar(@PathVariable Long id) {
        productoService.restaurar(id);
        return ResponseEntity.noContent().build();
    }
}