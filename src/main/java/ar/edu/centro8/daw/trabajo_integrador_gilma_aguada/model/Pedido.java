package ar.edu.centro8.daw.trabajo_integrador_gilma_aguada.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore; 

/**
 * Entidad Pedido
 * Representa un pedido realizado por un cliente en el sistema de reparación
 */
@Entity
@Table(name = "pedidos")
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pedido")
    private Integer idPedido;

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    @NotNull(message = "El cliente es obligatorio")
    @JsonIgnore
    private Cliente cliente;

    @Column(nullable = false)
    @NotNull(message = "La fecha es obligatoria")
    private LocalDateTime fecha;

    @Column(name = "estado_pago", nullable = false)
    @NotBlank(message = "El estado de pago es obligatorio")
    @Pattern(regexp = "PENDIENTE|PAGADO|CANCELADO", message = "El estado de pago debe ser PENDIENTE, PAGADO o CANCELADO")
    private String estadoPago;

    @Column(name = "direccion_envio", nullable = false)
    @NotBlank(message = "La dirección de envío es obligatoria")
    @Size(min = 5, max = 200, message = "La dirección debe tener entre 5 y 200 caracteres")
    private String direccionEnvio;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore 
    private List<PedidoDetalle> detalles = new ArrayList<>();

    public Pedido() {
    }

    public Pedido(Cliente cliente, LocalDateTime fecha, String estadoPago, String direccionEnvio) {
        this.cliente = cliente;
        this.fecha = fecha;
        this.estadoPago = estadoPago;
        this.direccionEnvio = direccionEnvio;
    }

    public Integer getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(Integer idPedido) {
        this.idPedido = idPedido;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getEstadoPago() {
        return estadoPago;
    }

    public void setEstadoPago(String estadoPago) {
        this.estadoPago = estadoPago;
    }

    public String getDireccionEnvio() {
        return direccionEnvio;
    }

    public void setDireccionEnvio(String direccionEnvio) {
        this.direccionEnvio = direccionEnvio;
    }

    public List<PedidoDetalle> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<PedidoDetalle> detalles) {
        this.detalles = detalles;
    }
}