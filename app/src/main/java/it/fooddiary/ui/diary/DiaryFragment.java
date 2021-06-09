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
import it.fooddiary.models.UserProperties;
import it.fooddiary.repositories.FoodRepository;
import it.fooddiary.repositories.UserRepository;
import it.fooddiary.ui.MainActivity;
import it.fooddiary.ui.meal.MealActivity;
import it.fooddiary.R;
import it.fooddiary.utils.Constants;
import it.fooddiary.utils.DateUtils;
import it.fooddiary.utils.MealType;
import it.fooddiary.viewmodels.food.FoodViewModel;
import it.fooddiary.viewmodels.food.FoodViewModelFactory;
import it.fooddiary.viewmodels.user.UserViewModel;
import it.fooddiary.viewmodels.user.UserViewModelFactory;

public class DiaryFragment extends Fragment {

    private static final String TAG = "DiaryFragment";

    private FragmentDiaryBinding binding;

    private final Date associatedDate;

    private FoodViewModel foodViewModel;
    private UserViewModel userViewModel;

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

        foodViewModel = new ViewModelProvider(this,
                new FoodViewModelFactory(requireActivity().getApplication(),
                        new FoodRepository(requireActivity().getApplication())))
                .get(FoodViewModel.class);
        userViewModel = new ViewModelProvider(this,
                new UserViewModelFactory(requireActivity().getApplication(),
                        new UserRepository(requireActivity().getApplication())))
                .get(UserViewModel.class);

        userViewModel.getUserProperties().observe(getViewLifecycleOwner(), new Observer<UserProperties>() {
            @Override
            public void onChanged(UserProperties mealProperties) {
                binding.setUserProperties(mealProperties);
                binding.invalidateAll();
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
                foodViewModel.getMealByTypeAndDate(MealType.BREAKFAST, associatedDate);
        LiveData<Meal> lunchMutableLiveData =
                foodViewModel.getMealByTypeAndDate(MealType.LUNCH, associatedDate);
        LiveData<Meal> dinnerMutableLiveData =
                foodViewModel.getMealByTypeAndDate(MealType.DINNER, associatedDate);
        LiveData<Meal> snacksMutableLiveData =
                foodViewModel.getMealByTypeAndDate(MealType.SNACKS, associatedDate);

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

        binding.breakfastCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra(Constants.MEAL_TYPE, MealType.BREAKFAST);
                startActivity(intent);
            }
        });
        binding.breakfastImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra(Constants.MEAL_TYPE, MealType.BREAKFAST);
                startActivity(intent);
            }
        });

        binding.lunchCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra(Constants.MEAL_TYPE, MealType.LUNCH);
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

        binding.dinnerCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra(Constants.MEAL_TYPE, MealType.DINNER);
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

        binding.snacksCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra(Constants.MEAL_TYPE, MealType.SNACKS);
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
