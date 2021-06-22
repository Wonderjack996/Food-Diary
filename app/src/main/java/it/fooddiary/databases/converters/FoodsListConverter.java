package it.fooddiary.databases.converters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import it.fooddiary.models.Food;

/**
 * Converter utilizzato per passare da lista di Food a stringa e viceversa.
 * La lista viene convertita in json tramite Gson, e riconvertita da stringa
 * sempre tramite Gson. Viene utilizzato per inserire una lista in room db.
 */
public class FoodsListConverter {

    @TypeConverter
    public static String listToTimestamp(List<Food> foodList) {
        if (foodList == null)
            return null;
        Gson gson = new Gson();
        Type type = new TypeToken<List<Food>>() {}.getType();
        return gson.toJson(foodList, type);
    }

    @TypeConverter
    public static List<Food> fromTimestamp(String foodList) {
        if (foodList == null)
            return null;
        Gson gson = new Gson();
        Type type = new TypeToken<List<Food>>() {}.getType();
        return gson.fromJson(foodList, type);
    }
}