package tp.models.services.georef;


import lombok.Getter;

import jakarta.persistence.*;

@Entity
@Table(name = "localidad")
@Getter
public class Localidad {

  @Id
  public Long id;

  @Column(name = "nombre")
  public String nombre;

  @ManyToOne
  @JoinColumn(name = "municipio_id", referencedColumnName = "id")
  public Municipio municipio;
}
