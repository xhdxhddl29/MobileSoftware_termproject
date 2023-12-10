package com.example.yumup.persentation.statistics;

import static java.util.stream.Collectors.groupingBy;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.yumup.data.DietEntity;
import com.example.yumup.data.MealType;
import com.example.yumup.persentation.calendar.DateEntity;
import com.example.yumup.utils.CalendarUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

public class StatisticsViewModel extends ViewModel {
    private CalendarUtil calendarUtil = new CalendarUtil();
    private MutableLiveData<DateEntity> _selectedDate = new MutableLiveData<>(new DateEntity(new Date()));
    public LiveData<DateEntity> selectedDate = _selectedDate;

    private MutableLiveData<DateEntity> _calendarPageDate = new MutableLiveData<>(new DateEntity(new Date()));
    public LiveData<DateEntity> calendarPageDate = _calendarPageDate;

    private MutableLiveData<ArrayList<DietEntity>> _dietList = new MutableLiveData<>(new ArrayList<>());
    public LiveData<ArrayList<DietEntity>> dietList = _dietList;

    private MutableLiveData<Float> _averageDailyCalories = new MutableLiveData<>();
    public LiveData<Float> averageDailyCalories = _averageDailyCalories;

    private MutableLiveData<Float> _totalMonthlyCalories = new MutableLiveData<>();
    public LiveData<Float> totalMonthlyCalories = _totalMonthlyCalories;

    private MutableLiveData<Integer> _monthlyTotalCost = new MutableLiveData<>();
    public LiveData<Integer> monthlyTotalCost = _monthlyTotalCost;

    private MutableLiveData<Integer> _averageDailyCost = new MutableLiveData<>();
    public LiveData<Integer> averageDailyCost = _averageDailyCost;

    private MutableLiveData<Integer> _totalBreakfastCost = new MutableLiveData<>(0);
    public LiveData<Integer> totalBreakfastCost = _totalBreakfastCost;

    private MutableLiveData<Integer> _totalLunchCost = new MutableLiveData<>(0);
    public LiveData<Integer> totalLunchCost = _totalLunchCost;

    private MutableLiveData<Integer> _totalDinnerCost = new MutableLiveData<>(0);
    public LiveData<Integer> totalDinnerCost = _totalDinnerCost;

    private MutableLiveData<Integer> _totalSnackCost = new MutableLiveData<>(0);
    public LiveData<Integer> totalSnackCost = _totalSnackCost;

    public void changeCalendarPageDate(Long dateTime) {
        _calendarPageDate.setValue(new DateEntity(new Date(dateTime)));
    }

    public void changeSelectedDate(DateEntity date) {
        _selectedDate.setValue(date);
    }

    public void changeDietList(List<DietEntity> monthlyDiets) {
        _dietList.setValue(new ArrayList<>(monthlyDiets));
        calculateDailyAverageCalories(monthlyDiets);
        calculateCost(monthlyDiets);
        calculateCostByMealType(monthlyDiets);
    }

    public void calculateDailyAverageCalories(List<DietEntity> monthlyDiets) {
        Float acc = 0f;
        Map<String, List<DietEntity>> dietListByDate = monthlyDiets.stream()
                .collect(groupingBy(diet -> diet.mealDateTime));

        for(Map.Entry<String, List<DietEntity>> entry : dietListByDate.entrySet()) {
            for (DietEntity diet: entry.getValue()) {
                acc += diet.calories;
            }
        }

        if(monthlyDiets.isEmpty()) {
            _totalMonthlyCalories.setValue(0f);
            _averageDailyCalories.setValue(0f);
        } else {
            _totalMonthlyCalories.setValue(acc);
            _averageDailyCalories.setValue(acc/dietListByDate.keySet().size());
        }
    }

    private void calculateCost(List<DietEntity> monthlyDiets) {
        AtomicReference<Float> acc = new AtomicReference<>(0f);
        Map<String, List<DietEntity>> dietListByDate = monthlyDiets.stream()
                .collect(groupingBy(diet -> diet.mealDateTime));

        monthlyDiets.forEach(it -> {
            acc.updateAndGet(v -> v + Float.valueOf(it.cost));
        });
        if(monthlyDiets.isEmpty()) {
            _monthlyTotalCost.setValue(0);
            _averageDailyCost.setValue(0);
        } else {
            _monthlyTotalCost.setValue(Math.round(acc.get()) );
            _averageDailyCost.setValue(Math.round(acc.get() / dietListByDate.keySet().size()));
        }
    }

    private void calculateCostByMealType(List<DietEntity> monthlyDiets) {
        Map<String, List<DietEntity>> dietListByMealType = monthlyDiets.stream()
                .collect(groupingBy(diet -> diet.mealType));

        for (Map.Entry<String, List<DietEntity>> entry : dietListByMealType.entrySet()) {
            AtomicReference<Integer> costSum = new AtomicReference<>(0);

            entry.getValue()
                    .stream()
                    .map(it -> Integer.parseInt(it.cost))
                    .forEach(it -> {
                        costSum.updateAndGet(v -> v + it);
                    });

            if(entry.getKey().equals(MealType.BREAKFAST.name())) {
                _totalBreakfastCost.setValue(costSum.get());
            } else if(entry.getKey().equals(MealType.LUNCH.name())) {
                _totalLunchCost.setValue(costSum.get());
            }  else if(entry.getKey().equals(MealType.DINNER.name())) {
                _totalDinnerCost.setValue(costSum.get());
            }  else if(entry.getKey().equals(MealType.SNACK.name())) {
                _totalSnackCost.setValue(costSum.get());
            } else {

            }
        }

        Set<String> keySet = dietListByMealType.keySet();
        if(!keySet.contains(MealType.BREAKFAST.name())) {
            _totalBreakfastCost.setValue(0);
        } else if(!keySet.contains(MealType.LUNCH.name())) {
            _totalLunchCost.setValue(0);
        } else if(!keySet.contains(MealType.DINNER.name())) {
            _totalDinnerCost.setValue(0);
        } else if(!keySet.contains(MealType.SNACK.name())) {
            _totalSnackCost.setValue(0);
        } else {

        }
    }
}
