package com.example.yumup.persentation.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.example.yumup.R;

import java.util.List;

public class RecordDateView extends View {
    public interface DateClickListener {
        public void onClickDate(DateEntity date);
    }
    private final Context context;
    public final DateEntity date;
    private final Boolean isThisMonth;
    private Rect bounds = new Rect();
    private Rect labelBounds = new Rect();
    private Canvas canvas;
    private TextPaint paint;
    private Paint labelPaint = new Paint();
    private TextPaint redPaint = new TextPaint();

    public Boolean selectedDate;
    public CalendarInfoEntity calendarData;

    public RecordDateView(Context context, @Nullable AttributeSet attrs, DateEntity date, Boolean isThisMonth ) {
        super(context, attrs);
        this.context = context;
        this.date = date;
        this.isThisMonth = isThisMonth;
        setBackground(ContextCompat.getDrawable(context, R.drawable.background_date));

        paint = new TextPaint();
        paint.setTextSize(convertDpToPx(13, getResources().getDisplayMetrics()));
        paint.setTypeface(ResourcesCompat.getFont(context, R.font.pyeongchang_regular));
        paint.getTextBounds(date.getCalendarDate(), 0, date.getCalendarDate().length(), bounds);

        redPaint.setTextSize(convertDpToPx(8, getResources().getDisplayMetrics()));
        redPaint.setTypeface(ResourcesCompat.getFont(context, R.font.pyeongchang_regular));
        redPaint.setColor(ContextCompat.getColor(context, R.color.red));

        labelPaint = new Paint();
        labelPaint.setStyle(Paint.Style.STROKE);
        labelPaint.setColor(ContextCompat.getColor(context, R.color.red));
    }


    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        if(canvas == null) {
            return;
        }
        this.canvas = canvas;
        drawDate(canvas);
        if(calendarData != null) {
            drawLabel(0, "식단 " + calendarData.dietCount + "개",  R.color.red);
        }
    }

    private void drawDate(Canvas canvas) {
        Integer textColor;
        if (!isThisMonth) {textColor = R.color.gray;} else {textColor = R.color.black;}
        paint.setColor(ContextCompat.getColor(context, textColor));

        canvas.drawText(
                date.getCalendarDate(),
                ((getWidth() - bounds.width()) / 2),
                (int) (getHeight() / 2.5),
                paint
        );
    }

    private void drawLabel(int idx, String title, int backgroundColor) {
        float initialTopPosition = (getHeight() / 4 - bounds.height() / 2) + bounds.height() + 7;
        float heightOfLabel = convertDpToPx(10, getResources().getDisplayMetrics());
        float spaceOfLabel = convertDpToPx(2, getResources().getDisplayMetrics());
        float xStart = (getWidth() * 0.1f);
        float xEnd = (getWidth() * 0.9f);
        float yTop = initialTopPosition + (idx * (heightOfLabel + spaceOfLabel)) + 10 ;
        float yBottom = yTop + convertDpToPx(10, getResources().getDisplayMetrics());

        paint.setColor(ContextCompat.getColor(context, backgroundColor));
        canvas.drawRoundRect(xStart, yTop, xEnd, yBottom, convertDpToPx(3, getResources().getDisplayMetrics()), convertDpToPx(3, getResources().getDisplayMetrics()), labelPaint);

        redPaint.getTextBounds(title, 0, title.length(), labelBounds);
        canvas.drawText(
                title,
                ((getWidth() - labelBounds.width()) / 2),
                ((yBottom + yTop) / 2) + (labelBounds.height() / 2) - 2,
                redPaint
        );
    }

    public void changeRecordIdList(CalendarInfoEntity calendarData) {
        this.calendarData = calendarData;
        invalidate();
    }

    static public Integer convertDpToPx(Integer dp, DisplayMetrics displayMetrics) {
        return  Math.round(dp * displayMetrics.density);
    }

    public void setOnDateClickListener(DateClickListener dateClickListener) {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dateClickListener.onClickDate(date);
            }
        });
    }
}
