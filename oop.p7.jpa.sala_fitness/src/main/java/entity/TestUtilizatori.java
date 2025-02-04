package entity;
import jakarta.persistence.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.*;
public class TestUtilizatori {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.
                createEntityManagerFactory("SalaFitnessJPA");
        EntityManager em = emf.createEntityManager();



        //POPULARE UTILIZATORI

        List<Utilizator> utilizatori=new ArrayList<Utilizator>();
        //Initializare explicita  a utilizatorilor

        utilizatori.add(new Antrenor(101, "Popescu Mircea", "Str. Alfa","Cardio", 3000.34, "0723401978"));
        utilizatori.add(new Antrenor(102, "Salcie Marian", "Str.Beta", "Fitness", 3500.90, "0755623098"));
        utilizatori.add(new Antrenor (103, "Bordeianu Mihai", "Str.Gama","Culturism şi Antrenament Funcţional", 5000.73, "0765039801"));
        utilizatori.add(new Antrenor(104, "Ivan Delia", "Str.Veseliei","Aerobic",4500.97, "0740563126") );
        utilizatori.add(new Client(105, "Sandi Elena", "Str.Armeana", new Date(25- 4 -2000), "0787347699",31));
        utilizatori.add(new Client(106, "Radu Maria", "Str.Codreanu",new Date (21-6-1999),"0734876322",32));
        utilizatori.add(new Client(107, "Avram Mara", "Str.Transilvaniei",new Date(28-9-1995),"0733448890",33));
        utilizatori.add(new Client(108, "Melniciuc Florin", "Str.Elena Doamna", new Date(20-7-1998),"0783476622",34));
        utilizatori.add(new Client(109, "Stefan David", "Str.Primaverii", new Date(1-6-2001),"0756658902",35));
        utilizatori.add(new Client(110, "Apostol Darius", "Str.Garii", new Date(8-5-1996),"0761907189",36));
        utilizatori.add(new Client (111, "Budur Paraschiv", "Str.Grivita", new Date(12-2-1998),"0740671263",37));
        utilizatori.add(new Client(112, "Ciobanu Andrei", "Str.1 Mai",new Date(10-10-1998),"0765901262",38));
        utilizatori.add(new Client(113, "Barsan Daniel", "Str.Teatrului", new Date(20-3-2001),"0755329086",39));
        utilizatori.add(new Client(114, "Istrate Adrian", "Str.Pacii", new Date(21-1-2000), "0755661123",40));
        utilizatori.add(new Client(115, "Eminovici Constantin", "Str.Unirii",new Date(25-12-1999),"0770456812",41));
        utilizatori.add(new Client(116, "Manea Alexandru", "Str.Sararie", new Date(5-5-2000),"0789278123",42));




        // Clean-up utilizatori
        em.getTransaction().begin();
        em.createQuery("DELETE FROM Utilizator u").executeUpdate();
        em.getTransaction().commit();


        // Create
        em.persist(utilizatori.get(0));
        em.persist(utilizatori.get(1));
        em.persist(utilizatori.get(2));
        em.persist(utilizatori.get(3));
        em.persist(utilizatori.get(4));
        em.persist(utilizatori.get(5));
        em.persist(utilizatori.get(6));
        em.persist(utilizatori.get(7));
        em.persist(utilizatori.get(8));
        em.persist(utilizatori.get(9));
        em.persist(utilizatori.get(10));
        em.persist(utilizatori.get(11));
        em.persist(utilizatori.get(12));
        em.persist(utilizatori.get(13));
        em.persist(utilizatori.get(14));
        em.persist(utilizatori.get(15));

        em.getTransaction().begin();
        em.getTransaction().commit();

        em.clear();

        // Read after create
        List<Utilizator> lstUtilizatoriPersistenti = em.
                createQuery("SELECT u FROM Utilizator u", Utilizator.class).getResultList();

        System.out.println("Lista utilizatori persistenti/salvati in baza de date");
        for (Utilizator u: lstUtilizatoriPersistenti)
            System.out.println("Id: " + u.getId() + ", nume: " + u.getNume()+", adresa: " +u.getAdresa() );

        // Tranzactie ce va cumula operatiile Update-Remove
        em.getTransaction().begin();
        // Read-Update
        Utilizator u102 = em.find(Utilizator.class, 102);
        if (u102 != null){
            u102.setNume("Mitru Alex");
        }
        // Read-Remove
        Utilizator u103 = (Utilizator) em
                .createQuery("SELECT u FROM Utilizator u WHERE u.id = 103")
                .getSingleResult();
        if (u103 != null){
            em.remove(u103);
        }
        // Realizare tranzactie - sincronizare cu baza de date
        em.getTransaction().commit();

        em.clear();

        lstUtilizatoriPersistenti = em.
                createQuery("SELECT u FROM Utilizator u", Utilizator.class).getResultList();
        System.out.println("Lista finala utilizatori persistenti/salvati in baza de date");
        for (Utilizator u: lstUtilizatoriPersistenti)
            System.out.println("Id: " + u.getId() + ", nume: " + u.getNume()+", adresa: "+u.getAdresa());
    }

}
