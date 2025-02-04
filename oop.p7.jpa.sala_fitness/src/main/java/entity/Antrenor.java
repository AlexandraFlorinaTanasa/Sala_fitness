package entity;
import lombok.*;
import jakarta.persistence.*;
@Entity
@Getter @Setter @NoArgsConstructor
public class Antrenor extends Utilizator{
    private String specializare;
    private Double salariu;
    private String numarTelefon;

    public Antrenor( Integer id,  String nume,   String adresa, String specializare, Double salariu, String numarTelefon) {
        super(id, nume, adresa);
        this.specializare = specializare;
        this.salariu = salariu;
        this.numarTelefon = numarTelefon;
    }
}
