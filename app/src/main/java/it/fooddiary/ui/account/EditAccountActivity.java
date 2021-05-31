package it.fooddiary.ui.account;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Objects;

import it.fooddiary.R;
import it.fooddiary.databinding.ActivityEditAccountBinding;
import it.fooddiary.ui.MainActivity;
import it.fooddiary.ui.login.LoginActivity;
import it.fooddiary.utils.Constants;

public class EditAccountActivity extends AppCompatActivity {
    private static final String TAG = "LoginPersonalDataFragment";

    private ActivityEditAccountBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEditAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).hide();


        binding.numberPickerHeight.setMaxValue(Constants.MAX_HEIGHT_CM);
        binding.numberPickerHeight.setMinValue(Constants.MIN_HEIGHT_CM);
        binding.numberPickerHeight.setValue(Constants.MID_HEIGHT_CM);
        binding.numberPickerWeight.setMaxValue(Constants.MAX_WEIGHT_KG);
        binding.numberPickerWeight.setMinValue(Constants.MIN_WEIGHT_KG);
        binding.numberPickerWeight.setValue(Constants.MID_WEIGHT_KG);
        binding.numberPickerAge.setMaxValue(Constants.MAX_AGE);
        binding.numberPickerAge.setMinValue(Constants.MIN_AGE);

        readInformation();


        binding.confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String error = "";
                int genderButtonId, activityButtonId, height, weight, age;

                genderButtonId = binding.genderRadioGroup.getCheckedRadioButtonId();
                activityButtonId = binding.activityRadioGroup.getCheckedRadioButtonId();
                height = binding.numberPickerHeight.getValue();
                weight = binding.numberPickerWeight.getValue();
                age = binding.numberPickerAge.getValue();

                if (genderButtonId < 0)
                    error += "- " + getResources().getString(R.string.gender_error) + "\n";
                if (activityButtonId < 0)
                    error += "- " + getResources().getString(R.string.activity_error);

                if (!error.equals("")) {
                    new MaterialAlertDialogBuilder(EditAccountActivity.this)
                            .setTitle(R.string.error)
                            .setMessage(error)
                            .setPositiveButton(R.string.ok, null)
                            .show();
                } else {
                    savePersonalData(age, genderButtonId, activityButtonId, height, weight);
                    Intent intent = new Intent(EditAccountActivity.this, MainActivity.class);
                    startActivity(intent);
                    EditAccountActivity.this.finish();
                }
            }
        });


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
    private void readInformation(){
        SharedPreferences preferences =
                getSharedPreferences(Constants.PERSONAL_DATA_PREFERENCES_FILE,
                        Context.MODE_PRIVATE);
        int age_user = preferences.getInt(Constants.USER_AGE, 0);
        String gender_user = preferences.getString(Constants.USER_GENDER, null);
        int height_user = preferences.getInt(Constants.USER_HEIGHT_CM, 0);
        int weight_user = preferences.getInt(Constants.USER_WEIGHT_KG, 0);
        String level_user = preferences.getString(Constants.USER_ACTIVITY_LEVEL, null);

        binding.numberPickerAge.setValue(age_user);
        binding.numberPickerHeight.setValue(Constants.MID_HEIGHT_CM);
        binding.numberPickerWeight.setValue(Constants.MID_WEIGHT_KG);

    }

}