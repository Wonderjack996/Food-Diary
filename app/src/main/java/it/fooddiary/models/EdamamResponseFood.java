package it.fooddiary.models;

import android.os.Parcel;
import android.os.Parcelable;

public class EdamamResponseFood implements Parcelable {

    private String foodId;
    private String label;
    private EdamamResponseNutrients nutrients;

    public EdamamResponseFood(String foodId, String label, EdamamResponseNutrients nutrients) {
        this.foodId = foodId;
        this.label = label;
        this.nutrients = nutrients;
    }

    public EdamamResponseFood() { }

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

    public EdamamResponseNutrients getNutrients() {
        return nutrients;
    }

    public void setNutrients(EdamamResponseNutrients nutrients) {
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

    protected EdamamResponseFood(Parcel in) {
        this.foodId = in.readString();
        this.label = in.readString();
        this.nutrients = in.readParcelable(EdamamResponseNutrients.class.getClassLoader());
    }

    public static final Parcelable.Creator<EdamamResponseFood> CREATOR = new Parcelable.Creator<EdamamResponseFood>() {
        @Override
        public EdamamResponseFood createFromParcel(Parcel source) {
            return new EdamamResponseFood(source);
        }

        @Override
        public EdamamResponseFood[] newArray(int size) {
            return new EdamamResponseFood[size];
        }
    };
}
