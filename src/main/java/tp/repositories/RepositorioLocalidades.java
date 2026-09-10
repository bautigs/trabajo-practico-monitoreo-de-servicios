package tp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import tp.services.georef.Localidad;

public interface RepositorioLocalidades extends JpaRepository<Localidad, Long> {
}
