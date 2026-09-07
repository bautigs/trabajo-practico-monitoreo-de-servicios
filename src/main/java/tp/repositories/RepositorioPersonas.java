package tp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tp.models.comunidad.Persona;

public interface RepositorioPersonas extends JpaRepository<Persona, Long> {
}
