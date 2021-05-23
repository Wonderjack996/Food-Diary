package it.fooddiary.models;

import android.os.Parcel;
import android.os.Parcelable;

public class EdamamResponseNutrients implements Parcelable {

    private double ENERC_KCAL;
    private double PROCNT;
    private double FAT;
    private double CHOCDF;

    public EdamamResponseNutrients(double ENERC_KCAL, double PROCNT, double FAT, double CHOCDF) {
        this.ENERC_KCAL = ENERC_KCAL;
        this.PROCNT = PROCNT;
        this.FAT = FAT;
        this.CHOCDF = CHOCDF;
    }

    public EdamamResponseNutrients() { }

    public double getENERC_KCAL() {
        return ENERC_KCAL;
    }

    public void setENERC_KCAL(double ENERC_KCAL) {
        this.ENERC_KCAL = ENERC_KCAL;
    }

    public double getPROCNT() {
        return PROCNT;
    }

    public void setPROCNT(double PROCNT) {
        this.PROCNT = PROCNT;
    }

    public double getFAT() {
        return FAT;
    }

    public void setFAT(double FAT) {
        this.FAT = FAT;
    }

    public double getCHOCDF() {
        return CHOCDF;
    }

    public void setCHOCDF(double CHOCDF) {
        this.CHOCDF = CHOCDF;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.ENERC_KCAL);
        dest.writeDouble(this.PROCNT);
        dest.writeDouble(this.FAT);
        dest.writeDouble(this.CHOCDF);
    }

    protected EdamamResponseNutrients(Parcel in) {
        this.ENERC_KCAL = in.readDouble();
        this.PROCNT = in.readDouble();
        this.FAT = in.readDouble();
        this.CHOCDF = in.readDouble();
    }

    public static final Creator<EdamamResponseNutrients> CREATOR = new Creator<EdamamResponseNutrients>() {
        @Override
        public EdamamResponseNutrients createFromParcel(Parcel source) {
            return new EdamamResponseNutrients(source);
        }

        @Override
        public EdamamResponseNutrients[] newArray(int size) {
            return new EdamamResponseNutrients[size];
        }
    };
}
