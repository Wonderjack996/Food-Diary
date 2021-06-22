package it.fooddiary.ui.search;

import android.app.Dialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import it.fooddiary.R;
import it.fooddiary.databases.IDatabaseOperation;
import it.fooddiary.databinding.DialogFoodInsertBinding;
import it.fooddiary.models.Food;
import it.fooddiary.utils.Constants;
import it.fooddiary.utils.MealType;

public class FoodInsertItemAlert extends DialogFragment {

    private static final String QUANTITY_CHOOSE = "QuantityChoose";
    private static final String CARBS_CHOOSE = "CarbsChoose";
    private static final String PROTEINS_CHOOSE = "ProteinsChoose";
    private static final String FATS_CHOOSE = "FatsChoose";
    private static final String MEAL_CHOOSE = "MealChoose";

    private static IDatabaseOperation databaseOperation;

    private DialogFoodInsertBinding binding;

    public FoodInsertItemAlert() { }

    public FoodInsertItemAlert(@NotNull @NonNull IDatabaseOperation dbOperation) {
        databaseOperation = dbOperation;
    }

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(requireContext());
        this.binding = DialogFoodInsertBinding.inflate(getLayoutInflater());

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            binding.mainLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        else
            binding.mainLinearLayout.setOrientation(LinearLayout.VERTICAL);

        dialogBuilder.setView(binding.getRoot());
        dialogBuilder.setNegativeButton(R.string.cancel, null);

        MealType[] mealTypes = MealType.values();
        String[] mealNames = new String[mealTypes.length];
        for (int i = 0; i < mealTypes.length; ++i)
            mealNames[i] = mealTypes[i].toString(getResources());
        binding.mealNumberPicker.setDisplayedValues(mealNames);
        binding.mealNumberPicker.setMinValue(0);
        binding.mealNumberPicker.setMaxValue(mealNames.length-1);

        binding.quantityNumberPicker.setMinValue(Constants.MIN_FOOD_GRAMS);
        binding.quantityNumberPicker.setMaxValue(Constants.MAX_FOOD_GRAMS);

        binding.carbsNumberPicker.setMaxValue(100);
        binding.carbsNumberPicker.setMinValue(0);
        binding.proteinsNumberPicker.setMaxValue(100);
        binding.proteinsNumberPicker.setMinValue(0);
        binding.fatsNumberPicker.setMaxValue(100);
        binding.fatsNumberPicker.setMinValue(0);

        binding.carbsNumberPicker.setOnValueChangedListener((picker, oldVal, newVal) -> {
            int currPro = binding.proteinsNumberPicker.getValue();
            int currFat = binding.fatsNumberPicker.getValue();
            binding.proteinsNumberPicker.setMaxValue(100-newVal-currFat);
            binding.fatsNumberPicker.setMaxValue(100-newVal-currPro);
        });

        binding.proteinsNumberPicker.setOnValueChangedListener((picker, oldVal, newVal) -> {
            int currCarb = binding.carbsNumberPicker.getValue();
            int currFat = binding.fatsNumberPicker.getValue();
            binding.carbsNumberPicker.setMaxValue(100-newVal-currFat);
            binding.fatsNumberPicker.setMaxValue(100-newVal-currCarb);
        });

        binding.fatsNumberPicker.setOnValueChangedListener((picker, oldVal, newVal) -> {
            int currPro = binding.proteinsNumberPicker.getValue();
            int currCarb = binding.carbsNumberPicker.getValue();
            binding.proteinsNumberPicker.setMaxValue(100-newVal-currCarb);
            binding.carbsNumberPicker.setMaxValue(100-newVal-currPro);
        });

        if (savedInstanceState != null) {
            int quantity = savedInstanceState.getInt(QUANTITY_CHOOSE, 0);
            int carbs = savedInstanceState.getInt(CARBS_CHOOSE, 0);
            int pro = savedInstanceState.getInt(PROTEINS_CHOOSE, 0);
            int fats = savedInstanceState.getInt(FATS_CHOOSE, 0);

            binding.quantityNumberPicker.setValue(quantity);
            binding.carbsNumberPicker.setValue(carbs);
            binding.proteinsNumberPicker.setValue(pro);
            binding.fatsNumberPicker.setValue(fats);

            binding.carbsNumberPicker.setMaxValue(100-pro-fats);
            binding.proteinsNumberPicker.setMaxValue(100-carbs-fats);
            binding.fatsNumberPicker.setMaxValue(100-carbs-pro);

            int mealChoose = savedInstanceState.getInt(MEAL_CHOOSE, 0);
            binding.mealNumberPicker.setValue(mealChoose);
        } else {
            binding.quantityNumberPicker.setValue(Constants.MID_FOOD_GRAMS);
        }

        dialogBuilder.setTitle(R.string.add_personal);

        dialogBuilder.setPositiveButton(R.string.add, (dialog, which) -> {
            String name = binding.editTextTextPersonName.getText().toString().trim();
            int quantity = binding.quantityNumberPicker.getValue();
            int mealTypeNum = binding.mealNumberPicker.getValue();
            MealType mealType = MealType.values()[mealTypeNum];
            double carbs = binding.carbsNumberPicker.getValue()/100d;
            double pro = binding.proteinsNumberPicker.getValue()/100d;
            double fats = binding.fatsNumberPicker.getValue()/100d;

            Food myFood = new Food(name, quantity, carbs, pro, fats);

            if (databaseOperation != null)
                databaseOperation.addFoodToMeal(myFood, mealType);
            else
                Snackbar.make(binding.getRoot(), R.string.error, Snackbar.LENGTH_LONG).show();
        });

        return dialogBuilder.create();
    }

    @Override
    public void onSaveInstanceState(@NonNull @NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        int quantity = binding.quantityNumberPicker.getValue();
        int carbs = binding.carbsNumberPicker.getValue();
        int pro = binding.proteinsNumberPicker.getValue();
        int fats = binding.fatsNumberPicker.getValue();

        outState.putInt(QUANTITY_CHOOSE, quantity);
        outState.putInt(CARBS_CHOOSE, carbs);
        outState.putInt(PROTEINS_CHOOSE, pro);
        outState.putInt(FATS_CHOOSE, fats);
        outState.putInt(MEAL_CHOOSE, binding.mealNumberPicker.getValue());
    }
}