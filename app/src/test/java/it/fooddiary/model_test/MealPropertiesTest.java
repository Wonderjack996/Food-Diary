package it.fooddiary.model_test;

import android.os.Parcel;
import android.os.Parcelable;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.fooddiary.models.Food;
import it.fooddiary.models.Meal;
import it.fooddiary.utils.MealType;

import static org.junit.Assert.assertEquals;

public class MealPropertiesTest {

    @Test
    public void getMacroAndCaloriesTest() {
        int i;
        int size = 3;
        Date date =  new Date();
        Meal meal = new Meal(MealType.LUNCH, date);
        for ( i = 0; i < size; i++){
            Food food = new Food("food", 100, 0.4, 0.2, 0.3);
            meal.addFood(food);
        }
        assertEquals(120, meal.getTotalCarbsGrams()); //carbs
        assertEquals(60, meal.getTotalProteinsGrams()); //protein
        assertEquals(90, meal.getTotalFatsGrams()); // fat
        assertEquals((40 * 4 + 20 * 4 + 30 * 9) * size, meal.getTotalCalories()); //calories
    }
}