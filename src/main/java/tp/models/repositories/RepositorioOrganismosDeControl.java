package tp.models.repositories;


import tp.models.entities.entidad.OrganismoDeControl;
import tp.server.Server;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import java.util.List;

public class RepositorioOrganismosDeControl {

  private static RepositorioOrganismosDeControl instancia = null;
  private EntityManager entityManager;

  public RepositorioOrganismosDeControl(EntityManagerFactory entityManagerFactory) {
    this.entityManager = entityManagerFactory.createEntityManager();
  }

  public static RepositorioOrganismosDeControl getInstancia() {
    if (instancia == null) {
      instancia = new RepositorioOrganismosDeControl(Server.entityManagerFactory);
    }
    return instancia;
  }

  public OrganismoDeControl findById(long id) {
    return this.entityManager.find(OrganismoDeControl.class, id);
  }

  public OrganismoDeControl findByNombre(String nombre) {
    return (OrganismoDeControl) this.entityManager
            .createQuery("from " + OrganismoDeControl.class.getName() + " where nombre = :n")
            .setParameter("n", nombre)
            .getSingleResult();
  }

  public void registrar(OrganismoDeControl organismoDeControl) {
    EntityTransaction tx = this.entityManager.getTransaction();
    tx.begin();
    this.entityManager.persist(organismoDeControl);
    tx.commit();
  }

  public void eliminar(OrganismoDeControl organismoDeControl) {
    EntityTransaction tx = this.entityManager.getTransaction();
    tx.begin();
    this.entityManager.remove(organismoDeControl);
    tx.commit();
  }

  public List all() {
    return this.entityManager
            .createQuery("from " + OrganismoDeControl.class.getName())
            .getResultList();
  }
}
