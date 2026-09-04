package tp.models.repositories;


import tp.models.entities.comunidad.Comunidad;
import tp.server.Server;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import java.util.List;

public class RepositorioComunidades {

  private static RepositorioComunidades instancia = null;
  private EntityManager entityManager;

  public RepositorioComunidades(EntityManagerFactory entityManagerFactory) {
    this.entityManager = entityManagerFactory.createEntityManager();
  }

  public static RepositorioComunidades getInstancia() {
    if (instancia == null) {
      instancia = new RepositorioComunidades(Server.entityManagerFactory);
    }

    return instancia;
  }

  public Comunidad findById(long id) {
    return this.entityManager.find(Comunidad.class, id);
  }

  public Comunidad findByNombre(String nombre) {
    return (Comunidad) this.entityManager
            .createQuery("from " + Comunidad.class.getName() + " where nombre = :n")
            .setParameter("n", nombre)
            .getSingleResult();
  }

  public void registrar(Comunidad comunidad) {
    EntityTransaction tx = this.entityManager.getTransaction();
    tx.begin();
    this.entityManager.persist(comunidad);
    tx.commit();
  }

  public void eliminar(Comunidad comunidad) {
    EntityTransaction tx = this.entityManager.getTransaction();
    tx.begin();
    this.entityManager.remove(comunidad);
    tx.commit();
  }

  public void update(Comunidad comunidad) {
    EntityTransaction tx = this.entityManager.getTransaction();
    tx.begin();
    this.entityManager.merge(comunidad);
    tx.commit();
  }

  public List<Comunidad> all() {
    return this.entityManager
            .createQuery("from " + Comunidad.class.getName())
            .getResultList();
  }
}
