package it.fooddiary.databases;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import it.fooddiary.databases.converters.DateConverter;
import it.fooddiary.databases.converters.FoodsListConverter;
import it.fooddiary.databases.converters.MealTypeConverter;
import it.fooddiary.models.Meal;
import it.fooddiary.utils.Constants;

@Database(entities = {Meal.class}, version = Constants.DATABASE_VERSION, exportSchema = false)
@TypeConverters({DateConverter.class, MealTypeConverter.class, FoodsListConverter.class})
public abstract class AppRoomDatabase extends RoomDatabase {

    private static volatile AppRoomDatabase instance;
    public abstract MealDao mealDao();

    public static AppRoomDatabase getDatabase(final Context context) {
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
