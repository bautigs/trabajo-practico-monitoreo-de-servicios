package tp.models.repositories;


import tp.models.entities.entidad.Empresa;
import tp.server.Server;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import java.util.List;

public class RepositorioEmpresas  {

  private static RepositorioEmpresas instancia = null;
  private EntityManager entityManager;

  public RepositorioEmpresas(EntityManagerFactory entityManagerFactory) {
    this.entityManager = entityManagerFactory.createEntityManager();
  }

  public static RepositorioEmpresas getInstancia() {
    if (instancia == null) {
      instancia = new RepositorioEmpresas(Server.entityManagerFactory);
    }
    return instancia;
  }

  public Empresa findById(long id) {
    return this.entityManager.find(Empresa.class, id);
  }

  public Empresa findByNombre(String nombre) {
    return (Empresa) this.entityManager
            .createQuery("from " + Empresa.class.getName() + " where nombre = :n")
            .setParameter("n", nombre)
            .getSingleResult();
  }

  public void registrar(Empresa empresa) {
    EntityTransaction tx = this.entityManager.getTransaction();
    tx.begin();
    this.entityManager.persist(empresa);
    tx.commit();
  }

  public void eliminar(Empresa empresa) {
    EntityTransaction tx = this.entityManager.getTransaction();
    tx.begin();
    this.entityManager.remove(empresa);
    tx.commit();
  }

  public List all() {
    return this.entityManager
            .createQuery("from " + Empresa.class.getName())
            .getResultList();
  }
}
