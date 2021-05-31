package it.fooddiary.databases;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.Date;
import java.util.List;

import it.fooddiary.models.Food;
import it.fooddiary.models.Meal;
import it.fooddiary.utils.MealType;

@Dao
public interface RecentFoodDao {

    @Insert
    void insert(Food food);

    @Delete
    void delete(Food food);

    @Update
    void update(Food food);

    @Query("SELECT * FROM recent_food")
    List<Food> getAll();



}