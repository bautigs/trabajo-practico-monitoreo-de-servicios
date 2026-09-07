package tp.models.entidad;

import lombok.Getter;
import lombok.Setter;
import tp.models.persistencia.Persistente;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "tipoEntidad")
@Getter
@Setter
public class TipoEntidad extends Persistente {

  private String nombre;
}
