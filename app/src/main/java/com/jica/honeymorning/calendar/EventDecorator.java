package com.jica.honeymorning.calendar;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import com.jica.honeymorning.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.Collection;
import java.util.HashSet;

/**
 * Decorate several days with a dot
 */
public class EventDecorator implements DayViewDecorator {

    private final Drawable drawable;
    private HashSet<CalendarDay> dates;

    public EventDecorator(Collection<CalendarDay> dates,Activity context) {
        drawable = context.getResources().getDrawable(R.drawable.stamp);
        this.dates = new HashSet<>(dates);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setBackgroundDrawable(drawable);
        //view.addSpan(new DotSpan(7, Color.RED)); // 날자밑에 점
    }
}
