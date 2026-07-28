
package ar.edu.centro8.daw.zonarefri.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ar.edu.centro8.daw.zonarefri.model.Cliente;
import ar.edu.centro8.daw.zonarefri.model.Pedido;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
    List<Pedido> findByCliente(Cliente cliente);

    List<Pedido> findByEstadoPago(String estadoPago);

    List<Pedido> findByFechaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    @Query("SELECT p FROM Pedido p WHERE p.estadoPago = 'Pendiente' ORDER BY p.fecha DESC")
    List<Pedido> findPedidosPendientes();

    @Query("SELECT p FROM Pedido p WHERE p.cliente.idCliente = :clienteId ORDER BY p.fecha DESC")
    List<Pedido> findPedidosByClienteId(@Param("clienteId") Integer clienteId);
}