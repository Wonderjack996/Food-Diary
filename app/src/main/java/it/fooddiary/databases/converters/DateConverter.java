package it.fooddiary.databases.converters;

import androidx.room.TypeConverter;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import it.fooddiary.utils.DateUtils;

public class DateConverter {

    @TypeConverter
    public static Date fromTimestamp(Long value) {
        if (value == null)
            return null;
        Date date = new Date(value);
        return date;
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        if (date == null)
            return null;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR, 0);
        return cal.getTimeInMillis();
    }
}
