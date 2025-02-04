package entity;
import jakarta.persistence.*;
import lombok.*;


import static jakarta.persistence.GenerationType.AUTO;

@Entity
@Data @EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor @RequiredArgsConstructor
public class Echipament {
    @EqualsAndHashCode.Include
    @NonNull
    @Id
    @GeneratedValue(strategy = AUTO)
    private Integer cod;//primary key
    @NonNull
    private String denumire;
    @Setter(AccessLevel.NONE)
    @NonNull
    private Double valoareEchipament;
    @NonNull
    private String grupaCompatibila;// grupa compatibile (musculara)
    @ManyToOne
    private Comanda comanda;


    public Echipament(Integer cod, String denumire, Double valoareEchipament, String grupaCompatibila, Comanda comanda) {
        this.cod = cod;
        this.denumire = denumire;
        this.valoareEchipament = valoareEchipament;
        this.grupaCompatibila = grupaCompatibila;
        this.comanda = comanda;
    }

    @Override
    public String toString() {
        return "Echipament{" + "cod=" + cod + ", denumire='" + denumire + '\'' + ", valoareEchipament=" + valoareEchipament +
                ", grupaCompatibila='" + grupaCompatibila + '\'' + ", comanda=" + comanda + '}';
    }
}














