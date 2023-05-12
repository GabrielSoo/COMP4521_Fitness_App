package com.example.comp4521_fitness_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.comp4521_fitness_app.data.CurrentUser;
import com.example.comp4521_fitness_app.database.weightLog.WeightLogDBHelper;
import com.example.comp4521_fitness_app.database.weightLog.WeightLogData;
import com.example.comp4521_fitness_app.utilities.DateAxisValueFormatter;
import com.example.comp4521_fitness_app.utilities.DateUtils;
import com.example.comp4521_fitness_app.utilities.Notification;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class WeightManagementActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    //UI components
    private Spinner mSpinnerRedirect;

    private TextView currentWeightValueTextView;
    private TextView goalWeightValueTextView;
    private TextView weightToGoalTextView;
    private TextView lastLoggedTextView;
    private EditText weightInput;
    private Button logButton;
    private LineChart lineChart;

    // data
    private WeightLogData latestData;
    private String username;
    private WeightLogDBHelper dbHelper;
    Notification notification;

    private float getMinWeight(List<Entry> entries) {
        float minWeight = Float.MAX_VALUE;
        for (Entry entry : entries) {
            if (entry.getY() < minWeight) {
                minWeight = entry.getY();
            }
        }
        return minWeight;
    }

    private float getMaxWeight(List<Entry> entries) {
        float maxWeight = Float.MIN_VALUE;
        for (Entry entry : entries) {
            if (entry.getY() > maxWeight) {
                maxWeight = entry.getY();
            }
        }
        return maxWeight;
    }

    private List<WeightLogData> generateMockData() {
        List<WeightLogData> mockData = new ArrayList<>();

        // Generate mock weight log data for the past 7 days
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -7);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for (int i = 6; i >= 0; i--) {
            calendar.add(Calendar.DAY_OF_YEAR, 1); // Move back one day
            String date = dateFormat.format(calendar.getTime());

            // Generate random weight values between 60kg and 70kg
            float weight = (float) (Math.random() * 10) + 60;

            WeightLogData logData = new WeightLogData(-1, "username", 0, "", weight, 0, date);
            mockData.add(logData);
        }

        return mockData;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_management);

        dbHelper = new WeightLogDBHelper(this);

        mSpinnerRedirect = findViewById(R.id.spinner_redirect);
        mSpinnerRedirect.setOnItemSelectedListener(this);

        // Initialize the TextViews
        currentWeightValueTextView = findViewById(R.id.currentWeightValueTextView);
        goalWeightValueTextView = findViewById(R.id.goalWeightValueTextView);
        weightToGoalTextView = findViewById(R.id.weightToGoalTextView);
        lastLoggedTextView = findViewById(R.id.lastLogged);
        weightInput = findViewById(R.id.weightInputEditText);
        logButton = findViewById(R.id.logButton);
        lineChart = findViewById(R.id.weightChart);

        // Get the username from the previous activity
        username = CurrentUser.getInstance().getUsername();

        // Set the default selection to "Weight management"
        String[] redirectOptions = getResources().getStringArray(R.array.redirect_options);
        int weightManagementIndex = Arrays.asList(redirectOptions).indexOf("Weight management");
        mSpinnerRedirect.setSelection(weightManagementIndex);

        notification = new Notification(
                this,  // Context
                "WeightManagementActivity",  // Activity
                "Weight Log Reminder",  // Title
                "Please log your weight.",  // Description
                7  // Interval (in days)
        );

        updateUI();
        drawGraph();

        logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logWeight();
            }
        });
    };

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        String redirectOption = adapterView.getItemAtPosition(position).toString();
        Intent intent;

        switch (redirectOption) {
            case "Home page":
                intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
                break;
            case "Nutrition management":
                intent = new Intent(this, NutritionManagementActivity.class);
                startActivity(intent);
                break;
            case "Fitness management":
                intent = new Intent(this, FitnessManagementActivity.class);
                startActivity(intent);
                break;
            case "Profile":
                intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // Do nothing
    }

    public void updateUI() {
        // Fill in fields with latest weight log data for this user
        latestData = dbHelper.getLatestWeightLogData(username);
        if (latestData != null) {
            currentWeightValueTextView.setText(String.format("%.1f kg", latestData.actualWeight));
            goalWeightValueTextView.setText(String.format("%.1f kg", latestData.goalWeight));
            float difference = latestData.actualWeight - latestData.goalWeight;
            if (difference >= 0) {
                weightToGoalTextView.setText(String.format("%.1f kg to lose", difference));
                weightToGoalTextView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.red));
            }
            else {

                weightToGoalTextView.setText(String.format("%.1f kg to gain", difference*-1));
                weightToGoalTextView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
            }
            lastLoggedTextView.setText("(Last logged - " + DateUtils.getRelativeDate(latestData.dateCreated) + ")");
        }
    }

    public void logWeight(){
        String weightStr = weightInput.getText().toString();

        if (TextUtils.isEmpty(weightStr)){
            Toast.makeText(this, "Please enter the weight", Toast.LENGTH_SHORT).show();
            return;
        }

        float weight = Float.parseFloat(weightStr);

        String dateCreated = DateUtils.getDateTime();

        // Save data to database
        WeightLogData data = new WeightLogData(-1, username, latestData.height, latestData.weightGoalType, weight, latestData.goalWeight, dateCreated);
        dbHelper.insertWeightLogData(data);

        // Show success message
        Toast.makeText(this, "Weight logged successfully", Toast.LENGTH_SHORT).show();
        updateUI();
        drawGraph();
        notification.fire();
    }

    public void drawGraph() {
        // Fetch the weight log data from the database
        List<WeightLogData> weightLogDataList = dbHelper.getAllWeightLogData(username);
        float targetWeight = dbHelper.getLatestWeightLogData(username).goalWeight;
        // Create a list of Entry objects to store weight and corresponding timestamp
        List<Entry> entries = new ArrayList<>();
        List<String> dates = new ArrayList<>();
        int counter = 0;

        // Iterate through the weight log data list and add entries to the list
        for (WeightLogData weightLogData : weightLogDataList) {

            String dateCreated = weightLogData.dateCreated;
            float weight = weightLogData.actualWeight;

            // Convert the date string to a Date object for further manipulation (optional)

            dates.add(DateUtils.getDateLabel(dateCreated));

            // Create a new Entry object
            Entry entry = new Entry(counter, weight);

            // Add the entry to the entries list
            entries.add(entry);
            counter++;
        }

        if (weightLogDataList.size() > 1) {
            // Create a LineDataSet with the weight entries
            LineDataSet dataSet = new LineDataSet(entries, "Weight Over Time");

            // Customize the appearance of the line chart
            dataSet.setColor(Color.BLUE);
            dataSet.setCircleColor(Color.BLUE);
            dataSet.setDrawValues(false);
            dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);

            // Create a LineData object with the LineDataSet
            LineData lineData = new LineData(dataSet);

            // Set the LineData to the LineChart
            lineChart.setData(lineData);

            // Target Weight
            LimitLine targetLine = new LimitLine(targetWeight, "Target");
            targetLine.setLineColor(Color.RED); // Customize the line color
            targetLine.setLineWidth(2f); // Customize the line width
            targetLine.setTextColor(Color.RED); // Customize the label text color
            targetLine.setTextSize(12f); // Customize the label text size
            targetLine.enableDashedLine(10f, 10f, 0f); // Enable dashed line (optional)


            // Configure the X-axis
            XAxis xAxis = lineChart.getXAxis();
            xAxis.setValueFormatter(new DateAxisValueFormatter(dates));
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setGranularity(1f);
            xAxis.setLabelCount(entries.size());

            int maxVisibleDataPoints = 10; // Maximum number of visible data points on the x-axis
            int numDataPoints = entries.size();
            int visibleRange = Math.min(numDataPoints, maxVisibleDataPoints);

            lineChart.setVisibleXRangeMaximum(visibleRange);
            lineChart.setDragEnabled(true);
            lineChart.setScaleEnabled(true);
            lineChart.setVisibleXRangeMinimum(1); // Set the minimum visible range
            lineChart.moveViewToX(numDataPoints - visibleRange); // Move the view to the end of the data

            // Configure the Y-axis
            YAxis yAxisLeft = lineChart.getAxisLeft();
            yAxisLeft.setAxisMinimum(getMinWeight(entries) - 5f);
            yAxisLeft.setAxisMaximum(getMaxWeight(entries) + 5f);
            yAxisLeft.addLimitLine(targetLine);

            YAxis yAxisRight = lineChart.getAxisRight();
            yAxisRight.setEnabled(false);

            // Remove the legend
            lineChart.getLegend().setEnabled(false);

            // Refresh the chart
            lineChart.invalidate();
        }
    }
}
