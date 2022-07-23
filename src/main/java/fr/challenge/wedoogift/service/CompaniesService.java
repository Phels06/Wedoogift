package fr.challenge.wedoogift.service;

import fr.challenge.wedoogift.entities.Deposit;
import fr.challenge.wedoogift.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CompaniesService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CompaniesService.class);

    public boolean addDeposit(Deposit deposit) {
        boolean isAllowed = checkIsAllowed(deposit.getSender(), deposit);
        LOGGER.info("Droit d'émettre un Deposit:{}", isAllowed);
        if (isAllowed) {
            LOGGER.info("Deposit ajouté chez l'envoyeur et le receveur");
            deposit.getReceiver().getDepositsReceived().add(deposit);
            deposit.getSender().getDepositsSent().add(deposit);
        }
        return isAllowed;
    }

    private boolean checkIsAllowed(User company, Deposit deposit) {
        LOGGER.info("Vérification si l'envoyeur peut émettre un Deposit");
        return deposit.getAmount() > 0 && company.getBalance() >= deposit.getAmount();
    }
}
