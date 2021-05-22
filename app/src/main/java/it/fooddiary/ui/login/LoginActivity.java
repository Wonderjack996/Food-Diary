package it.fooddiary.ui.login;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import it.fooddiary.R;
import it.fooddiary.databinding.ActivityLoginBinding;
import it.fooddiary.databinding.ActivityMainBinding;
import it.fooddiary.ui.MainActivity;
import it.fooddiary.util.Constants;
import it.fooddiary.util.DateUtils;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginPersonalDataFragment";

    private ActivityLoginBinding binding;

    private MaterialDatePicker<Long> dateBirthPicker;

    private Date selectedDate = null;

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        Calendar instance = Calendar.getInstance();
        instance.setTime(instance.getTime());
        instance.add(Calendar.YEAR, -Constants.MIN_AGE);

        CalendarConstraints.Builder calendarConstraints = new CalendarConstraints.Builder()
                .setEnd(instance.getTimeInMillis())
                .setOpenAt(instance.getTimeInMillis());

        try {
            instance.setTime(Objects.requireNonNull(DateUtils.dateFormat.parse("1/1/1900")));
            calendarConstraints = calendarConstraints.setStart(instance.getTimeInMillis());
        } catch (ParseException e) { }

        dateBirthPicker = MaterialDatePicker.Builder
                .datePicker()
                .setTitleText(R.string.date_birth)
                .setCalendarConstraints(calendarConstraints.build())
                .build();
        dateBirthPicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                Long date = (Long) selection;
                selectedDate = new Date(date);
                binding.editTextDateBirth.setText(DateUtils.dateFormat.format(selectedDate));
            }
        });

        binding.numberPickerHeight.setMaxValue(Constants.MAX_HEIGHT_CM);
        binding.numberPickerHeight.setMinValue(Constants.MIN_HEIGHT_CM);
        binding.numberPickerHeight.setValue(Constants.MID_HEIGHT_CM);

        binding.numberPickerWeight.setMaxValue(Constants.MAX_WEIGHT_KG);
        binding.numberPickerWeight.setMinValue(Constants.MIN_WEIGHT_KG);
        binding.numberPickerWeight.setValue(Constants.MID_WEIGHT_KG);

        binding.editTextDateBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateBirthPicker.show(getSupportFragmentManager(), TAG);
            }
        });

        binding.nextPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String error = "";
                String name, surname;
                int checkedButtonId, height, weight;

                name = binding.editTextName.getText().toString();
                surname = binding.editTextSurname.getText().toString();
                checkedButtonId = binding.activityRadioGroup.getCheckedRadioButtonId();
                height = binding.numberPickerHeight.getValue();
                weight = binding.numberPickerWeight.getValue();

                if (name.equals(""))
                    error += "- " + getResources().getString(R.string.name_error) + "\n";
                if (surname.equals(""))
                    error += "- " + getResources().getString(R.string.surname_error) + "\n";
                if (selectedDate == null)
                    error += "- " + getResources().getString(R.string.birth_error) + "\n";
                if (checkedButtonId < 0)
                    error += "- " + getResources().getString(R.string.activity_error);

                if (!error.equals("")) {
                    new MaterialAlertDialogBuilder(LoginActivity.this)
                            .setTitle(R.string.error)
                            .setMessage(error)
                            .setPositiveButton(R.string.ok, null)
                            .show();
                } else {
                    savePersonalData(name, surname, selectedDate, checkedButtonId, height, weight);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    LoginActivity.this.finish();
                }
            }
        });
    }

    private void savePersonalData(String name, String surname, Date birthDate,
                                  int checkedId, int height, int weight) {
        SharedPreferences preferences = getSharedPreferences(Constants.PERSONAL_DATA_PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(Constants.USER_NAME, name);
        editor.putString(Constants.USER_SURNAME, surname);
        editor.putString(Constants.USER_DATE_BIRTH, DateUtils.dateFormat.format(birthDate));
        editor.putInt(Constants.USER_HEIGHT_CM, height);
        editor.putInt(Constants.USER_WEIGHT_KG, weight);
        switch(checkedId) {
            case R.id.highRadioButton:
                editor.putString(Constants.USER_ACTIVITY_LEVEL, getResources()
                        .getString(R.string.high));
                break;
            case R.id.lowRadioButton:
                editor.putString(Constants.USER_ACTIVITY_LEVEL, getResources()
                        .getString(R.string.low));
                break;
            default:
                editor.putString(Constants.USER_ACTIVITY_LEVEL, getResources()
                        .getString(R.string.mid));
                break;
        }

        editor.apply();
    }
}