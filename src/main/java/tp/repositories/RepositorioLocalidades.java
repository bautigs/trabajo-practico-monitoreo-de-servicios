package tp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tp.models.services.georef.Localidad;

public interface RepositorioLocalidades extends JpaRepository<Localidad, Long> {
}
