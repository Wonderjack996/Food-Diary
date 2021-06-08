package it.fooddiary.models;

public class MealProperties {

    private int caloriesDailyIntake;
    private final double carbsPercent;
    private final double proteinsPercent;
    private final double fatsPercent;

    public MealProperties(int caloriesDailyIntake,
                          double carbsPercent, double proteinsPercent, double fatsPercent) {
        this.caloriesDailyIntake = caloriesDailyIntake;
        if (carbsPercent >= 0 && proteinsPercent >= 0 && fatsPercent >= 0) {
            this.carbsPercent = carbsPercent;
            this.proteinsPercent = proteinsPercent;
            this.fatsPercent = fatsPercent;
            return;
        }
        this.carbsPercent = 0;
        this.proteinsPercent = 0;
        this.fatsPercent = 0;
    }

    public int getCaloriesDailyIntake() {
        return caloriesDailyIntake;
    }

    public void setCaloriesDailyIntake(int caloriesDailyIntake) {
        this.caloriesDailyIntake = caloriesDailyIntake;
    }

    public int getCarbsGrams() {
        int carbCal = (int) (caloriesDailyIntake * carbsPercent);
        return carbCal / 4;
    }

    public int getProteinsGrams() {
        int proCal = (int) (caloriesDailyIntake * proteinsPercent);
        return proCal / 4;
    }

    public int getFatsGrams() {
        int fatCal = (int) (caloriesDailyIntake * fatsPercent);
        return fatCal / 9;
    }
}
