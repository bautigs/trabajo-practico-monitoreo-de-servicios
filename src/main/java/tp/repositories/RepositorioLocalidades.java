package tp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import tp.services.georef.Localidad;
import java.util.List;

public interface RepositorioLocalidades extends JpaRepository<Localidad, Long> {
    List<Localidad> findByMunicipioIdOrderByNombreAsc(Long municipioId);
}
