package entity;
import jakarta.persistence.*;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.*;
public class TestComenzi {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.
                createEntityManagerFactory("SalaFitnessJPA");
        EntityManager em = emf.createEntityManager();


        // Populare produse
        List<Echipament> lstEchipamentePersistente = em.
                createQuery("SELECT e FROM Echipament e", Echipament.class).getResultList();
        if (!lstEchipamentePersistente.isEmpty()) {
            em.getTransaction().begin();
            for (Echipament e : lstEchipamentePersistente)
                em.remove(e);
            em.getTransaction().commit();
        }
        //


        // Create
        em.getTransaction().begin();
        em.persist(new Echipament(1, "Echipament A", 200.00, "picioare"));
        em.persist(new Echipament(2, "Echipament B", 300.00, "piept"));
        em.persist(new Echipament(3, "Echipament C", 65.00, "spate"));
        em.persist(new Echipament(4, "Echipament D", 55.00, "abdomen"));
        em.persist(new Echipament(5, "Echipament E", 150.00, "biceps"));
        em.persist(new Echipament(6, "Echipament F", 150.00, "triceps"));
        em.persist(new Echipament(7, "Echipament G", 55.00, "umeri"));
        em.getTransaction().commit();
        // Read after create
        lstEchipamentePersistente = em.
                createQuery("SELECT e FROM Echipament e", Echipament.class).getResultList();


        System.out.println("Lista echipamentelor persistente/salvate in baza de date");
        for (Echipament e : lstEchipamentePersistente)
            System.out.println("Cod: " + e.getCod() + ", nume: " + e.getDenumire() +
                    ", pret: " + e.getValoareEchipament() + ", grupa compatibila: " + e.getGrupaCompatibila());


    }
}
