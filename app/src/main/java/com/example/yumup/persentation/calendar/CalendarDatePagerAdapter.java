package com.example.yumup.persentation.calendar;

import static com.example.yumup.utils.Constants.BUNDLE_TIME_MILLS;
import static com.example.yumup.utils.Constants.START_POSITION;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.yumup.utils.CalendarUtil;

import java.util.ArrayList;
import java.util.Date;

public class CalendarDatePagerAdapter extends FragmentStateAdapter {
    private CalendarUtil calendarUtil;
    private Long initialTimeMills;
    private ArrayList<CalendarDateFragment> calendarFragments = new ArrayList<>();
    private ArrayList<Long> items = new ArrayList<>();
    private RecordDateView.DateClickListener dateClickListener;
    private Integer themeColor;



    public CalendarDatePagerAdapter(@NonNull Fragment fragment, CalendarUtil calendarUtil, Integer themeColor, RecordDateView.DateClickListener dateClickListener) {
        super(fragment);
        this.calendarUtil = calendarUtil;
        this.dateClickListener = dateClickListener;
        this.themeColor = themeColor;
        initialTimeMills = calendarUtil.getFirstDateOfCurrentMonth(new Date().getTime());
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        CalendarDateFragment calendarDateFragment = new CalendarDateFragment();
        calendarDateFragment.setDateClickListener(new SelectDateListener() {
            @Override
            public void selectDate(DateEntity dateEntity) {
                dateClickListener.onClickDate(dateEntity);
                calendarFragments.forEach(it -> {
                    it.changeSelectedDate(dateEntity);
                });
            }
        });
        Bundle bundle = new Bundle();
        bundle.putLong(BUNDLE_TIME_MILLS, getItemId(position));
        calendarDateFragment.setArguments(bundle);
        calendarFragments.add(calendarDateFragment);
        return calendarDateFragment;
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public long getItemId(int position) {
        items.add(calendarUtil.changeMonth(initialTimeMills, position - START_POSITION));
        return calendarUtil.changeMonth(initialTimeMills, position - START_POSITION);
    }

    @Override
    public boolean containsItem(long itemId) {
        return items.contains(itemId);
    }
}
