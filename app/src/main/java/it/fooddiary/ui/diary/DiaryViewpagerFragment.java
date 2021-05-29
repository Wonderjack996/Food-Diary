package it.fooddiary.ui.diary;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.util.Date;

import it.fooddiary.R;
import it.fooddiary.utils.Constants;
import it.fooddiary.viewmodels.AppViewModel;

public class DiaryViewpagerFragment extends Fragment {

    private static final String TAG = "DiaryViewpagerFragment";

    private static final String DISPLAYED_PAGE_INDEX = "DisplayedPageIndex";

    private static final int NUM_PRELOADED_FRAGMENT = 31;

    private MaterialDatePicker<Long> datePicker;
    private Date displayedDate;

    private ViewPager diaryViewPager;
    private DiaryPagerAdapter diaryPagerAdapter;

    private AppViewModel viewModel;

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
        viewModel = new ViewModelProvider(this).get(AppViewModel.class);

        // set up date picker
        datePicker = MaterialDatePicker.Builder.datePicker().setTitleText("Select date").build();
        datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                onDateChanged(new Date((long) selection));
            }
        });

        displayedDate = viewModel
                .getCurrentDate(getActivity()
                        .getSharedPreferences(Constants.CURRENT_DATE_PREFERENCES_FILE,
                                Context.MODE_PRIVATE));
        diaryPagerAdapter = new DiaryPagerAdapter(getChildFragmentManager(),
                displayedDate, NUM_PRELOADED_FRAGMENT);

        return inflater.inflate(R.layout.fragment_viewpager_diary, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        diaryViewPager = view.findViewById(R.id.diary_viewPager);
        diaryViewPager.setAdapter(diaryPagerAdapter);
        diaryViewPager.setCurrentItem(NUM_PRELOADED_FRAGMENT/2);

        diaryViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                DiaryFragment x = (DiaryFragment) diaryPagerAdapter.getItem(position);
                displayedDate = x.getAssociatedDate();
                viewModel.setCurrentDate(displayedDate, getActivity()
                        .getSharedPreferences(Constants.CURRENT_DATE_PREFERENCES_FILE,
                                Context.MODE_PRIVATE));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void onDateChanged(Date date) {
        displayedDate = date;
        viewModel.setCurrentDate(displayedDate, getActivity()
                .getSharedPreferences(Constants.CURRENT_DATE_PREFERENCES_FILE,
                        Context.MODE_PRIVATE));

        diaryPagerAdapter = new DiaryPagerAdapter(getChildFragmentManager(),
                date, NUM_PRELOADED_FRAGMENT);
        diaryViewPager.setAdapter(diaryPagerAdapter);
        diaryViewPager.setCurrentItem(NUM_PRELOADED_FRAGMENT/2);
    }

    private void onItemCalendarClicked() {
        datePicker.show(getParentFragmentManager(), TAG);
    }
}