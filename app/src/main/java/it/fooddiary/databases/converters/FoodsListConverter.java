package it.fooddiary.databases.converters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import it.fooddiary.models.Food;

public class FoodsListConverter {

    @TypeConverter
    public String listToTimestamp(List<Food> foodList) {
        if (foodList == null)
            return null;
        Gson gson = new Gson();
        Type type = new TypeToken<List<Food>>() {}.getType();
        String ret = gson.toJson(foodList, type);
        return ret;
    }

    @TypeConverter
    public List<Food> fromTimestamp(String foodList) {
        if (foodList == null)
            return null;
        Gson gson = new Gson();
        Type type = new TypeToken<List<Food>>() {}.getType();
        List<Food> ret = gson.fromJson(foodList, type);
        return ret;
    }
}
