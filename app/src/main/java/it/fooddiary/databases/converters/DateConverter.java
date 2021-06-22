package it.fooddiary.databases.converters;

import androidx.room.TypeConverter;

import java.util.Calendar;
import java.util.Date;

/**
 * Convertor da Date a Long e da Long a Date, usato per
 * salvare un data in room db.
 */
public class DateConverter {

    @TypeConverter
    public static Date fromTimestamp(Long value) {
        if (value == null)
            return null;
        return new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        if (date == null)
            return null;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        /*
         * Per fare in modo una data consideri la sola parte relativa a giorno,
         * mese ed anno, resetto tutti gli altri valori.
         */
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR, 0);

        return cal.getTimeInMillis();
    }
}