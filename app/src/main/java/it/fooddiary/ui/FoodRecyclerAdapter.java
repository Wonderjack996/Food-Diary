package it.fooddiary.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import it.fooddiary.R;
import it.fooddiary.databinding.HolderFoodItemBinding;
import it.fooddiary.models.Food;

public class FoodRecyclerAdapter extends
        RecyclerView.Adapter<FoodRecyclerAdapter.FoodSearchedViewHolder> {

    private static final String TAG = "FoodSearchRecAdapter";

    private final List<Food> foodDataset;
    private final FragmentManager fragmentManager;
    private final IFoodAlert foodAlert;

    public FoodRecyclerAdapter(FragmentManager fragmentManager, IFoodAlert foodAlert) {
        this.foodDataset = new ArrayList<>();
        this.fragmentManager = fragmentManager;
        this.foodAlert = foodAlert;
    }



    public void setFoodDataset(List<Food> foods) {
        foodDataset.clear();
        foodDataset.addAll(foods);
        notifyDataSetChanged();
    }

    public Food getFood(Food tmp) {
        if (foodDataset.contains(tmp))
            return foodDataset.get(foodDataset.indexOf(tmp));
        return null;
    }

    public Food getFoodByPosition(int position) {
        if (position >= 0 && position < foodDataset.size())
            return foodDataset.get(position);
        return null;
    }

    public Food removeItem(int position) {
        if (position >= 0 && position < foodDataset.size())
            return foodDataset.remove(position);
        return null;
    }

    public void addItem(Food food, int position) {
        if (position >= 0 && position < foodDataset.size())
            foodDataset.add(position, food);
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
