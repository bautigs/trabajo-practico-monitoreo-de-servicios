package tp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import tp.services.georef.Municipio;
import java.util.List;

public interface RepositorioMunicipios extends JpaRepository<Municipio, Long> {
    List<Municipio> findByProvinciaIdOrderByNombreAsc(Long provinciaId);
}
