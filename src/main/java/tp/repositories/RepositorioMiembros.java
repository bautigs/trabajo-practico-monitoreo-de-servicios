package tp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tp.models.comunidad.Miembro;

public interface RepositorioMiembros extends JpaRepository<Miembro, Long> {

}
