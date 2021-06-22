package it.fooddiary.viewmodels.user;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.jetbrains.annotations.NotNull;

import it.fooddiary.models.UserProperties;
import it.fooddiary.repositories.UserRepository;

public class UserViewModel extends AndroidViewModel {

    private final UserRepository repository;

    public UserViewModel(@NonNull @NotNull Application application,
                         @NonNull @NotNull UserRepository repository) {
        super(application);
        this.repository = repository;
    }

    public LiveData<Integer> loginWithMailAndPassword(@NonNull @NotNull String mail,
                                                      @NonNull @NotNull String password) {
        return repository.loginWithMailAndPassword(mail, password);
    }

    public LiveData<Integer> registerWithMailAndPassword(@NonNull @NotNull String mail,
                                                         @NonNull @NotNull String password,
                                                         @NonNull @NotNull UserProperties insertData) {
        return repository.registerWithMailAndPassword(mail, password, insertData);
    }

    public void logout() {
        repository.logout();
    }

    public String getUserAuthId() {
        return repository.getUserAuthIdFromSharedPreferences();
    }

    public LiveData<UserProperties> getUserProperties() {
        return repository.getUserProperties();
    }

    public LiveData<Integer> setUserProperties(@NonNull @NotNull UserProperties newProperties) {
        return repository.setUserProperties(newProperties);
    }
}