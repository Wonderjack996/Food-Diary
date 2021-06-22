package it.fooddiary.model_test;

import org.junit.Before;
import org.junit.Test;

import it.fooddiary.models.UserProperties;
import it.fooddiary.utils.Constants;

import static org.junit.Assert.*;

public class UserPropertiesTest {

    private UserProperties userProperties;

    @Before
    public void setUp() throws Exception {
        userProperties = new UserProperties(21, Constants.GENDER_MALE, 180,
                75, Constants.ACTIVITY_LOW);
    }

    @Test
    public void getCaloriesDailyIntake() {
        assertEquals(2076, userProperties.getCaloriesDailyIntake());
    }

    @Test
    public void getCarbsGrams() {
        assertEquals(259, userProperties.getCarbsGrams());
    }

    @Test
    public void getProteinsGrams() {
        assertEquals(103, userProperties.getProteinsGrams());
    }

    @Test
    public void getFatsGrams() {
        assertEquals(69, userProperties.getFatsGrams());
    }
}