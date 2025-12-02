
package ar.edu.centro8.daw.trabajo_integrador_gilma_aguada.service;

import ar.edu.centro8.daw.trabajo_integrador_gilma_aguada.model.Pedido;
import ar.edu.centro8.daw.trabajo_integrador_gilma_aguada.model.PedidoDetalle;
import ar.edu.centro8.daw.trabajo_integrador_gilma_aguada.model.Cliente;
import ar.edu.centro8.daw.trabajo_integrador_gilma_aguada.model.Producto;
import ar.edu.centro8.daw.trabajo_integrador_gilma_aguada.repositories.PedidoDetalleRepository;
import ar.edu.centro8.daw.trabajo_integrador_gilma_aguada.repositories.PedidoRepository;
import ar.edu.centro8.daw.trabajo_integrador_gilma_aguada.exception.ResourceNotFoundException;
import ar.edu.centro8.daw.trabajo_integrador_gilma_aguada.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private PedidoDetalleRepository pedidoDetalleRepository;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private ClienteService clienteService;

    public List<Pedido> obtenerTodos() {
        return pedidoRepository.findAll();
    }

    public Pedido obtenerPorId(Integer id) {
        if (id == null) {
            throw new BusinessException("El id del pedido no puede ser nulo");
        }
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado con id: " + id));
    }

    public Pedido crear(Pedido pedido) {
        if (pedido.getCliente() == null || pedido.getCliente().getIdCliente() == null) {
            throw new BusinessException("Debe especificar un cliente para el pedido");
        }

        Cliente cliente = clienteService.obtenerPorId(pedido.getCliente().getIdCliente());
        pedido.setCliente(cliente);

        if (pedido.getFecha() == null) {
            pedido.setFecha(LocalDateTime.now());
        }

        if (pedido.getEstadoPago() == null || pedido.getEstadoPago().trim().isEmpty()) {
            pedido.setEstadoPago("Pendiente");
        }

        if (pedido.getDireccionEnvio() == null || pedido.getDireccionEnvio().trim().isEmpty()) {
            throw new BusinessException("La dirección de envío es obligatoria");
        }

        Pedido pedidoGuardado = pedidoRepository.save(pedido);

        if (pedido.getDetalles() != null && !pedido.getDetalles().isEmpty()) {
            for (PedidoDetalle detalle : pedido.getDetalles()) {
                agregarDetalle(pedidoGuardado.getIdPedido(), detalle);
            }
        }

        return obtenerPorId(pedidoGuardado.getIdPedido());
    }

    public PedidoDetalle agregarDetalle(Integer pedidoId, PedidoDetalle detalle) {
        Pedido pedido = obtenerPorId(pedidoId);

        if (detalle.getProducto() == null || detalle.getProducto().getIdProducto() == null) {
            throw new BusinessException("Debe especificar un producto válido");
        }

        Producto producto = productoService.obtenerPorId(detalle.getProducto().getIdProducto());

        if (!productoService.verificarDisponibilidad(producto.getIdProducto(), detalle.getCantidad())) {
            throw new BusinessException("Stock insuficiente para el producto: " + producto.getNombre());
        }

        if (detalle.getPrecioUnitario() == null) {
            detalle.setPrecioUnitario(producto.getPrecio());
        }

        detalle.setPedido(pedido);
        detalle.setProducto(producto);

        productoService.reducirStock(producto.getIdProducto(), detalle.getCantidad());

        return pedidoDetalleRepository.save(detalle);
    }

    public Pedido actualizarEstadoPago(Integer id, String nuevoEstado) {
        Pedido pedido = obtenerPorId(id);

        List<String> estadosPermitidos = List.of("Pendiente", "Pagado", "Cancelado");
        if (!estadosPermitidos.contains(nuevoEstado)) {
            throw new BusinessException("Estado de pago no válido. Valores permitidos: " + estadosPermitidos);
        }

        pedido.setEstadoPago(nuevoEstado);
        return pedidoRepository.save(pedido);
    }

    public Pedido actualizar(Integer id, Pedido pedidoActualizado) {
        Pedido pedidoExistente = obtenerPorId(id);

        pedidoExistente.setDireccionEnvio(pedidoActualizado.getDireccionEnvio());
        pedidoExistente.setEstadoPago(pedidoActualizado.getEstadoPago());

        return pedidoRepository.save(pedidoExistente);
    }

    public void eliminar(Integer id) {
        Pedido pedido = obtenerPorId(id);
        if (pedido != null) {
            pedidoRepository.delete(pedido);
        }
    }

    public List<Pedido> obtenerPorCliente(Integer clienteId) {
        return pedidoRepository.findPedidosByClienteId(clienteId);
    }

    public List<Pedido> obtenerPorEstado(String estado) {
        return pedidoRepository.findByEstadoPago(estado);
    }

    public List<Pedido> obtenerPendientes() {
        return pedidoRepository.findPedidosPendientes();
    }

    public BigDecimal calcularTotal(Integer pedidoId) {
        BigDecimal total = pedidoDetalleRepository.calcularTotalPedido(pedidoId);
        return total != null ? total : BigDecimal.ZERO;
    }
}