package it.fooddiary.ui.search.searched;

import android.app.Activity;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import it.fooddiary.R;
import it.fooddiary.databinding.ActivityLoginBinding;
import it.fooddiary.databinding.DialogFoodSearchedItemBinding;
import it.fooddiary.databinding.HolderFoodItemBinding;
import it.fooddiary.models.Food;
import it.fooddiary.utils.Constants;
import it.fooddiary.utils.MealType;

public class FoodSearchedRecyclerAdapter extends
        RecyclerView.Adapter<FoodSearchedRecyclerAdapter.FoodSearchedViewHolder> {

    private static final String TAG = "FoodSearchRecAdapter";

    private final List<Food> foodDataset;
    private final Activity ownerActivity;
    private final IFoodAdded foodAdded;

    public FoodSearchedRecyclerAdapter(Activity ownerActivity, IFoodAdded foodAdded) {
        this.foodDataset = new ArrayList<>();
        this.ownerActivity = ownerActivity;
        this.foodAdded = foodAdded;
    }

    public void setFoodDataset(List<Food> foods) {
        foodDataset.clear();
        foodDataset.addAll(foods);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FoodSearchedViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                         int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.holder_food_item, parent, false);
        HolderFoodItemBinding bind = DataBindingUtil.bind(view);
        return new FoodSearchedViewHolder(view, bind);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodSearchedViewHolder holder, int position) {
        holder.bind(foodDataset.get(position));
    }

    @Override
    public int getItemCount() {
        return foodDataset.size();
    }

    public class FoodSearchedViewHolder extends RecyclerView.ViewHolder {

        private final HolderFoodItemBinding holderFoodItemBinding;
        private final DialogFoodSearchedItemBinding dialogFoodSearchedItemBinding;
        private final MaterialAlertDialogBuilder foodDetailsDialog;

        public FoodSearchedViewHolder(View view, HolderFoodItemBinding binding) {
            super(view);
            this.holderFoodItemBinding = binding;
            this.dialogFoodSearchedItemBinding = DialogFoodSearchedItemBinding
                    .inflate(ownerActivity.getLayoutInflater());
            this.foodDetailsDialog = new MaterialAlertDialogBuilder(ownerActivity);

            foodDetailsDialog.setView(dialogFoodSearchedItemBinding.getRoot());
            foodDetailsDialog.setNegativeButton(R.string.cancel, null);

            dialogFoodSearchedItemBinding.quantityNumberPicker
                    .setMinValue(Constants.MIN_FOOD_GRAMS);
            dialogFoodSearchedItemBinding.quantityNumberPicker
                    .setMaxValue(Constants.MAX_FOOD_GRAMS);

            MealType[] mealTypes = MealType.values();
            String[] mealNames = new String[mealTypes.length];
            for (int i = 0; i < mealTypes.length; ++i)
                mealNames[i] = mealTypes[i].toString(ownerActivity.getResources());
            dialogFoodSearchedItemBinding.mealNumberPicker.setDisplayedValues(mealNames);
            dialogFoodSearchedItemBinding.mealNumberPicker.setMinValue(0);
            dialogFoodSearchedItemBinding.mealNumberPicker.setMaxValue(mealNames.length-1);
        }

        public void bind(Food foodClicked) {
            holderFoodItemBinding.setFood(foodClicked);
            dialogFoodSearchedItemBinding.setFood(foodClicked);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogFoodSearchedItemBinding.quantityNumberPicker
                            .setValue(foodClicked.getQuantity());

                    foodDetailsDialog.setTitle(foodClicked.getName());

                    foodDetailsDialog.setPositiveButton("add",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    int quantity = dialogFoodSearchedItemBinding
                                            .quantityNumberPicker.getValue();
                                    int mealTypeNum = dialogFoodSearchedItemBinding
                                            .mealNumberPicker.getValue();
                                    MealType mealType = MealType.values()[mealTypeNum];
                                    foodClicked.setQuantity(quantity);

                                    if (foodAdded != null)
                                        foodAdded.onFoodAddedToMeal(foodClicked, mealType);
                                    else
                                        Snackbar.make(holderFoodItemBinding.getRoot(),
                                                R.string.error, Snackbar.LENGTH_LONG)
                                                .show();
                                }
                            });

                    if (dialogFoodSearchedItemBinding.getRoot().getParent() != null)
                        ((ViewGroup)dialogFoodSearchedItemBinding.getRoot()
                                .getParent()).removeAllViews();

                    foodDetailsDialog.show();
                }
            });

            holderFoodItemBinding.executePendingBindings();
        }
    }
}
