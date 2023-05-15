package com.example.comp4521_fitness_app.FitnessActivities;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.comp4521_fitness_app.R;
import com.example.comp4521_fitness_app.data.CurrentExercise;
import com.example.comp4521_fitness_app.database.fitnessLog.ExerciseData;
import com.example.comp4521_fitness_app.database.fitnessLog.ExerciseSetData;
import com.example.comp4521_fitness_app.database.fitnessLog.FitnessLogDBHelper;
import com.example.comp4521_fitness_app.utilities.DateUtils;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CardioActivity extends AppCompatActivity {
    private TextView cardioTitleTextView;
    private TextView highestCaloriesBurnedTextView;
    private LineChart caloriesBurnedChart;
    private int exerciseId;
    List<ExerciseSetData> exerciseLogs;
    private FitnessLogDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardio);

        cardioTitleTextView = findViewById(R.id.text_cardio_title);
        highestCaloriesBurnedTextView = findViewById(R.id.text_highest_calories_burned_value);
        caloriesBurnedChart = findViewById(R.id.caloriesBurnedChart);

        dbHelper = new FitnessLogDBHelper(this);

        exerciseId = CurrentExercise.getInstance().getId();

        ExerciseData exerciseData = dbHelper.getExerciseById(exerciseId);
        if (exerciseData != null) {
            String cardioTitle = exerciseData.exerciseName;
            cardioTitleTextView.setText(cardioTitle);
        }

        exerciseLogs = dbHelper.queryExerciseLogsByExerciseId(exerciseId);
        int highestCaloriesBurned = calculateHighestCaloriesBurned(exerciseLogs);
        highestCaloriesBurnedTextView.setText(String.valueOf(highestCaloriesBurned));

        // TODO: Display the cardio activity data in the chart using the LineChart library
        drawGraph();
    }

    private int calculateHighestCaloriesBurned(List<ExerciseSetData> exerciseLogs) {
        int highestCaloriesBurned = 0;
        for (ExerciseSetData exerciseSetData : exerciseLogs) {
            int caloriesBurned = exerciseSetData.caloriesBurned;
            if (caloriesBurned > highestCaloriesBurned) {
                highestCaloriesBurned = caloriesBurned;
            }
        }
        return highestCaloriesBurned;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }

    public void drawGraph() {
        // Create a list of Entry objects to store calories burned and corresponding timestamp
        List<Entry> entries = new ArrayList<>();
        List<String> dates = new ArrayList<>();
        int counter = 0;

        // Iterate through the exercise log data and add entries to the list
        for (ExerciseSetData exerciseSetData : exerciseLogs) {
            String dateCreated = exerciseSetData.dateCreated;
            int caloriesBurned = exerciseSetData.caloriesBurned;

            dates.add(DateUtils.getDateLabel(dateCreated));

            // Create a new Entry object
            Entry entry = new Entry(counter, caloriesBurned);

            // Add the entry to the entries list
            entries.add(entry);
            counter++;
        }

        if (exerciseLogs.size() > 1) {
            // Create a LineDataSet with the calories burned entries
            LineDataSet dataSet = new LineDataSet(entries, "Calories Burned Over Time");

            // Customize the appearance of the line chart
            dataSet.setColor(Color.RED);
            dataSet.setCircleColor(Color.RED);
            dataSet.setDrawValues(false);
            dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);

            // Create a LineData object with the LineDataSet
            LineData lineData = new LineData(dataSet);

            // Set the LineData to the caloriesBurnedChart
            caloriesBurnedChart.setData(lineData);
            caloriesBurnedChart.getDescription().setEnabled(false);

            // Configure the X-axis
            XAxis xAxis = caloriesBurnedChart.getXAxis();
            xAxis.setValueFormatter(new IndexAxisValueFormatter(dates));
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setGranularity(1f);
            xAxis.setLabelCount(entries.size());

            int maxVisibleDataPoints = 10; // Maximum number of visible data points on the x-axis
            int numDataPoints = entries.size();
            int visibleRange = Math.min(numDataPoints, maxVisibleDataPoints);

            caloriesBurnedChart.setVisibleXRangeMaximum(visibleRange);
            caloriesBurnedChart.setDragEnabled(true);
            caloriesBurnedChart.setScaleEnabled(true);
            caloriesBurnedChart.setVisibleXRangeMinimum(1); // Set the minimum visible range
            caloriesBurnedChart.moveViewToX(numDataPoints - visibleRange); // Move the view to the end of the data

            // Configure the Y-axis
            YAxis yAxisLeft = caloriesBurnedChart.getAxisLeft();
            yAxisLeft.setAxisMinimum(getMinCaloriesBurned(entries) - 20f);
            yAxisLeft.setAxisMaximum(getMaxCaloriesBurned(entries) + 20f);

            YAxis yAxisRight = caloriesBurnedChart.getAxisRight();
            yAxisRight.setEnabled(false);

            // Remove the legend
            caloriesBurnedChart.getLegend().setEnabled(false);

            // Refresh the chart
            caloriesBurnedChart.invalidate();
        }
    }

    private float getMinCaloriesBurned(List<Entry> entries) {
        float minValue = Float.MAX_VALUE;

        for (Entry entry : entries) {
            float caloriesBurned = entry.getY();
            if (caloriesBurned < minValue) {
                minValue = caloriesBurned;
            }
        }

        return minValue;
    }

    private float getMaxCaloriesBurned(List<Entry> entries) {
        float maxValue = Float.MIN_VALUE;

        for (Entry entry : entries) {
            float caloriesBurned = entry.getY();
            if (caloriesBurned > maxValue) {
                maxValue = caloriesBurned;
            }
        }

        return maxValue;
    }

}
