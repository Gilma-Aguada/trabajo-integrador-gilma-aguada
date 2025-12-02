
package ar.edu.centro8.daw.trabajo_integrador_gilma_aguada.controller;

import ar.edu.centro8.daw.trabajo_integrador_gilma_aguada.model.Cliente;
import ar.edu.centro8.daw.trabajo_integrador_gilma_aguada.model.Producto;
import ar.edu.centro8.daw.trabajo_integrador_gilma_aguada.model.Pedido;
import ar.edu.centro8.daw.trabajo_integrador_gilma_aguada.service.ClienteService;
import ar.edu.centro8.daw.trabajo_integrador_gilma_aguada.service.ProductoService;
import ar.edu.centro8.daw.trabajo_integrador_gilma_aguada.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

@Controller
@RequestMapping("/thymeleaf")
public class ThymeleafController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private PedidoService pedidoService;

    @GetMapping
    public String dashboard(Model model) {
        List<Cliente> clientes = clienteService.obtenerTodos();
        List<Producto> productos = productoService.obtenerTodos();
        List<Pedido> pedidos = pedidoService.obtenerTodos();
        List<Pedido> pedidosPendientes = pedidoService.obtenerPendientes();
        List<Producto> productosBajoStock = productoService.obtenerProductosConBajoStock();

        model.addAttribute("clientes", clientes);
        model.addAttribute("productos", productos);
        model.addAttribute("pedidos", pedidos);
        model.addAttribute("pedidosPendientes", pedidosPendientes);
        model.addAttribute("productosBajoStock", productosBajoStock);

        model.addAttribute("totalClientes", clientes.size());
        model.addAttribute("totalProductos", productos.size());
        model.addAttribute("totalPedidos", pedidos.size());
        model.addAttribute("cantidadPendientes", pedidosPendientes.size());

        return "dashboard";
    }
}