package tp.models.repositories;

import tp.models.entities.servicios.Servicio;
import tp.server.Server;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import java.util.List;


public class RepositorioServicios {

  private static RepositorioServicios instancia = null;
  private EntityManager entityManager;

  public RepositorioServicios(EntityManagerFactory entityManagerFactory) {
    this.entityManager = entityManagerFactory.createEntityManager();
  }

  public static RepositorioServicios getInstancia() {
    if (instancia == null) {
      instancia = new RepositorioServicios(Server.entityManagerFactory);
    }
    return instancia;
  }

  public Servicio findById(long id) {
    return this.entityManager.find(Servicio.class, id);
  }

  public Servicio findByNombre(String nombre) {
    return (Servicio) this.entityManager
            .createQuery("from " + Servicio.class.getName() + " where nombre = :n")
            .setParameter("n", nombre)
            .getSingleResult();
  }

  public void registrar(Servicio servicio) {
    EntityTransaction tx = this.entityManager.getTransaction();
    tx.begin();
    this.entityManager.persist(servicio);
    tx.commit();
  }

  public void eliminar(Servicio servicio) {
    EntityTransaction tx = this.entityManager.getTransaction();
    tx.begin();
    this.entityManager.remove(servicio);
    tx.commit();
  }

  public List all() {
    return this.entityManager
            .createQuery("from " + Servicio.class.getName())
            .getResultList();
  }
}
