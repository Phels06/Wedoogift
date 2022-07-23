package fr.challenge.wedoogift.web.rest;

import fr.challenge.wedoogift.entities.Deposit;
import fr.challenge.wedoogift.entities.Gift;
import fr.challenge.wedoogift.entities.Meal;
import fr.challenge.wedoogift.entities.User;
import fr.challenge.wedoogift.service.CompaniesService;
import fr.challenge.wedoogift.service.UserService;
import fr.challenge.wedoogift.web.rest.dto.DistributeDepositDto;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Calendar;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/companies")
public class CompaniesController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CompaniesController.class);

    @Autowired
    private CompaniesService companiesService;

    @Autowired
    private UserService userService;

    @ApiOperation(consumes = MediaType.APPLICATION_JSON_VALUE, value = "Distribute gift deposits to a user")
    @PostMapping(value = {"/distributegift"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity distributeGift(@Valid @RequestBody DistributeDepositDto inDto) {
        return distribute("gift", inDto);
    }

    @ApiOperation(consumes = MediaType.APPLICATION_JSON_VALUE, value = "Distribute meal deposits to a user")
    @PostMapping(value = {"/distributemeal"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity distributeMeal(@Valid @RequestBody DistributeDepositDto inDto) {
        return distribute("meal", inDto);
    }

    private ResponseEntity distribute(String distributeType, DistributeDepositDto inDto) {
        LOGGER.info("Récupération du User émetteur");
        Optional<User> companyOpt = userService.findById(inDto.getIdCompany());
        LOGGER.info("Récupération du User receveur");
        Optional<User> userOpt = userService.findById(inDto.getIdUser());
        if (!companyOpt.isPresent() || !userOpt.isPresent()) {
            LOGGER.info("Un User est manquant");
            LOGGER.info("Présence User émetteur:{}", companyOpt.isPresent());
            LOGGER.info("Présence User receveur:{}", userOpt.isPresent());
            LOGGER.info("Traitement KO : User non trouvé");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        LOGGER.info("Récupération de la date d'émission");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(Calendar.getInstance().getTime());
        LOGGER.info("Date d'émission:{}", calendar.getTime());

        Deposit deposit;
        if (distributeType.equals("gift")) {
            LOGGER.info("L'objet distribué est un gift");
            deposit = new Gift(inDto.getAmount(), companyOpt.get(), userOpt.get(), calendar.getTime());
        } else {
            LOGGER.info("L'objet distribué est un meal");
            deposit = new Meal(inDto.getAmount(), companyOpt.get(), userOpt.get(), calendar.getTime());
        }

        boolean depositSuccessful = companiesService.addDeposit(deposit);
        if (!depositSuccessful) {
            LOGGER.info("Traitement KO : l'émetteur n'a pas les fonds suffisants");
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        LOGGER.info("Sauvegarde des User et de leur Deposits");
        userService.save(userOpt.get());

        LOGGER.info("Traitement OK");
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
