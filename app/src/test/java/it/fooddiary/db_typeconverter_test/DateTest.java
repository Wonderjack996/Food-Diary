package it.fooddiary.db_typeconverter_test;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import it.fooddiary.databases.converters.DateConverter;
import it.fooddiary.utils.DateUtils;

import static org.junit.Assert.*;

public class DateTest {

    @Test
    public void fromTimestampTest() {
        assertNull(DateConverter.fromTimestamp(null));

        Date currentDate = Calendar.getInstance().getTime();
        Date myCurrentDate = DateConverter.fromTimestamp(currentDate.getTime());
        assertEquals(currentDate, myCurrentDate);
    }

    @Test
    public void toTimestampTest() {
        assertNull(DateConverter.dateToTimestamp(null));
        assertFalse(DateUtils.dateEquals(Calendar.getInstance().getTime(), null));
        assertFalse(DateUtils.dateEquals(Calendar.getInstance().getTime(), null));
        assertFalse(DateUtils.dateEquals(null, null));

        Date currentDate = Calendar.getInstance().getTime();
        Long longCurrentDate = currentDate.getTime();
        Date myCurrDate = DateConverter.fromTimestamp(longCurrentDate);
        assertTrue(DateUtils.dateEquals(currentDate, myCurrDate));
    }
}