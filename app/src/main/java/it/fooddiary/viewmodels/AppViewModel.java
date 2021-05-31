package it.fooddiary.viewmodels;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.fooddiary.models.Food;
import it.fooddiary.models.Meal;
import it.fooddiary.models.MealProperties;
import it.fooddiary.models.edamam_models.EdamamResponse;
import it.fooddiary.repositories.AppRepository;
import it.fooddiary.utils.MealType;

public class AppViewModel extends AndroidViewModel {

    private AppRepository repository;

    public AppViewModel(@NonNull @NotNull Application application) {
        super(application);
    }

    public AppViewModel(@NonNull @NotNull Application application,
                        AppRepository repository) {
        super(application);
        this.repository = repository;
    }

    public Date getCurrentDate(SharedPreferences preferences) {
        return repository.loadCurrentDate(preferences);
    }

    public void setCurrentDate(Date date, SharedPreferences preferences) {
        repository.saveCurrentDate(date, preferences);
    }

    public LiveData<Meal> getMealByTypeAndDate(MealType mealType, Date date) {
        return repository.getMealByTypeAndDate(mealType, date);
    }

    public LiveData<Integer> insertFoodInMeal(Food foodToInsert, MealType mealType, Date date) {
        return repository.insertFoodInMeal(foodToInsert, mealType, date);
    }

    public LiveData<Integer> removeFoodFromMeal(Food foodToRemove, MealType mealType, Date date) {
        return repository.removeFoodFromMeal(foodToRemove, mealType, date);
    }

    public LiveData<MealProperties> getMealProperties() {
        return repository.getMealProperties();
    }

    public void setCaloriesIntake(int newIntake) {
        repository.setDailyCaloriesIntake(newIntake);
    }

    public LiveData<EdamamResponse> getEdamamResponse(String ingredient) {
        return repository.fetchFoods(ingredient);
    }
    public LiveData<Integer> addFoodToRecent(Food foodToAdd){
        return repository.addFoodToRecent(foodToAdd);
    }
}
