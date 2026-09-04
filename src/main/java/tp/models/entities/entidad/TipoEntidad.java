package tp.models.entities.entidad;

import lombok.Getter;
import lombok.Setter;
import tp.models.entities.persistencia.Persistente;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "tipoEntidad")
@Getter
@Setter
public class TipoEntidad extends Persistente {

  private String nombre;
}
