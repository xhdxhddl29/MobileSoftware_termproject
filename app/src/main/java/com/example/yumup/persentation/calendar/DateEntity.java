package com.example.yumup.persentation.calendar;

import static com.example.yumup.utils.Constants.dateFormat;
import static com.example.yumup.utils.Constants.yearMonthFormat;

import java.util.Calendar;
import java.util.Date;

public class DateEntity {
    private Calendar cal;

    private Date date;
    public Date getDate() {return date;}

    private String formattedDate;
    public String getFormattedDate() {return dateFormat.format(getDate());}

    private String yearMonth;
    public String getYearMonth() {return yearMonthFormat.format(getDate());}

    private String month;
    public String getMonth() {
        cal.setTime(getDate());
        return String.valueOf(cal.get(Calendar.MONTH) + 1);
    }

    private String calendarDate;
    public String getCalendarDate() {
        cal.setTime(getDate());
        return String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
    }

    public DateEntity(Date date) {
        this.date = date;
        cal = Calendar.getInstance();
    }
}
