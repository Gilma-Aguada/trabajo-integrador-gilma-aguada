
package ar.edu.centro8.daw.trabajo_integrador_gilma_aguada.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Access(AccessType.FIELD)
public class PedidoDetalleId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "id_producto", columnDefinition = "INT")
    private Integer productoId;

    @Column(name = "id_pedido", columnDefinition = "INT")
    private Integer pedidoId;

    public PedidoDetalleId() {
    }

    public PedidoDetalleId(Integer pedidoId, Integer productoId) {
        this.pedidoId = pedidoId;
        this.productoId = productoId;
    }

    public Integer getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(Integer pedidoId) {
        this.pedidoId = pedidoId;
    }

    public Integer getProductoId() {
        return productoId;
    }

    public void setProductoId(Integer productoId) {
        this.productoId = productoId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof PedidoDetalleId))
            return false;
        PedidoDetalleId that = (PedidoDetalleId) o;
        return Objects.equals(pedidoId, that.pedidoId) &&
                Objects.equals(productoId, that.productoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pedidoId, productoId);
    }
}