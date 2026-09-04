package tp.models.entities.servicios;

import lombok.Getter;
import lombok.Setter;
import tp.models.entities.persistencia.Persistente;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "tipoServicio")
@Getter
@Setter
public class TipoServicio extends Persistente {

    @Column(name = "nombre")
    private String nombre;
}
