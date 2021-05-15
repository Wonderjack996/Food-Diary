package it.fooddiary.util;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    public static final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);

    public static boolean dateEquals(Date d1, Date d2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(d1);
        cal2.setTime(d2);
        return cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH) &&
                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
    }
}
