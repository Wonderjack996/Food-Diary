package it.fooddiary.ui.diary;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import it.fooddiary.MainActivity;
import it.fooddiary.R;

public class DiaryFragment extends Fragment {

    private static final String TAG = "DiaryFragment";

    private MaterialDatePicker<Long> datePicker;

    private Date currentDate;

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
                onDateChanged((long)selection);
            }
        });

        // set up toolbar and onclick on menu item
        MainActivity main = (MainActivity) getActivity();
        assert main != null;
        main.setDiaryToolbar();
        main.getCurrentToolbar().setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
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

        return root;
    }

    private void onDateChanged(long selectedDate) {
        currentDate = new Date(selectedDate);
        Log.d(TAG, currentDate.toString());
    }

    private void onItemCalendarClicked() {
        Log.d(TAG, "calendar opened");
        datePicker.show(getParentFragmentManager(), TAG);
    }
}
