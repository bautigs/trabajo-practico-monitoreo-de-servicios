package tp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tp.models.comunidad.Comunidad;

public interface RepositorioComunidades extends JpaRepository<Comunidad, Long> {
}
