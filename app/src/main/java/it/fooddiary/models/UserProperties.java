package it.fooddiary.models;

import it.fooddiary.utils.Constants;

public class UserProperties {

    private int age;
    private int gender;
    private int heightCm;
    private int weightKg;
    private int activityLevel;

    public UserProperties() { }

    public UserProperties(int age, int gender, int heightCm, int weightKg, int activityLevel) {
        this.age = age;
        this.gender = gender;
        this.heightCm = heightCm;
        this.weightKg = weightKg;
        this.activityLevel = activityLevel;
    }

    public int getCaloriesDailyIntake() {
        int bmr;
        if (gender == Constants.GENDER_MALE)
            bmr = Constants.calculateBMR_Male(weightKg, heightCm, age);
        else
            bmr = Constants.calculateBMR_Female(weightKg, heightCm, age);
        switch (activityLevel) {
            case Constants.ACTIVITY_HIGH:
                bmr = (int)(bmr * Constants.HIGH_ACTIVITY_VALUE);
                break;
            case Constants.ACTIVITY_LOW:
                bmr = (int)(bmr * Constants.LOW_ACTIVITY_VALUE);
                break;
            default:
                bmr = (int)(bmr * Constants.MID_ACTIVITY_VALUE);
                break;
        }
        return bmr;
    }

    public int getCarbsGrams() {
        int carbCal = (int) (getCaloriesDailyIntake() * Constants.DEFAULT_CARBS_PERCENT_DAILY);
        return carbCal / 4;
    }

    public int getProteinsGrams() {
        int proCal = (int) (getCaloriesDailyIntake() * Constants.DEFAULT_PROTEINS_PERCENT_DAILY);
        return proCal / 4;
    }

    public int getFatsGrams() {
        int fatCal = (int) (getCaloriesDailyIntake() * Constants.DEFAULT_FATS_PERCENT_DAILY);
        return fatCal / 9;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getHeightCm() {
        return heightCm;
    }

    public void setHeightCm(int heightCm) {
        this.heightCm = heightCm;
    }

    public int getWeightKg() {
        return weightKg;
    }

    public void setWeightKg(int weightKg) {
        this.weightKg = weightKg;
    }

    public int getActivityLevel() {
        return activityLevel;
    }

    public void setActivityLevel(int activityLevel) {
        this.activityLevel = activityLevel;
    }
}
