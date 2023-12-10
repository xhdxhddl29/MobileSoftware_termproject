package com.example.yumup.utils;

import static java.util.Calendar.DAY_OF_WEEK;

import com.example.yumup.persentation.calendar.DateEntity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CalendarUtil {
    private Calendar calendar = Calendar.getInstance(Locale.KOREA);

    public DateEntity getTodayDate() {
        return new DateEntity(new Date());
    }

    public Long getFirstDateOfCurrentMonth(Long date) {
        calendar.setTime(new Date(date));
        calendar.set(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.getActualMinimum(Calendar.DAY_OF_MONTH)
        );
        return calendar.getTime().getTime();
    }

    public Long changeMonth(Long startDate, Integer plusMonth) {
        calendar.setTime(new Date(startDate));
        calendar.add(Calendar.MONTH, plusMonth);
        return calendar.getTime().getTime();
    }

    public ArrayList<DateEntity> getCalendarDates(Calendar calendar, Long time) {
        ArrayList<DateEntity> calendarDates = new ArrayList<DateEntity>();
        calendar.setTime(new Date(getFirstDateOfCurrentMonth(time)));
        calendarDates.addAll(getPreviousMonthDates(calendar));
        calendarDates.addAll(getCurrentMonthDates(calendar));
        calendarDates.addAll(getNextMonthDates(calendar));
        return calendarDates;
    }

    public ArrayList<DateEntity> getPreviousMonthDates(Calendar calendar) {
        ArrayList<DateEntity> calendarDateList = new ArrayList<DateEntity>();
        Integer firstDay = calendar.get(DAY_OF_WEEK);
        calendar.add(Calendar.DATE, -(firstDay-2));
        calendarDateList.add(new DateEntity(calendar.getTime()));

        for(int i=0; i < firstDay-2; i++) {
            calendar.add(Calendar.DATE, 1);
            calendarDateList.add(new DateEntity(calendar.getTime()));
        }
        return calendarDateList;
    }

    public ArrayList<DateEntity> getCurrentMonthDates(Calendar calendar) {
        ArrayList<DateEntity> calendarDateList = new ArrayList<DateEntity>();
        Integer firstDate = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
        Integer lastDate = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        for(int i=0; i < lastDate - firstDate + 2; i++) {
            calendar.add(Calendar.DATE, 1);
            calendarDateList.add(new DateEntity(calendar.getTime()));
        }
        return calendarDateList;
    }

    public ArrayList<DateEntity> getNextMonthDates(Calendar calendar) {
        ArrayList<DateEntity> calendarDateList = new ArrayList<DateEntity>();
        Integer lastDay = calendar.get(DAY_OF_WEEK);
        Integer nextMonthDateCount;
        if(lastDay == 0) { nextMonthDateCount = 0; } else {nextMonthDateCount = 7 - (lastDay);}

        if(lastDay != 0) {
            for(int i=0; i < nextMonthDateCount+1; i++) {
                calendar.add(Calendar.DATE, 1);
                calendarDateList.add(new DateEntity(calendar.getTime()));
            }
        }

        return calendarDateList;
    }
}
