
package ar.edu.centro8.daw.trabajo_integrador_gilma_aguada.service;

import ar.edu.centro8.daw.trabajo_integrador_gilma_aguada.model.Cliente;
import ar.edu.centro8.daw.trabajo_integrador_gilma_aguada.repositories.ClienteRepository;
import ar.edu.centro8.daw.trabajo_integrador_gilma_aguada.exception.ResourceNotFoundException;
import ar.edu.centro8.daw.trabajo_integrador_gilma_aguada.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public List<Cliente> obtenerTodos() {
        return clienteRepository.findAll();
    }

    public Cliente obtenerPorId(Integer id) {
        if (id == null) {
            throw new BusinessException("El id del cliente no puede ser nulo");
        }
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id));
    }

    public Cliente crear(Cliente cliente) {
        Optional<Cliente> existente = clienteRepository.findByEmail(cliente.getEmail());
        if (existente.isPresent()) {
            throw new BusinessException("Ya existe un cliente registrado con el email: " + cliente.getEmail());
        }

        if (cliente.getTelefono() == null) {
            throw new BusinessException("El teléfono es obligatorio");
        }

        if (cliente.getNombre() == null || cliente.getNombre().trim().isEmpty()) {
            throw new BusinessException("El nombre es obligatorio");
        }

        return clienteRepository.save(cliente);
    }

    public Cliente actualizar(Integer id, Cliente clienteActualizado) {
        Cliente clienteExistente = obtenerPorId(id);

        if (!clienteExistente.getEmail().equals(clienteActualizado.getEmail())) {
            Optional<Cliente> clienteConMismoEmail = clienteRepository.findByEmail(clienteActualizado.getEmail());
            if (clienteConMismoEmail.isPresent() && !clienteConMismoEmail.get().getIdCliente().equals(id)) {
                throw new BusinessException("El email ya está registrado por otro cliente");
            }
        }

        clienteExistente.setNombre(clienteActualizado.getNombre());
        clienteExistente.setEmail(clienteActualizado.getEmail());
        clienteExistente.setTelefono(clienteActualizado.getTelefono());

        return clienteRepository.save(clienteExistente);
    }

    public void eliminar(Integer id) {
        Cliente cliente = obtenerPorId(id);
        if (cliente != null) {
            clienteRepository.delete(cliente);
        }
    }

    public List<Cliente> buscarPorNombre(String nombre) {
        return clienteRepository.findByNombreContainingIgnoreCase(nombre);
    }

    public Optional<Cliente> buscarPorEmail(String email) {
        return clienteRepository.findByEmail(email);
    }
}