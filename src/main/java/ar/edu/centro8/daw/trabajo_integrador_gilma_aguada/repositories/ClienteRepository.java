
package ar.edu.centro8.daw.trabajo_integrador_gilma_aguada.repositories;

import ar.edu.centro8.daw.trabajo_integrador_gilma_aguada.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
    List<Cliente> findByNombreContainingIgnoreCase(String nombre);

    Optional<Cliente> findByEmail(String email);

    Optional<Cliente> findByTelefono(Integer telefono);
}