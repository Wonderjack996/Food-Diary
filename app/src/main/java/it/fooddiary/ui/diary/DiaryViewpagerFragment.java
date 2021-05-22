package it.fooddiary.ui.diary;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import it.fooddiary.ui.MainActivity;
import it.fooddiary.R;
import it.fooddiary.util.Constants;
import it.fooddiary.util.DateUtils;

public class DiaryViewpagerFragment extends Fragment {

    private static final String TAG = "DiaryViewpagerFragment";

    private static final int NUM_PRELOADED_FRAGMENT = 31;

    private MaterialDatePicker<Long> datePicker;

    private ViewPager diaryViewPager;
    private DiaryPagerAdapter diaryPagerAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.toolbar_menu_diary, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_calendar:
                onItemCalendarClicked();
                break;
            default:
                return false;
        }
        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        // set up date picker
        datePicker = MaterialDatePicker.Builder.datePicker().setTitleText("Select date").build();
        datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                onDateChanged(new Date((long) selection));
            }
        });

        diaryPagerAdapter = new DiaryPagerAdapter(getChildFragmentManager(),
                loadCurrentDate(), NUM_PRELOADED_FRAGMENT);

        return inflater.inflate(R.layout.fragment_viewpager_diary, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        diaryViewPager = view.findViewById(R.id.diary_viewPager);
        diaryViewPager.setAdapter(diaryPagerAdapter);
        diaryViewPager.setCurrentItem(NUM_PRELOADED_FRAGMENT / 2);
    }

    private void onDateChanged(Date date) {
        saveCurrentDate(date);
        diaryPagerAdapter = new DiaryPagerAdapter(getChildFragmentManager(),
                date, NUM_PRELOADED_FRAGMENT);
        diaryViewPager.setAdapter(diaryPagerAdapter);
        diaryViewPager.setCurrentItem(NUM_PRELOADED_FRAGMENT / 2);
    }

    private void onItemCalendarClicked() {
        datePicker.show(getParentFragmentManager(), TAG);
    }

    private Date loadCurrentDate() {
        Date ret;
        SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        String date = preferences.getString(Constants.CURRENT_DATE, null);

        if (date == null)
            ret = Calendar.getInstance().getTime();
        else {
            try {
                ret = DateUtils.dateFormat.parse(date);
            } catch (ParseException e) {
                ret = Calendar.getInstance().getTime();
            }
        }

        return ret;
    }

    private void saveCurrentDate(Date date) {
        SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(Constants.CURRENT_DATE, DateUtils.dateFormat.format(date));

        editor.apply();
    }
}