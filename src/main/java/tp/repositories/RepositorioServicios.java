package tp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tp.models.servicios.Servicio;

public interface RepositorioServicios extends JpaRepository<Servicio, Long> {
}
