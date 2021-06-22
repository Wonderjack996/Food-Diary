package it.fooddiary.viewmodels.food;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.List;

import it.fooddiary.models.Food;
import it.fooddiary.models.Meal;
import it.fooddiary.models.edamam_models.EdamamResponse;
import it.fooddiary.repositories.FoodRepository;
import it.fooddiary.utils.MealType;

public class FoodViewModel extends AndroidViewModel {

    private final FoodRepository repository;

    public FoodViewModel(@NonNull @NotNull Application application,
                         @NonNull @NotNull FoodRepository repository) {
        super(application);
        this.repository = repository;
    }

    public Date getCurrentDate() {
        return repository.loadCurrentDate();
    }

    public void setCurrentDate(@NonNull @NotNull Date date) {
        repository.saveCurrentDate(date);
    }

    public LiveData<Meal> getMealByTypeAndDate(@NonNull @NotNull MealType mealType,
                                               @NonNull @NotNull Date date) {
        return repository.getMealByTypeAndDate(mealType, date);
    }

    public LiveData<Integer> insertFoodInMeal(@NonNull @NotNull Food foodToInsert,
                                              @NonNull @NotNull MealType mealType,
                                              @NonNull @NotNull Date date) {
        return repository.insertFoodInMeal(foodToInsert, mealType, date);
    }

    public LiveData<Integer> removeFoodFromMeal(@NonNull @NotNull Food foodToRemove,
                                                @NonNull @NotNull MealType mealType,
                                                @NonNull @NotNull Date date) {
        return repository.removeFoodFromMeal(foodToRemove, mealType, date);
    }

    public LiveData<Integer> updateFoodInMeal(@NonNull @NotNull Food foodToUpdate,
                                              @NonNull @NotNull MealType mealType,
                                              @NonNull @NotNull Date date) {
        return repository.updateFoodInMeal(foodToUpdate, mealType, date);
    }

    public LiveData<EdamamResponse> getEdamamResponse(@NonNull @NotNull String ingredient) {
        return repository.fetchFoods(ingredient);
    }

    public LiveData<Integer> addFoodToRecent(@NonNull @NotNull Food foodToAdd) {
        return repository.addFoodToRecent(foodToAdd);
    }

    public LiveData<List<Food>> getRecentFoods() {
        return repository.getRecentFoods();
    }

    public LiveData<Integer> removeFoodFromRecent(@NonNull @NotNull Food foodToRemove) {
        return repository.removeFoodFormRecent(foodToRemove);
    }
}