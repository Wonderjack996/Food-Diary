package it.fooddiary.models.edamam_models;

import android.os.Parcel;
import android.os.Parcelable;

import it.fooddiary.models.Food;

public class EdamamFood implements Parcelable {

    private String foodId;
    private String label;
    private EdamamNutrients nutrients;

    public EdamamFood(String foodId, String label, EdamamNutrients nutrients) {
        this.foodId = foodId;
        this.label = label;
        this.nutrients = nutrients;
    }

    public EdamamFood() { }

    public Food toFood() {
        String name = this.label;
        String id = this.foodId;
        int quantity = 100;
        double carbsPerc = this.nutrients.getCHOCDF()/quantity;
        double prosPerc = this.nutrients.getPROCNT()/quantity;
        double fatsPerc = this.nutrients.getFAT()/quantity;
        return new Food(name, id, quantity, carbsPerc, prosPerc, fatsPerc);
    }

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public EdamamNutrients getNutrients() {
        return nutrients;
    }

    public void setNutrients(EdamamNutrients nutrients) {
        this.nutrients = nutrients;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.foodId);
        dest.writeString(this.label);
        dest.writeParcelable(this.nutrients, flags);
    }

    protected EdamamFood(Parcel in) {
        this.foodId = in.readString();
        this.label = in.readString();
        this.nutrients = in.readParcelable(EdamamNutrients.class.getClassLoader());
    }

    public static final Parcelable.Creator<EdamamFood> CREATOR = new Parcelable.Creator<EdamamFood>() {
        @Override
        public EdamamFood createFromParcel(Parcel source) {
            return new EdamamFood(source);
        }

        @Override
        public EdamamFood[] newArray(int size) {
            return new EdamamFood[size];
        }
    };
}
