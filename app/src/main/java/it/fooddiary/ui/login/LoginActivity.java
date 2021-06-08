package it.fooddiary.ui.login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

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
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import it.fooddiary.R;
import it.fooddiary.databinding.ActivityLoginBinding;
import it.fooddiary.repositories.UserRepository;
import it.fooddiary.ui.MainActivity;
import it.fooddiary.utils.Constants;
import it.fooddiary.utils.DateUtils;
import it.fooddiary.viewmodels.user.UserViewModel;
import it.fooddiary.viewmodels.user.UserViewModelFactory;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginPersonalDataFragment";
    private static final String CURRENT_ERROR = "CurrentError";

    private ActivityLoginBinding binding;
    private UserViewModel userViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userViewModel = new ViewModelProvider(this,
                new UserViewModelFactory(getApplication(), new UserRepository(getApplication())))
                .get(UserViewModel.class);

        Objects.requireNonNull(getSupportActionBar()).hide();

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

        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = binding.mailEditText.getText().toString();
                String password = binding.passwordEditText.getText().toString();
                boolean isMailValid = Constants.isMailValid(mail);
                boolean isPasswordValid = Constants.isPasswordValid(password);

                if (isMailValid && isPasswordValid) {
                    userViewModel.loginWithMailAndPassword(mail, password)
                            .observe(LoginActivity.this, new Observer<Integer>() {
                        @Override
                        public void onChanged(Integer integer) {
                            switch (integer) {
                                case Constants.FIREBASE_LOGIN_OK:
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    LoginActivity.this.finish();
                                    break;
                                default:
                                    Snackbar.make(binding.getRoot(),
                                            R.string.error,
                                            Snackbar.LENGTH_LONG).show();
                                    break;
                            }
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
            }
        });

        binding.singInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = binding.mailEditText.getText().toString();
                String password = binding.passwordEditText.getText().toString();
                boolean isMailValid = Constants.isMailValid(mail);
                boolean isPasswordValid = Constants.isPasswordValid(password);

                if (isMailValid && isPasswordValid) {
                    userViewModel.registerWithMailAndPassword(mail, password)
                            .observe(LoginActivity.this, new Observer<Integer>() {
                                @Override
                                public void onChanged(Integer integer) {
                                    switch (integer) {
                                        case Constants.FIREBASE_REGISTER_OK:
                                            Snackbar.make(binding.getRoot(),
                                                    R.string.ok,
                                                    Snackbar.LENGTH_LONG).show();
                                            break;
                                        default:
                                            Snackbar.make(binding.getRoot(),
                                                    R.string.error,
                                                    Snackbar.LENGTH_LONG).show();
                                            break;
                                    }
                                }
                            });
                } else {
                    if (!isMailValid)
                        binding.mailEditText.setError("Mail not valid!");
                    if(!isPasswordValid)
                        binding.passwordEditText.setError("Password not valid!");
                }
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
                bmr = Constants.calculateBMR_Male(weight, height, age);
                editor.putInt(Constants.USER_GENDER, Constants.GENDER_MALE);
                break;
            default:
                bmr = Constants.calculateBMR_Female(weight, height, age);
                editor.putInt(Constants.USER_GENDER, Constants.GENDER_FEMALE);
                break;
        }
        switch(activityId) {
            case R.id.highRadioButton:
                editor.putInt(Constants.USER_ACTIVITY_LEVEL, Constants.ACTIVITY_HIGH);
                editor.putInt(Constants.USER_DAILY_INTAKE_KCAL, (int)(bmr*1.725));
                break;
            case R.id.lowRadioButton:
                editor.putInt(Constants.USER_ACTIVITY_LEVEL, Constants.ACTIVITY_LOW);
                editor.putInt(Constants.USER_DAILY_INTAKE_KCAL, (int)(bmr*1.2));
                break;
            default:
                editor.putInt(Constants.USER_ACTIVITY_LEVEL, Constants.ACTIVITY_MID);
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
}