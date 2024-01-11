package com.distribuida.servicios;

import com.distribuida.db.Persona;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import java.util.List;

@ApplicationScoped
public class ServicioPersonaImpl implements ServicioPersona {
    @Inject
    EntityManager em;

    @Override
    public List<Persona> findAll() {
        return em.createQuery("select o from Persona o")
                .getResultList();
    }

    public Persona findById(Integer id) {
        return em.find(Persona.class, id);
    }

    @Override
    public boolean deleteById(Integer id) {
        Persona p = this.findById(id);
        var tx = em.getTransaction();

        try {
            tx.begin();
            em.remove(p);
            tx.commit();
            return true;
        } catch (Exception ex) {
            tx.rollback();
            return false;
        }
    }

    public boolean insert(Persona p) {
        var tx = em.getTransaction();

        try {
            tx.begin();
            em.persist(p);
            tx.commit();
            return true;
        } catch (Exception ex) {
            tx.rollback();
            return false;
        }
    }

    public boolean update(Persona p) {
        var tx = em.getTransaction();

        try {
            tx.begin();
            em.merge(p);
            tx.commit();
            return true;
        } catch (Exception ex) {
            tx.rollback();
            return false;
        }
    }
}
