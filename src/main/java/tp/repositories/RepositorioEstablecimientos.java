package tp.repositories;



import org.springframework.data.jpa.repository.JpaRepository;
import tp.models.entidad.Establecimiento;


public interface RepositorioEstablecimientos extends JpaRepository<Establecimiento, Long> {
}
