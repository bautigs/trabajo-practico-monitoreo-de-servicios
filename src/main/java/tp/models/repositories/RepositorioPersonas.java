package tp.models.repositories;

import tp.models.entities.comunidad.Persona;
import tp.server.Server;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import java.util.List;

public class RepositorioPersonas {

  private static RepositorioPersonas instancia = null;
  private EntityManager entityManager;

  public RepositorioPersonas(EntityManagerFactory entityManagerFactory) {
    this.entityManager = entityManagerFactory.createEntityManager();

  }

  public static RepositorioPersonas getInstancia() {
    if (instancia == null) {
      instancia = new RepositorioPersonas(Server.entityManagerFactory);
    }
    return instancia;
  }

  public Persona findById(long id) {
    return this.entityManager.find(Persona.class, id);
  }

  public Persona findByNombre(String nombre) {
    return (Persona) this.entityManager
            .createQuery("from " + Persona.class.getName() + " where nombre = :n")
            .setParameter("n", nombre)
            .getSingleResult();
  }

  public Persona findByUsuario(Long usuario_id) {
    return (Persona) this.entityManager
            .createQuery("from " + Persona.class.getName() + " where usuario_id = :n")
            .setParameter("n", usuario_id)
            .getSingleResult();
  }

  public void registrar(Persona persona) {
    EntityTransaction tx = this.entityManager.getTransaction();
    tx.begin();
    this.entityManager.persist(persona);
    tx.commit();
  }

  public void eliminar(Persona persona) {
    EntityTransaction tx = this.entityManager.getTransaction();
    tx.begin();
    this.entityManager.remove(persona);
    tx.commit();
  }

  public void update(Persona persona) {
    EntityTransaction tx = this.entityManager.getTransaction();
    tx.begin();
    this.entityManager.merge(persona);
    tx.commit();
  }

  public void refresh(Persona persona) {
    EntityTransaction tx = this.entityManager.getTransaction();
    tx.begin();
    this.entityManager.refresh(persona);
    tx.commit();
  }

  public List<Persona> all() {
    return this.entityManager
            .createQuery("from " + Persona.class.getName())
            .getResultList();
  }
}
