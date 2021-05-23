package it.fooddiary.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class EdamamResponse implements Parcelable {

    private String text;
    private List<EdamamRecord> hints;
    private List<EdamamRecord> parsed;

    public EdamamResponse(String text, List<EdamamRecord> hints, List<EdamamRecord> parsed) {
        this.text = text;
        this.hints = hints;
        this.parsed = parsed;
    }

    public EdamamResponse() { }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<EdamamRecord> getHints() {
        return hints;
    }

    public void setHints(List<EdamamRecord> hints) {
        this.hints = hints;
    }

    public List<EdamamRecord> getParsed() {
        return parsed;
    }

    public void setParsed(List<EdamamRecord> parsed) {
        this.parsed = parsed;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.text);
        dest.writeTypedList(this.hints);
        dest.writeTypedList(this.parsed);
    }

    protected EdamamResponse(Parcel in) {
        this.text = in.readString();
        this.hints = in.createTypedArrayList(EdamamRecord.CREATOR);
        this.parsed = in.createTypedArrayList(EdamamRecord.CREATOR);
    }

    public static final Creator<EdamamResponse> CREATOR = new Creator<EdamamResponse>() {
        @Override
        public EdamamResponse createFromParcel(Parcel source) {
            return new EdamamResponse(source);
        }

        @Override
        public EdamamResponse[] newArray(int size) {
            return new EdamamResponse[size];
        }
    };
}
