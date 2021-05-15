package it.fooddiary;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

import it.fooddiary.models.Food;

public class FoodRecyclerAdapter extends RecyclerView.Adapter<FoodRecyclerAdapter.FoodViewHolder>{

    private final List<Food> foodDataset;

    public FoodRecyclerAdapter(List<Food> foods) {
        this.foodDataset = foods;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.holder_food_item, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        holder.bindFood(foodDataset.get(position));
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

    public static class FoodViewHolder extends RecyclerView.ViewHolder {

        private final TextView name;
        private final TextView calories;
        private final TextView quantity;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.foodName);
            calories = itemView.findViewById(R.id.foodCalories);
            quantity = itemView.findViewById(R.id.foodQuantity);
        }

        public void bindFood(Food x) {
            name.setText(x.getName());
            calories.setText(String.valueOf(x.getTotalCalories()) + " KCals");
            quantity.setText(String.valueOf(x.getQuantity()) + " g");
        }
    }
}
