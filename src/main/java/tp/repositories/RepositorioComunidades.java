package tp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tp.models.entities.comunidad.Comunidad;

public interface RepositorioComunidades extends JpaRepository<Comunidad, Long> {
}
