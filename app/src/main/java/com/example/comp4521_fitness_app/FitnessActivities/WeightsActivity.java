package com.example.comp4521_fitness_app.FitnessActivities;

import android.graphics.Color;
import android.os.Bundle;
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
import java.util.List;

public class WeightsActivity extends AppCompatActivity {
    private TextView weightsTitleTextView;
    private TextView maxWeightLiftedTextView;
    private LineChart trainingVolumeChart;
    private LineChart setsChart;
    private LineChart repsChart;
    private LineChart weightChart;
    private int exerciseId;
    private List<ExerciseSetData> exerciseLogs;
    private FitnessLogDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weights);

        weightsTitleTextView = findViewById(R.id.text_weights_title);
        maxWeightLiftedTextView = findViewById(R.id.text_highest_weight_lifted_value);
        trainingVolumeChart = findViewById(R.id.trainingVolumeChart);
        setsChart = findViewById(R.id.setsChart);
        repsChart = findViewById(R.id.repsChart);
        weightChart = findViewById(R.id.weightChart);

        dbHelper = new FitnessLogDBHelper(this);

        exerciseId = CurrentExercise.getInstance().getId();

        ExerciseData exerciseData = dbHelper.getExerciseById(exerciseId);
        if (exerciseData != null) {
            String weightsTitle = exerciseData.exerciseName;
            weightsTitleTextView.setText(weightsTitle);
        }

        exerciseLogs = dbHelper.queryExerciseLogsByExerciseId(exerciseId);
        int maxWeightLifted = calculateMaxWeightLifted(exerciseLogs);
        maxWeightLiftedTextView.setText(String.valueOf(maxWeightLifted));

        // Display the weights activity data in the charts using the LineChart library
        drawTrainingVolumeGraph();
        drawSetsGraph();
        drawRepsGraph();
        drawWeightGraph();
    }

    private int calculateMaxWeightLifted(List<ExerciseSetData> exerciseLogs) {
        int maxWeightLifted = 0;
        for (ExerciseSetData exerciseSetData : exerciseLogs) {
            int weightLifted = exerciseSetData.weight;
            if (weightLifted > maxWeightLifted) {
                maxWeightLifted = weightLifted;
            }
        }
        return maxWeightLifted;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }

    public void drawTrainingVolumeGraph() {
        List<Entry> entries = new ArrayList<>();
        List<String> dates = new ArrayList<>();
        int counter = 0;

        for (ExerciseSetData exerciseSetData : exerciseLogs) {
            String dateCreated = exerciseSetData.dateCreated;
            int reps = exerciseSetData.reps;
            float weight = exerciseSetData.weight;
            int sets = exerciseSetData.sets;

            int trainingVolume = reps * Math.round(weight) * sets;

            dates.add(DateUtils.getDateLabel(dateCreated));

            Entry entry = new Entry(counter, trainingVolume);
            entries.add(entry);
            counter++;
        }

        if (exerciseLogs.size() > 1) {
            LineDataSet dataSet = new LineDataSet(entries, "Training Volume Over Time");
            dataSet.setColor(Color.GREEN);
            dataSet.setCircleColor(Color.GREEN);
            dataSet.setDrawValues(false);
            dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);

            LineData lineData = new LineData(dataSet);
            trainingVolumeChart.setData(lineData);
            trainingVolumeChart.getDescription().setEnabled(false);

            XAxis xAxis = trainingVolumeChart.getXAxis();
            xAxis.setValueFormatter(new IndexAxisValueFormatter(dates));
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setGranularity(1f);
            xAxis.setLabelCount(entries.size());

            int maxVisibleDataPoints = 10;
            int numDataPoints = entries.size();
            int visibleRange = Math.min(numDataPoints, maxVisibleDataPoints);

            trainingVolumeChart.setVisibleXRangeMaximum(visibleRange);
            trainingVolumeChart.setDragEnabled(true);
            trainingVolumeChart.setScaleEnabled(true);
            trainingVolumeChart.setVisibleXRangeMinimum(1);
            trainingVolumeChart.moveViewToX(numDataPoints - visibleRange);

            YAxis yAxisLeft = trainingVolumeChart.getAxisLeft();
            yAxisLeft.setAxisMinimum(getMinTrainingVolume(entries) - 20f);
            yAxisLeft.setAxisMaximum(getMaxTrainingVolume(entries) + 20f);

            YAxis yAxisRight = trainingVolumeChart.getAxisRight();
            yAxisRight.setEnabled(false);

            trainingVolumeChart.getLegend().setEnabled(false);
            trainingVolumeChart.invalidate();
        }
    }

    private float getMinTrainingVolume(List<Entry> entries) {
        float minValue = Float.MAX_VALUE;

        for (Entry entry : entries) {
            float trainingVolume = entry.getY();
            if (trainingVolume < minValue) {
                minValue = trainingVolume;
            }
        }

        return minValue;
    }

    private float getMaxTrainingVolume(List<Entry> entries) {
        float maxValue = Float.MIN_VALUE;

        for (Entry entry : entries) {
            float trainingVolume = entry.getY();
            if (trainingVolume > maxValue) {
                maxValue = trainingVolume;
            }
        }

        return maxValue;
    }

    public void drawSetsGraph() {
        List<Entry> entries = new ArrayList<>();
        List<String> dates = new ArrayList<>();
        int counter = 0;

        for (ExerciseSetData exerciseSetData : exerciseLogs) {
            String dateCreated = exerciseSetData.dateCreated;
            int sets = exerciseSetData.sets;

            dates.add(DateUtils.getDateLabel(dateCreated));

            Entry entry = new Entry(counter, sets);
            entries.add(entry);
            counter++;
        }

        if (exerciseLogs.size() > 1) {
            LineDataSet dataSet = new LineDataSet(entries, "Sets Over Time");
            dataSet.setColor(Color.BLUE);
            dataSet.setCircleColor(Color.BLUE);
            dataSet.setDrawValues(false);
            dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);

            LineData lineData = new LineData(dataSet);
            setsChart.setData(lineData);
            setsChart.getDescription().setEnabled(false);

            XAxis xAxis = setsChart.getXAxis();
            xAxis.setValueFormatter(new IndexAxisValueFormatter(dates));
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setGranularity(1f);
            xAxis.setLabelCount(entries.size());

            int maxVisibleDataPoints = 10;
            int numDataPoints = entries.size();
            int visibleRange = Math.min(numDataPoints, maxVisibleDataPoints);
            setsChart.setVisibleXRangeMaximum(visibleRange);
            setsChart.setDragEnabled(true);
            setsChart.setScaleEnabled(true);
            setsChart.setVisibleXRangeMinimum(1);
            setsChart.moveViewToX(numDataPoints - visibleRange);

            YAxis yAxisLeft = setsChart.getAxisLeft();
            yAxisLeft.setAxisMinimum(getMinSets(entries) - 1f);
            yAxisLeft.setAxisMaximum(getMaxSets(entries) + 1f);

            YAxis yAxisRight = setsChart.getAxisRight();
            yAxisRight.setEnabled(false);

            setsChart.getLegend().setEnabled(false);
            setsChart.invalidate();
        }
    }

    private float getMinSets(List<Entry> entries) {
        float minValue = Float.MAX_VALUE;

        for (Entry entry : entries) {
            float sets = entry.getY();
            if (sets < minValue) {
                minValue = sets;
            }
        }

        return minValue;
    }

    private float getMaxSets(List<Entry> entries) {
        float maxValue = Float.MIN_VALUE;

        for (Entry entry : entries) {
            float sets = entry.getY();
            if (sets > maxValue) {
                maxValue = sets;
            }
        }

        return maxValue;
    }

    public void drawRepsGraph() {
        List<Entry> entries = new ArrayList<>();
        List<String> dates = new ArrayList<>();
        int counter = 0;

        for (ExerciseSetData exerciseSetData : exerciseLogs) {
            String dateCreated = exerciseSetData.dateCreated;
            int reps = exerciseSetData.reps;

            dates.add(DateUtils.getDateLabel(dateCreated));

            Entry entry = new Entry(counter, reps);
            entries.add(entry);
            counter++;
        }

        if (exerciseLogs.size() > 1) {
            LineDataSet dataSet = new LineDataSet(entries, "Reps Over Time");
            dataSet.setColor(Color.YELLOW);
            dataSet.setCircleColor(Color.YELLOW);
            dataSet.setDrawValues(false);
            dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);

            LineData lineData = new LineData(dataSet);
            repsChart.setData(lineData);
            repsChart.getDescription().setEnabled(false);

            XAxis xAxis = repsChart.getXAxis();
            xAxis.setValueFormatter(new IndexAxisValueFormatter(dates));
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setGranularity(1f);
            xAxis.setLabelCount(entries.size());

            int maxVisibleDataPoints = 10;
            int numDataPoints = entries.size();
            int visibleRange = Math.min(numDataPoints, maxVisibleDataPoints);

            repsChart.setVisibleXRangeMaximum(visibleRange);
            repsChart.setDragEnabled(true);
            repsChart.setScaleEnabled(true);
            repsChart.setVisibleXRangeMinimum(1);
            repsChart.moveViewToX(numDataPoints - visibleRange);

            YAxis yAxisLeft = repsChart.getAxisLeft();
            yAxisLeft.setAxisMinimum(getMinReps(entries) - 1f);
            yAxisLeft.setAxisMaximum(getMaxReps(entries) + 1f);

            YAxis yAxisRight = repsChart.getAxisRight();
            yAxisRight.setEnabled(false);

            repsChart.getLegend().setEnabled(false);
            repsChart.invalidate();
        }
    }

    private float getMinReps(List<Entry> entries) {
        float minValue = Float.MAX_VALUE;

        for (Entry entry : entries) {
            float reps = entry.getY();
            if (reps < minValue) {
                minValue = reps;
            }
        }

        return minValue;
    }

    private float getMaxReps(List<Entry> entries) {
        float maxValue = Float.MIN_VALUE;

        for (Entry entry : entries) {
            float weight = entry.getY();
            if (weight > maxValue) {
                maxValue = weight;
            }
        }

        return maxValue;
    }

    public void drawWeightGraph() {
        List<Entry> entries = new ArrayList<>();
        List<String> dates = new ArrayList<>();
        int counter = 0;

        for (ExerciseSetData exerciseSetData : exerciseLogs) {
            String dateCreated = exerciseSetData.dateCreated;
            float weight = exerciseSetData.weight;

            dates.add(DateUtils.getDateLabel(dateCreated));

            Entry entry = new Entry(counter, weight);
            entries.add(entry);
            counter++;
        }

        if (exerciseLogs.size() > 1) {
            LineDataSet dataSet = new LineDataSet(entries, "Weight Over Time");
            dataSet.setColor(Color.MAGENTA);
            dataSet.setCircleColor(Color.MAGENTA);
            dataSet.setDrawValues(false);
            dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);

            LineData lineData = new LineData(dataSet);
            weightChart.setData(lineData);
            weightChart.getDescription().setEnabled(false);

            XAxis xAxis = weightChart.getXAxis();
            xAxis.setValueFormatter(new IndexAxisValueFormatter(dates));
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setGranularity(1f);
            xAxis.setLabelCount(entries.size());

            int maxVisibleDataPoints = 10;
            int numDataPoints = entries.size();
            int visibleRange = Math.min(numDataPoints, maxVisibleDataPoints);

            weightChart.setVisibleXRangeMaximum(visibleRange);
            weightChart.setDragEnabled(true);
            weightChart.setScaleEnabled(true);
            weightChart.setVisibleXRangeMinimum(1);
            weightChart.moveViewToX(numDataPoints - visibleRange);

            YAxis yAxisLeft = weightChart.getAxisLeft();
            yAxisLeft.setAxisMinimum(getMinWeight(entries) - 5f);
            yAxisLeft.setAxisMaximum(getMaxWeight(entries) + 5f);

            YAxis yAxisRight = weightChart.getAxisRight();
            yAxisRight.setEnabled(false);

            weightChart.getLegend().setEnabled(false);
            weightChart.invalidate();
        }
    }

    private float getMinWeight(List<Entry> entries) {
        float minValue = Float.MAX_VALUE;

        for (Entry entry : entries) {
            float weight = entry.getY();
            if (weight < minValue) {
                minValue = weight;
            }
        }

        return minValue;
    }

    private float getMaxWeight(List<Entry> entries) {
        float maxValue = Float.MIN_VALUE;

        for (Entry entry : entries) {
            float weight = entry.getY();
            if (weight > maxValue) {
                maxValue = weight;
            }
        }

        return maxValue;
    }
}


