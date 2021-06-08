package it.fooddiary.model_test;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import org.junit.Test;

import it.fooddiary.models.Food;
import it.fooddiary.utils.Constants;

import static org.junit.Assert.*;

public class FoodTest {

    @Test
    public void constructorTest() {
        Food food = new Food(null, -1, -1, -1, -1);
        assertEquals(food.getName(), "");
        assertEquals(food.getQuantity(), Constants.MIN_FOOD_GRAMS);

        food = new Food("prova", 10001, 0, 0, 0);
        assertEquals(food.getName(), "prova");
        assertEquals(food.getQuantity(), Constants.MAX_FOOD_GRAMS);

        food.setQuantity(100);
        assertEquals(food.getQuantity(), 100);
    }

    @Test
    public void getMacroAndCaloriesTest() {
        Food food = new Food("prova", 200, 0.4, 0.2, 0.3);

        assertEquals(80, food.getTotalCarbsGrams());
        assertEquals(40, food.getTotalProteinsGrams());
        assertEquals(60, food.getTotalFatsGrams());
        assertEquals(80*4 + 40*4 + 60*9, food.getTotalCalories());
    }
}
