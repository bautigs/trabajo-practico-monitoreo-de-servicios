package tp.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import tp.models.comunidad.Persona;

public interface RepositorioPersonas extends JpaRepository<Persona, Long> {
    Optional<Persona> findByKeycloakId(String id);
}
