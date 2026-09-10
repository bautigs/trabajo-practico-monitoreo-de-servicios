package tp.services.georef;

import lombok.Getter;

import jakarta.persistence.*;

@Entity
@Table(name = "municipio")
@Getter
public class Municipio {

    @Id
    public Long id;

    @Column(name = "nombre")
    public String nombre;

    @ManyToOne
    @JoinColumn(name = "provincia_id", referencedColumnName = "id")
    public Provincia provincia;
}
