package tp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tp.models.entidad.TipoEntidad;

public interface RepositorioTipoDeEntidad extends JpaRepository<TipoEntidad, Long> {
}
