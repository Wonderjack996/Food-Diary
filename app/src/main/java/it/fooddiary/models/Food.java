package it.fooddiary.models;

import java.util.Objects;

import it.fooddiary.utils.Constants;

public class Food implements IFoodProperties {

    private final String id;
    private final String name;
    private final double carbsPercent;
    private final double proteinsPercent;
    private final double fatsPercent;
    private int quantity = 0;

    public Food(String name, String id, int quantity,
                double carbsPercent, double proteinsPercent, double fatsPercent) {
        if (name != null)
            this.name = name;
        else
            this.name = "";

        if (id != null)
            this.id = id;
        else
            this.id = "";

        if (quantity >= Constants.MIN_FOOD_GRAMS && quantity <= Constants.MAX_FOOD_GRAMS)
            this.quantity = quantity;
        else
            this.quantity = 0;

        if (carbsPercent > 0 && proteinsPercent > 0 && fatsPercent > 0) {
            double tot = carbsPercent + proteinsPercent + fatsPercent;
            if (tot > 0.9 && tot <= 1) {
                this.carbsPercent = carbsPercent;
                this.proteinsPercent = proteinsPercent;
                this.fatsPercent = fatsPercent;
                return;
            }
        }
        this.carbsPercent = 0;
        this.proteinsPercent = 0;
        this.fatsPercent = 0;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public int getTotalCarbsGrams() {
        return (int)(quantity*carbsPercent);
    }

    @Override
    public int getTotalProteinsGrams() {
        return (int)(quantity*proteinsPercent);
    }

    @Override
    public int getTotalFatsGrams() {
        return (int)(quantity*fatsPercent);
    }

    @Override
    public int getTotalCalories() {
        return getTotalCarbsGrams()*Constants.CALORIES_PER_CARB_GRAM +
                getTotalProteinsGrams()*Constants.CALORIES_PER_PROTEIN_GRAM +
                getTotalFatsGrams()*Constants.CALORIES_PER_FAT_GRAM;
    }

    public void setQuantity(int newQuantity) {
        if (newQuantity > 0)
            this.quantity = newQuantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Food food = (Food) o;
        return id.equals(food.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
