package tp.models.repositories;

import tp.models.entities.services.georef.Provincia;
import tp.server.Server;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import java.util.List;

public class RepositorioProvincias {

  private static RepositorioProvincias instancia = null;
  private EntityManager entityManager;

  public RepositorioProvincias(EntityManagerFactory entityManagerFactory) {
    this.entityManager = entityManagerFactory.createEntityManager();
  }

  public static RepositorioProvincias getInstancia() {
    if (instancia == null) {
      instancia = new RepositorioProvincias(Server.entityManagerFactory);
    }
    return instancia;
  }

  public Provincia findById(long id) {
    return this.entityManager.find(Provincia.class, id);
  }

  public Provincia findByNombre(String nombre) {
    return (Provincia) this.entityManager
            .createQuery("from " + Provincia.class.getName() + " where nombre = :n")
            .setParameter("n", nombre)
            .getSingleResult();
  }

  public void registrar(Provincia provincia) {
    EntityTransaction tx = this.entityManager.getTransaction();
    tx.begin();
    this.entityManager.persist(provincia);
    tx.commit();
  }

  public void eliminar(Provincia provincia) {
    EntityTransaction tx = this.entityManager.getTransaction();
    tx.begin();
    this.entityManager.remove(provincia);
    tx.commit();
  }

  public List all() {
    return this.entityManager
            .createQuery("from " + Provincia.class.getName())
            .getResultList();
  }
}
