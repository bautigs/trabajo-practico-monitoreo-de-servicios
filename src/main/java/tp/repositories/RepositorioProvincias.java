package tp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import tp.services.georef.Provincia;

public interface RepositorioProvincias extends JpaRepository<Provincia, Long> {
}
