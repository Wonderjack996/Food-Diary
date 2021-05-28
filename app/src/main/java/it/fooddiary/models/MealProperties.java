package it.fooddiary.models;

public class MealProperties {

    private int caloriesDailyIntake;
    private float carbsPercent;
    private float proteinsPercent;
    private float fatsPercent;

    public MealProperties(int caloriesDailyIntake,
                          float carbsPercent, float proteinsPercent, float fatsPercent) {
        this.caloriesDailyIntake = caloriesDailyIntake;
        this.carbsPercent = carbsPercent;
        this.proteinsPercent = proteinsPercent;
        this.fatsPercent = fatsPercent;
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

    public void setCarbsPercent(float carbsPercent) {
        this.carbsPercent = carbsPercent;
    }

    public int getProteinsGrams() {
        int proCal = (int) (caloriesDailyIntake * proteinsPercent);
        return proCal / 4;
    }

    public void setProteinsPercent(float proteinsPercent) {
        this.proteinsPercent = proteinsPercent;
    }

    public int getFatsGrams() {
        int fatCal = (int) (caloriesDailyIntake * fatsPercent);
        return fatCal / 9;
    }

    public void setFatsPercent(float fatsPercent) {
        this.fatsPercent = fatsPercent;
    }
}
