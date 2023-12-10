package com.example.yumup.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "diet")
public class DietEntity {
    @PrimaryKey(autoGenerate = true)
    public Integer id;
    public String food;
    public String image;
    public String place;
    public String mealType;
    public String cost;
    public Float calories;
    public String mealDateTime;

    public DietEntity(Integer id, String food, String image, String place, String mealType, String cost, Float calories, String mealDateTime) {
        this.id = id;
        this.food = food;
        this.image = image;
        this.place = place;
        this.mealType = mealType;
        this.cost = cost;
        this.calories = calories;
        this.mealDateTime = mealDateTime;
    }
}

