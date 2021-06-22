package it.fooddiary.viewmodels.user;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import org.jetbrains.annotations.NotNull;

import it.fooddiary.repositories.UserRepository;

public class UserViewModelFactory implements ViewModelProvider.Factory {

    private final Application application;
    private final UserRepository userRepository;

    public UserViewModelFactory(@NonNull @NotNull Application application,
                                @NonNull @NotNull UserRepository userRepository) {
        this.application = application;
        this.userRepository = userRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new UserViewModel(application, userRepository);
    }
}