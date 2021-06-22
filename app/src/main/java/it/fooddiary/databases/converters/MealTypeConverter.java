package it.fooddiary.databases.converters;

import androidx.room.TypeConverter;

import it.fooddiary.utils.MealType;

/**
 * Converter utilizzato per passare da enum MealType a stringa, e viceversa.
 * Utilizzato per inserire un MealType in room db.
 */
public class MealTypeConverter {

    @TypeConverter
    public static MealType fromTimestamp(String value) {
        if (value == null)
            return null;
        return MealType.valueOf(value);
    }

    @TypeConverter
    public static String mealTypeToTimestamp(MealType mealType) {
        if (mealType == null)
            return null;
        return mealType.toString();
    }
}