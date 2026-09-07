package tp.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import tp.models.entidad.Empresa;

public interface RepositorioEmpresas extends JpaRepository<Empresa, Long> {

}
