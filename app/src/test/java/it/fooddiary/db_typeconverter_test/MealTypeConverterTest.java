package it.fooddiary.db_typeconverter_test;

import org.junit.Test;

import it.fooddiary.databases.converters.MealTypeConverter;
import it.fooddiary.utils.MealType;

import static org.junit.Assert.*;

public class MealTypeConverterTest {

    @Test
    public void fromTimestampTest() {
        assertNull(MealTypeConverter.fromTimestamp(null));

        assertEquals(MealTypeConverter.fromTimestamp("BREAKFAST"), MealType.BREAKFAST);
        assertEquals(MealTypeConverter.fromTimestamp("LUNCH"), MealType.LUNCH);
        assertEquals(MealTypeConverter.fromTimestamp("DINNER"), MealType.DINNER);
        assertEquals(MealTypeConverter.fromTimestamp("SNACKS"), MealType.SNACKS);
    }

    @Test
    public void toTimestampTest() {
        assertNull(MealTypeConverter.mealTypeToTimestamp(null));

        assertEquals("BREAKFAST", MealTypeConverter.mealTypeToTimestamp(MealType.BREAKFAST));
        assertEquals("LUNCH", MealTypeConverter.mealTypeToTimestamp(MealType.LUNCH));
        assertEquals("DINNER", MealTypeConverter.mealTypeToTimestamp(MealType.DINNER));
        assertEquals("SNACKS", MealTypeConverter.mealTypeToTimestamp(MealType.SNACKS));
    }
}
