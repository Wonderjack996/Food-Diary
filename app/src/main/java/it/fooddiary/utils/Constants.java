package it.fooddiary.utils;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

import it.fooddiary.BuildConfig;

public class Constants {

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    //"(?=.*[0-9])" +         //at least 1 digit
                    //"(?=.*[a-z])" +         //at least 1 lower case letter
                    //"(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +        //any letter
                    "(?=.*[@#$%^&+=!?_-])" +  //at least 1 special character
                    "(?=\\S+$)" +             //no white spaces
                    ".{8,}" +                 //at least 8 characters
                    "$");

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+");

    /**
     * Calories values
     */
    public static final int CALORIES_PER_CARB_GRAM = 4;
    public static final int CALORIES_PER_PROTEIN_GRAM = 4;
    public static final int CALORIES_PER_FAT_GRAM = 9;
    public static final float DEFAULT_CARBS_PERCENT_DAILY = 0.5f;
    public static final float DEFAULT_PROTEINS_PERCENT_DAILY = 0.2f;
    public static final float DEFAULT_FATS_PERCENT_DAILY = 0.3f;
    public static final float HIGH_ACTIVITY_VALUE = 1.725f;
    public static final float MID_ACTIVITY_VALUE = 1.55f;
    public static final float LOW_ACTIVITY_VALUE = 1.2f;

    /**
     * Login Fields
     */
    public static final String USER_GENDER = "UserGender";
    public static final String USER_AGE = "UserAge";
    public static final String USER_WEIGHT_KG = "UserWeightKg";
    public static final String USER_HEIGHT_CM = "UserHeightCm";
    public static final String USER_ACTIVITY_LEVEL = "UserActivityLevel";
    public static final int GENDER_MALE = 0;
    public static final int GENDER_FEMALE = 1;
    public static final int ACTIVITY_HIGH = 2;
    public static final int ACTIVITY_MID = 3;
    public static final int ACTIVITY_LOW = 4;
    public static final int OTHER = 5;

    /**
     * Data Ranges
     */
    public static final int MIN_AGE = 10;
    public static final int MAX_AGE = 120;
    public static final int MID_AGE = 30;
    public static final int MAX_HEIGHT_CM = 300;
    public static final int MIN_HEIGHT_CM = 10;
    public static final int MID_HEIGHT_CM = 160;
    public static final int MAX_WEIGHT_KG = 1000;
    public static final int MIN_WEIGHT_KG = 10;
    public static final int MID_WEIGHT_KG = 70;
    public static final int MAX_FOOD_GRAMS = 10000;
    public static final int MIN_FOOD_GRAMS = 1;
    public static final int MID_FOOD_GRAMS = 100;

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
    public static final String DATABASE_NAME = "food_diary_database";
    public static final int DATABASE_VERSION = 1;

    /**
     * Database status codes
     */
    public static final int DATABASE_INSERT_OK = 200;
    public static final int DATABASE_UPDATE_OK = 210;
    public static final int DATABASE_REMOVE_OK = 220;
    public static final int DATABASE_REMOVE_RECENT_FOOD_OK =230 ;
    public static final int DATABASE_INSERT_RECENT_FOOD_OK = 240;
    public static final int DATABASE_INSERT_ALREADY_PRESENT = 300;
    public static final int DATABASE_INSERT_ERROR = 400;
    public static final int DATABASE_UPDATE_ERROR = 410;
    public static final int DATABASE_REMOVE_ERROR = 420;
    public static final int DATABASE_REMOVE_NOT_PRESENT = 430;
    public static final int DATABASE_INSERT_RECENT_FOOD_ERROR = 440;
    public static final int DATABASE_REMOVE_RECENT_FOOD_ERROR = 450;

    /**
     * Firebase authentication
     */
    public static final int FIREBASE_LOGIN_OK = 100;
    public static final int FIREBASE_LOGIN_ERROR = 101;
    public static final int FIREBASE_REGISTER_OK = 102;
    public static final int FIREBASE_REGISTER_ERROR = 103;
    public static final int FIREBASE_UPDATE_OK = 104;
    public static final int FIREBASE_UPDATE_ERROR = 105;

    /**
     * Firebase database
     */
    public static final String FIREBASE_DATABASE_NAME = "https://food-diary-6ce0a-default-rtdb.europe-west1.firebasedatabase.app/";
    public static final String FIREBASE_USERS_TABLE = "users";

    /**
     * Preferences files
     */
    public static final String CURRENT_DATE_PREFERENCES_FILE = "CurrentDatePreferencesFile";
    public static final String PERSONAL_DATA_PREFERENCES_FILE = "PersonalDataPreferencesFile";
    public static final String FIREBASE_USER_PREFERENCES_FILE = "FirebaseUserPreferencesFile";

    public static final String FIREBASE_USER_ID = "FirebaseUserId";
    public static final String CURRENT_DATE = "CurrentDate";
    public static final String MEAL_TYPE = "MealType";
    public static final int MIN_INSERTED_INGREDIENT_LENGTH = 2;
    public static final int NUM_PRELOADED_FRAGMENT = 31;


    public static int calculateBMR_Male(int weight, int height, int age) {
        if (weight < Constants.MIN_WEIGHT_KG || weight > Constants.MAX_WEIGHT_KG)
            return -1;
        if (height < Constants.MIN_HEIGHT_CM || height > Constants.MAX_HEIGHT_CM)
            return -1;
        if (age < Constants.MIN_AGE || age > Constants.MAX_AGE)
            return -1;
        return 10*weight + 6*height - 5*age + 5;
    }

    public static int calculateBMR_Female(int weight, int height, int age) {
        if (weight < Constants.MIN_WEIGHT_KG || weight > Constants.MAX_WEIGHT_KG)
            return -1;
        if (height < Constants.MIN_HEIGHT_CM || height > Constants.MAX_HEIGHT_CM)
            return -1;
        if (age < Constants.MIN_AGE || age > Constants.MAX_AGE)
            return -1;
        return 10*weight + 6*height - 5*age - 161;
    }

    public static boolean isMailValid(@NonNull @NotNull String mail) {
        if (mail.isEmpty())
            return false;
        return EMAIL_PATTERN.matcher(mail).matches();
    }

    public static boolean isPasswordValid(@NonNull @NotNull String password) {
        if (password.isEmpty())
            return false;
        return PASSWORD_PATTERN.matcher(password).matches();
    }
}