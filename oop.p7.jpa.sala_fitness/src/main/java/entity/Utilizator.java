package entity;
import lombok.*;
import jakarta.persistence.*;
@Entity
@Data @EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor @RequiredArgsConstructor
public class Utilizator {
    @EqualsAndHashCode.Include
     @NonNull @Id private Integer id; //primary key
    @NonNull private String nume;
   @NonNull private String adresa;
}


