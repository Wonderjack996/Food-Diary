package it.fooddiary.ui.login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import it.fooddiary.R;
import it.fooddiary.databinding.ActivityLoginBinding;
import it.fooddiary.ui.MainActivity;
import it.fooddiary.utils.Constants;
import it.fooddiary.utils.DateUtils;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginPersonalDataFragment";
    private static final String CURRENT_ERROR = "CurrentError";

    private ActivityLoginBinding binding;

    private String currentError = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).hide();

        if (savedInstanceState != null) {
            if (savedInstanceState.getString(CURRENT_ERROR) != null) {
                currentError = savedInstanceState.getString(CURRENT_ERROR);
                showError();
            }
        }

        binding.numberPickerHeight.setMaxValue(Constants.MAX_HEIGHT_CM);
        binding.numberPickerHeight.setMinValue(Constants.MIN_HEIGHT_CM);
        binding.numberPickerHeight.setValue(Constants.MID_HEIGHT_CM);

        binding.numberPickerWeight.setMaxValue(Constants.MAX_WEIGHT_KG);
        binding.numberPickerWeight.setMinValue(Constants.MIN_WEIGHT_KG);
        binding.numberPickerWeight.setValue(Constants.MID_WEIGHT_KG);

        binding.numberPickerAge.setMaxValue(Constants.MAX_AGE);
        binding.numberPickerAge.setMinValue(Constants.MIN_AGE);

        binding.nextPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentError = "";
                int genderButtonId, activityButtonId, height, weight, age;

                genderButtonId = binding.genderRadioGroup.getCheckedRadioButtonId();
                activityButtonId = binding.activityRadioGroup.getCheckedRadioButtonId();
                height = binding.numberPickerHeight.getValue();
                weight = binding.numberPickerWeight.getValue();
                age = binding.numberPickerAge.getValue();

                if (genderButtonId < 0)
                    currentError += "- " + getResources().getString(R.string.gender_error) + "\n";
                if (activityButtonId < 0)
                    currentError += "- " + getResources().getString(R.string.activity_error);

                if (!currentError.equals("")) {
                    showError();
                } else {
                    savePersonalData(age, genderButtonId, activityButtonId, height, weight);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    LoginActivity.this.finish();
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull @NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(CURRENT_ERROR, currentError);
    }

    private void showError() {
        if (currentError != null && !currentError.equals("")) {
            new MaterialAlertDialogBuilder(LoginActivity.this)
                    .setTitle(R.string.error)
                    .setMessage(currentError)
                    .setPositiveButton(R.string.ok, null)
                    .setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            currentError = "";
                        }
                    })
                    .show();
        }
    }

    private void savePersonalData(int age, int genderId, int activityId,
                                  int height, int weight) {
        int bmr;
        SharedPreferences preferences =
                getSharedPreferences(Constants.PERSONAL_DATA_PREFERENCES_FILE,
                        Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putInt(Constants.USER_AGE, age);
        editor.putInt(Constants.USER_HEIGHT_CM, height);
        editor.putInt(Constants.USER_WEIGHT_KG, weight);
        switch(genderId) {
            case R.id.maleRadioButton:
                bmr = calculateBMR_Male(weight, height, age);
                editor.putString(Constants.USER_GENDER, getResources()
                        .getString(R.string.male));
                break;
            default:
                bmr = calculateBMR_Female(weight, height, age);
                editor.putString(Constants.USER_GENDER, getResources()
                        .getString(R.string.female));
                break;
        }
        switch(activityId) {
            case R.id.highRadioButton:
                editor.putString(Constants.USER_ACTIVITY_LEVEL, getResources()
                        .getString(R.string.high));
                editor.putInt(Constants.USER_DAILY_INTAKE_KCAL, (int)(bmr*1.725));
                break;
            case R.id.lowRadioButton:
                editor.putString(Constants.USER_ACTIVITY_LEVEL, getResources()
                        .getString(R.string.low));
                editor.putInt(Constants.USER_DAILY_INTAKE_KCAL, (int)(bmr*1.2));
                break;
            default:
                editor.putString(Constants.USER_ACTIVITY_LEVEL, getResources()
                        .getString(R.string.mid));
                editor.putInt(Constants.USER_DAILY_INTAKE_KCAL, (int)(bmr*1.55));
                break;
        }
        editor.putFloat(Constants.USER_DAILY_CARBS_PERCENT,
                Constants.DEFAULT_CARBS_PERCENT_DAILY);
        editor.putFloat(Constants.USER_DAILY_PROTEINS_PERCENT,
                Constants.DEFAULT_PROTEINS_PERCENT_DAILY);
        editor.putFloat(Constants.USER_DAILY_FATS_PERCENT,
                Constants.DEFAULT_FATS_PERCENT_DAILY);

        editor.apply();
    }

    private int calculateBMR_Male(int weight, int height, int age) {
        return 10*weight + 6*height - 5*age + 5;
    }

    private int calculateBMR_Female(int weight, int height, int age) {
        return 10*weight + 6*height - 5*age - 161;
    }
}