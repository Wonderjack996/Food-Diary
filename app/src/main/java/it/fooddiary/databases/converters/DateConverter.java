package it.fooddiary.databases.converters;

import androidx.room.TypeConverter;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import it.fooddiary.utils.DateUtils;

public class DateConverter {

    @TypeConverter
    public static Date fromTimestamp(String value) {
        if (value == null)
            return null;
        try {
            return DateUtils.dateFormat.parse(value);
        } catch (ParseException e) {
            return null;
        }
    }

    @TypeConverter
    public static String dateToTimestamp(Date date) {
        return date == null ? null : DateUtils.dateFormat.format(date);
    }
}
