package it.fooddiary.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import org.jetbrains.annotations.NotNull;

import it.fooddiary.repositories.UserRepository;
import it.fooddiary.ui.login.LoginActivity;
import it.fooddiary.viewmodels.user.UserViewModel;
import it.fooddiary.viewmodels.user.UserViewModelFactory;

public class LaunchScreenActivity extends AppCompatActivity {

    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userViewModel = new ViewModelProvider(this,
                new UserViewModelFactory(getApplication(), new UserRepository(getApplication())))
                .get(UserViewModel.class);

        Intent intent = chooseActivityToLaunch();

        startActivity(intent);
        finish();
    }

    @NotNull
    private Intent chooseActivityToLaunch() {
        Intent ret;
        String authToken = userViewModel.getUserAuthId();

        if (authToken == null || authToken.isEmpty())
            ret = new Intent(this, LoginActivity.class);
        else
            ret = new Intent(this, MainActivity.class);

        return ret;
    }
}