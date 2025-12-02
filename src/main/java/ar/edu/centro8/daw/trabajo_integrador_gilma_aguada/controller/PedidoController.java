// src/main/java/ar/edu/centro8/daw/trabajo_integrador_gilma_aguada/controller/PedidoController.java
package ar.edu.centro8.daw.trabajo_integrador_gilma_aguada.controller;

import ar.edu.centro8.daw.trabajo_integrador_gilma_aguada.model.Pedido;
import ar.edu.centro8.daw.trabajo_integrador_gilma_aguada.model.PedidoDetalle;
import ar.edu.centro8.daw.trabajo_integrador_gilma_aguada.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador para la gestión de pedidos
 */
@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = "*")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    /**
     * Crea un nuevo pedido validando los datos del request
     * @param pedido Datos del pedido a crear (validado con @Valid)
     * @return Pedido creado con status 201
     */
    @PostMapping
    public ResponseEntity<Pedido> crear(@Valid @RequestBody Pedido pedido) {
        Pedido nuevoPedido = pedidoService.crear(pedido);
        return new ResponseEntity<>(nuevoPedido, HttpStatus.CREATED);
    }

    /**
     * Obtiene todos los pedidos
     * @return Lista de pedidos
     */
    @GetMapping
    public ResponseEntity<List<Pedido>> obtenerTodos() {
        List<Pedido> pedidos = pedidoService.obtenerTodos();
        return ResponseEntity.ok(pedidos);
    }

    /**
     * Obtiene un pedido por su ID
     * @param id Identificador del pedido
     * @return Pedido encontrado
     */
    @GetMapping("/{id}")
    public ResponseEntity<Pedido> obtenerPorId(@PathVariable Integer id) {
        Pedido pedido = pedidoService.obtenerPorId(id);
        return ResponseEntity.ok(pedido);
    }

    /**
     * Actualiza un pedido existente validando los datos del request
     * @param id Identificador del pedido
     * @param pedido Datos actualizados (validado con @Valid)
     * @return Pedido actualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<Pedido> actualizar(@PathVariable Integer id, @Valid @RequestBody Pedido pedido) {
        Pedido pedidoActualizado = pedidoService.actualizar(id, pedido);
        return ResponseEntity.ok(pedidoActualizado);
    }

    /**
     * Elimina un pedido por su ID
     * @param id Identificador del pedido
     * @return Sin contenido (204)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        pedidoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Agrega un detalle a un pedido existente
     * @param id Identificador del pedido
     * @param detalle Detalle del producto a agregar (validado con @Valid)
     * @return Detalle creado con status 201
     */
    @PostMapping("/{id}/detalles")
    public ResponseEntity<PedidoDetalle> agregarDetalle(@PathVariable Integer id, @Valid @RequestBody PedidoDetalle detalle) {
        PedidoDetalle nuevoDetalle = pedidoService.agregarDetalle(id, detalle);
        return new ResponseEntity<>(nuevoDetalle, HttpStatus.CREATED);
    }

    /**
     * Actualiza el estado de pago de un pedido
     * @param id Identificador del pedido
     * @param body Map con el nuevo estado de pago
     * @return Pedido con estado actualizado
     */
    @PatchMapping("/{id}/estado")
    public ResponseEntity<Pedido> actualizarEstado(@PathVariable Integer id, @RequestBody Map<String, String> body) {
        String nuevoEstado = body.get("estadoPago");
        Pedido pedidoActualizado = pedidoService.actualizarEstadoPago(id, nuevoEstado);
        return ResponseEntity.ok(pedidoActualizado);
    }

    /**
     * Obtiene todos los pedidos de un cliente específico
     * @param clienteId Identificador del cliente
     * @return Lista de pedidos del cliente
     */
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Pedido>> obtenerPorCliente(@PathVariable Integer clienteId) {
        List<Pedido> pedidos = pedidoService.obtenerPorCliente(clienteId);
        return ResponseEntity.ok(pedidos);
    }

    /**
     * Obtiene todos los pedidos con estado de pago PENDIENTE
     * @return Lista de pedidos pendientes
     */
    @GetMapping("/pendientes")
    public ResponseEntity<List<Pedido>> obtenerPendientes() {
        List<Pedido> pedidos = pedidoService.obtenerPendientes();
        return ResponseEntity.ok(pedidos);
    }

    /**
     * Calcula el total de un pedido (suma de todos sus detalles)
     * @param id Identificador del pedido
     * @return Map con el total del pedido
     */
    @GetMapping("/{id}/total")
    public ResponseEntity<Map<String, BigDecimal>> calcularTotal(@PathVariable Integer id) {
        BigDecimal total = pedidoService.calcularTotal(id);
        Map<String, BigDecimal> response = new HashMap<>();
        response.put("total", total);
        return ResponseEntity.ok(response);
    }
}