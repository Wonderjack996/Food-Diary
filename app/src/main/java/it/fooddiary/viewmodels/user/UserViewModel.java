package it.fooddiary.viewmodels.user;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.jetbrains.annotations.NotNull;

import it.fooddiary.repositories.UserRepository;

public class UserViewModel extends AndroidViewModel {

    private final UserRepository repository;

    public UserViewModel(@NonNull @NotNull Application application,
                         @NonNull @NotNull UserRepository repository) {
        super(application);
        this.repository = repository;
    }

    public LiveData<Integer> loginWithMailAndPassword(String mail, String password) {
        return repository.loginWithMailAndPassword(mail, password);
    }

    public LiveData<Integer> registerWithMailAndPassword(String mail, String password) {
        return repository.registerWithMailAndPassword(mail, password);
    }

    public void logout() {
        repository.logout();
    }
}
