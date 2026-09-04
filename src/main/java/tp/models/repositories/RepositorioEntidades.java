package tp.models.repositories;

import tp.models.entities.entidad.Entidad;
import tp.server.Server;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import java.util.List;

public class RepositorioEntidades {

    private static RepositorioEntidades instancia = null;
    private EntityManager entityManager;

    public RepositorioEntidades(EntityManagerFactory entityManagerFactory) {
        this.entityManager = entityManagerFactory.createEntityManager();
    }

    public static RepositorioEntidades getInstancia() {
        if (instancia == null) {
            instancia = new RepositorioEntidades(Server.entityManagerFactory);
        }
        return instancia;
    }

    public Entidad findById(long id) {
        return this.entityManager.find(Entidad.class, id);
    }

    public Entidad findByNombre(String nombre) {
        return (Entidad) this.entityManager
                .createQuery("from " + Entidad.class.getName() + " where nombre = :n")
                .setParameter("n", nombre)
                .getSingleResult();
    }

    public void registrar(Entidad entidad) {
        EntityTransaction tx = this.entityManager.getTransaction();
        tx.begin();
        this.entityManager.persist(entidad);
        tx.commit();
    }

    public void eliminar(Entidad entidad) {
        EntityTransaction tx = this.entityManager.getTransaction();
        tx.begin();
        this.entityManager.remove(entidad);
        tx.commit();
    }
    public void update(Entidad entidad) {
        EntityTransaction tx = this.entityManager.getTransaction();
        tx.begin();
        this.entityManager.merge(entidad);
        tx.commit();
    }

    public List<Entidad> all() {
        return this.entityManager
                .createQuery("from " + Entidad.class.getName())
                .getResultList();
    }
}
