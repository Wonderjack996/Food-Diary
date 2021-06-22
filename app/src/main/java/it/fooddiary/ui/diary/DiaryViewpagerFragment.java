package it.fooddiary.ui.diary;

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

import org.jetbrains.annotations.NotNull;

import java.util.Date;

import it.fooddiary.R;
import it.fooddiary.databinding.FragmentViewpagerDiaryBinding;
import it.fooddiary.repositories.FoodRepository;
import it.fooddiary.utils.Constants;
import it.fooddiary.viewmodels.food.FoodViewModel;
import it.fooddiary.viewmodels.food.FoodViewModelFactory;

public class DiaryViewpagerFragment extends Fragment {

    private static final String TAG = "DiaryViewpagerFragment";

    private FragmentViewpagerDiaryBinding binding;
    private DiaryPagerAdapter diaryPagerAdapter;

    private MaterialDatePicker<Long> datePicker;
    private Date displayedDate;

    private FoodViewModel viewModel;

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
        if (item.getItemId() == R.id.item_calendar) {
            onItemCalendarClicked();
        } else {
            return false;
        }

        return true;
    }

    @Override
    public View onCreateView(@NotNull @NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentViewpagerDiaryBinding.inflate(inflater);

        viewModel = new ViewModelProvider(this,
                new FoodViewModelFactory(requireActivity().getApplication(),
                        new FoodRepository(requireActivity().getApplication())))
                .get(FoodViewModel.class);

        // set up date picker
        datePicker = MaterialDatePicker.Builder.datePicker().setTitleText("Select date").build();
        datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                if (selection != null)
                    onDateChanged(new Date((long) selection));
            }
        });

        displayedDate = viewModel.getCurrentDate();
        diaryPagerAdapter = new DiaryPagerAdapter(getChildFragmentManager(), displayedDate);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.diaryViewPager.setSaveEnabled(false);
        binding.diaryViewPager.setAdapter(diaryPagerAdapter);

        binding.diaryViewPager.setCurrentItem(Constants.NUM_PRELOADED_FRAGMENT /2);

        binding.diaryViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Fragment tmp = diaryPagerAdapter.getItem(position);
                if (tmp instanceof DiaryFragment) {
                    displayedDate = ((DiaryFragment) tmp).getAssociatedDate();
                    viewModel.setCurrentDate(displayedDate);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void onDateChanged(@NonNull @NotNull Date date) {
        displayedDate = date;
        viewModel.setCurrentDate(displayedDate);

        diaryPagerAdapter = new DiaryPagerAdapter(getChildFragmentManager(), date);

        binding.diaryViewPager.setAdapter(diaryPagerAdapter);

        binding.diaryViewPager.setCurrentItem(Constants.NUM_PRELOADED_FRAGMENT/2);
    }

    private void onItemCalendarClicked() {
        datePicker.show(getParentFragmentManager(), TAG);
    }
}