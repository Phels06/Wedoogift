package fr.challenge.wedoogift.service;

import fr.challenge.wedoogift.entities.Deposit;
import fr.challenge.wedoogift.entities.Gift;
import fr.challenge.wedoogift.entities.Meal;
import fr.challenge.wedoogift.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class CompaniesServiceTest {

    private static Calendar calendar;

    private static User company;
    private static User otherCompany;
    private static User user;

    @Autowired
    private CompaniesService companiesService;

    @BeforeEach
    public void setUp() {
        calendar = Calendar.getInstance();
        calendar.setTime(Calendar.getInstance().getTime());

        company = new User();
        company.setName("Tesla");
        company.setFlagCompany(1);

        otherCompany = new User();
        otherCompany.setName("Other company");
        otherCompany.setFlagCompany(1);

        Gift giftCompany = new Gift(900, company, otherCompany, calendar.getTime());
        company.getDepositsReceived().add(giftCompany);

        user = new User();
        user.setName("John");
        user.setFlagCompany(0);
    }

    @ParameterizedTest
    @CsvSource(value = {"1, true, gift", "901, false, gift", "1000, false, gift", "900, true, gift", "0, false, gift", "-1, false, gift",
            "1, true, meal", "901, false, meal", "1000, false, meal", "900, true, meal", "0, false, meal", "-1, false, meal"})
    public void CompagniesService_addDepositTest(int amountDeposit, boolean isDepositAdded, String distributeType) {
        Deposit deposit;
        if (distributeType.equals("gift")) {
            deposit = new Gift(amountDeposit, company, user, calendar.getTime());
        } else {
            deposit = new Meal(amountDeposit, company, user, calendar.getTime());
        }

        boolean depositSuccessfulCalcule = companiesService.addDeposit(deposit);

        assertAll(
                () -> assertEquals(isDepositAdded, user.getDepositsReceived().stream().anyMatch(depositReceived -> depositReceived.equals(deposit))),
                () -> assertEquals(isDepositAdded, company.getDepositsSent().stream().anyMatch(depositSent -> depositSent.equals(deposit))),
                () -> assertEquals(isDepositAdded, depositSuccessfulCalcule)
        );
    }

    @ParameterizedTest
    @CsvSource(value = {"1, true, gift", "901, true, gift", "1000, true, gift", "1001, false, gift", "1100, false, gift", "900, true, gift", "0, false, gift", "-1, false, gift",
            "1, true, meal", "901, true, meal", "1000, true, meal", "1001, false, meal", "1100, false, meal", "900, true, meal", "0, false, meal", "-1, false, meal"})
    public void CompagniesService_addDepositTest_companyIsBalanceWithGiftAndMeal(int amountDeposit, boolean isDepositAdded, String distributeType) {
        Meal mealCompany = new Meal(100, company, otherCompany, calendar.getTime());
        company.getDepositsReceived().add(mealCompany);

        Deposit deposit;
        if (distributeType.equals("gift")) {
            deposit = new Gift(amountDeposit, company, user, calendar.getTime());
        } else {
            deposit = new Meal(amountDeposit, company, user, calendar.getTime());
        }

        boolean depositSuccessfulCalcule = companiesService.addDeposit(deposit);

        assertAll(
                () -> assertEquals(isDepositAdded, user.getDepositsReceived().stream().anyMatch(depositReceived -> depositReceived.equals(deposit))),
                () -> assertEquals(isDepositAdded, company.getDepositsSent().stream().anyMatch(depositSent -> depositSent.equals(deposit))),
                () -> assertEquals(isDepositAdded, depositSuccessfulCalcule)
        );
    }

}
