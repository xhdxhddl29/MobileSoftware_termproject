package com.example.yumup.utils;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class Constants {
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
    public static final SimpleDateFormat dayFormat = new SimpleDateFormat("E", Locale.KOREA);
    public static final SimpleDateFormat yearMonthFormat = new SimpleDateFormat("yyyy.MM", Locale.KOREA);
    public static final SimpleDateFormat fullDateFormat = new SimpleDateFormat("yyyy.MM.d", Locale.KOREA);
    public static final SimpleDateFormat koreanDateFormat = new SimpleDateFormat("yyyy.MM.dd.E", Locale.KOREAN);
    public static final String BUNDLE_TIME_MILLS = "BUNDLE_TIME_MILLS";
    public static final Integer START_POSITION = Integer.MAX_VALUE / 2;
    public static final String SELECTED_DATE = "SELECTED_DATE";
}