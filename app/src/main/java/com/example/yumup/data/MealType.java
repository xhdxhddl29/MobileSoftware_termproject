package com.example.yumup.data;

import com.example.yumup.R;

import java.util.Arrays;
import java.util.Optional;

public enum MealType {
    BREAKFAST("아침", R.drawable.shape_green_20dp, R.color.green),
    LUNCH("점심", R.drawable.shape_orange_20dp, R.color.orange),
    DINNER("저녁", R.drawable.shape_blue_20dp, R.color.blue),
    SNACK("간식", R.drawable.shape_red_20dp, R.color.red);

    String type;
    Integer backgroundRes;
    Integer colorRes;

    MealType(String type, Integer backgroundRes, Integer colorRes) {
        this.type = type;
        this.backgroundRes = backgroundRes;
        this.colorRes = colorRes;
    }
    public static Integer findDrawable(String typeName) {
        Optional<MealType> mealType = Arrays.stream(values()).filter(it -> it.name().equals(typeName))
                .findFirst();
        if(mealType.isPresent()) {
            return mealType.get().backgroundRes;
        } else {
            return R.drawable.shape_green_20dp;
        }
    }

    public static String findMealType(String typeName) {
        Optional<MealType> mealType = Arrays.stream(values()).filter(it -> it.name().equals(typeName))
                .findFirst();
        if(mealType.isPresent()) {
            return mealType.get().type;
        } else {
            return "아침";
        }
    }

    public static String toMealTypeName(String type) {
        Optional<MealType> mealType = Arrays.stream(values()).filter(it -> it.type.equals(type))
                .findFirst();
        return mealType.map(Enum::name).orElseGet(BREAKFAST::name);
    }

    public static Integer findMealTypeColorUsingType(String type) {
        Optional<MealType> mealType = Arrays.stream(values()).filter(it -> it.type.equals(type))
                .findFirst();
        if(mealType.isPresent()) {
            return mealType.get().colorRes;
        } else {
            return R.color.green;
        }
    }
}
