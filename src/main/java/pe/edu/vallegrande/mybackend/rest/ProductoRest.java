package pe.edu.vallegrande.mybackend.rest;

import pe.edu.vallegrande.mybackend.dto.ApiResponse;
import pe.edu.vallegrande.mybackend.model.Producto;
import pe.edu.vallegrande.mybackend.service.ProductoService;
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

@RestController
@RequestMapping("/v1/api/productos")
@CrossOrigin(origins = "*")
@Tag(name = "Productos — Master", description = "API para la gestión de catálogo de productos")
public class ProductoRest {

    @Autowired
    private ProductoService productoService;

    // GET /v1/api/productos — Listado filtrado y paginado
    @GetMapping
    @Operation(summary = "Listar productos con filtros y paginación")
    public ResponseEntity<Page<Producto>> listar(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) Boolean activo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,desc") String sort) {
        
        // Configurar ordenamiento
        String[] sortParams = sort.split(",");
        Sort sortOrder = Sort.by(sortParams[0]);
        if (sortParams.length > 1 && "desc".equalsIgnoreCase(sortParams[1])) {
            sortOrder = sortOrder.descending();
        } else {
            sortOrder = sortOrder.ascending();
        }

        Pageable pageable = PageRequest.of(page, size, sortOrder);
        Page<Producto> result = productoService.listarConFiltros(nombre, categoria, activo, pageable);
        return ResponseEntity.ok(result);
    }

    // GET /v1/api/productos/{id}
    @GetMapping("/{id}")
    @Operation(summary = "Obtener producto por ID")
    public ResponseEntity<ApiResponse<Producto>> listarPorId(@PathVariable Long id) {
        return productoService.listarPorId(id)
                .map(p -> ResponseEntity.ok(new ApiResponse<>(p, "Producto encontrado", true)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(null, "Producto no encontrado con ID: " + id, false)));
    }

    // POST /v1/api/productos
    @PostMapping
    @Operation(summary = "Crear nuevo producto")
    public ResponseEntity<ApiResponse<Producto>> crear(@RequestBody Producto producto) {
        Producto creado = productoService.crear(producto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(creado, "Producto creado exitosamente", true));
    }

    // PUT /v1/api/productos/{id}
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar producto existente")
    public ResponseEntity<ApiResponse<Producto>> editar(@PathVariable Long id, @RequestBody Producto producto) {
        producto.setId(id);
        Producto editado = productoService.editar(producto);
        return ResponseEntity.ok(new ApiResponse<>(editado, "Producto actualizado exitosamente", true));
    }

    // DELETE /v1/api/productos/{id} — Eliminar lógico
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar lógicamente un producto")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        productoService.eliminar(id);
        return ResponseEntity.ok(new ApiResponse<>(null, "Producto eliminado lógicamente", true));
    }

    // PATCH /v1/api/productos/{id}/toggle-activo — Toggle activo/inactivo
    @PatchMapping("/{id}/toggle-activo")
    @Operation(summary = "Alternar estado activo/inactivo del producto")
    public ResponseEntity<ApiResponse<Producto>> toggleActivo(@PathVariable Long id) {
        Producto existing = productoService.listarPorId(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
        Producto actualizado;
        if (Boolean.TRUE.equals(existing.getEstado())) {
            actualizado = productoService.eliminar(id);
        } else {
            actualizado = productoService.restaurar(id);
        }
        return ResponseEntity.ok(new ApiResponse<>(actualizado, "Estado alternado con éxito", true));
    }

    // GET /v1/api/productos/categorias — Listar categorías únicas
    @GetMapping("/categorias")
    @Operation(summary = "Obtener lista única de categorías de productos activos")
    public ResponseEntity<List<String>> getCategorias() {
        return ResponseEntity.ok(productoService.obtenerCategorias());
    }
}