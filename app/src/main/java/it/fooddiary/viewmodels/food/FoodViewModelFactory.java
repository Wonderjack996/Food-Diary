package it.fooddiary.viewmodels.food;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import org.jetbrains.annotations.NotNull;

import it.fooddiary.repositories.FoodRepository;

public class FoodViewModelFactory implements ViewModelProvider.Factory {

    private final Application application;
    private final FoodRepository foodRepository;

    public FoodViewModelFactory(@NonNull @NotNull Application application,
                                @NonNull @NotNull FoodRepository foodRepository) {
        this.application = application;
        this.foodRepository = foodRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new FoodViewModel(application, foodRepository);
    }
}