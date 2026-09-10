package tp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import tp.services.georef.Municipio;

public interface RepositorioMunicipios extends JpaRepository<Municipio, Long> {

}
