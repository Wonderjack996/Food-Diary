package it.fooddiary.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.fooddiary.utils.MealType;

@Entity(tableName = "meals")
public class Meal implements IFoodProperties {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "meal_type")
    private final MealType mealType;

    @ColumnInfo(name = "meal_date")
    private final Date mealDate;

    @ColumnInfo(name = "meal_foods")
    private List<Food> mealFoods;

    public Meal(MealType mealType, Date mealDate) {
        this.mealType = mealType;
        this.mealDate = mealDate;
        this.mealFoods = new ArrayList<Food>();
    }

    public boolean addFood(Food food) {
        return mealFoods.add(food);
    }

    public boolean removeFood(Food food) {
        return mealFoods.remove(food);
    }

    @Override
    public int getTotalCalories() {
        int total = 0;
        for (Food x : mealFoods)
            total += x.getTotalCalories();
        return total;
    }

    @Override
    public int getTotalCarbsGrams() {
        int total = 0;
        for (Food x : mealFoods)
            total += x.getTotalCarbsGrams();
        return total;
    }

    @Override
    public int getTotalProteinsGrams() {
        int total = 0;
        for (Food x : mealFoods)
            total += x.getTotalProteinsGrams();
        return total;
    }

    @Override
    public int getTotalFatsGrams() {
        int total = 0;
        for (Food x : mealFoods)
            total += x.getTotalFatsGrams();
        return total;
    }

    public MealType getMealType() {
        return mealType;
    }

    public Date getMealDate() {
        return mealDate;
    }

    public List<Food> getMealFoods() {
        return mealFoods;
    }

    public void setMealFoods(List<Food> mealFoods) {
        this.mealFoods = mealFoods;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
