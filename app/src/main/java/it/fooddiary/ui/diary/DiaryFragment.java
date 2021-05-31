package it.fooddiary.ui.diary;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.Calendar;
import java.util.Date;

import it.fooddiary.databinding.FragmentDiaryBinding;
import it.fooddiary.models.Meal;
import it.fooddiary.models.MealProperties;
import it.fooddiary.repositories.AppRepository;
import it.fooddiary.ui.MainActivity;
import it.fooddiary.ui.meal.MealActivity;
import it.fooddiary.R;
import it.fooddiary.utils.Constants;
import it.fooddiary.utils.DateUtils;
import it.fooddiary.utils.MealType;
import it.fooddiary.viewmodels.AppViewModel;
import it.fooddiary.viewmodels.AppViewModelFactory;

public class DiaryFragment extends Fragment {

    private static final String TAG = "DiaryFragment";

    private FragmentDiaryBinding binding;

    private final Date associatedDate;

    private AppViewModel viewModel;

    public DiaryFragment() {
        this.associatedDate = Calendar.getInstance().getTime();
    }

    public DiaryFragment(Date date) {
        this.associatedDate = date;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_diary, container, false);

        setupOpenMealImageButton();

        viewModel = new ViewModelProvider(this,
                new AppViewModelFactory(requireActivity().getApplication(),
                        new AppRepository(requireActivity().getApplication())))
                .get(AppViewModel.class);

        binding.setMealProperties(viewModel.getMealProperties().getValue());
        binding.invalidateAll();

        viewModel.getMealProperties().observe(getViewLifecycleOwner(), new Observer<MealProperties>() {
            @Override
            public void onChanged(MealProperties mealProperties) {
                if (binding.getMealProperties() == null)
                    binding.setMealProperties(mealProperties);
            }
        });

        reloadMealsFromDB();

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        reloadMealsFromDB();

        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();
        calendar.setTime(today);
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        Date yesterday = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, 2);
        Date tomorrow = calendar.getTime();

        if(DateUtils.dateEquals(associatedDate, today))
            ((MainActivity)getActivity()).changeToolbarTitle(getResources().getString(R.string.today));
        else if(DateUtils.dateEquals(associatedDate, yesterday))
            ((MainActivity)getActivity()).changeToolbarTitle(getResources().getString(R.string.yesterday));
        else if(DateUtils.dateEquals(associatedDate, tomorrow))
            ((MainActivity)getActivity()).changeToolbarTitle(getResources().getString(R.string.tomorrow));
        else
            ((MainActivity)getActivity()).changeToolbarTitle(DateUtils.dateFormat.format(associatedDate));
    }

    private void reloadMealsFromDB() {
        LiveData<Meal> breakfastMutableLiveData =
                viewModel.getMealByTypeAndDate(MealType.BREAKFAST, associatedDate);
        LiveData<Meal> lunchMutableLiveData =
                viewModel.getMealByTypeAndDate(MealType.LUNCH, associatedDate);
        LiveData<Meal> dinnerMutableLiveData =
                viewModel.getMealByTypeAndDate(MealType.DINNER, associatedDate);
        LiveData<Meal> snacksMutableLiveData =
                viewModel.getMealByTypeAndDate(MealType.SNACKS, associatedDate);

        breakfastMutableLiveData.observe(getViewLifecycleOwner(), new Observer<Meal>() {
            @Override
            public void onChanged(Meal meal) {
                binding.setBreakfastMeal(meal);
            }
        });
        lunchMutableLiveData.observe(getViewLifecycleOwner(), new Observer<Meal>() {
            @Override
            public void onChanged(Meal meal) {
                binding.setLunchMeal(meal);
            }
        });
        dinnerMutableLiveData.observe(getViewLifecycleOwner(), new Observer<Meal>() {
            @Override
            public void onChanged(Meal meal) {
                binding.setDinnerMeal(meal);
            }
        });
        snacksMutableLiveData.observe(getViewLifecycleOwner(), new Observer<Meal>() {
            @Override
            public void onChanged(Meal meal) {
                binding.setSnacksMeal(meal);
            }
        });
    }

    private void setupOpenMealImageButton() {
        Intent intent = new Intent(getActivity(), MealActivity.class);
        intent.putExtra(Constants.CURRENT_DATE, associatedDate);

        binding.breakfastImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra(Constants.MEAL_TYPE, MealType.BREAKFAST);
                startActivity(intent);
            }
        });

        binding.lunchImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra(Constants.MEAL_TYPE, MealType.LUNCH);
                startActivity(intent);
            }
        });

        binding.dinnerImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra(Constants.MEAL_TYPE, MealType.DINNER);
                startActivity(intent);
            }
        });

        binding.snacksImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra(Constants.MEAL_TYPE, MealType.SNACKS);
                startActivity(intent);
            }
        });
    }

    public Date getAssociatedDate() {
        return associatedDate;
    }
}
