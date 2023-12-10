package com.example.yumup.utils;

import static com.example.yumup.data.MealType.findDrawable;
import static com.example.yumup.data.MealType.findMealType;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;

public class BindingAdapters {

    @BindingAdapter("setColor")
    public static void setColor(View view, @ColorInt int color) {
        view.setBackgroundColor(color);
    }

    @BindingAdapter("setImageUrl")
    public static void setImageUrl(ImageView view, String url) {
        Glide.with(view.getContext()).load(url)
                .into(view);

    }
    @BindingAdapter("setMealTypeRes")
    public static void setMealTypeRes(View view, String mealType) {
        view.setBackground(ContextCompat.getDrawable(view.getContext(), findDrawable(mealType)));
    }

    @BindingAdapter("setMealTypeText")
    public static void setMealTypeText(TextView view, String mealType) {
        view.setText(findMealType(mealType));
    }

    @BindingAdapter("setImageDrawable")
    public static void setImageDrawable(ImageView view, @DrawableRes Integer drawable) {
        Glide.with(view.getContext()).load(drawable)
                .into(view);
    }

    @BindingAdapter("setAmount")
    public static void setAmount(TextView textView, String amount) {
        textView.setText(amount + "원");
    }

    @BindingAdapter("setPercentage")
    public static void setPercentage(TextView textView, Integer integer) {
        textView.setText(integer + "%");
    }
}