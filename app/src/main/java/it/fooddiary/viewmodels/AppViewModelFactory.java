package it.fooddiary.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import it.fooddiary.repositories.AppRepository;

public class AppViewModelFactory implements ViewModelProvider.Factory {

    private final Application application;
    private final AppRepository appRepository;

    public AppViewModelFactory(Application application, AppRepository appRepository) {
        this.application = application;
        this.appRepository = appRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new AppViewModel(application, appRepository);
    }
}
