package it.fooddiary.databases;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import it.fooddiary.models.Food;

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

    @Query("DELETE FROM recent_food WHERE food_name = :foodToRemove")
    void deleteFood(String foodToRemove);

    @Query("DELETE FROM recent_food WHERE primaryKey IN (SELECT MIN(primaryKey) FROM recent_food)")
    void deleteOlder();
}