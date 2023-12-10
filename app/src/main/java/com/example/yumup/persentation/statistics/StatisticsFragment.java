package com.example.yumup.persentation.statistics;

import static com.example.yumup.data.MealPlace.findColorUsingPlace;
import static com.example.yumup.data.MealType.findMealType;
import static com.example.yumup.data.MealType.findMealTypeColorUsingType;
import static com.example.yumup.persentation.calendar.CalendarLayout.DAYS_PER_WEEK;
import static com.example.yumup.persentation.calendar.CalendarLayout.convertDpToPx;
import static com.example.yumup.utils.Constants.START_POSITION;
import static java.util.stream.Collectors.groupingBy;

import android.os.Bundle;
import android.text.style.TypefaceSpan;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.yumup.R;
import com.example.yumup.data.DietEntity;
import com.example.yumup.data.YumUpDatabase;
import com.example.yumup.databinding.FragmentStatisticsBinding;
import com.example.yumup.persentation.calendar.CalendarDatePagerAdapter;
import com.example.yumup.persentation.calendar.DateEntity;
import com.example.yumup.persentation.calendar.RecordDateView;
import com.example.yumup.utils.CalendarUtil;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class StatisticsFragment extends Fragment {
    private FragmentStatisticsBinding binding;
    private CalendarUtil calendarUtil = new CalendarUtil();
    private CalendarDatePagerAdapter datesPagerAdapter;
    private StatisticsViewModel viewModel;
    private YumUpDatabase database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentStatisticsBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(StatisticsViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        database = YumUpDatabase.getInstance(requireContext());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setWeekDays();
        setDatesViewPager();
        changeMonth();
        updateMonthlyDiets();
    }

    private void setWeekDays() {
        DisplayMetrics dp = new DisplayMetrics();
        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(dp);

        ArrayList<String> days = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.days)));
        Integer dayWidth = (dp.widthPixels - convertDpToPx(40, dp)) / DAYS_PER_WEEK;
        for(int i = 0; i<7; i++) {
            TextView dayView = new TextView(requireContext());
            dayView.setText(days.get(i));
            dayView.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
            dayView.setTextSize(convertDpToPx(6, dp));
            dayView.setWidth(dayWidth);
            dayView.setHeight(convertDpToPx(24, dp));
            dayView.setGravity(Gravity.CENTER);
            binding.linearlayoutDays.addView(dayView);
        }
        binding.linearlayoutDays.invalidate();
    }

    private void setDatesViewPager() {
        datesPagerAdapter = new CalendarDatePagerAdapter(
                this,
                calendarUtil,
                R.color.orange,
                new RecordDateView.DateClickListener() {
                    @Override
                    public void onClickDate(DateEntity date) {
                        viewModel.changeSelectedDate(date);
                    }
                }
        );

        binding.viewpagerCalendar.setAdapter(datesPagerAdapter);
        binding.viewpagerCalendar.setCurrentItem(START_POSITION, false);
        binding.viewpagerCalendar.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Long timeMills = datesPagerAdapter.getItemId(position);
                viewModel.changeCalendarPageDate(timeMills);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
        viewModel.changeCalendarPageDate(datesPagerAdapter.getItemId(START_POSITION));
    }

    private void changeMonth() {
        binding.imageviewNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.viewpagerCalendar.setCurrentItem(
                        binding.viewpagerCalendar.getCurrentItem() + 1
                );
            }
        });

        binding.imageviewPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.viewpagerCalendar.setCurrentItem(
                        binding.viewpagerCalendar.getCurrentItem() - 1
                );
            }
        });

        viewModel.calendarPageDate.observe(getViewLifecycleOwner(), new Observer<DateEntity>() {
            @Override
            public void onChanged(DateEntity dateEntity) {
                viewModel.changeDietList(database.dietDao().getDietsByMonth(dateEntity.getYearMonth()));
            }
        });
    }

    private void updateMonthlyDiets() {
        viewModel.dietList.observe(getViewLifecycleOwner(), new Observer<ArrayList<DietEntity>>() {
            @Override
            public void onChanged(ArrayList<DietEntity> dietEntities) {
                setMealTimePieChart();
                setMealPlacePieChart();
            }
        });
    }

    private void setMealTimePieChart() {
        ArrayList<DietEntity> diets = viewModel.dietList.getValue();
        ArrayList<PieEntry> pieEntries = new ArrayList<>();

        PieDataSet pieDataSet;

        if(diets!= null && !diets.isEmpty()) {
            Integer dietsCount = diets.size();
            Map<String, List<DietEntity>> countByMealType = diets.stream()
                    .collect(groupingBy(diet -> (diet.mealType)));

            for(Map.Entry<String, List<DietEntity>> entry : countByMealType.entrySet()) {
                pieEntries.add(new PieEntry((((float) entry.getValue().size())/ dietsCount) * 100, findMealType(entry.getKey())));
            }

            pieDataSet = new PieDataSet(pieEntries, "");
            ArrayList<Integer> pieColors = new ArrayList<>();
            pieEntries.forEach(pieEntry -> {
                Integer pieColor = ContextCompat.getColor(requireContext(), findMealTypeColorUsingType(pieEntry.getLabel()));
                pieColors.add(pieColor);
            });
            pieDataSet.setColors(pieColors);
            pieDataSet.setValueTextSize(10f);
            drawPieChart(pieDataSet, binding.piechartTime, false);
        } else {
            pieEntries.add(new PieEntry(100, ""));
            pieDataSet = new PieDataSet(pieEntries, "");
            pieDataSet.setColors(ContextCompat.getColor(requireContext(), R.color.green));
            drawPieChart(pieDataSet, binding.piechartTime, true);
        }
    }

    public void setMealPlacePieChart() {
        ArrayList<DietEntity> diets = viewModel.dietList.getValue();
        ArrayList<PieEntry> pieEntries = new ArrayList<>();

        PieDataSet pieDataSet;

        if(diets!= null && !diets.isEmpty()) {
            Integer dietsCount = diets.size();
            Map<String, List<DietEntity>> countByMealPlace = diets.stream()
                    .collect(groupingBy(diet -> (diet.place)));

            for(Map.Entry<String, List<DietEntity>> entry : countByMealPlace.entrySet()) {
                String label = "";
                pieEntries.add(new PieEntry((((float) entry.getValue().size())/ dietsCount) * 100, entry.getKey()));
            }

            pieDataSet = new PieDataSet(pieEntries, "");
            ArrayList<Integer> pieColors = new ArrayList<>();
            pieEntries.forEach(pieEntry -> {
                Integer pieColor = ContextCompat.getColor(requireContext(), findColorUsingPlace(pieEntry.getLabel(), requireContext()));
                pieColors.add(pieColor);
            });
            pieDataSet.setColors(pieColors);
            pieDataSet.setValueTextSize(10f);
            drawPieChart(pieDataSet, binding.piechartPlace, false);
        } else {
            pieEntries.add(new PieEntry(100, ""));
            pieDataSet = new PieDataSet(pieEntries, "");
            pieDataSet.setColors(ContextCompat.getColor(requireContext(), R.color.green));
            drawPieChart(pieDataSet, binding.piechartPlace, true);
        }
    }

    private void drawPieChart(PieDataSet pieDataSet, PieChart chart, Boolean isEmpty) {
        if(isEmpty) {
            pieDataSet.setDrawValues(false);
            chart.setDrawEntryLabels(false);
        } else {
            pieDataSet.setDrawValues(true);
            chart.setDrawEntryLabels(true);
        }
        pieDataSet.setValueTypeface(ResourcesCompat.getFont(requireContext(), R.font.pyeongchang_regular));
        pieDataSet.setValueTextColor(ContextCompat.getColor(requireContext(), R.color.black));
        chart.setData(new PieData(pieDataSet));
        chart.getDescription().setEnabled(false);
        chart.getLegend().setEnabled(false);
        chart.setRotationEnabled(false);
        chart.setEntryLabelColor(ContextCompat.getColor(requireContext(), R.color.black));
        chart.setEntryLabelTypeface(ResourcesCompat.getFont(requireContext(), R.font.pyeongchang_regular));
        chart.setEntryLabelTextSize(8f);
        chart.animateY(0, Easing.EaseInOutQuad);
        chart.animate();
    }
}