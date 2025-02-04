package entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static jakarta.persistence.TemporalType.DATE;

@Entity
@Data @EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor @RequiredArgsConstructor
public class Comanda {
    @EqualsAndHashCode.Include
    @NonNull @Id private Integer id;
    @Temporal(DATE)
    @NonNull private Date dataComanda;

    @OneToMany(mappedBy = "comanda", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Echipament> echipamente = new ArrayList<Echipament>();
    @ManyToOne
    private Client client;


    /* Adaugare proprietate valoare totala*/
    @Setter(AccessLevel.NONE)
    private Double valoareTotala;
    private Double calculValoareTotala(){
        Double valoare = 0.0;
        for (Echipament e: echipamente)
            valoare += e.getValoareEchipament();
        return valoare;
    }
    public Double getValoareTotala(){
        if (valoareTotala == null)
            this.valoareTotala = calculValoareTotala();
        return valoareTotala;
    }

    /* Adaugare operatie manipulare detalii colectie*/
    public void adaugaEchipament(Echipament echipament){
        if(!this.echipamente.contains(echipament)) {
            this.echipamente.add(echipament);
            this.valoareTotala = calculValoareTotala();
        }
    }
    public void stergeEchipament(Echipament echipament){
        if(!this.echipamente.contains(echipament)) {
            this.echipamente.remove(echipament);
            this.valoareTotala = calculValoareTotala();
        }
    }

    @Override
    public String toString() {
        return "Comanda{" + "id=" + id + ", dataComanda=" + dataComanda + ", echipamente=" + echipamente +
                ", client=" + client + ", valoareTotala=" + valoareTotala + '}';
    }
}



