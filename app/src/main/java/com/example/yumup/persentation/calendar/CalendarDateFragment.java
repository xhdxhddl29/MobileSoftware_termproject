package com.example.yumup.persentation.calendar;

import static com.example.yumup.utils.Constants.BUNDLE_TIME_MILLS;
import static com.example.yumup.utils.Constants.koreanDateFormat;

import static java.util.stream.Collectors.groupingBy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.yumup.data.DietEntity;
import com.example.yumup.data.YumUpDatabase;
import com.example.yumup.databinding.FragmentCalendarDateBinding;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class CalendarDateFragment extends Fragment {
    private FragmentCalendarDateBinding binding;
    private CalendarDateViewModel viewModel;
    private SelectDateListener selectDateListener;
    private YumUpDatabase database;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCalendarDateBinding.inflate(inflater, container, false);
        database = YumUpDatabase.getInstance(requireContext());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        viewModel = new ViewModelProvider(this).get(CalendarDateViewModel.class);
        getMonthTimeMills();
        updateCalendarDateUI();
    }

    private void getMonthTimeMills() {
        if(getArguments() != null) {
            Long timeMills = getArguments().getLong(BUNDLE_TIME_MILLS);
            viewModel.changeSelectedMontTime(timeMills);
            getArguments().clear();
        }
        viewModel.selectedMonthId.observe(getViewLifecycleOwner(), new Observer<Long>() {
            @Override
            public void onChanged(Long aLong) {
                viewModel.getMonthDates();
            }
        });
    }

    private void updateCalendarDateUI() {
        viewModel.calendarDates.observe(getViewLifecycleOwner(), new Observer<ArrayList<DateEntity>>() {
            @Override
            public void onChanged(ArrayList<DateEntity> dateEntities) {
                binding.calendar.removeAllViewsInLayout();
                binding.calendar.initCalendar(viewModel.selectedMonth.getValue(), dateEntities);
                binding.calendar.setDateClickListener(new RecordDateView.DateClickListener() {
                    @Override
                    public void onClickDate(DateEntity date) {


                    }
                });
                getAllHistories();
            }
        });
    }


    private void getAllHistories() {
        DateEntity date = new DateEntity(new Date(viewModel.selectedMonthId.getValue()));
        List<CalendarInfoEntity> calendarInfoData = new ArrayList<>();
        Map<String, List<DietEntity>> dietsByDate = database.dietDao()
                .getDietsByMonth(date.getYearMonth())
                .stream()
                .collect(groupingBy( diet -> diet.mealDateTime));

        dietsByDate.values()
                .stream().map(it -> {
                    Date dietDate;
                    try {
                        dietDate = koreanDateFormat.parse(it.get(0).mealDateTime);
                        return new CalendarInfoEntity(new DateEntity(dietDate), it.size());
                    } catch (ParseException e) {
                        return null;
                    }
                }).forEach(it -> {
                    if(it != null) {calendarInfoData.add(it);}
                });

        binding.calendar.setRecords(calendarInfoData);
    }

    public CalendarDateFragment setDateClickListener(SelectDateListener selectDateListener) {
        this.selectDateListener = selectDateListener;
        return this;
    }

    public void changeSelectedDate(DateEntity date) {
        if(binding != null) {
            viewModel.changeSelectedDate(date);
            binding.calendar.changeSelectedDate(date);
        }
    }
}
