package tp.services.georef;

import lombok.Getter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "provincia")
@Getter
public class Provincia {

    @Id
    public Long id;

    @Column(name = "nombre")
    public String nombre;
}
