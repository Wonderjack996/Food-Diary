package it.fooddiary.models;

public class Food {

    public static final int MAX_FOOD_GRAMS_QUANTITY = 1000;
    public static final int MIN_FOOD_GRAMS_QUANTITY = 1;

    private final String name;
    private int quantity = 0;
    private final double carbsPercent;
    private final double proteinsPercent;
    private final double fatsPercent;

    public Food(String name, int quantity,
                double carbsPercent, double proteinsPercent, double fatsPercent) {
        if (name != null)
            this.name = name;
        else
            this.name = "";

        if (quantity >= MIN_FOOD_GRAMS_QUANTITY && quantity <= MAX_FOOD_GRAMS_QUANTITY)
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

    public int getCarbGrams() {
        return (int)(quantity*carbsPercent);
    }

    public int getProteinGrams() {
        return (int)(quantity*proteinsPercent);
    }

    public int getFatGrams() {
        return (int)(quantity*fatsPercent);
    }

    public int getTotalCalories() {
        return getCarbGrams()*4 + getProteinGrams()*4 + getFatGrams()*9;
    }

    public void setQuantity(int newQuantity) {
        if (newQuantity > 0)
            this.quantity = newQuantity;
    }
}
