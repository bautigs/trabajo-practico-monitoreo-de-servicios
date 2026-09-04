package tp.models.repositories;

import tp.models.entities.comunidad.Miembro;
import tp.server.Server;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import java.util.List;

public class RepositorioMiembros {

    private static RepositorioMiembros instancia = null;
    private EntityManager entityManager;

    public RepositorioMiembros(EntityManagerFactory entityManagerFactory) {
        this.entityManager = entityManagerFactory.createEntityManager();
    }

    public static RepositorioMiembros getInstancia() {
        if (instancia == null) {
            instancia = new RepositorioMiembros(Server.entityManagerFactory);
        }
        return instancia;
    }

    public Miembro findById(long id) {
        return this.entityManager.find(Miembro.class, id);
    }

    public Miembro findByNombre(String nombre) {
        return (Miembro) this.entityManager
                .createQuery("from " + Miembro.class.getName() + " where nombre = :n")
                .setParameter("n", nombre)
                .getSingleResult();
    }

    public List findByIdPersona(String id) {
        return this.entityManager
                .createQuery("from " + Miembro.class.getName() + " where persona_id = :n")
                .setParameter("n", id)
                .getResultList();
    }

    public void registrar(Miembro miembro) {
        EntityTransaction tx = this.entityManager.getTransaction();
        tx.begin();
        this.entityManager.persist(miembro);
        tx.commit();
    }

    public void eliminar(Miembro miembro) {
        EntityTransaction tx = this.entityManager.getTransaction();
        tx.begin();
        this.entityManager.remove(miembro);
        tx.commit();
    }

    public void update(Miembro miembro) {
        EntityTransaction tx = this.entityManager.getTransaction();
        tx.begin();
        this.entityManager.merge(miembro);
        tx.commit();
    }

    public void refresh(Miembro miembro) {
        EntityTransaction tx = this.entityManager.getTransaction();
        tx.begin();
        this.entityManager.refresh(miembro);
        tx.commit();
    }

    public List<Miembro> all() {
        return this.entityManager
                .createQuery("from " + Miembro.class.getName())
                .getResultList();
    }


}
