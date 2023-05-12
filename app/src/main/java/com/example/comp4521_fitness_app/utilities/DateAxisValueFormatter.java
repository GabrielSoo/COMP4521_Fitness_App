package com.example.comp4521_fitness_app.utilities;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DateAxisValueFormatter extends ValueFormatter {

    private final List<String> dates;

    public DateAxisValueFormatter(List<String> dates) {
        this.dates = dates;
    }

    @Override
    public String getAxisLabel(float value, AxisBase axis) {
        int index = (int) value;
        if (dates.size() > 0 && dates.size() > index){
            return dates.get(index);
        }
        return "";
    }
}