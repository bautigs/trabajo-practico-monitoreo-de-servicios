package tp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tp.models.comunidad.Usuario;

public interface RepositorioUsuarios extends JpaRepository<Usuario, Long> {
}
