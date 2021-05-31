package it.fooddiary.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import it.fooddiary.utils.MealType;

@Entity(tableName = "meals")
public class Meal implements IFoodProperties, Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "meal_type")
    private final MealType mealType;

    @ColumnInfo(name = "meal_date")
    private final Date mealDate;

    @ColumnInfo(name = "meal_foods")
    private List<Food> mealFoods;

    public Meal(MealType mealType, Date mealDate) {
        this.mealType = mealType;
        this.mealDate = mealDate;
        this.mealFoods = new ArrayList<Food>();
    }

    public boolean addFood(Food food) {
        return mealFoods.add(food);
    }

    public boolean removeFood(Food food) {
        return mealFoods.remove(food);
    }

    @Override
    public int getTotalCalories() {
        int total = 0;
        for (Food x : mealFoods)
            total += x.getTotalCalories();
        return total;
    }

    @Override
    public int getTotalCarbsGrams() {
        int total = 0;
        for (Food x : mealFoods)
            total += x.getTotalCarbsGrams();
        return total;
    }

    @Override
    public int getTotalProteinsGrams() {
        int total = 0;
        for (Food x : mealFoods)
            total += x.getTotalProteinsGrams();
        return total;
    }

    @Override
    public int getTotalFatsGrams() {
        int total = 0;
        for (Food x : mealFoods)
            total += x.getTotalFatsGrams();
        return total;
    }

    public MealType getMealType() {
        return mealType;
    }

    public Date getMealDate() {
        return mealDate;
    }

    public List<Food> getMealFoods() {
        return mealFoods;
    }

    public void setMealFoods(List<Food> mealFoods) {
        this.mealFoods = mealFoods;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.mealType == null ? -1 : this.mealType.ordinal());
        dest.writeLong(this.mealDate != null ? this.mealDate.getTime() : -1);
        dest.writeTypedList(this.mealFoods);
    }

    protected Meal(Parcel in) {
        this.id = in.readInt();
        int tmpMealType = in.readInt();
        this.mealType = tmpMealType == -1 ? null : MealType.values()[tmpMealType];
        long tmpMealDate = in.readLong();
        this.mealDate = tmpMealDate == -1 ? null : new Date(tmpMealDate);
        this.mealFoods = in.createTypedArrayList(Food.CREATOR);
    }

    public static final Parcelable.Creator<Meal> CREATOR = new Parcelable.Creator<Meal>() {
        @Override
        public Meal createFromParcel(Parcel source) {
            return new Meal(source);
        }

        @Override
        public Meal[] newArray(int size) {
            return new Meal[size];
        }
    };
}
