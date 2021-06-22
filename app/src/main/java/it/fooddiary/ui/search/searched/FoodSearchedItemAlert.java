package it.fooddiary.ui.search.searched;

import android.app.Dialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.jetbrains.annotations.NotNull;

import it.fooddiary.R;
import it.fooddiary.databases.IDatabaseOperation;
import it.fooddiary.databinding.DialogFoodSearchedItemBinding;
import it.fooddiary.models.Food;
import it.fooddiary.ui.IFoodAlert;
import it.fooddiary.utils.Constants;
import it.fooddiary.utils.MealType;

public class FoodSearchedItemAlert extends DialogFragment implements IFoodAlert {

    private static final String LAST_FOOD_CLICKED = "LastFoodClicked";

    private static IDatabaseOperation databaseOperation;

    private DialogFoodSearchedItemBinding binding;

    private Food foodClicked = new Food("error",0,
            0, 0, 0);

    public FoodSearchedItemAlert() { }

    public FoodSearchedItemAlert(IDatabaseOperation dbOperation) {
        databaseOperation = dbOperation;
    }

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(requireContext());
        this.binding = DialogFoodSearchedItemBinding.inflate(getLayoutInflater());

        MealType[] mealTypes = MealType.values();
        String[] mealNames = new String[mealTypes.length];
        for (int i = 0; i < mealTypes.length; ++i)
            mealNames[i] = mealTypes[i].toString(getResources());
        binding.mealNumberPicker.setDisplayedValues(mealNames);
        binding.mealNumberPicker.setMinValue(0);
        binding.mealNumberPicker.setMaxValue(mealNames.length-1);

        if (savedInstanceState != null) {
            if (savedInstanceState.getParcelable(LAST_FOOD_CLICKED) != null)
                foodClicked = savedInstanceState.getParcelable(LAST_FOOD_CLICKED);
            if (savedInstanceState.getInt(Constants.MEAL_TYPE, -1) != -1)
                binding.mealNumberPicker.setValue(savedInstanceState.getInt(Constants.MEAL_TYPE));
        }

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            binding.mainLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        else
            binding.mainLinearLayout.setOrientation(LinearLayout.VERTICAL);

        dialogBuilder.setView(binding.getRoot());
        dialogBuilder.setNegativeButton(R.string.cancel, null);

        binding.quantityNumberPicker
                .setMinValue(Constants.MIN_FOOD_GRAMS);
        binding.quantityNumberPicker
                .setMaxValue(Constants.MAX_FOOD_GRAMS);

        binding.setFood(foodClicked);
        binding.quantityNumberPicker.setOnValueChangedListener((picker, oldVal, newVal) -> {
            foodClicked.setQuantity(newVal);
            binding.invalidateAll();
        });
        binding.quantityNumberPicker.setValue(foodClicked.getQuantity());
        dialogBuilder.setTitle(foodClicked.getName());
        dialogBuilder.setPositiveButton(R.string.add, (dialog, which) -> {
            int quantity = binding.quantityNumberPicker.getValue();
            int mealTypeNum = binding.mealNumberPicker.getValue();
            MealType mealType = MealType.values()[mealTypeNum];
            foodClicked.setQuantity(quantity);
            if (databaseOperation != null)
                databaseOperation.addFoodToMeal(foodClicked, mealType);
        });
        return dialogBuilder.create();
    }

    @Override
    public void onSaveInstanceState(@NonNull @NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(LAST_FOOD_CLICKED, foodClicked);
        outState.putInt(Constants.MEAL_TYPE, binding.mealNumberPicker.getValue());
    }

    @Override
    public void setFood(@NotNull @NonNull Food food) {
        this.foodClicked = food;
    }
}