package entity;

import jakarta.persistence.*;
import jakarta.persistence.*;
import lombok.*;
@Entity
@Data @EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor @RequiredArgsConstructor

public class ClasaAntrenament {
    @NonNull @Id private Integer id; //primary key
    @NonNull private String nume;
    @ManyToOne private Abonament abonament;
    @ManyToOne
    private Antrenor antrenorResponsabil;
}
