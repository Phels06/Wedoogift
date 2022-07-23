package fr.challenge.wedoogift.entities;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MealTest {

    @ParameterizedTest
    @CsvSource(value = {"1,1,2020,28,2,2021", "27,2,2020,28,2,2021", "31,12,2020,28,2,2021"})
    public void mealTest1(int jourMeal, int moisMeal, int anneeMeal,
                          int jourExpiration, int moisExpiration, int anneeExpiration) {
        User tesla = new User();
        User john = new User();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, jourMeal);
        calendar.set(Calendar.MONTH, moisMeal - 1);
        calendar.set(Calendar.YEAR, anneeMeal);

        Meal meal = new Meal(100, tesla, john, calendar.getTime());

        Calendar calendarExpirationAttendu = Calendar.getInstance();
        calendarExpirationAttendu.set(Calendar.DAY_OF_MONTH, jourExpiration);
        calendarExpirationAttendu.set(Calendar.MONTH, moisExpiration - 1);
        calendarExpirationAttendu.set(Calendar.YEAR, anneeExpiration);

        Calendar calendarMealExpirationCalcule = Calendar.getInstance();
        calendarMealExpirationCalcule.setTime(meal.getDateExpiration());

        assertEquals(calendarExpirationAttendu.get(Calendar.DAY_OF_YEAR), calendarMealExpirationCalcule.get(Calendar.DAY_OF_YEAR));
    }

}
