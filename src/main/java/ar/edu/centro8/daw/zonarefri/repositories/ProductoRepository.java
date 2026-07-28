

package ar.edu.centro8.daw.zonarefri.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ar.edu.centro8.daw.zonarefri.model.Producto;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    List<Producto> findByNombreContainingIgnoreCase(String nombre);

    List<Producto> findByStockGreaterThan(Integer stock);

    List<Producto> findByPrecioBetween(BigDecimal precioMin, BigDecimal precioMax);

    @Query("SELECT p FROM Producto p WHERE p.stock < 10")
    List<Producto> findProductosConBajoStock();
}