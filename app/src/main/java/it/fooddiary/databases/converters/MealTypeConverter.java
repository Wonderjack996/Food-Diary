package it.fooddiary.databases.converters;

import androidx.room.TypeConverter;

import it.fooddiary.models.Meal;
import it.fooddiary.utils.MealType;

public class MealTypeConverter {

    @TypeConverter
    public static MealType fromTimestamp(String value) {
        if (value == null)
            return null;
        MealType ret = MealType.valueOf(value);
        return ret;
    }

    @TypeConverter
    public static String mealTypeToTimestamp(MealType mealType) {
        if (mealType == null)
            return null;
        String ret = "";
        switch (mealType) {
            case BREAKFAST:
                ret = MealType.BREAKFAST.name();
                break;
            case LUNCH:
                ret = MealType.LUNCH.name();
                break;
            case DINNER:
                ret = MealType.DINNER.name();
                break;
            case SNACKS:
                ret = MealType.SNACKS.name();
                break;
        }
        return ret;
    }
}
