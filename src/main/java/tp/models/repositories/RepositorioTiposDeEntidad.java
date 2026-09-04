package tp.models.repositories;

import tp.models.entities.entidad.TipoEntidad;
import tp.server.Server;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import java.util.List;

public class RepositorioTiposDeEntidad {

  private static RepositorioTiposDeEntidad instancia = null;
  private EntityManager entityManager;

  public RepositorioTiposDeEntidad(EntityManagerFactory entityManagerFactory) {
    this.entityManager = entityManagerFactory.createEntityManager();
  }

  public static RepositorioTiposDeEntidad getInstancia() {
    if (instancia == null) {
      instancia = new RepositorioTiposDeEntidad(Server.entityManagerFactory);
    }
    return instancia;
  }

  public TipoEntidad findById(long id) {
    return this.entityManager.find(TipoEntidad.class, id);
  }

  public TipoEntidad findByNombre(String nombre) {
    return (TipoEntidad) this.entityManager
            .createQuery("from " + TipoEntidad.class.getName() + " where nombre = :n")
            .setParameter("n", nombre)
            .getSingleResult();
  }

  public void registrar(TipoEntidad tipoEntidad) {
    EntityTransaction tx = this.entityManager.getTransaction();
    tx.begin();
    this.entityManager.persist(tipoEntidad);
    tx.commit();
  }

  public void eliminar(TipoEntidad tipoEntidad) {
    EntityTransaction tx = this.entityManager.getTransaction();
    tx.begin();
    this.entityManager.remove(tipoEntidad);
    tx.commit();
  }

  public List all() {
    return this.entityManager
            .createQuery("from " + TipoEntidad.class.getName())
            .getResultList();
  }
}
