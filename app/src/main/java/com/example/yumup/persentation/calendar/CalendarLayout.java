package com.example.yumup.persentation.calendar;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CalendarLayout extends ViewGroup {
    private Context context;
    public static final int DAYS_PER_WEEK = 7;
    public static final int WEEKS_PER_MONTH = 6;
    private DateEntity selectedDate;


    public CalendarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        Integer idx = 0;
        Integer dayWidth = getWidth() / DAYS_PER_WEEK;
        Integer dayHeight = Double.valueOf(((getHeight())) / WEEKS_PER_MONTH).intValue();

        for(int childIdx = 0; childIdx < ((ViewGroup) this).getChildCount(); childIdx ++ ) {
            View child = ((ViewGroup) this).getChildAt(childIdx);
            Integer left = (idx % DAYS_PER_WEEK) * dayWidth;
            Integer top = (idx / DAYS_PER_WEEK) * dayHeight;
            child.layout(
                    left,
                    top + convertDpToPx(2, getResources().getDisplayMetrics()),
                    left + dayWidth ,
                    top + dayHeight
            );
            idx++;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    public void initCalendar(String selectedMonth, List<DateEntity> list) {
        for(DateEntity date: list) {
            RecordDateView dateView;
            boolean isThisMonth;
            if(Objects.equals(selectedMonth, date.getMonth())) {
                isThisMonth = true;
            } else {
                isThisMonth = false;
            }
            dateView = new RecordDateView(context, null, date, isThisMonth);
            dateView.setId(date.hashCode());
            addView(dateView);
        }
    }

    public void setRecords(List<CalendarInfoEntity> calendarInfoEntityList) {
        ArrayList<RecordDateView> dateViewList = new ArrayList<>();
        for(int childIdx = 0; childIdx < ((ViewGroup) this).getChildCount(); childIdx ++ ) {
            View childView = getChildAt(childIdx);
            if(childView instanceof RecordDateView) {
                dateViewList.add((RecordDateView) childView);
                ((RecordDateView) childView).calendarData = null;
                ((RecordDateView) childView).requestLayout();
            }
        }

        for (RecordDateView dateView : dateViewList) {
            Optional<CalendarInfoEntity> data =  calendarInfoEntityList.stream().filter(calendarInfo -> {
               return ((RecordDateView) dateView).date.getFormattedDate()
                        .equals(calendarInfo.date.getFormattedDate());
            }).findFirst();
            data.ifPresent(calendarInfoEntity -> ((RecordDateView) dateView).changeRecordIdList(calendarInfoEntity));
        }
    }

    public void setDateClickListener(RecordDateView.DateClickListener dateClickListener) {
        for(int childIdx = 0; childIdx < ((ViewGroup) this).getChildCount(); childIdx ++ ) {
            ((RecordDateView) getChildAt(childIdx)).setOnDateClickListener(new RecordDateView.DateClickListener() {
                @Override
                public void onClickDate(DateEntity date) {
                    dateClickListener.onClickDate(date);
                }
            });
        }
    }

    static public Integer convertDpToPx(Integer dp, DisplayMetrics displayMetrics) {
        return  Math.round(dp * displayMetrics.density);
    }

    private void findSelectedDateView(DateEntity date) {
        for(int childIdx = 0; childIdx < ((ViewGroup) this).getChildCount(); childIdx ++ ) {
            RecordDateView dateView = (RecordDateView) getChildAt(childIdx);
            if(dateView.date == date) {
                dateView.selectedDate = true;
            } else {
                dateView.selectedDate = false;
            }
            dateView.invalidate();
        }
    }

    public void changeSelectedDate(DateEntity date) {
        selectedDate = date;
        findSelectedDateView(selectedDate);
    }
}
