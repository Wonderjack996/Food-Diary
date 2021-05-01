package it.fooddiary.ui.diary;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.LayoutInflater;
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

public class DiaryFragment extends Fragment {

    private static final String TAG = "DiaryFragment";

    private MaterialDatePicker<Long> datePicker;
    private MainActivity mainActivity;

    private Date currentDate;
    private final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainActivity = (MainActivity) getActivity();
        assert mainActivity != null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // set up fragment diary view
        View root = inflater.inflate(R.layout.fragment_diary, container, false);

        // set up date picker
        datePicker = MaterialDatePicker.Builder.datePicker().setTitleText("Select date").build();
        datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                onDateChanged(new Date((long)selection));
            }
        });

        // set up toolbar and onclick on menu item
        mainActivity.setDiaryToolbar();
        mainActivity.getCurrentToolbar().setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch( item.getItemId() ) {
                    case R.id.item_calendar:
                        onItemCalendarClicked();
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });

        setupOpenMealImageButton(root);

        loadCurrentDate();

        return root;
    }

    private void setupOpenMealImageButton(View root) {
        ImageButton breakfastAddButton = root.findViewById(R.id.breakfast_imageButton);
        breakfastAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MealsActivity.class);
                intent.putExtra(Constants.MEALS_NAME, getResources().getString(R.string.breakfast));
                startActivity(intent);
            }
        });

        ImageButton lunchAddButton = root.findViewById(R.id.lunch_imageButton);
        lunchAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MealsActivity.class);
                intent.putExtra(Constants.MEALS_NAME, getResources().getString(R.string.lunch));
                startActivity(intent);
            }
        });

        ImageButton dinnerAddButton = root.findViewById(R.id.dinner_imageButton);
        dinnerAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MealsActivity.class);
                intent.putExtra(Constants.MEALS_NAME, getResources().getString(R.string.dinner));
                startActivity(intent);
            }
        });

        ImageButton snacksAddButton = root.findViewById(R.id.snacks_imageButton);
        snacksAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MealsActivity.class);
                intent.putExtra(Constants.MEALS_NAME, getResources().getString(R.string.snacks));
                startActivity(intent);
            }
        });
    }

    private void onDateChanged(Date date) {
        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();
        calendar.setTime(today);
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        Date yesterday = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, 2);
        Date tomorrow = calendar.getTime();

        currentDate = date;
        if(dateEquals(currentDate, today))
            ((MainActivity)getActivity())
                    .changeToolbarTitle(getResources().getString(R.string.today));
        else if(dateEquals(currentDate, yesterday))
            ((MainActivity)getActivity())
                    .changeToolbarTitle(getResources().getString(R.string.yesterday));
        else if(dateEquals(currentDate, tomorrow))
            ((MainActivity)getActivity()).
                    changeToolbarTitle(getResources().getString(R.string.tomorrow));
        else
            ((MainActivity)getActivity()).changeToolbarTitle(dateFormat.format(currentDate));

        saveCurrentDate();
    }

    private void saveCurrentDate() {
        SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(Constants.CURRENT_DATE, dateFormat.format(currentDate));

        editor.apply();
    }

    private void loadCurrentDate() {
        SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);

        String date = preferences.getString(Constants.CURRENT_DATE, null);
        if( date == null )
            currentDate = Calendar.getInstance().getTime();
        else {
            try {
                currentDate = dateFormat.parse(date);
            } catch (ParseException e) {
                currentDate = Calendar.getInstance().getTime();
            }
        }

        onDateChanged(currentDate);
    }

    private boolean dateEquals(Date d1, Date d2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(d1);
        cal2.setTime(d2);
        return cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH) &&
                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
    }

    private void onItemCalendarClicked() {
        Log.d(TAG, "calendar opened");
        datePicker.show(getParentFragmentManager(), TAG);
    }
}
