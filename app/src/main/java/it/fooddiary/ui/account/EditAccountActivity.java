package it.fooddiary.ui.account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Objects;

import it.fooddiary.R;
import it.fooddiary.databinding.ActivityEditAccountBinding;
import it.fooddiary.ui.MainActivity;
import it.fooddiary.utils.Constants;

public class EditAccountActivity extends AppCompatActivity {
    private static final String TAG = "EditAccountActivity";

    private ActivityEditAccountBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEditAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle(R.string.editAccount);

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
                    Intent intent = new Intent(EditAccountActivity.this, AccountFragment.class);
                    startActivity(intent);
                    EditAccountActivity.this.finish();
                    //onBackPressed();
                }
            }
        });


    }

    private void readInformation(){
        SharedPreferences preferences =
                getSharedPreferences(Constants.PERSONAL_DATA_PREFERENCES_FILE,
                        Context.MODE_PRIVATE);
        int age_user = preferences.getInt(Constants.USER_AGE, 0);
        String gender_user = preferences.getString(Constants.USER_GENDER, null);
        int height_user = preferences.getInt(Constants.USER_HEIGHT_CM, Constants.MID_HEIGHT_CM);
        int weight_user = preferences.getInt(Constants.USER_WEIGHT_KG, Constants.MID_WEIGHT_KG);
        String level_user = preferences.getString(Constants.USER_ACTIVITY_LEVEL, null);

        binding.numberPickerAge.setValue(age_user);
        binding.numberPickerHeight.setValue(height_user);
        binding.numberPickerWeight.setValue(weight_user);

        if(gender_user.equals(getString(R.string.male))){
            binding.maleRadioButton.setChecked(true);
        } else if(gender_user.equals(getString(R.string.female))){
            binding.femaleRadioButton.setChecked(true);
        }

        if(level_user.equals(getString(R.string.high))){
            binding.highRadioButton.setChecked(true);
        } else if(level_user.equals(getString(R.string.mid))){
            binding.midRadioButton.setChecked(true);
        } else if(level_user.equals(getString(R.string.low))){
            binding.lowRadioButton.setChecked(true);
        }
    }

    private void savePersonalData(int age, int genderId, int activityId,
                                  int height, int weight) {
        int bmr;
        SharedPreferences preferences =
                getSharedPreferences(Constants.PERSONAL_DATA_PREFERENCES_FILE,
                        Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.remove(Constants.USER_AGE);
        editor.remove(Constants.USER_HEIGHT_CM);
        editor.remove(Constants.USER_WEIGHT_KG);
        editor.putInt(Constants.USER_AGE, age);
        editor.putInt(Constants.USER_HEIGHT_CM, height);
        editor.putInt(Constants.USER_WEIGHT_KG, weight);

        editor.remove(Constants.USER_GENDER);
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

        editor.remove(Constants.USER_ACTIVITY_LEVEL);
        editor.remove(Constants.USER_DAILY_INTAKE_KCAL);
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
        editor.remove(Constants.USER_DAILY_CARBS_PERCENT);
        editor.remove(Constants.USER_DAILY_PROTEINS_PERCENT);
        editor.remove(Constants.USER_DAILY_FATS_PERCENT);
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
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                return false;
        }
        return true;
    }
}