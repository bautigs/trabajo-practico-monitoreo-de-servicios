package tp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tp.models.servicios.Incidente;

public interface RepositorioIncidentes extends JpaRepository<Incidente, Long> {
}
