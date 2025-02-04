package entity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;


import jakarta.persistence.*;

public class TestGenerareSchemaSQL {
    public static void main(String[] args){
        // Comutare in persistence.xml
        // <property name="jakarta.persistence.schema-generation.database.action" value="drop-and-create"/>
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SalaFitnessJPA");
        EntityManager em = emf.createEntityManager();
        //
    }
}
