package com.example.yumup.data;

import android.content.Context;

import com.example.yumup.R;

import java.util.Arrays;
import java.util.Optional;

public enum MealPlace {
    RESTAURANT1(R.string.restaurant1, R.color.green),
    RESTAURANT2(R.string.restaurant2, R.color.orange),
    RESTAURANT3(R.string.restaurant3,  R.color.blue),
    RESTAURANT4(R.string.restaurant4,  R.color.red);

    Integer place;
    Integer colorRes;

    MealPlace(Integer place, Integer colorRes) {
        this.place = place;
        this.colorRes = colorRes;
    }

    public static Integer findColorUsingPlace(String place, Context context) {
        Optional<MealPlace> restaurant = Arrays.stream(values()).filter(it -> context.getString(it.place).equals(place))
                .findFirst();
        if(restaurant.isPresent()) {
            return restaurant.get().colorRes;
        } else {
            return R.color.green;
        }
    }
}
