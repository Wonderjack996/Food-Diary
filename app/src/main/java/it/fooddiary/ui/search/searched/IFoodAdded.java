package it.fooddiary.ui.search.searched;

import it.fooddiary.models.Food;
import it.fooddiary.utils.MealType;

public interface IFoodAdded {

    void onFoodAddedToMeal(Food foodToAdd, MealType mealToModify);
}
