package fr.challenge.wedoogift.entities;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Date;

@Getter
@Setter
public class Gift extends Deposit {

    private static final Logger LOGGER = LoggerFactory.getLogger(Gift.class);

    public Gift(int amount, User sender, User receiver, Date dateReceived) {
        super(amount, sender, receiver, dateReceived, computeDateExpiration(dateReceived), 1, 0);
    }

    static private Date computeDateExpiration(Date dateReceived) {
        LOGGER.info("Calcule de la date d'expiration du Gift");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateReceived);
        calendar.add(Calendar.DAY_OF_YEAR, 365 - 1);

        LOGGER.info("Date d'expiration calculée:{}", calendar.getTime());
        return calendar.getTime();
    }
}
