package tp.models.entities.persistencia;

import lombok.Getter;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
@Getter
@MappedSuperclass
public abstract class Persistente {

  @Id
  @GeneratedValue
  private Long id;
}
