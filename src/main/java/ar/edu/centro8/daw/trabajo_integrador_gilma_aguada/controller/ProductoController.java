
package ar.edu.centro8.daw.trabajo_integrador_gilma_aguada.controller;

import ar.edu.centro8.daw.trabajo_integrador_gilma_aguada.model.Producto;
import ar.edu.centro8.daw.trabajo_integrador_gilma_aguada.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Controlador para la gestión de productos
 */
@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    /**
     * Crea un nuevo producto validando los datos del request
     * @param producto Datos del producto a crear (validado con @Valid)
     * @return Producto creado con status 201
     */
    @PostMapping
    public ResponseEntity<Producto> crear(@Valid @RequestBody Producto producto) {
        Producto nuevoProducto = productoService.crear(producto);
        return new ResponseEntity<>(nuevoProducto, HttpStatus.CREATED);
    }

    /**
     * Obtiene todos los productos
     * @return Lista de productos
     */
    @GetMapping
    public ResponseEntity<List<Producto>> obtenerTodos() {
        List<Producto> productos = productoService.obtenerTodos();
        return ResponseEntity.ok(productos);
    }

    /**
     * Obtiene un producto por su ID
     * @param id Identificador del producto
     * @return Producto encontrado
     */
    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerPorId(@PathVariable Integer id) {
        Producto producto = productoService.obtenerPorId(id);
        return ResponseEntity.ok(producto);
    }

    /**
     * Actualiza un producto existente validando los datos del request
     * @param id Identificador del producto
     * @param producto Datos actualizados (validado con @Valid)
     * @return Producto actualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizar(@PathVariable Integer id, @Valid @RequestBody Producto producto) {
        Producto productoActualizado = productoService.actualizar(id, producto);
        return ResponseEntity.ok(productoActualizado);
    }

    /**
     * Elimina un producto por su ID
     * @param id Identificador del producto
     * @return Sin contenido (204)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Busca productos por nombre
     * @param nombre Nombre o parte del nombre a buscar
     * @return Lista de productos que coinciden
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<Producto>> buscarPorNombre(@RequestParam String nombre) {
        List<Producto> productos = productoService.buscarPorNombre(nombre);
        return ResponseEntity.ok(productos);
    }

    /**
     * Obtiene todos los productos con stock disponible
     * @return Lista de productos con stock mayor a 0
     */
    @GetMapping("/con-stock")
    public ResponseEntity<List<Producto>> obtenerConStock() {
        List<Producto> productos = productoService.obtenerConStock();
        return ResponseEntity.ok(productos);
    }

    /**
     * Obtiene productos con stock bajo (menor a 10)
     * @return Lista de productos con bajo stock
     */
    @GetMapping("/bajo-stock")
    public ResponseEntity<List<Producto>> obtenerConBajoStock() {
        List<Producto> productos = productoService.obtenerProductosConBajoStock();
        return ResponseEntity.ok(productos);
    }
}