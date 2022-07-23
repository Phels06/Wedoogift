package fr.challenge.wedoogift.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "deposit")
public class Deposit {

    private static final Logger LOGGER = LoggerFactory.getLogger(Deposit.class);

    @Id
    @Column(name = "id_deposit")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "amount", nullable = false)
    private int amount;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "id_sender", nullable = false)
    private User sender;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_receiver", nullable = false)
    private User receiver;

    @Temporal(TemporalType.DATE)
    @Column(name = "date_received", nullable = false)
    private Date dateReceived;

    @Temporal(TemporalType.DATE)
    @Column(name = "date_expiration", nullable = false)
    private Date dateExpiration;

    @Column(name = "flag_gift", nullable = false)
    private int flagGift = 0;

    @Column(name = "flag_meal", nullable = false)
    private int flagMeal = 0;

    @Column(name = "flag_used", nullable = false)
    private int flagUsed = 0;

    public Deposit(int amount, User sender, User receiver, Date dateReceived, Date dateExpiration, int flagGift, int flagMeal) {
        LOGGER.info("Création du Deposit");
        this.amount = amount;
        this.sender = sender;
        this.receiver = receiver;
        this.dateReceived = dateReceived;
        this.dateExpiration = dateExpiration;
        this.flagGift = flagGift;
        this.flagMeal = flagMeal;
    }
}
