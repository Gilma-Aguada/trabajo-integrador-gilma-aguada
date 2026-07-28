
package ar.edu.centro8.daw.zonarefri.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ar.edu.centro8.daw.zonarefri.model.Cliente;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
    List<Cliente> findByNombreContainingIgnoreCase(String nombre);

    Optional<Cliente> findByEmail(String email);

    Optional<Cliente> findByTelefono(Integer telefono);
}