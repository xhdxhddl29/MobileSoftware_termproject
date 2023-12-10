package com.example.yumup.persentation.write;

import static com.example.yumup.utils.Constants.koreanDateFormat;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Date;

public class WriteDietViewModel extends ViewModel {
    private MutableLiveData<String> _selectedPlace = new MutableLiveData<String>("");
    public LiveData<String> selectedPlace = _selectedPlace;

    private MutableLiveData<String> _selectedDate = new MutableLiveData<String>(koreanDateFormat.format(new Date()));
    public LiveData<String> selectedDate = _selectedDate;

    private MutableLiveData<String> _selectedTime = new MutableLiveData<String>("");
    public LiveData<String> selectedTime = _selectedTime;

    private MutableLiveData<String> _foodImage = new MutableLiveData<>("");
    public LiveData<String> foodImage = _foodImage;

    public MutableLiveData<String> foodName = new MutableLiveData<>("");
    public MutableLiveData<String> estimate = new MutableLiveData<>("");
    public MutableLiveData<String> cost = new MutableLiveData<>("");

    public void changeSelectedPlace(String place) {
        _selectedPlace.setValue(place);
    }

    public void changeSelectedTime(String time) {
        _selectedTime.setValue(time);
    }

    public void changeFoodImage(String imageUri) {
        _foodImage.setValue(imageUri);
    }

    public void changeSelectedDate(String date) {
        _selectedDate.setValue(date);
    }

    public Boolean checkIsInputsEmpty() {
        return foodName.getValue().isEmpty() ||
                estimate.getValue().isEmpty() ||
                selectedTime.getValue().isEmpty() ||
                selectedPlace.getValue().isEmpty() ||
                cost.getValue().isEmpty() ;
    }

    public void clearInfo() {
        foodName.setValue("");
        _foodImage.setValue("");
        estimate.setValue("");
        cost.setValue("");
    }
}
