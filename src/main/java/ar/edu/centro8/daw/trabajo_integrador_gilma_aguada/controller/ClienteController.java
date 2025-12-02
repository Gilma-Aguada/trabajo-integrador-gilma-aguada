
package ar.edu.centro8.daw.trabajo_integrador_gilma_aguada.controller;

import ar.edu.centro8.daw.trabajo_integrador_gilma_aguada.model.Cliente;
import ar.edu.centro8.daw.trabajo_integrador_gilma_aguada.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

/**
 * Controlador para la gestión de clientes
 */
@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "*")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    /**
     * Crea un nuevo cliente validando los datos del request
     * @param cliente Datos del cliente a crear (validado con @Valid)
     * @return Cliente creado con status 201
     */
    @PostMapping
    public ResponseEntity<Cliente> crear(@Valid @RequestBody Cliente cliente) {
        Cliente nuevoCliente = clienteService.crear(cliente);
        return new ResponseEntity<>(nuevoCliente, HttpStatus.CREATED);
    }

    /**
     * Obtiene todos los clientes
     * @return Lista de clientes
     */
    @GetMapping
    public ResponseEntity<List<Cliente>> obtenerTodos() {
        List<Cliente> clientes = clienteService.obtenerTodos();
        return ResponseEntity.ok(clientes);
    }

    /**
     * Obtiene un cliente por su ID
     * @param id Identificador del cliente
     * @return Cliente encontrado
     */
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> obtenerPorId(@PathVariable Integer id) {
        Cliente cliente = clienteService.obtenerPorId(id);
        return ResponseEntity.ok(cliente);
    }

    /**
     * Actualiza un cliente existente validando los datos del request
     * @param id Identificador del cliente
     * @param cliente Datos actualizados (validado con @Valid)
     * @return Cliente actualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> actualizar(@PathVariable Integer id, @Valid @RequestBody Cliente cliente) {
        Cliente clienteActualizado = clienteService.actualizar(id, cliente);
        return ResponseEntity.ok(clienteActualizado);
    }

    /**
     * Elimina un cliente por su ID
     * @param id Identificador del cliente
     * @return Sin contenido (204)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        clienteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Busca clientes por nombre
     * @param nombre Nombre o parte del nombre a buscar
     * @return Lista de clientes que coinciden
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<Cliente>> buscarPorNombre(@RequestParam String nombre) {
        List<Cliente> clientes = clienteService.buscarPorNombre(nombre);
        return ResponseEntity.ok(clientes);
    }

    /**
     * Busca un cliente por su email
     * @param email Email del cliente
     * @return Cliente encontrado o 404 si no existe
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<Cliente> buscarPorEmail(@PathVariable String email) {
        Optional<Cliente> cliente = clienteService.buscarPorEmail(email);
        return cliente.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}