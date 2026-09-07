package tp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tp.models.entidad.Entidad;

public interface RepositorioEntidades extends JpaRepository<Entidad,Long> {

}
