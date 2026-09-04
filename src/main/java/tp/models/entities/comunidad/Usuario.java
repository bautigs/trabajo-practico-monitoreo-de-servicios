package tp.models.entities.comunidad;

import lombok.Getter;
import lombok.Setter;
import tp.models.entities.persistencia.Persistente;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "usuario")
public class Usuario extends Persistente {

  @Column(name = "nombreUsuario")
  private String nombreUsuario;

  @Column(name = "contrasenia")
  private String contrasenia;


  private LocalDate fechaDeAlta;
}
