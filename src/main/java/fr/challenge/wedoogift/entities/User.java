package fr.challenge.wedoogift.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "\"user\"")
public class User {

    private static final Logger LOGGER = LoggerFactory.getLogger(User.class);

    @Id
    @Column(name = "id_user")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "\"name\"", nullable = false)
    private String name;

    @Column(name = "flag_company", nullable = false)
    private int flagCompany;

    @OneToMany(mappedBy = "sender", fetch = FetchType.LAZY)
    private List<Deposit> depositsSent = new ArrayList<>();

    @OneToMany(mappedBy = "receiver", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Deposit> depositsReceived = new ArrayList<>();

    public int getBalance() {
        LOGGER.info("Récupération de la date pour le calcule du Balance");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(Calendar.getInstance().getTime());
        LOGGER.info("Date de calcul du Balance:{}", calendar.getTime());

        LOGGER.info("Calcule du Balance");
        int balance = 0;
        for (Deposit deposit : depositsReceived) {
            if (calendar.getTime().before(deposit.getDateExpiration())) {
                balance += deposit.getAmount();
            }
        }
        for (Deposit deposit : depositsSent) {
            if (calendar.getTime().before(deposit.getDateExpiration())) {
                balance -= deposit.getAmount();
            }
        }
        LOGGER.info("Valeur du Balance calculé:{}", balance);
        return balance;
    }
}
