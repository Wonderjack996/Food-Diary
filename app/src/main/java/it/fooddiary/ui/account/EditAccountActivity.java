package it.fooddiary.ui.account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import it.fooddiary.R;
import it.fooddiary.databinding.ActivityEditAccountBinding;
import it.fooddiary.models.UserProperties;
import it.fooddiary.repositories.FoodRepository;
import it.fooddiary.repositories.UserRepository;
import it.fooddiary.utils.Constants;
import it.fooddiary.viewmodels.food.FoodViewModel;
import it.fooddiary.viewmodels.food.FoodViewModelFactory;
import it.fooddiary.viewmodels.user.UserViewModel;
import it.fooddiary.viewmodels.user.UserViewModelFactory;

public class EditAccountActivity extends AppCompatActivity {
    private static final String TAG = "EditAccountActivity";

    private ActivityEditAccountBinding binding;

    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEditAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        userViewModel = new ViewModelProvider(this,
                new UserViewModelFactory(getApplication(),
                        new UserRepository(getApplication())))
                .get(UserViewModel.class);

        getSupportActionBar().setTitle(R.string.editAccount);

        binding.numberPickerHeight.setMaxValue(Constants.MAX_HEIGHT_CM);
        binding.numberPickerHeight.setMinValue(Constants.MIN_HEIGHT_CM);
        binding.numberPickerWeight.setMaxValue(Constants.MAX_WEIGHT_KG);
        binding.numberPickerWeight.setMinValue(Constants.MIN_WEIGHT_KG);
        binding.numberPickerAge.setMaxValue(Constants.MAX_AGE);
        binding.numberPickerAge.setMinValue(Constants.MIN_AGE);

        if (savedInstanceState != null) {
            if (savedInstanceState.getInt(Constants.USER_AGE, 0) != 0)
                binding.numberPickerAge.setValue(savedInstanceState.getInt(Constants.USER_AGE));
            else binding.numberPickerAge.setValue(Constants.MID_AGE);

            if (savedInstanceState.getInt(Constants.USER_WEIGHT_KG, 0) != 0)
                binding.numberPickerWeight.setValue(savedInstanceState.getInt(Constants.USER_WEIGHT_KG));
            else  binding.numberPickerWeight.setValue(Constants.MID_WEIGHT_KG);

            if (savedInstanceState.getInt(Constants.USER_HEIGHT_CM, 0) != 0)
                binding.numberPickerHeight.setValue(savedInstanceState.getInt(Constants.USER_HEIGHT_CM));
            else binding.numberPickerHeight.setValue(Constants.MID_HEIGHT_CM);

            switch (savedInstanceState.getInt(Constants.USER_GENDER, Constants.GENDER_FEMALE)) {
                case Constants.GENDER_FEMALE:
                    binding.femaleRadioButton.setChecked(true);
                    break;
                case Constants.GENDER_MALE:
                    binding.maleRadioButton.setChecked(true);
                    break;
            }

            switch (savedInstanceState.getInt(Constants.USER_ACTIVITY_LEVEL, Constants.ACTIVITY_MID)) {
                case Constants.ACTIVITY_HIGH:
                    binding.highRadioButton.setChecked(true);
                    break;
                case Constants.ACTIVITY_MID:
                    binding.midRadioButton.setChecked(true);
                    break;
                case Constants.ACTIVITY_LOW:
                    binding.lowRadioButton.setChecked(true);
                    break;
            }
        } else {
            binding.numberPickerWeight.setValue(Constants.MID_WEIGHT_KG);
            binding.numberPickerHeight.setValue(Constants.MID_HEIGHT_CM);
            binding.numberPickerAge.setValue(Constants.MID_AGE);
            readInformation();
        }

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
                    int gender, activityLevel;
                    switch (genderButtonId) {
                        case R.id.maleRadioButton:
                            gender = Constants.GENDER_MALE;
                            break;
                        default:
                            gender = Constants.GENDER_FEMALE;
                            break;
                    }
                    switch (activityButtonId) {
                        case R.id.highRadioButton:
                            activityLevel = Constants.ACTIVITY_HIGH;
                            break;
                        case R.id.midRadioButton:
                            activityLevel = Constants.ACTIVITY_MID;
                            break;
                        default:
                            activityLevel = Constants.ACTIVITY_LOW;
                            break;
                    }
                    userViewModel.setUserProperties(new UserProperties(age, gender, height,
                            weight, activityLevel));
                    onBackPressed();
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull @NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        int height, weight, age, genderButtonId, activityButtonId;

        genderButtonId = binding.genderRadioGroup.getCheckedRadioButtonId();
        activityButtonId = binding.activityRadioGroup.getCheckedRadioButtonId();
        height = binding.numberPickerHeight.getValue();
        weight = binding.numberPickerWeight.getValue();
        age = binding.numberPickerAge.getValue();

        outState.putInt(Constants.USER_AGE, age);
        outState.putInt(Constants.USER_WEIGHT_KG, weight);
        outState.putInt(Constants.USER_HEIGHT_CM, height);
        outState.putInt(Constants.USER_ACTIVITY_LEVEL, genderButtonId);
        outState.putInt(Constants.USER_GENDER, activityButtonId);
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

    private void readInformation(){
        userViewModel.getUserProperties().observe(this, new Observer<UserProperties>() {
            @Override
            public void onChanged(UserProperties userProperties) {
                int age_user = userProperties.getAge();
                int gender_user = userProperties.getGender();
                int height_user = userProperties.getHeightCm();
                int weight_user = userProperties.getWeightKg();
                int level_user = userProperties.getActivityLevel();

                binding.numberPickerAge.setValue(age_user);
                binding.numberPickerHeight.setValue(height_user);
                binding.numberPickerWeight.setValue(weight_user);

                switch (gender_user) {
                    case Constants.GENDER_FEMALE:
                        binding.femaleRadioButton.setChecked(true);
                        break;
                    case Constants.GENDER_MALE:
                        binding.maleRadioButton.setChecked(true);
                        break;
                }

                switch (level_user) {
                    case Constants.ACTIVITY_HIGH:
                        binding.highRadioButton.setChecked(true);
                        break;
                    case Constants.ACTIVITY_MID:
                        binding.midRadioButton.setChecked(true);
                        break;
                    case Constants.ACTIVITY_LOW:
                        binding.lowRadioButton.setChecked(true);
                        break;
                }
            }
        });
    }
}