package it.fooddiary.ui.search.searched;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

import it.fooddiary.R;
import it.fooddiary.databinding.DialogFoodSearchedItemBinding;
import it.fooddiary.models.Food;
import it.fooddiary.repositories.AppRepository;
import it.fooddiary.utils.Constants;
import it.fooddiary.utils.MealType;
import it.fooddiary.viewmodels.AppViewModel;
import it.fooddiary.viewmodels.AppViewModelFactory;

public class FoodSearchedItemAlert extends DialogFragment implements IFoodAlert {

    private static final String LAST_FOOD_CLICKED = "LastFoodClicked";

    private DialogFoodSearchedItemBinding binding;
    private AppViewModel viewModel;

    private Food foodClicked = new Food("error", "error",
            0, 0, 0, 0);

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.getParcelable(LAST_FOOD_CLICKED) != null)
                foodClicked = savedInstanceState.getParcelable(LAST_FOOD_CLICKED);
        }

        this.binding = DialogFoodSearchedItemBinding.inflate(getLayoutInflater());
        this.viewModel = new ViewModelProvider(this,
                new AppViewModelFactory(getActivity().getApplication(),
                        new AppRepository(requireActivity().getApplication())))
                .get(AppViewModel.class);
        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(getContext());

        dialogBuilder.setView(binding.getRoot());
        dialogBuilder.setNegativeButton(R.string.cancel, null);

        binding.quantityNumberPicker
                .setMinValue(Constants.MIN_FOOD_GRAMS);
        binding.quantityNumberPicker
                .setMaxValue(Constants.MAX_FOOD_GRAMS);

        MealType[] mealTypes = MealType.values();
        String[] mealNames = new String[mealTypes.length];
        for (int i = 0; i < mealTypes.length; ++i)
            mealNames[i] = mealTypes[i].toString(getResources());
        binding.mealNumberPicker.setDisplayedValues(mealNames);
        binding.mealNumberPicker.setMinValue(0);
        binding.mealNumberPicker.setMaxValue(mealNames.length-1);

        binding.setFood(foodClicked);
        binding.quantityNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                foodClicked.setQuantity(newVal);
                binding.invalidateAll();
            }
        });
        binding.quantityNumberPicker.setValue(foodClicked.getQuantity());
        dialogBuilder.setTitle(foodClicked.getName());
        dialogBuilder.setPositiveButton("add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int quantity = binding.quantityNumberPicker.getValue();
                int mealTypeNum = binding.mealNumberPicker.getValue();
                MealType mealType = MealType.values()[mealTypeNum];
                foodClicked.setQuantity(quantity);
                onFoodAddedToMeal(foodClicked, mealType);
            }
        });
        return dialogBuilder.create();
    }

    @Override
    public void onSaveInstanceState(@NonNull @NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(LAST_FOOD_CLICKED, foodClicked);
    }

    @Override
    public void setFood(Food food) {
        this.foodClicked = food;
    }

    private void onFoodAddedToMeal(Food foodToAdd, MealType mealToModify) {
        Date currentDate = viewModel.getCurrentDate(getActivity()
                .getSharedPreferences(Constants.CURRENT_DATE_PREFERENCES_FILE,
                        Context.MODE_PRIVATE));

        LiveData<Integer> databaseResponseLiveData = viewModel.insertFoodInMeal(foodToAdd,
                mealToModify, currentDate);

        databaseResponseLiveData.observe(FoodSearchedItemAlert.this.getActivity(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                switch (integer) {
                    case Constants.DATABASE_INSERT_OK:
                    case Constants.DATABASE_UPDATE_OK:
                        Snackbar.make(binding.getRoot(), R.string.added,
                                Snackbar.LENGTH_SHORT)
                                .setAction("Undo", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        onFoodRemovedToMeal(foodToAdd, mealToModify);
                                    }
                                })
                                .show();
                        break;
                    case Constants.DATABASE_INSERT_ERROR:
                    case Constants.DATABASE_UPDATE_ERROR:
                        Snackbar.make(binding.getRoot(), R.string.error,
                                Snackbar.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    private void onFoodRemovedToMeal(Food foodToRemove, MealType mealToModify) {
        Date currentDate = viewModel.getCurrentDate(getActivity()
                .getSharedPreferences(Constants.CURRENT_DATE_PREFERENCES_FILE,
                        Context.MODE_PRIVATE));

        LiveData<Integer> databaseResponse = viewModel
                .removeFoodFromMeal(foodToRemove, mealToModify, currentDate);

        databaseResponse.observe(FoodSearchedItemAlert.this.getActivity(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                switch (integer) {
                    case Constants.DATABASE_REMOVE_OK:
                        Snackbar.make(binding.getRoot(), R.string.removed,
                                Snackbar.LENGTH_SHORT).show();
                        break;
                    case Constants.DATABASE_REMOVE_NOT_PRESENT:
                        Snackbar.make(binding.getRoot(), R.string.not_found,
                                Snackbar.LENGTH_SHORT).show();
                        break;
                    case Constants.DATABASE_REMOVE_ERROR:
                        Snackbar.make(binding.getRoot(), R.string.error,
                                Snackbar.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }
}
