package it.fooddiary.ui.meal;

import android.app.Activity;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

import it.fooddiary.R;
import it.fooddiary.databinding.DialogFoodItemBinding;
import it.fooddiary.databinding.HolderFoodItemBinding;
import it.fooddiary.models.Food;
import it.fooddiary.utils.Constants;

public class FoodRecyclerAdapter extends RecyclerView.Adapter<FoodRecyclerAdapter.FoodViewHolder>{

    private final List<Food> foodDataset;
    private final Activity ownerActivity;

    public FoodRecyclerAdapter(List<Food> foods, Activity owner) {
        this.foodDataset = foods;
        this.ownerActivity = owner;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.holder_food_item, parent, false);
        HolderFoodItemBinding binding = DataBindingUtil.bind(view);
        return new FoodViewHolder(view, binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        holder.bind(foodDataset.get(position));
    }

    @Override
    public int getItemCount() {
        return foodDataset.size();
    }

    public Food removeItem(int position) {
        return foodDataset.remove(position);
    }

    public void addItem(Food food, int position) {
        foodDataset.add(position, food);
    }

    public class FoodViewHolder extends RecyclerView.ViewHolder {

        private final HolderFoodItemBinding holderFoodItemBinding;
        private final DialogFoodItemBinding dialogActivityMealBinding;
        private final MaterialAlertDialogBuilder foodDetailsDialog;

        public FoodViewHolder(View view, HolderFoodItemBinding binding) {
            super(view);
            this.holderFoodItemBinding = binding;
            this.dialogActivityMealBinding = DialogFoodItemBinding
                    .inflate(ownerActivity.getLayoutInflater());
            this.foodDetailsDialog = new MaterialAlertDialogBuilder(ownerActivity);

            foodDetailsDialog.setView(dialogActivityMealBinding.getRoot());
            foodDetailsDialog.setNegativeButton(R.string.cancel, null);

            dialogActivityMealBinding.quantityNumberPicker
                    .setMinValue(Constants.MIN_FOOD_GRAMS);
            dialogActivityMealBinding.quantityNumberPicker
                    .setMaxValue(Constants.MAX_FOOD_GRAMS);
        }

        public void bind(Food foodClicked) {
            holderFoodItemBinding.setFood(foodClicked);
            dialogActivityMealBinding.setFood(foodClicked);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogActivityMealBinding.quantityNumberPicker
                            .setValue(foodClicked.getQuantity());

                    foodDetailsDialog.setTitle(foodClicked.getName());

                    foodDetailsDialog.setPositiveButton(R.string.ok,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    int newQuantity = dialogActivityMealBinding
                                            .quantityNumberPicker.getValue();
                                    if (foodClicked.getQuantity() != newQuantity) {
                                        foodClicked.setQuantity(newQuantity);
                                        holderFoodItemBinding.invalidateAll();
                                        dialogActivityMealBinding.invalidateAll();
                                    }
                                }
                            });

                    if (dialogActivityMealBinding.getRoot().getParent() != null)
                        ((ViewGroup)dialogActivityMealBinding.getRoot()
                                .getParent()).removeAllViews();

                    foodDetailsDialog.show();
                }
            });

            holderFoodItemBinding.executePendingBindings();
        }
    }
}
