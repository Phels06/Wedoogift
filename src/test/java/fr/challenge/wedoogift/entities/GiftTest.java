package fr.challenge.wedoogift.entities;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GiftTest {

    @ParameterizedTest
    @CsvSource(value = {"15,6,2021,14,6,2022", "1,1,2021,31,12,2021", "1,1,2024,30,12,2024"})
    public void giftTest1(int jourGift, int moisGift, int anneeGift,
                          int jourExpiration, int moisExpiration, int anneeExpiration) {
        User tesla = new User();
        User john = new User();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, jourGift);
        calendar.set(Calendar.MONTH, moisGift - 1);
        calendar.set(Calendar.YEAR, anneeGift);

        Gift gift = new Gift(100, tesla, john, calendar.getTime());

        Calendar calendarExpirationAttendu = Calendar.getInstance();
        calendarExpirationAttendu.set(Calendar.DAY_OF_MONTH, jourExpiration);
        calendarExpirationAttendu.set(Calendar.MONTH, moisExpiration - 1);
        calendarExpirationAttendu.set(Calendar.YEAR, anneeExpiration);

        Calendar calendarGiftExpirationCalcule = Calendar.getInstance();
        calendarGiftExpirationCalcule.setTime(gift.getDateExpiration());

        assertEquals(calendarExpirationAttendu.get(Calendar.DAY_OF_YEAR), calendarGiftExpirationCalcule.get(Calendar.DAY_OF_YEAR));
    }

}
