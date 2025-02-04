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

public class Abonament {
    @EqualsAndHashCode.Include
   @NonNull @Id private Integer id; //primary key
@Temporal(DATE)
private Date dataAbonament;
    @OneToMany(mappedBy = "abonament", cascade = CascadeType.ALL, orphanRemoval = true)
    private List <ClasaAntrenament> clasaAntrenament= new ArrayList<>(); //full body, piept, brate, picioare, etc
    private Integer durataAbonament; //cate luni dureaza abonamentul
    private Double pret;


}
