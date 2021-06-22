package it.fooddiary.models.edamam_models;

import android.os.Parcel;

import android.os.Parcelable;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Classe utilizzata per wrappare una risposta ricevuta da edamam api.
 */
public class EdamamResponse implements Parcelable {

    private String status;
    private String message;
    private String text;
    private List<EdamamRecord> hints;
    private List<EdamamRecord> parsed;

    public EdamamResponse(String status,
                          @NonNull @NotNull String message,
                          String text,
                          List<EdamamRecord> hints,
                          List<EdamamRecord> parsed) {
        this.status = status;
        this.message = message;
        this.text = text;
        this.hints = hints;
        this.parsed = parsed;
    }

    public EdamamResponse() { }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

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
        dest.writeString(this.status);
        dest.writeString(this.message);
        dest.writeString(this.text);
        dest.writeTypedList(this.hints);
        dest.writeTypedList(this.parsed);
    }

    protected EdamamResponse(Parcel in) {
        this.status = in.readString();
        this.message = in.readString();
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