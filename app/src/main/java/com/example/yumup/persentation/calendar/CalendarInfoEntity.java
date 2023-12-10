package com.example.yumup.persentation.calendar;

public class CalendarInfoEntity {
    public DateEntity date;
    public Integer dietCount;


    public CalendarInfoEntity(DateEntity date, Integer dietCount) {
        this.date = date;
        this.dietCount = dietCount;
    }
}
