package com.example.yumup.persentation.calendar;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.yumup.utils.CalendarUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CalendarDateViewModel extends ViewModel {
    private CalendarUtil calendarUtil = new CalendarUtil();
    private Calendar calendar = Calendar.getInstance();

    private final MutableLiveData<Long> _selectedMonthId = new MutableLiveData<>(null);
    public LiveData<Long> selectedMonthId = _selectedMonthId;

    private final MutableLiveData<String> _selectedMonth = new MutableLiveData<>("");
    public LiveData<String> selectedMonth = _selectedMonth;

    private final MutableLiveData<DateEntity> _selectedDate = new MutableLiveData<>(new DateEntity(new Date()));
    public LiveData<DateEntity> selectedDate = _selectedDate;

    private final MutableLiveData<ArrayList<DateEntity>> _calendarDates = new MutableLiveData<>();
    public LiveData<ArrayList<DateEntity>> calendarDates = _calendarDates;

    public void changeSelectedMontTime(Long id) {
        _selectedMonthId.setValue(id);
        _selectedMonth.setValue(new DateEntity(new Date(id)).getMonth());
    }

    public void getMonthDates() {
        _calendarDates.setValue(calendarUtil.getCalendarDates(calendar, selectedMonthId.getValue()));
    }

    public void changeSelectedDate(DateEntity dateEntity) {
        _selectedDate.setValue(dateEntity);
    }
}
