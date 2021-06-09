package it.fooddiary.model_test;

import org.junit.Before;
import org.junit.Test;

public class MealPropertiesTest {

    private MealProperties mealProperties;

    @Before
    public void setUp() throws Exception {
        mealProperties = new MealProperties(3500, 0.4,
                0.3, 0.2);
    }

    @Test
    public void getCarbsGrams() {
        assertEquals(350, mealProperties.getCarbsGrams());
    }

    @Test
    public void getProteinsGrams() {
        assertEquals(262, mealProperties.getProteinsGrams());
    }

    @Test
    public void getFatsGrams() {
        assertEquals(77, mealProperties.getFatsGrams());
    }
}