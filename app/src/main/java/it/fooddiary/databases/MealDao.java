package it.fooddiary.databases;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.Date;
import java.util.List;

import it.fooddiary.models.Meal;
import it.fooddiary.utils.MealType;

@Dao
public interface MealDao {

    @Insert
    void insert(Meal meal);

    @Delete
    void delete(Meal meal);

    @Update
    void update(Meal meal);

    @Query("SELECT * FROM meals WHERE meal_type = :mealType AND meal_date = :date")
    List<Meal> getMealsByDateAndType(MealType mealType, Date date);

    @Query("SELECT * FROM meals WHERE meal_type = :mealType")
    List<Meal> getMealsByType(MealType mealType);
}
