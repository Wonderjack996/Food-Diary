package it.fooddiary.util;

import it.fooddiary.R;

public class Constants {

    /**
     * Login Fields
     */
    public static final String PERSONAL_DATA_PREFERENCES_FILE = "PersonalDataPreferencesFile";
    public static final String USER_NAME = "UserName";
    public static final String USER_SURNAME = "UserSurname";
    public static final String USER_DATE_BIRTH = "UserDateBirth";
    public static final String USER_WEIGHT_KG = "UserWeightKg";
    public static final String USER_HEIGHT_CM = "UserHeightCm";
    public static final String USER_ACTIVITY_LEVEL = "UserActivityLevel";

    /**
     * Data Ranges
     */
    public static final int MIN_AGE = 10;
    public static final int MAX_AGE = 120;
    public static final int MAX_CALORIES_KCAL = 10000;
    public static final int MIN_CALORIES_KCAL = 1;
    public static final int MID_CALORIES_KCAL = 200;
    public static final int MAX_HEIGHT_CM = 300;
    public static final int MIN_HEIGHT_CM = 10;
    public static final int MID_HEIGHT_CM = 160;
    public static final int MAX_WEIGHT_KG = 1000;
    public static final int MIN_WEIGHT_KG = 10;
    public static final int MID_WEIGHT_KG = 70;
    public static final int MAX_FOOD_GRAMS = 10000;
    public static final int MIN_FOOD_GRAMS = 1;

    /**
     * Edamam API Fields
     */
    public static final String API_BASE_URL = "https://api.edamam.com/";
    public static final String API_APP_ID = "41f8bc34";
    public static final String API_APP_KEY = "56c1815d351d662dd081f1d853710bc2";
    public static final String API_NUTRITION_TYPE_LOGGING = "logging";
    public static final String API_CATEGORY_LABEL_FOOD = "food";
    public static final String API_CATEGORY_GENERIC_FOOD = "generic-foods";

    public static final String MEALS_NAME = "MealsName";

    public static final String CURRENT_DATE = "CurrentDate";
}
