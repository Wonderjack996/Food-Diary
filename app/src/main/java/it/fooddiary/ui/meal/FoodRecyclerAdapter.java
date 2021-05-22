package it.fooddiary.ui.meal;

import android.app.Activity;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

import it.fooddiary.R;
import it.fooddiary.databinding.HolderFoodItemBinding;
import it.fooddiary.models.Food;

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

        private final HolderFoodItemBinding binding;

        public FoodViewHolder(View view, HolderFoodItemBinding binding) {
            super(view);
            this.binding = binding;
        }

        public void bind(Food foodClicked) {
            binding.setFood(foodClicked);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MaterialAlertDialogBuilder modifyQuantityAlert =
                            new MaterialAlertDialogBuilder(ownerActivity);
                    NumberPicker numberPicker = new NumberPicker(ownerActivity);
                    numberPicker.setMinValue(Food.MIN_FOOD_GRAMS_QUANTITY);
                    numberPicker.setMaxValue(Food.MAX_FOOD_GRAMS_QUANTITY);
                    modifyQuantityAlert.setTitle(R.string.modifyQuantity);
                    modifyQuantityAlert.setView(numberPicker);
                    modifyQuantityAlert.setPositiveButton(R.string.ok,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    int newQuantity = numberPicker.getValue();
                                    if (foodClicked.getQuantity() != newQuantity) {
                                        foodClicked.setQuantity(newQuantity);
                                        binding.invalidateAll();
                                    }
                                }
                            });
                    modifyQuantityAlert.setNegativeButton(R.string.cancel, null);
                    numberPicker.setValue(foodClicked.getQuantity());
                    modifyQuantityAlert.show();
                }
            });

            binding.executePendingBindings();
        }
    }
}
