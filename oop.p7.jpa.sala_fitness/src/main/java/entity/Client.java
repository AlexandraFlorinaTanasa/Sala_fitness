package entity;
import lombok.*;
import jakarta.persistence.*;

import java.util.Date;

import static jakarta.persistence.TemporalType.DATE;
import jakarta.persistence.*;
import lombok.*;
@Entity
@Getter @Setter @NoArgsConstructor
public class Client extends Utilizator {
    @Temporal(DATE)
    private Date dataNastere;
    private String numarTelefon;
    private Integer numarAbonamentAchizitionat;

    public Client( Integer id,  String nume,   String adresa, Date dataNastere, String numarTelefon, Integer numarAbonamentAchizitionat) {
        super(id, nume, adresa);
        this.dataNastere = dataNastere;
        this.numarTelefon = numarTelefon;
        this.numarAbonamentAchizitionat = numarAbonamentAchizitionat;
    }
}
