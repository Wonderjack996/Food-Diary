package it.fooddiary.models;

public class Food {

    private final String name;
    private final int quantity;
    private final int carbGrams;
    private final int proteinGrams;
    private final int fatGrams;
    private final int totalCalories;

    public Food(String name, int quantity, int carbGrams, int proteinGrams, int fatGrams) {
        this.name = name;
        this.quantity = quantity;
        this.carbGrams = carbGrams;
        this.proteinGrams = proteinGrams;
        this.fatGrams = fatGrams;
        this.totalCalories = carbGrams*4 + proteinGrams*4 + fatGrams*9;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getCarbGrams() {
        return carbGrams;
    }

    public int getProteinGrams() {
        return proteinGrams;
    }

    public int getFatGrams() {
        return fatGrams;
    }

    public int getTotalCalories() {
        return totalCalories;
    }
}
