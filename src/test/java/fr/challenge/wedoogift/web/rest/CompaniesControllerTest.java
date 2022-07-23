package fr.challenge.wedoogift.web.rest;

import fr.challenge.wedoogift.entities.User;
import fr.challenge.wedoogift.service.CompaniesService;
import fr.challenge.wedoogift.service.UserService;
import fr.challenge.wedoogift.web.rest.dto.DistributeDepositDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
class CompaniesControllerTest {

    private static User company;
    private static User user;
    private static DistributeDepositDto distributeDepositDto;

    @Mock
    private UserService userService;

    @Mock
    private CompaniesService companiesService;

    @Spy
    @InjectMocks
    private CompaniesController companiesController;

    @BeforeEach
    public void setUp() {
        company = new User();
        company.setName("Tesla");
        company.setFlagCompany(1);

        user = new User();
        user.setName("John");
        user.setFlagCompany(0);

        distributeDepositDto = new DistributeDepositDto();
        distributeDepositDto.setAmount(100);
        distributeDepositDto.setIdUser(3L);
        distributeDepositDto.setIdCompany(2L);
    }

    @ParameterizedTest
    @CsvSource(value = {"user", "company"})
    public void testDistributeGift_userNotFound(String userNotFound) {
        User user = new User();
        Optional<User> userOpt = Optional.of(user);

        if (userNotFound.equals("user")) {
            doReturn(Optional.empty()).when(userService).findById(3L);
            doReturn(userOpt).when(userService).findById(2L);
        } else {
            doReturn(Optional.empty()).when(userService).findById(2L);
            doReturn(userOpt).when(userService).findById(3L);
        }

        assertEquals(new ResponseEntity<>(HttpStatus.NOT_FOUND), companiesController.distributeGift(distributeDepositDto));
    }

    @ParameterizedTest
    @CsvSource(value = {"false, FORBIDDEN", "true, OK"})
    public void testDistributeGift(boolean isAddDepositSuccessful, HttpStatus reponseAttendue) {
        Optional<User> userOpt = Optional.of(user);
        Optional<User> companyOpt = Optional.of(company);

        doReturn(companyOpt).when(userService).findById(2L);
        doReturn(userOpt).when(userService).findById(3L);

        doReturn(isAddDepositSuccessful).when(companiesService).addDeposit(any());

        assertEquals(new ResponseEntity<>(reponseAttendue), companiesController.distributeGift(distributeDepositDto));
    }

}
