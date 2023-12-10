package com.example.yumup.persentation.detail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.yumup.data.DietEntity;

public class DietDetailViewModel extends ViewModel {
    private MutableLiveData<DietEntity> _diet = new MutableLiveData<>();
    public LiveData<DietEntity> diet = _diet;

    public void changeDiet(DietEntity diet) {
        _diet.setValue(diet);
    }
}
