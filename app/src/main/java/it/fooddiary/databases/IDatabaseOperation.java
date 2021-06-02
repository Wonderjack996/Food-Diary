package it.fooddiary.databases;

import it.fooddiary.models.Food;
import it.fooddiary.utils.MealType;

public interface IDatabaseOperation {

    void addFoodToMeal(Food foodToAdd, MealType mealType);

    void modifyFood(Food updatedFood);
}
