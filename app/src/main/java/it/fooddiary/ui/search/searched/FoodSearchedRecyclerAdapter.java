package it.fooddiary.ui.search.searched;

import android.app.Activity;
import android.app.Application;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import it.fooddiary.R;
import it.fooddiary.databinding.HolderFoodItemBinding;
import it.fooddiary.models.Food;
import it.fooddiary.utils.MealType;

public class FoodSearchedRecyclerAdapter extends
        RecyclerView.Adapter<FoodSearchedRecyclerAdapter.FoodSearchedViewHolder> {

    private static final String TAG = "FoodSearchRecAdapter";

    private final List<Food> foodDataset;
    private final FragmentManager fragmentManager;
    private final IFoodAlert foodAlert;

    public FoodSearchedRecyclerAdapter(FragmentManager fragmentManager, IFoodAlert foodAlert) {
        this.foodDataset = new ArrayList<>();
        this.fragmentManager = fragmentManager;
        this.foodAlert = foodAlert;
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

        public FoodSearchedViewHolder(View view,
                                      HolderFoodItemBinding binding) {
            super(view);
            this.holderFoodItemBinding = binding;
        }

        public void bind(Food foodClicked) {
            holderFoodItemBinding.setFood(foodClicked);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    foodAlert.setFood(foodClicked);
                    if (foodAlert instanceof DialogFragment)
                        ((DialogFragment)foodAlert).show(fragmentManager, TAG);
                }
            });

            holderFoodItemBinding.executePendingBindings();
        }
    }
}
