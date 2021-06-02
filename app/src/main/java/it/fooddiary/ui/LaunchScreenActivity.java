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

        if (preferences.getInt(Constants.USER_GENDER, Constants.OTHER)
                == Constants.OTHER)
            ret = new Intent(this, LoginActivity.class);
        else if (preferences.getInt(Constants.USER_AGE, 0) == 0)
            ret = new Intent(this, LoginActivity.class);
        else if (preferences.getInt(Constants.USER_HEIGHT_CM, 0) == 0)
            ret = new Intent(this, LoginActivity.class);
        else if (preferences.getInt(Constants.USER_WEIGHT_KG, 0) == 0)
            ret = new Intent(this, LoginActivity.class);
        else if (preferences.getInt(Constants.USER_ACTIVITY_LEVEL, Constants.OTHER)
                == Constants.OTHER)
            ret = new Intent(this, LoginActivity.class);
        else if (preferences.getInt(Constants.USER_DAILY_INTAKE_KCAL, 0) == 0)
            ret = new Intent(this, LoginActivity.class);
        else if (preferences.getFloat(Constants.USER_DAILY_CARBS_PERCENT, 0f) == 0f)
            ret = new Intent(this, LoginActivity.class);
        else if (preferences.getFloat(Constants.USER_DAILY_PROTEINS_PERCENT, 0f) == 0f)
            ret = new Intent(this, LoginActivity.class);
        else if (preferences.getFloat(Constants.USER_DAILY_FATS_PERCENT, 0f) == 0f)
            ret = new Intent(this, LoginActivity.class);
        else
            ret = new Intent(this, MainActivity.class);

        return ret;
    }
}