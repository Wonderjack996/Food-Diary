package it.fooddiary.ui.login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import it.fooddiary.R;
import it.fooddiary.databinding.ActivityLoginBinding;
import it.fooddiary.models.UserProperties;
import it.fooddiary.repositories.UserRepository;
import it.fooddiary.ui.MainActivity;
import it.fooddiary.utils.Constants;
import it.fooddiary.viewmodels.user.UserViewModel;
import it.fooddiary.viewmodels.user.UserViewModelFactory;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginPersonalDataFragment";

    private ActivityLoginBinding binding;
    private UserViewModel userViewModel;

    @SuppressLint("ShowToast")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userViewModel = new ViewModelProvider(this,
                new UserViewModelFactory(getApplication(), new UserRepository(getApplication())))
                .get(UserViewModel.class);

        if (getSupportActionBar() != null)
            getSupportActionBar().hide();

        binding.numberPickerHeight.setMaxValue(Constants.MAX_HEIGHT_CM);
        binding.numberPickerHeight.setMinValue(Constants.MIN_HEIGHT_CM);

        binding.numberPickerWeight.setMaxValue(Constants.MAX_WEIGHT_KG);
        binding.numberPickerWeight.setMinValue(Constants.MIN_WEIGHT_KG);

        binding.numberPickerAge.setMaxValue(Constants.MAX_AGE);
        binding.numberPickerAge.setMinValue(Constants.MIN_AGE);

        if (savedInstanceState != null) {
            if (savedInstanceState.getInt(Constants.USER_AGE, 0) != 0)
                binding.numberPickerAge
                        .setValue(savedInstanceState.getInt(Constants.USER_AGE));
            else
                binding.numberPickerAge.setValue(Constants.MID_AGE);

            if (savedInstanceState.getInt(Constants.USER_WEIGHT_KG, 0) != 0)
                binding.numberPickerWeight
                        .setValue(savedInstanceState.getInt(Constants.USER_WEIGHT_KG));
            else
                binding.numberPickerWeight.setValue(Constants.MID_WEIGHT_KG);

            if (savedInstanceState.getInt(Constants.USER_HEIGHT_CM, 0) != 0)
                binding.numberPickerHeight
                        .setValue(savedInstanceState.getInt(Constants.USER_HEIGHT_CM));
            else
                binding.numberPickerHeight.setValue(Constants.MID_HEIGHT_CM);
        } else {
            binding.numberPickerAge.setValue(Constants.MID_AGE);
            binding.numberPickerWeight.setValue(Constants.MID_WEIGHT_KG);
            binding.numberPickerHeight.setValue(Constants.MID_HEIGHT_CM);
        }

        binding.loginButton.setOnClickListener(v -> {
            String mail = binding.mailEditText.getText().toString();
            String password = binding.passwordEditText.getText().toString();

            boolean isMailValid = Constants.isMailValid(mail);
            boolean isPasswordValid = Constants.isPasswordValid(password);

            if (isMailValid && isPasswordValid) {
                userViewModel.loginWithMailAndPassword(mail, password)
                        .observe(LoginActivity.this, integer -> {
                            if (integer == Constants.FIREBASE_LOGIN_OK) {
                                Intent intent = new Intent(LoginActivity.this,
                                        MainActivity.class);
                                startActivity(intent);
                                LoginActivity.this.finish();
                            } else {
                                Snackbar.make(binding.getRoot(), R.string.error,
                                        Snackbar.LENGTH_LONG)
                                        .setAnchorView(binding.buttonLinearLayout)
                                        .show();
                            }
                        });
            } else {
                if (!isMailValid)
                    binding.mailEditText.setError(getResources()
                            .getString(R.string.mail_not_valid));
                if(!isPasswordValid)
                    binding.passwordEditText.setError(getResources()
                            .getString(R.string.password_not_valid));
            }
        });

        binding.singInButton.setOnClickListener(v -> {
            String mail = binding.mailEditText.getText().toString();
            String password = binding.passwordEditText.getText().toString();

            boolean isMailValid = Constants.isMailValid(mail);
            boolean isPasswordValid = Constants.isPasswordValid(password);

            UserProperties insertProperties = readDataFromForms();

            if (isMailValid && isPasswordValid) {
                userViewModel.registerWithMailAndPassword(mail, password, insertProperties)
                        .observe(LoginActivity.this, integer -> {
                            if (integer == Constants.FIREBASE_REGISTER_OK) {
                                Intent intent = new Intent(LoginActivity.this,
                                        MainActivity.class);
                                startActivity(intent);
                                LoginActivity.this.finish();
                            } else {
                                Snackbar.make(binding.getRoot(),
                                        R.string.error,
                                        Snackbar.LENGTH_LONG)
                                        .setAnchorView(binding.buttonLinearLayout)
                                        .show();
                            }
                        });
            } else {
                if (!isMailValid)
                    binding.mailEditText.setError(getResources()
                            .getString(R.string.mail_not_valid));
                if(!isPasswordValid)
                    binding.passwordEditText.setError(getResources()
                            .getString(R.string.password_not_valid));
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull @NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        int height, weight, age;

        height = binding.numberPickerHeight.getValue();
        weight = binding.numberPickerWeight.getValue();
        age = binding.numberPickerAge.getValue();

        outState.putInt(Constants.USER_AGE, age);
        outState.putInt(Constants.USER_WEIGHT_KG, weight);
        outState.putInt(Constants.USER_HEIGHT_CM, height);
    }

    @NotNull
    @Contract(" -> new")
    private UserProperties readDataFromForms() {
        int age, gender, heightCm, weightKg, activityLevel;

        age = binding.numberPickerAge.getValue();
        heightCm = binding.numberPickerHeight.getValue();
        weightKg = binding.numberPickerWeight.getValue();

        if (binding.genderRadioGroup.getCheckedRadioButtonId() == R.id.maleRadioButton) {
            gender = Constants.GENDER_MALE;
        } else {
            gender = Constants.GENDER_FEMALE;
        }

        int activityButtonId = binding.activityRadioGroup.getCheckedRadioButtonId();
        if (activityButtonId == R.id.highRadioButton)
            activityLevel = Constants.ACTIVITY_HIGH;
        else if (activityButtonId == R.id.lowRadioButton)
            activityLevel = Constants.ACTIVITY_LOW;
        else
            activityLevel = Constants.ACTIVITY_MID;

        return new UserProperties(age, gender, heightCm, weightKg, activityLevel);
    }
}