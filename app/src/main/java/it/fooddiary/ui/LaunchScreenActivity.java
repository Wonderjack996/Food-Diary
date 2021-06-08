package it.fooddiary.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import it.fooddiary.ui.login.LoginActivity;
import it.fooddiary.utils.Constants;

public class LaunchScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = chooseActivityToLaunch();

        startActivity(intent);
        finish();
    }

    private Intent chooseActivityToLaunch() {
        Intent ret;
        SharedPreferences preferences =
                getSharedPreferences(Constants.FIREBASE_USER_PREFERENCES_FILE,
                        Context.MODE_PRIVATE);

        if (preferences.getString(Constants.FIREBASE_USER_TOKEN, "").isEmpty())
            ret = new Intent(this, LoginActivity.class);
        else
            ret = new Intent(this, MainActivity.class);

        return ret;
    }
}