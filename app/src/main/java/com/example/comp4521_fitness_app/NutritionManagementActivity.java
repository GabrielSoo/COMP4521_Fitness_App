package com.example.comp4521_fitness_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.comp4521_fitness_app.nutritionActivity.BreakfastActivity;
import com.example.comp4521_fitness_app.nutritionActivity.DinnerActivity;
import com.example.comp4521_fitness_app.nutritionActivity.LunchActivity;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class NutritionManagementActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner mSpinnerRedirect;
    private TextView dailyCaloriesTextView, goalCaloriesTextView, mCarbsTextView, mProteinTextView, mFatsTextView;;
    private ProgressBar calories, mCarbsProgressBar, mProteinProgressBar, mFatsProgressBar;
    private TextView breakfastCaloriesTextView, breakfastCarbTextView, breakfastProteinTextView, breakfastFatTextView;
    private TextView lunchCaloriesTextView, lunchCarbTextView, lunchProteinTextView, lunchFatTextView;
    private TextView dinnerCaloriesTextView, dinnerCarbTextView, dinnerProteinTextView, dinnerFatTextView;
    private Button mBreakfastButton, mLunchButton, mDinnerButton;

    private float mCaloriesValue, mGoalValue, mCarbsValue, mProteinValue, mFatValue;
    private float mBreakfastCaloriesValue, mBreakfastCarbValue, mBreakfastProteinValue, mBreakfastFatValue;
    private float mLunchCaloriesValue, mLunchCarbValue, mLunchProteinValue, mLunchFatValue;
    private float mDinnerCaloriesValue, mDinnerCarbValue, mDinnerProteinValue, mDinnerFatValue;
    private Button previousButton, nextButton;
    private TextView currentDate;
    private String Date;
    private DateFormat dateFormat;

    private SharedPreferences mSharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition_management);

        mSpinnerRedirect = findViewById(R.id.spinner_redirect);
        mSpinnerRedirect.setOnItemSelectedListener(this);

        // Set the default selection to "Nutrition management"
        String[] redirectOptions = getResources().getStringArray(R.array.redirect_options);
        int nutritionManagementIndex = Arrays.asList(redirectOptions).indexOf("Nutrition management");
        mSpinnerRedirect.setSelection(nutritionManagementIndex);

        // Initialize SharedPreferences
        mSharedPreferences = getSharedPreferences("NutritionData", MODE_PRIVATE);

        // Uncomment to reset log for testing
        //mSharedPreferences.edit().clear().apply();

        // Get today's date in the format "yyyy-MM-dd"
        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date = mSharedPreferences.getString("selectedDate", dateFormat.format(new Date()));

        currentDate = findViewById(R.id.date);
        currentDate.setText(Date);

        // Get the saved nutrition data from SharedPreferences
        mCaloriesValue = mSharedPreferences.getFloat(Date + "CALORIES_VALUE", 0);
        mGoalValue = mSharedPreferences.getFloat(Date + "GOAL_VALUE", 0);
        mCarbsValue = mSharedPreferences.getFloat(Date + "CARBS_VALUE", 0);
        mProteinValue = mSharedPreferences.getFloat(Date + "PROTEIN_VALUE", 0);
        mFatValue = mSharedPreferences.getFloat(Date + "FAT_VALUE", 0);
        mBreakfastCaloriesValue = mSharedPreferences.getFloat(Date + "BREAKFAST_CALORIES_VALUE", 0);
        mBreakfastCarbValue = mSharedPreferences.getFloat(Date + "BREAKFAST_CARB_VALUE", 0);
        mBreakfastProteinValue = mSharedPreferences.getFloat(Date + "BREAKFAST_PROTEIN_VALUE", 0);
        mBreakfastFatValue = mSharedPreferences.getFloat(Date + "BREAKFAST_FAT_VALUE", 0);
        mLunchCaloriesValue = mSharedPreferences.getFloat(Date + "LUNCH_CALORIES_VALUE", 0);
        mLunchCarbValue = mSharedPreferences.getFloat(Date + "LUNCH_CARB_VALUE", 0);
        mLunchProteinValue = mSharedPreferences.getFloat(Date + "LUNCH_PROTEIN_VALUE", 0);
        mLunchFatValue = mSharedPreferences.getFloat(Date + "LUNCH_FAT_VALUE", 0);
        mDinnerCaloriesValue = mSharedPreferences.getFloat(Date + "DINNER_CALORIES_VALUE", 0);
        mDinnerCarbValue = mSharedPreferences.getFloat(Date + "DINNER_CARB_VALUE", 0);
        mDinnerProteinValue = mSharedPreferences.getFloat(Date + "DINNER_PROTEIN_VALUE", 0);
        mDinnerFatValue = mSharedPreferences.getFloat(Date + "DINNER_FAT_VALUE", 0);

        // Retrieve the extra from the Intent
        Intent intent = getIntent();
        if (intent != null) {
            int breakfastCalories = intent.getIntExtra("BREAKFAST_CALORIES", 0);
            int breakfastCarbs = intent.getIntExtra("BREAKFAST_CARBS", 0);
            int breakfastProteins = intent.getIntExtra("BREAKFAST_PROTEINS", 0);
            int breakfastFats = intent.getIntExtra("BREAKFAST_FATS", 0);

            int lunchCalories = intent.getIntExtra("LUNCH_CALORIES", 0);
            int lunchCarbs = intent.getIntExtra("LUNCH_CARBS", 0);
            int lunchProteins = intent.getIntExtra("LUNCH_PROTEINS", 0);
            int lunchFats = intent.getIntExtra("LUNCH_FATS", 0);

            int dinnerCalories = intent.getIntExtra("DINNER_CALORIES", 0);
            int dinnerCarbs = intent.getIntExtra("DINNER_CARBS", 0);
            int dinnerProteins = intent.getIntExtra("DINNER_PROTEINS", 0);
            int dinnerFats = intent.getIntExtra("DINNER_FATS", 0);

            // Update the mBreakfastCaloriesValue with the value from the Intent
            mBreakfastCaloriesValue += breakfastCalories;
            mBreakfastCarbValue += breakfastCarbs;
            mBreakfastProteinValue += breakfastProteins;
            mBreakfastFatValue += breakfastFats;

            // Update the mLunchCaloriesValue with the value from the Intent
            mLunchCaloriesValue += lunchCalories;
            mLunchCarbValue += lunchCarbs;
            mLunchProteinValue += lunchProteins;
            mLunchFatValue += lunchFats;

            // Update the mDinnerCaloriesValue with the value from the Intent
            mDinnerCaloriesValue += dinnerCalories;
            mDinnerCarbValue += dinnerCarbs;
            mDinnerProteinValue += dinnerProteins;
            mDinnerFatValue += dinnerFats;

            mCaloriesValue += breakfastCalories + lunchCalories + dinnerCalories;
            mCarbsValue += breakfastCarbs + lunchCarbs + dinnerCarbs;
            mProteinValue += breakfastProteins + lunchProteins + dinnerProteins;
            mFatValue += breakfastFats + lunchFats + dinnerFats;

            // Update the TextView with the new value
            breakfastCaloriesTextView = findViewById(R.id.breakfastCalories);
            breakfastCaloriesTextView.setText(Float.toString(mBreakfastCaloriesValue) + "kCal");

            breakfastCarbTextView = findViewById(R.id.breakfastCarbs);
            breakfastCarbTextView.setText(Float.toString(mBreakfastCarbValue) + " g\nCarb");

            breakfastProteinTextView = findViewById(R.id.breakfastProteins);
            breakfastProteinTextView.setText(Float.toString(mBreakfastProteinValue) + " g\nProtein");

            breakfastFatTextView = findViewById(R.id.breakfastFats);
            breakfastFatTextView.setText(Float.toString(mBreakfastFatValue) + " g\nFat");

            // Update the TextView with the new value
            lunchCaloriesTextView = findViewById(R.id.lunchCalories);
            lunchCaloriesTextView.setText(Float.toString(mLunchCaloriesValue) + "kCal");

            lunchCarbTextView = findViewById(R.id.lunchCarbs);
            lunchCarbTextView.setText(Float.toString(mLunchCarbValue) + " g\nCarb");

            lunchProteinTextView = findViewById(R.id.lunchProteins);
            lunchProteinTextView.setText(Float.toString(mLunchProteinValue) + " g\nProtein");

            lunchFatTextView = findViewById(R.id.lunchFats);
            lunchFatTextView.setText(Float.toString(mLunchFatValue) + " g\nFat");

            // Update the TextView with the new value
            dinnerCaloriesTextView = findViewById(R.id.dinnerCalories);
            dinnerCaloriesTextView.setText(Float.toString(mDinnerCaloriesValue) + "kCal");

            dinnerCarbTextView = findViewById(R.id.dinnerCarbs);
            dinnerCarbTextView.setText(Float.toString(mDinnerCarbValue) + " g\nCarb");

            dinnerProteinTextView = findViewById(R.id.dinnerProteins);
            dinnerProteinTextView.setText(Float.toString(mDinnerProteinValue) + " g\nProtein");

            dinnerFatTextView = findViewById(R.id.dinnerFats);
            dinnerFatTextView.setText(Float.toString(mDinnerFatValue) + " g\nFat");

        }

        // Save the updated nutrition data to SharedPreferences
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putFloat(Date + "CALORIES_VALUE", mCaloriesValue);
        editor.putFloat(Date + "GOAL_VALUE", mGoalValue);
        editor.putFloat(Date + "CARBS_VALUE", mCarbsValue);
        editor.putFloat(Date + "PROTEIN_VALUE", mProteinValue);
        editor.putFloat(Date + "FAT_VALUE", mFatValue);
        editor.putFloat(Date + "BREAKFAST_CALORIES_VALUE", mBreakfastCaloriesValue);
        editor.putFloat(Date + "BREAKFAST_CARB_VALUE", mBreakfastCarbValue);
        editor.putFloat(Date + "BREAKFAST_PROTEIN_VALUE", mBreakfastProteinValue);
        editor.putFloat(Date + "BREAKFAST_FAT_VALUE", mBreakfastFatValue);
        editor.putFloat(Date + "LUNCH_CALORIES_VALUE", mLunchCaloriesValue);
        editor.putFloat(Date + "LUNCH_CARB_VALUE", mLunchCarbValue);
        editor.putFloat(Date + "LUNCH_PROTEIN_VALUE", mLunchProteinValue);
        editor.putFloat(Date + "LUNCH_FAT_VALUE", mLunchFatValue);
        editor.putFloat(Date + "DINNER_CALORIES_VALUE", mDinnerCaloriesValue);
        editor.putFloat(Date + "DINNER_CARB_VALUE", mDinnerCarbValue);
        editor.putFloat(Date + "DINNER_PROTEIN_VALUE", mDinnerProteinValue);
        editor.putFloat(Date + "DINNER_FAT_VALUE", mDinnerFatValue);
        editor.apply();

        // Find the Carbs TextView and set its text dynamically
        mCarbsTextView = findViewById(R.id.carbs_text_view);
        mCarbsProgressBar = findViewById(R.id.carbs_progress_bar);
        float carbs_goal = (float) mGoalValue * 0.128f;
        int carbs_percentage = (int) (((float) mCarbsValue / (float) carbs_goal) * 100);
        mCarbsProgressBar.setProgress(carbs_percentage);
        String carbsText = mCarbsValue + "g\nCarb";
        mCarbsTextView.setText(carbsText);

        // Find the Protein TextView and set its text dynamically
        mProteinTextView = findViewById(R.id.protein_text_view);
        mProteinProgressBar = findViewById(R.id.protein_progress_bar);
        float protein_goal = (float) mGoalValue * 0.045f;
        int protein_percentage = (int) (((float) mProteinValue / (float) protein_goal) * 100);
        mProteinProgressBar.setProgress(protein_percentage);
        String proteinText = mProteinValue + "g\nProtein";
        mProteinTextView.setText(proteinText);

        // Find the Fats TextView and set its text dynamically
        mFatsTextView = findViewById(R.id.fats_text_view);
        mFatsProgressBar = findViewById(R.id.fats_progress_bar);
        float fat_goal = (float) mGoalValue * 0.037f;
        int fat_percentage = (int) (((float) mFatValue / (float) fat_goal) * 100);
        mFatsProgressBar.setProgress(fat_percentage);
        String fatsText = mFatValue + "g\nFat";
        mFatsTextView.setText(fatsText);

        // Calculate the overall progress value based on the individual progress values of each nutrient
        calories = findViewById(R.id.progress_bar);
        int calories_percentage = (int) (((float) mCaloriesValue / (float) mGoalValue) * 100);
        calories.setProgress(calories_percentage);

        // Find the TextView for daily calories and set its text dynamically
        dailyCaloriesTextView = findViewById(R.id.text_daily_calories_value);
        String dailyCaloriesValue = mCaloriesValue + " kCal";
        dailyCaloriesTextView.setText(dailyCaloriesValue);

        // Find the TextView for daily calories and set its text dynamically
        goalCaloriesTextView = findViewById(R.id.text_goal_calories_value);
        String goalCaloriesValue = mGoalValue + " kCal";
        Log.d("YourTag", "mGoalValue: " + mGoalValue);
        goalCaloriesTextView.setText(goalCaloriesValue);

        mBreakfastButton = findViewById(R.id.breakfastButton);
        mBreakfastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NutritionManagementActivity.this, BreakfastActivity.class);
                startActivity(intent);
            }
        });

        mLunchButton = findViewById(R.id.lunchButton);
        mLunchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NutritionManagementActivity.this, LunchActivity.class);
                startActivity(intent);
            }
        });

        mDinnerButton = findViewById(R.id.dinnerButton);
        mDinnerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NutritionManagementActivity.this, DinnerActivity.class);
                startActivity(intent);
            }
        });

        previousButton = findViewById(R.id.previous_button);
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Subtract one day from the current date
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                try {
                    Date currentDate = dateFormat.parse(Date);
                    calendar.setTime(currentDate);
                    calendar.add(Calendar.DAY_OF_YEAR, -1);
                    Date = dateFormat.format(calendar.getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                // Retrieve the nutrition data for the new date from SharedPreferences
                mCaloriesValue = mSharedPreferences.getFloat(Date + "CALORIES_VALUE", 0);
                mGoalValue = mSharedPreferences.getFloat(Date + "GOAL_VALUE", 0);
                mCarbsValue = mSharedPreferences.getFloat(Date + "CARBS_VALUE", 0);
                mProteinValue = mSharedPreferences.getFloat(Date + "PROTEIN_VALUE", 0);
                mFatValue = mSharedPreferences.getFloat(Date + "FAT_VALUE", 0);
                mBreakfastCaloriesValue = mSharedPreferences.getFloat(Date + "BREAKFAST_CALORIES_VALUE", 0);
                mBreakfastCarbValue = mSharedPreferences.getFloat(Date + "BREAKFAST_CARB_VALUE", 0);
                mBreakfastProteinValue = mSharedPreferences.getFloat(Date + "BREAKFAST_PROTEIN_VALUE", 0);
                mBreakfastFatValue = mSharedPreferences.getFloat(Date + "BREAKFAST_FAT_VALUE", 0);
                mLunchCaloriesValue = mSharedPreferences.getFloat(Date + "LUNCH_CALORIES_VALUE", 0);
                mLunchCarbValue = mSharedPreferences.getFloat(Date + "LUNCH_CARB_VALUE", 0);
                mLunchProteinValue = mSharedPreferences.getFloat(Date + "LUNCH_PROTEIN_VALUE", 0);
                mLunchFatValue = mSharedPreferences.getFloat(Date + "LUNCH_FAT_VALUE", 0);
                mDinnerCaloriesValue = mSharedPreferences.getFloat(Date + "DINNER_CALORIES_VALUE", 0);
                mDinnerCarbValue = mSharedPreferences.getFloat(Date + "DINNER_CARB_VALUE", 0);
                mDinnerProteinValue = mSharedPreferences.getFloat(Date + "DINNER_PROTEIN_VALUE", 0);
                mDinnerFatValue = mSharedPreferences.getFloat(Date + "DINNER_FAT_VALUE", 0);

                // Update the UI with the nutrition data for the new date
                currentDate.setText(Date);

                String dailyCaloriesValue = mCaloriesValue + " kCal";
                dailyCaloriesTextView.setText(dailyCaloriesValue);

                String goalCaloriesValue = mGoalValue + " kCal";
                goalCaloriesTextView.setText(goalCaloriesValue);

                int calories_percentage = (int) (((float) mCaloriesValue / (float) mGoalValue) * 100);
                calories.setProgress(calories_percentage);

                int carbs_percentage = (int) (((float) mCarbsValue / (float) carbs_goal) * 100);
                mCarbsProgressBar.setProgress(carbs_percentage);
                String carbsText = mCarbsValue + "g\nCarb";
                mCarbsTextView.setText(carbsText);

                int protein_percentage = (int) (((float) mProteinValue / (float) protein_goal) * 100);
                mProteinProgressBar.setProgress(protein_percentage);
                String proteinText = mProteinValue + "g\nProtein";
                mProteinTextView.setText(proteinText);

                int fat_percentage = (int) (((float) mFatValue / (float) fat_goal) * 100);
                mFatsProgressBar.setProgress(fat_percentage);
                String fatsText = mFatValue + "g\nFat";
                mFatsTextView.setText(fatsText);

                breakfastCaloriesTextView.setText(Float.toString(mBreakfastCaloriesValue) + "kCal");
                breakfastCarbTextView.setText(Float.toString(mBreakfastCarbValue) + " g\nCarb");
                breakfastProteinTextView.setText(Float.toString(mBreakfastProteinValue) + " g\nProtein");
                breakfastFatTextView.setText(Float.toString(mBreakfastFatValue) + " g\nFat");

                lunchCaloriesTextView.setText(Float.toString(mLunchCaloriesValue) + "kCal");
                lunchCarbTextView.setText(Float.toString(mLunchCarbValue) + " g\nCarb");
                lunchProteinTextView.setText(Float.toString(mLunchProteinValue) + " g\nProtein");
                lunchFatTextView.setText(Float.toString(mLunchFatValue) + " g\nFat");

                dinnerCaloriesTextView.setText(Float.toString(mDinnerCaloriesValue) + "kCal");
                dinnerCarbTextView.setText(Float.toString(mDinnerCarbValue) + " g\nCarb");
                dinnerProteinTextView.setText(Float.toString(mDinnerProteinValue) + " g\nProtein");
                dinnerFatTextView.setText(Float.toString(mDinnerFatValue) + " g\nFat");

                // Save the selected date in SharedPreferences
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putString("selectedDate", currentDate.getText().toString());
                editor.apply();
            }
        });


        nextButton = findViewById(R.id.next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Subtract one day from the current date
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                try {
                    Date currentDate = dateFormat.parse(Date);
                    calendar.setTime(currentDate);
                    calendar.add(Calendar.DAY_OF_YEAR, +1);
                    Date = dateFormat.format(calendar.getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                // Retrieve the nutrition data for the new date from SharedPreferences
                mCaloriesValue = mSharedPreferences.getFloat(Date + "CALORIES_VALUE", 0);
                mGoalValue = mSharedPreferences.getFloat(Date + "GOAL_VALUE", 0);
                mCarbsValue = mSharedPreferences.getFloat(Date + "CARBS_VALUE", 0);
                mProteinValue = mSharedPreferences.getFloat(Date + "PROTEIN_VALUE", 0);
                mFatValue = mSharedPreferences.getFloat(Date + "FAT_VALUE", 0);
                mBreakfastCaloriesValue = mSharedPreferences.getFloat(Date + "BREAKFAST_CALORIES_VALUE", 0);
                mBreakfastCarbValue = mSharedPreferences.getFloat(Date + "BREAKFAST_CARB_VALUE", 0);
                mBreakfastProteinValue = mSharedPreferences.getFloat(Date + "BREAKFAST_PROTEIN_VALUE", 0);
                mBreakfastFatValue = mSharedPreferences.getFloat(Date + "BREAKFAST_FAT_VALUE", 0);
                mLunchCaloriesValue = mSharedPreferences.getFloat(Date + "LUNCH_CALORIES_VALUE", 0);
                mLunchCarbValue = mSharedPreferences.getFloat(Date + "LUNCH_CARB_VALUE", 0);
                mLunchProteinValue = mSharedPreferences.getFloat(Date + "LUNCH_PROTEIN_VALUE", 0);
                mLunchFatValue = mSharedPreferences.getFloat(Date + "LUNCH_FAT_VALUE", 0);
                mDinnerCaloriesValue = mSharedPreferences.getFloat(Date + "DINNER_CALORIES_VALUE", 0);
                mDinnerCarbValue = mSharedPreferences.getFloat(Date + "DINNER_CARB_VALUE", 0);
                mDinnerProteinValue = mSharedPreferences.getFloat(Date + "DINNER_PROTEIN_VALUE", 0);
                mDinnerFatValue = mSharedPreferences.getFloat(Date + "DINNER_FAT_VALUE", 0);

                // Update the UI with the nutrition data for the new date
                currentDate.setText(Date);

                String dailyCaloriesValue = mCaloriesValue + " kCal";
                dailyCaloriesTextView.setText(dailyCaloriesValue);

                String goalCaloriesValue = mGoalValue + " kCal";
                goalCaloriesTextView.setText(goalCaloriesValue);

                int calories_percentage = (int) (((float) mCaloriesValue / (float) mGoalValue) * 100);
                calories.setProgress(calories_percentage);

                int carbs_percentage = (int) (((float) mCarbsValue / (float) carbs_goal) * 100);
                mCarbsProgressBar.setProgress(carbs_percentage);
                String carbsText = mCarbsValue + "g\nCarb";
                mCarbsTextView.setText(carbsText);

                int protein_percentage = (int) (((float) mProteinValue / (float) protein_goal) * 100);
                mProteinProgressBar.setProgress(protein_percentage);
                String proteinText = mProteinValue + "g\nProtein";
                mProteinTextView.setText(proteinText);

                int fat_percentage = (int) (((float) mFatValue / (float) fat_goal) * 100);
                mFatsProgressBar.setProgress(fat_percentage);
                String fatsText = mFatValue + "g\nFat";
                mFatsTextView.setText(fatsText);

                breakfastCaloriesTextView.setText(Float.toString(mBreakfastCaloriesValue) + "kCal");
                breakfastCarbTextView.setText(Float.toString(mBreakfastCarbValue) + " g\nCarb");
                breakfastProteinTextView.setText(Float.toString(mBreakfastProteinValue) + " g\nProtein");
                breakfastFatTextView.setText(Float.toString(mBreakfastFatValue) + " g\nFat");

                lunchCaloriesTextView.setText(Float.toString(mLunchCaloriesValue) + "kCal");
                lunchCarbTextView.setText(Float.toString(mLunchCarbValue) + " g\nCarb");
                lunchProteinTextView.setText(Float.toString(mLunchProteinValue) + " g\nProtein");
                lunchFatTextView.setText(Float.toString(mLunchFatValue) + " g\nFat");

                dinnerCaloriesTextView.setText(Float.toString(mDinnerCaloriesValue) + "kCal");
                dinnerCarbTextView.setText(Float.toString(mDinnerCarbValue) + " g\nCarb");
                dinnerProteinTextView.setText(Float.toString(mDinnerProteinValue) + " g\nProtein");
                dinnerFatTextView.setText(Float.toString(mDinnerFatValue) + " g\nFat");

                // Save the selected date in SharedPreferences
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putString("selectedDate", currentDate.getText().toString());
                editor.apply();
            }
        });

        drawGraph();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        String redirectOption = adapterView.getItemAtPosition(position).toString();
        Intent intent;

        switch (redirectOption) {
            case "Home Page":
                intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
                break;
            case "Weight management":
                intent = new Intent(this, WeightManagementActivity.class);
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

    private void drawGraph() {
        // Get the current date - 1 month
        // Get today's date in the format "yyyy-MM-dd"
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        Date startDate = calendar.getTime();

        // Get the goalCalories and actualCalories data for the last month
        ArrayList<Entry> goalEntries = new ArrayList<>();
        ArrayList<Entry> actualEntries = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            String date = dateFormat.format(startDate);
            float goalCalories = mSharedPreferences.getFloat(date + "GOAL_VALUE", 0);
            float actualCalories = mSharedPreferences.getFloat(date + "CALORIES_VALUE", 0);
            goalEntries.add(new Entry(i, goalCalories));
            actualEntries.add(new Entry(i, actualCalories));
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            startDate = calendar.getTime();
        }

        // Draw the graph
        LineChart lineChart = findViewById(R.id.nutritionChart);
        Graph(lineChart, goalEntries, actualEntries);
    }

    private void Graph(LineChart lineChart, ArrayList<Entry> goalEntries, ArrayList<Entry> actualEntries) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        Date startDate = calendar.getTime();
        // Set the data
        LineDataSet goalCaloriesDataSet = new LineDataSet(goalEntries, "Goal Calories");
        goalCaloriesDataSet.setColor(Color.RED);
        goalCaloriesDataSet.setCircleColor(Color.RED);

        LineDataSet actualCaloriesDataSet = new LineDataSet(actualEntries, "Actual Calories");
        actualCaloriesDataSet.setColor(Color.BLUE);
        actualCaloriesDataSet.setCircleColor(Color.BLUE);

        LineData lineData = new LineData(goalCaloriesDataSet, actualCaloriesDataSet);

        // Set the X-axis label formatter to show dates in "MMM dd" format
        final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd", Locale.getDefault());
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                calendar.setTime(startDate);
                calendar.add(Calendar.DAY_OF_MONTH, (int) value);
                Date date = calendar.getTime();
                return dateFormat.format(date);
            }
        });

        // Set the chart settings
        lineChart.setData(lineData);
        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setPinchZoom(true);
        lineChart.setDrawGridBackground(false);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getDescription().setEnabled(false);

        // Refresh the chart
        lineChart.invalidate();
    }

}