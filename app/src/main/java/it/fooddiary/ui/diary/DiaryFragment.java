package it.fooddiary.ui.diary;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GestureDetectorCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import it.fooddiary.MainActivity;
import it.fooddiary.MealsActivity;
import it.fooddiary.R;
import it.fooddiary.ui.search.SearchFragment;
import it.fooddiary.util.Constants;
import it.fooddiary.util.DateUtils;

public class DiaryFragment extends Fragment {

    private static final String TAG = "DiaryFragment";

    private final Date currentDate;

    public DiaryFragment(Date date) {
        this.currentDate = date;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // set up fragment diary view
        View root = inflater.inflate(R.layout.fragment_diary, container, false);

        setupOpenMealImageButton(root);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();
        calendar.setTime(today);
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        Date yesterday = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, 2);
        Date tomorrow = calendar.getTime();

        if(DateUtils.dateEquals(currentDate, today))
            ((MainActivity)getActivity()).changeToolbarTitle(getResources().getString(R.string.today));
        else if(DateUtils.dateEquals(currentDate, yesterday))
            ((MainActivity)getActivity()).changeToolbarTitle(getResources().getString(R.string.yesterday));
        else if(DateUtils.dateEquals(currentDate, tomorrow))
            ((MainActivity)getActivity()).changeToolbarTitle(getResources().getString(R.string.tomorrow));
        else
            ((MainActivity)getActivity()).changeToolbarTitle(DateUtils.dateFormat.format(currentDate));
    }

    private void setupOpenMealImageButton(View root) {
        Intent intent = new Intent(getActivity(), MealsActivity.class);
        intent.putExtra(Constants.CURRENT_DATE, DateUtils.dateFormat.format(currentDate));

        ImageButton breakfastAddButton = root.findViewById(R.id.breakfast_imageButton);
        breakfastAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra(Constants.MEALS_NAME, getResources().getString(R.string.breakfast));
                startActivity(intent);
            }
        });

        ImageButton lunchAddButton = root.findViewById(R.id.lunch_imageButton);
        lunchAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra(Constants.MEALS_NAME, getResources().getString(R.string.lunch));
                startActivity(intent);
            }
        });

        ImageButton dinnerAddButton = root.findViewById(R.id.dinner_imageButton);
        dinnerAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra(Constants.MEALS_NAME, getResources().getString(R.string.dinner));
                startActivity(intent);
            }
        });

        ImageButton snacksAddButton = root.findViewById(R.id.snacks_imageButton);
        snacksAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra(Constants.MEALS_NAME, getResources().getString(R.string.snacks));
                startActivity(intent);
            }
        });
    }

    public Date getCurrentDate() {
        return currentDate;
    }
}
