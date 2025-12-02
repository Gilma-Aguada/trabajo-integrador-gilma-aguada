
package ar.edu.centro8.daw.trabajo_integrador_gilma_aguada.service;

import ar.edu.centro8.daw.trabajo_integrador_gilma_aguada.model.Producto;
import ar.edu.centro8.daw.trabajo_integrador_gilma_aguada.repositories.ProductoRepository;
import ar.edu.centro8.daw.trabajo_integrador_gilma_aguada.exception.ResourceNotFoundException;
import ar.edu.centro8.daw.trabajo_integrador_gilma_aguada.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> obtenerTodos() {
        return productoRepository.findAll();
    }

    @SuppressWarnings("null")
    public Producto obtenerPorId(Integer id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));
    }

    @SuppressWarnings("null")
    public Producto crear(Producto producto) {
        validarProducto(producto);
        return productoRepository.save(producto);
    }

    public Producto actualizar(Integer id, Producto productoActualizado) {
        Producto productoExistente = obtenerPorId(id);
        validarProducto(productoActualizado);

        productoExistente.setNombre(productoActualizado.getNombre());
        productoExistente.setPrecio(productoActualizado.getPrecio());
        productoExistente.setStock(productoActualizado.getStock());

        return productoRepository.save(productoExistente);
    }

    private void validarProducto(Producto producto) {
        if (producto.getPrecio() == null || producto.getPrecio().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("El precio debe ser mayor a 0");
        }
        if (producto.getStock() == null || producto.getStock() < 0) {
            throw new BusinessException("El stock no puede ser negativo");
        }
        if (producto.getNombre() == null || producto.getNombre().trim().isEmpty()) {
            throw new BusinessException("El nombre del producto es obligatorio");
        }
    }

    public boolean verificarDisponibilidad(Integer productoId, Integer cantidadSolicitada) {
        Producto producto = obtenerPorId(productoId);
        return producto.getStock() >= cantidadSolicitada;
    }

    public void reducirStock(Integer productoId, Integer cantidad) {
        Producto producto = obtenerPorId(productoId);

        if (producto.getStock() < cantidad) {
            throw new BusinessException("Stock insuficiente para el producto: " + producto.getNombre());
        }

        producto.setStock(producto.getStock() - cantidad);
        productoRepository.save(producto);
    }

    @SuppressWarnings("null")
    public void eliminar(Integer id) {
        Producto producto = obtenerPorId(id);
        productoRepository.delete(producto);
    }

    public List<Producto> buscarPorNombre(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre);
    }

    public List<Producto> obtenerConStock() {
        return productoRepository.findByStockGreaterThan(0);
    }

    public List<Producto> obtenerProductosConBajoStock() {
        return productoRepository.findProductosConBajoStock();
    }
}