
package ar.edu.centro8.daw.zonarefri.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ar.edu.centro8.daw.zonarefri.model.Pedido;
import ar.edu.centro8.daw.zonarefri.model.PedidoDetalle;
import ar.edu.centro8.daw.zonarefri.model.PedidoDetalleId;
import ar.edu.centro8.daw.zonarefri.model.Producto;

import java.math.BigDecimal;
import java.util.List;

public interface PedidoDetalleRepository extends JpaRepository<PedidoDetalle, PedidoDetalleId> {
    List<PedidoDetalle> findByPedido(Pedido pedido);

    List<PedidoDetalle> findByProducto(Producto producto);

    @Query("SELECT COALESCE(SUM(pd.precioUnitario * pd.cantidad), 0) FROM PedidoDetalle pd WHERE pd.pedido.idPedido = :pedidoId")
    BigDecimal calcularTotalPedido(@Param("pedidoId") Integer pedidoId);
}