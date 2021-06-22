package it.fooddiary.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import it.fooddiary.utils.Constants;

@Entity(tableName = "recent_food")
public class Food implements IFoodProperties, Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int primaryKey;

    @ColumnInfo(name = "food_name")
    private final String name;

    @ColumnInfo(name = "food_carbs")
    private final double carbsPercent;

    @ColumnInfo(name = "food_protein")
    private final double proteinsPercent;

    @ColumnInfo(name = "food_fat")
    private final double fatsPercent;

    @ColumnInfo(name = "food_quantity")
    private int quantity = 0;

    public Food(@NonNull @NotNull String name,
                int quantity,
                double carbsPercent,
                double proteinsPercent,
                double fatsPercent) {
        this.name = name;

        if (quantity < Constants.MIN_FOOD_GRAMS)
            this.quantity = Constants.MIN_FOOD_GRAMS;
        else
            this.quantity = Math.min(quantity, Constants.MAX_FOOD_GRAMS);

        if (carbsPercent >= 0 && carbsPercent <= 100)
            this.carbsPercent = carbsPercent;
        else
            this.carbsPercent = 0;

        if (proteinsPercent >= 0 && proteinsPercent <= 100)
            this.proteinsPercent = proteinsPercent;
        else
            this.proteinsPercent = 0;

        if (fatsPercent >= 0 && fatsPercent <= 100)
            this.fatsPercent = fatsPercent;
        else
            this.fatsPercent = 0;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public int getTotalCarbsGrams() {
        return (int)(quantity*carbsPercent);
    }

    @Override
    public int getTotalProteinsGrams() {
        return (int)(quantity*proteinsPercent);
    }

    @Override
    public int getTotalFatsGrams() {
        return (int)(quantity*fatsPercent);
    }

    @Override
    public int getTotalCalories() {
        return getTotalCarbsGrams()*Constants.CALORIES_PER_CARB_GRAM +
                getTotalProteinsGrams()*Constants.CALORIES_PER_PROTEIN_GRAM +
                getTotalFatsGrams()*Constants.CALORIES_PER_FAT_GRAM;
    }

    public int getPrimaryKey() {
        return primaryKey;
    }

    public double getCarbsPercent() {
        return carbsPercent;
    }

    public double getProteinsPercent() {
        return proteinsPercent;
    }

    public double getFatsPercent() {
        return fatsPercent;
    }

    public void setPrimaryKey(int primaryKey) {
        this.primaryKey = primaryKey;
    }

    public void setQuantity(int newQuantity) {
        if (newQuantity < Constants.MIN_FOOD_GRAMS)
            this.quantity = Constants.MIN_FOOD_GRAMS;
        else
            this.quantity = Math.min(newQuantity, Constants.MAX_FOOD_GRAMS);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Food food = (Food) o;
        return name.toUpperCase().equals(food.name.toUpperCase());
    }

    @Override
    public int hashCode() {
        return name.toUpperCase().hashCode();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.primaryKey);
        dest.writeString(this.name);
        dest.writeDouble(this.carbsPercent);
        dest.writeDouble(this.proteinsPercent);
        dest.writeDouble(this.fatsPercent);
        dest.writeInt(this.quantity);
    }

    protected Food(Parcel in) {
        this.primaryKey = in.readInt();
        this.name = in.readString();
        this.carbsPercent = in.readDouble();
        this.proteinsPercent = in.readDouble();
        this.fatsPercent = in.readDouble();
        this.quantity = in.readInt();
    }

    public static final Creator<Food> CREATOR = new Creator<Food>() {
        @Override
        public Food createFromParcel(Parcel source) {
            return new Food(source);
        }

        @Override
        public Food[] newArray(int size) {
            return new Food[size];
        }
    };
}