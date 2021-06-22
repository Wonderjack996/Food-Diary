package it.fooddiary.utils;

import android.content.res.Resources;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import it.fooddiary.R;

public enum MealType {
    
    BREAKFAST,
    LUNCH,
    DINNER,
    SNACKS;

    @NonNull
    @NotNull
    public String toString(@NonNull @NotNull Resources res) {
        switch(this) {
            case BREAKFAST:
                return res.getString(R.string.breakfast);
            case LUNCH:
                return res.getString(R.string.lunch);
            case DINNER:
                return res.getString(R.string.dinner);
            case SNACKS:
                return res.getString(R.string.snacks);
        }
        return "";
    }
}