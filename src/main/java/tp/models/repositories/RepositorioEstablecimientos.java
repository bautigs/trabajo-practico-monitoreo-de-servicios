package tp.models.repositories;


import tp.models.entities.entidad.Establecimiento;
import tp.server.Server;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import java.util.List;

public class RepositorioEstablecimientos {

  private static RepositorioEstablecimientos instancia = null;
  private EntityManager entityManager;

  public RepositorioEstablecimientos(EntityManagerFactory entityManagerFactory) {
    this.entityManager = entityManagerFactory.createEntityManager();
  }

  public static RepositorioEstablecimientos getInstancia() {
    if (instancia == null) {
      instancia = new RepositorioEstablecimientos(Server.entityManagerFactory);
    }
    return instancia;
  }

  public Establecimiento findById(long id) {
    return this.entityManager.find(Establecimiento.class, id);
  }

  public Establecimiento findByNombre(String nombre) {
    return (Establecimiento) this.entityManager
            .createQuery("from " + Establecimiento.class.getName() + " where nombre = :n")
            .setParameter("n", nombre)
            .getSingleResult();
  }

  public void registrar(Establecimiento establecimiento) {
    EntityTransaction tx = this.entityManager.getTransaction();
    tx.begin();
    this.entityManager.persist(establecimiento);
    tx.commit();
  }

  public void eliminar(Establecimiento establecimiento) {
    EntityTransaction tx = this.entityManager.getTransaction();
    tx.begin();
    this.entityManager.remove(establecimiento);
    tx.commit();
  }

  public List all() {
    return this.entityManager
            .createQuery("from " + Establecimiento.class.getName())
            .getResultList();
  }
}
