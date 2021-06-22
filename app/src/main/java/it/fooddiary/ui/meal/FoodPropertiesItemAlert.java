package it.fooddiary.ui.meal;

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
import it.fooddiary.databinding.DialogFoodPropertiesItemBinding;
import it.fooddiary.models.Food;
import it.fooddiary.ui.IFoodAlert;
import it.fooddiary.utils.Constants;

public class FoodPropertiesItemAlert extends DialogFragment implements IFoodAlert {

    private static final String LAST_FOOD_CLICKED = "LastFoodClicked";

    private static IDatabaseOperation databaseOperation;

    private DialogFoodPropertiesItemBinding binding;

    private Food foodClicked = new Food("error",0,
            0, 0, 0);

    public FoodPropertiesItemAlert() { }

    public FoodPropertiesItemAlert(@NonNull @NotNull IDatabaseOperation dbOperation) {
        databaseOperation = dbOperation;
    }

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(requireContext());
        this.binding = DialogFoodPropertiesItemBinding.inflate(getLayoutInflater());

        if (savedInstanceState != null) {
            if (savedInstanceState.getParcelable(LAST_FOOD_CLICKED) != null)
                foodClicked = savedInstanceState.getParcelable(LAST_FOOD_CLICKED);
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

        dialogBuilder.setPositiveButton(R.string.ok, (dialog, which) -> {
            int quantity = binding.quantityNumberPicker.getValue();
            if (databaseOperation != null)
                databaseOperation.modifyFood(foodClicked);
            foodClicked.setQuantity(quantity);
        });

        return dialogBuilder.create();
    }

    @Override
    public void onSaveInstanceState(@NonNull @NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(LAST_FOOD_CLICKED, foodClicked);
    }

    @Override
    public void setFood(@NotNull @NonNull Food food) {
        this.foodClicked = food;
    }
}