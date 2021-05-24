package it.fooddiary.utils;

import it.fooddiary.BuildConfig;

public class Constants {

    /**
     * Calories values
     */
    public static final int CALORIES_PER_CARB_GRAM = 4;
    public static final int CALORIES_PER_PROTEIN_GRAM = 4;
    public static final int CALORIES_PER_FAT_GRAM = 9;

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
    public static final String API_APP_ID = BuildConfig.EDAMAM_APP_ID;
    public static final String API_APP_KEY = BuildConfig.EDAMAM_APP_KEY;
    public static final String API_NUTRITION_TYPE_LOGGING = "logging";
    public static final String API_CATEGORY_LABEL_FOOD = "food";
    public static final String API_CATEGORY_GENERIC_FOOD = "generic-foods";

    /**
     * Database Fields
     */
    public static final String DATABASE_NAME = "app_database";
    public static final int DATABASE_VERSION = 1;

    public static final String MEALS_NAME = "MealsName";

    public static final String CURRENT_DATE = "CurrentDate";
}
