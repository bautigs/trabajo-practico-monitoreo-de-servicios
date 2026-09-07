package tp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tp.models.servicios.TipoServicio;

public interface RepositorioTipoDeServicio extends JpaRepository<TipoServicio, Long> {
}
