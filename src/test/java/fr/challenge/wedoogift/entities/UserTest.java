package fr.challenge.wedoogift.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UserTest {

    private static User company;
    private static User user;

    @BeforeEach
    public void setUp() {
        company = new User();
        company.setName("Tesla");
        company.setFlagCompany(1);
        user = new User();
        user.setName("john");
        user.setFlagCompany(0);
    }

    @Test
    public void userTest1() {
        assertAll(
                () -> assertEquals("Tesla", company.getName()),
                () -> assertEquals(1, company.getFlagCompany()));
    }

    @Test
    public void userTest2() {
        assertAll(
                () -> assertEquals("john", user.getName()),
                () -> assertEquals(0, user.getFlagCompany()));
    }

    @ParameterizedTest
    @CsvSource(value = {"0,0,0", "0,100,-366", "100,100,1"})
    public void userTestGiftBalance(int balanceResult, int giftValue, int differenceDays) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(Calendar.getInstance().getTime());
        calendar.add(Calendar.DAY_OF_YEAR, differenceDays);
        Gift gift = new Gift(giftValue, company, user, calendar.getTime());
        user.getDepositsReceived().add(gift);
        company.getDepositsSent().add(gift);

        assertAll(
                () -> assertEquals(balanceResult, user.getBalance()),
                () -> assertEquals(-balanceResult, company.getBalance()));
    }

    @ParameterizedTest
    @CsvSource(value = {"0,0,0", "0,100,-642", "100,100,1"})
    public void userTestMealBalance(int balanceResult, int giftValue, int differenceDays) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(Calendar.getInstance().getTime());
        calendar.add(Calendar.DAY_OF_YEAR, differenceDays);
        Meal meal = new Meal(giftValue, company, user, calendar.getTime());
        user.getDepositsReceived().add(meal);
        company.getDepositsSent().add(meal);

        assertAll(
                () -> assertEquals(balanceResult, user.getBalance()),
                () -> assertEquals(-balanceResult, company.getBalance()));
    }

}
