package it.fooddiary.databases;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import org.jetbrains.annotations.NotNull;

import it.fooddiary.databases.converters.DateConverter;
import it.fooddiary.databases.converters.FoodsListConverter;
import it.fooddiary.databases.converters.MealTypeConverter;
import it.fooddiary.models.Food;
import it.fooddiary.models.Meal;
import it.fooddiary.utils.Constants;

@Database(entities = {Meal.class, Food.class},
        version = Constants.DATABASE_VERSION,
        exportSchema = false)
@TypeConverters({DateConverter.class, MealTypeConverter.class, FoodsListConverter.class})
public abstract class AppRoomDatabase extends RoomDatabase {

    private static volatile AppRoomDatabase instance;

    public abstract MealDao mealDao();

    public abstract RecentFoodDao recentFoodDao();

    public static AppRoomDatabase getDatabase(@NonNull @NotNull final Context context) {
        if (instance == null) {
            synchronized (AppRoomDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppRoomDatabase.class, Constants.DATABASE_NAME).build();
                }
            }
        }
        return instance;
    }
}