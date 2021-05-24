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
        Intent ret = null;
        SharedPreferences preferences =
                getSharedPreferences(Constants.PERSONAL_DATA_PREFERENCES_FILE,
                        Context.MODE_PRIVATE);

        if (preferences.getString(Constants.USER_NAME, null) == null)
            ret = new Intent(this, LoginActivity.class);
        else if (preferences.getString(Constants.USER_SURNAME, null) == null)
            ret = new Intent(this, LoginActivity.class);
        else if (preferences.getString(Constants.USER_DATE_BIRTH, null) == null)
            ret = new Intent(this, LoginActivity.class);
        else if (preferences.getInt(Constants.USER_HEIGHT_CM, 0) == 0)
            ret = new Intent(this, LoginActivity.class);
        else if (preferences.getInt(Constants.USER_WEIGHT_KG, 0) == 0)
            ret = new Intent(this, LoginActivity.class);
        else if (preferences.getString(Constants.USER_ACTIVITY_LEVEL, null) == null)
            ret = new Intent(this, LoginActivity.class);
        else
            ret = new Intent(this, MainActivity.class);

        return ret;
    }
}