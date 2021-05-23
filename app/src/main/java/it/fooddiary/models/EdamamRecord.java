package it.fooddiary.models;

import android.os.Parcel;
import android.os.Parcelable;

public class EdamamRecord implements Parcelable {

    private EdamamResponseFood food;
    private double quantity;

    public EdamamRecord(EdamamResponseFood food, double quantity) {
        this.food = food;
        this.quantity = quantity;
    }

    public EdamamRecord() { }

    public EdamamResponseFood getFood() {
        return food;
    }

    public void setFood(EdamamResponseFood food) {
        this.food = food;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.food, flags);
        dest.writeDouble(this.quantity);
    }

    protected EdamamRecord(Parcel in) {
        this.food = in.readParcelable(EdamamResponseFood.class.getClassLoader());
        this.quantity = in.readDouble();
    }

    public static final Parcelable.Creator<EdamamRecord> CREATOR = new Parcelable.Creator<EdamamRecord>() {
        @Override
        public EdamamRecord createFromParcel(Parcel source) {
            return new EdamamRecord(source);
        }

        @Override
        public EdamamRecord[] newArray(int size) {
            return new EdamamRecord[size];
        }
    };
}
