package tp.models.repositories;

import tp.models.entities.services.georef.Municipio;
import tp.server.Server;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import java.util.List;

public class RepositorioMunicipios {

  private static RepositorioMunicipios instancia = null;
  private EntityManager entityManager;

  public RepositorioMunicipios(EntityManagerFactory entityManagerFactory) {
    this.entityManager = entityManagerFactory.createEntityManager();
  }

  public static RepositorioMunicipios getInstancia() {
    if (instancia == null) {
      instancia = new RepositorioMunicipios(Server.entityManagerFactory);
    }
    return instancia;
  }

  public Municipio findById(long id) {
    return this.entityManager.find(Municipio.class, id);
  }

  public Municipio findByNombre(String nombre) {
    return (Municipio) this.entityManager
            .createQuery("from " + Municipio.class.getName() + " where nombre = :n")
            .setParameter("n", nombre)
            .getSingleResult();
  }

  public void registrar(Municipio municipio) {
    EntityTransaction tx = this.entityManager.getTransaction();
    tx.begin();
    this.entityManager.persist(municipio);
    tx.commit();
  }

  public void eliminar(Municipio municipio) {
    EntityTransaction tx = this.entityManager.getTransaction();
    tx.begin();
    this.entityManager.remove(municipio);
    tx.commit();
  }

  public List all() {
    return this.entityManager
            .createQuery("from " + Municipio.class.getName())
            .getResultList();
  }

  public List<Municipio> findByProvincia(Long id) {
    return this.entityManager
            .createQuery("from " + Municipio.class.getName() + " where provincia_id = :provinciaId", Municipio.class)
            .setParameter("provinciaId", id)
            .getResultList();
  }

}
