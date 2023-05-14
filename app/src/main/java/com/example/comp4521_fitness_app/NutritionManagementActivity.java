package com.example.comp4521_fitness_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.comp4521_fitness_app.data.CurrentUser;
import com.example.comp4521_fitness_app.database.nutritionLog.NutritionLogDBHelper;
import com.example.comp4521_fitness_app.database.nutritionLog.NutritionLogData;
import com.example.comp4521_fitness_app.database.weightLog.WeightLogData;
import com.example.comp4521_fitness_app.nutritionActivity.BreakfastActivity;
import com.example.comp4521_fitness_app.nutritionActivity.DinnerActivity;
import com.example.comp4521_fitness_app.nutritionActivity.LunchActivity;
import com.example.comp4521_fitness_app.utilities.DateUtils;

import java.util.Arrays;

public class NutritionManagementActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner mSpinnerRedirect;
    private Button logButton;
    private TextView dailyCaloriesTextView, goalCaloriesTextView;
    private ProgressBar calories;
    private TextView mCarbsTextView, mProteinTextView, mFatsTextView;
    private ProgressBar mCarbsProgressBar, mProteinProgressBar, mFatsProgressBar;
    private TextView breakfastCaloriesTextView, breakfastCarbTextView, breakfastProteinTextView, breakfastFatTextView;
    private TextView lunchCaloriesTextView, lunchCarbTextView, lunchProteinTextView, lunchFatTextView;
    private TextView dinnerCaloriesTextView, dinnerCarbTextView, dinnerProteinTextView, dinnerFatTextView;
    private Button mBreakfastButton, mLunchButton, mDinnerButton;

    private float mCaloriesValue = 0, mGoalValue = 3000, mCarbsValue = 0, mProteinValue = 0, mFatValue = 0;
    private float mBreakfastCaloriesValue = 0, mBreakfastCarbValue = 0, mBreakfastProteinValue = 0, mBreakfastFatValue = 0;
    private float mLunchCaloriesValue = 0, mLunchCarbValue = 0, mLunchProteinValue = 0, mLunchFatValue = 0;
    private float mDinnerCaloriesValue = 0, mDinnerCarbValue = 0, mDinnerProteinValue = 0, mDinnerFatValue = 0;
    private SharedPreferences mSharedPreferences;
    // data
    private NutritionLogData latestData;
    private String username;
    private NutritionLogDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition_management);

        dbHelper = new NutritionLogDBHelper(this);

        mSpinnerRedirect = findViewById(R.id.spinner_redirect);
        mSpinnerRedirect.setOnItemSelectedListener(this);

        // Initialize the views
        dailyCaloriesTextView = findViewById(R.id.text_daily_calories_value);
        goalCaloriesTextView = findViewById(R.id.text_goal_calories_value);
        mCarbsTextView = findViewById(R.id.carbs_text_view);
        mProteinTextView = findViewById(R.id.protein_text_view);
        mFatsTextView = findViewById(R.id.fats_text_view);
        breakfastCaloriesTextView = findViewById(R.id.breakfastCalories);
        breakfastCarbTextView = findViewById(R.id.breakfastCarbs);
        breakfastProteinTextView = findViewById(R.id.breakfastProteins);
        breakfastFatTextView = findViewById(R.id.breakfastFats);
        lunchCaloriesTextView = findViewById(R.id.lunchCalories);
        lunchCarbTextView = findViewById(R.id.lunchCarbs);
        lunchProteinTextView = findViewById(R.id.lunchProteins);
        lunchFatTextView = findViewById(R.id.lunchFats);
        dinnerCaloriesTextView = findViewById(R.id.dinnerCalories);
        dinnerCarbTextView = findViewById(R.id.dinnerCarbs);
        dinnerProteinTextView = findViewById(R.id.dinnerProteins);
        dinnerFatTextView = findViewById(R.id.dinnerFats);
        logButton = findViewById(R.id.logButton);

        // Get the username from the previous activity
        username = CurrentUser.getInstance().getUsername();

        // Set the default selection to "Nutrition management"
        String[] redirectOptions = getResources().getStringArray(R.array.redirect_options);
        int nutritionManagementIndex = Arrays.asList(redirectOptions).indexOf("Nutrition management");
        mSpinnerRedirect.setSelection(nutritionManagementIndex);

        // Get the saved nutrition data
        if (dailyCaloriesTextView != null && !TextUtils.isEmpty(dailyCaloriesTextView.getText().toString())) {
            mCaloriesValue = Float.parseFloat(dailyCaloriesTextView.getText().toString());
        } else {
            // handle the case where dailyCaloriesTextView is null or empty
            mCaloriesValue = 0.0f;
        }
        if (mCarbsTextView != null && !TextUtils.isEmpty(mCarbsTextView.getText().toString())) {
            mCarbsValue = Float.parseFloat(mCarbsTextView.getText().toString());
        } else {
            mCarbsValue = 0.0f;
        }
        if (mProteinTextView != null && !TextUtils.isEmpty(mProteinTextView.getText().toString())) {
            mProteinValue = Float.parseFloat(mProteinTextView.getText().toString());
        } else {
            mProteinValue = 0.0f;
        }
        if (mFatsTextView != null && !TextUtils.isEmpty(mFatsTextView.getText().toString())) {
            mFatValue = Float.parseFloat(mFatsTextView.getText().toString());
        } else {
            mFatValue = 0.0f;
        }

        if (breakfastCaloriesTextView != null && !TextUtils.isEmpty(breakfastCaloriesTextView.getText().toString())) {
            mBreakfastCaloriesValue = Float.parseFloat(breakfastCaloriesTextView.getText().toString());
        } else {
            mBreakfastCaloriesValue = 0.0f;
        }
        if (breakfastCarbTextView != null && !TextUtils.isEmpty(breakfastCarbTextView.getText().toString())) {
            mBreakfastCarbValue = Float.parseFloat(breakfastCarbTextView.getText().toString());
        } else {
            mBreakfastCarbValue = 0.0f;
        }
        if (breakfastProteinTextView != null && !TextUtils.isEmpty(breakfastProteinTextView.getText().toString())) {
            mBreakfastProteinValue = Float.parseFloat(breakfastProteinTextView.getText().toString());
        } else {
            mBreakfastProteinValue = 0.0f;
        }
        if (breakfastFatTextView != null && !TextUtils.isEmpty(breakfastFatTextView.getText().toString())) {
            mBreakfastFatValue = Float.parseFloat(breakfastFatTextView.getText().toString());
        } else {
            mBreakfastFatValue = 0.0f;
        }

        if (lunchCaloriesTextView != null && !TextUtils.isEmpty(lunchCaloriesTextView.getText().toString())) {
            mLunchCaloriesValue = Float.parseFloat(lunchCaloriesTextView.getText().toString());
        } else {
            mLunchCaloriesValue = 0.0f;
        }
        if (lunchCarbTextView != null && !TextUtils.isEmpty(lunchCarbTextView.getText().toString())) {
            mLunchCarbValue = Float.parseFloat(lunchCarbTextView.getText().toString());
        } else {
            mLunchCarbValue = 0.0f;
        }
        if (lunchProteinTextView != null && !TextUtils.isEmpty(lunchProteinTextView.getText().toString())) {
            mLunchProteinValue = Float.parseFloat(lunchProteinTextView.getText().toString());
        } else {
            mLunchProteinValue = 0.0f;
        }
        if (lunchFatTextView != null && !TextUtils.isEmpty(lunchFatTextView.getText().toString())) {
            mLunchFatValue = Float.parseFloat(lunchFatTextView.getText().toString());
        } else {
            mLunchFatValue = 0.0f;
        }

        if (dinnerCaloriesTextView != null && !TextUtils.isEmpty(dinnerCaloriesTextView.getText().toString())) {
            mDinnerCaloriesValue = Float.parseFloat(dinnerCaloriesTextView.getText().toString());
        } else {
            mDinnerCaloriesValue = 0.0f;
        }
        if (dinnerCarbTextView != null && !TextUtils.isEmpty(dinnerCarbTextView.getText().toString())) {
            mDinnerCarbValue = Float.parseFloat(dinnerCarbTextView.getText().toString());
        } else {
            mDinnerCarbValue = 0.0f;
        }
        if (dinnerProteinTextView != null && !TextUtils.isEmpty(dinnerProteinTextView.getText().toString())) {
            mDinnerProteinValue = Float.parseFloat(dinnerProteinTextView.getText().toString());
        } else {
            mDinnerProteinValue = 0.0f;
        }
        if (dinnerFatTextView != null && !TextUtils.isEmpty(dinnerFatTextView.getText().toString())) {
            mDinnerFatValue = Float.parseFloat(dinnerFatTextView.getText().toString());
        } else {
            mDinnerFatValue = 0.0f;
        }

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
            breakfastCaloriesTextView.setText(String.format("%.1f", mBreakfastCaloriesValue) + "kCal");

            breakfastCarbTextView = findViewById(R.id.breakfastCarbs);
            breakfastCarbTextView.setText(String.format("%.1f", mBreakfastCarbValue) + " g\nCarb");

            breakfastProteinTextView = findViewById(R.id.breakfastProteins);
            breakfastProteinTextView.setText(String.format("%.1f", mBreakfastProteinValue) + " g\nProtein");

            breakfastFatTextView = findViewById(R.id.breakfastFats);
            breakfastFatTextView.setText(String.format("%.1f", mBreakfastFatValue) + " g\nFat");

            // Update the TextView with the new value
            lunchCaloriesTextView = findViewById(R.id.lunchCalories);
            lunchCaloriesTextView.setText(String.format("%.1f", mLunchCaloriesValue) + "kCal");

            lunchCarbTextView = findViewById(R.id.lunchCarbs);
            lunchCarbTextView.setText(String.format("%.1f", mLunchCarbValue) + " g\nCarb");

            lunchProteinTextView = findViewById(R.id.lunchProteins);
            lunchProteinTextView.setText(String.format("%.1f", mLunchProteinValue) + " g\nProtein");

            lunchFatTextView = findViewById(R.id.lunchFats);
            lunchFatTextView.setText(String.format("%.1f", mLunchFatValue) + " g\nFat");

            // Update the TextView with the new value
            dinnerCaloriesTextView = findViewById(R.id.dinnerCalories);
            dinnerCaloriesTextView.setText(String.format("%.1f", mDinnerCaloriesValue) + "kCal");

            dinnerCarbTextView = findViewById(R.id.dinnerCarbs);
            dinnerCarbTextView.setText(String.format("%.1f", mDinnerCarbValue) + " g\nCarb");

            dinnerProteinTextView = findViewById(R.id.dinnerProteins);
            dinnerProteinTextView.setText(String.format("%.1f", mDinnerProteinValue) + " g\nProtein");

            dinnerFatTextView = findViewById(R.id.dinnerFats);
            dinnerFatTextView.setText(String.format("%.1f", mDinnerFatValue) + " g\nFat");
        }

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
        String dailyCaloriesValue = mCaloriesValue + " kcal";
        dailyCaloriesTextView.setText(dailyCaloriesValue);

        // Find the TextView for daily calories and set its text dynamically
        goalCaloriesTextView = findViewById(R.id.text_goal_calories_value);
        String goalCaloriesValue = mGoalValue + " kcal";
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

        updateUI();
        logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logNutrition();
            }
        });
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

    public void updateUI() {
        // Fill in fields with latest weight log data for this user
        latestData = dbHelper.getLatestNutritionLogData(username);
        if (latestData != null) {
            // Fill in daily calories
            dailyCaloriesTextView.setText(String.valueOf(latestData.actualCalories));
            goalCaloriesTextView.setText(String.valueOf(latestData.goalCalories));
            // Fill in macros
            mCarbsTextView.setText(String.valueOf(latestData.carbsValue));
            mProteinTextView.setText(String.valueOf(latestData.proteinValue));
            mFatsTextView.setText(String.valueOf(latestData.fatValue));
            // Fill in breakfast data
            breakfastCaloriesTextView.setText(String.valueOf(latestData.breakfastCaloriesValue));
            breakfastCarbTextView.setText(String.valueOf(latestData.breakfastCarbValue));
            breakfastProteinTextView.setText(String.valueOf(latestData.breakfastProteinValue));
            breakfastFatTextView.setText(String.valueOf(latestData.breakfastFatValue));
            // Fill in lunch data
            lunchCaloriesTextView.setText(String.valueOf(latestData.lunchCaloriesValue));
            lunchCarbTextView.setText(String.valueOf(latestData.lunchCarbValue));
            lunchProteinTextView.setText(String.valueOf(latestData.lunchProteinValue));
            lunchFatTextView.setText(String.valueOf(latestData.lunchFatValue));
            // Fill in dinner data
            dinnerCaloriesTextView.setText(String.valueOf(latestData.dinnerCaloriesValue));
            dinnerCarbTextView.setText(String.valueOf(latestData.dinnerCarbValue));
            dinnerProteinTextView.setText(String.valueOf(latestData.dinnerProteinValue));
            dinnerFatTextView.setText(String.valueOf(latestData.dinnerFatValue));
        }
    }

    public void logNutrition(){
        String CaloriesStr = dailyCaloriesTextView.getText().toString();
        String CarbStr = mCarbsTextView.getText().toString();
        String ProteinStr = mProteinTextView.getText().toString();
        String FatStr = mFatsTextView.getText().toString();
        String BreakfastCaloriesStr = breakfastCaloriesTextView.getText().toString();
        String BreakfastCarbStr = breakfastCarbTextView.getText().toString();
        String BreakfastProteinStr = breakfastProteinTextView.getText().toString();
        String BreakfastFatStr = breakfastFatTextView.getText().toString();
        String LunchCaloriesStr = lunchCaloriesTextView.getText().toString();
        String LunchCarbStr = lunchCarbTextView.getText().toString();
        String LunchProteinStr = lunchProteinTextView.getText().toString();
        String LunchFatStr = lunchFatTextView.getText().toString();
        String DinnerCaloriesStr = dinnerCaloriesTextView.getText().toString();
        String DinnerCarbStr = dinnerCarbTextView.getText().toString();
        String DinnerProteinStr = dinnerProteinTextView.getText().toString();
        String DinnerFatStr = dinnerFatTextView.getText().toString();

        float calories;
        if (CaloriesStr != null && !TextUtils.isEmpty(CaloriesStr)) {
            CaloriesStr = CaloriesStr.replaceAll("[^\\d.]", "");
            calories = Float.parseFloat(CaloriesStr);
        } else {
            // handle the case where CaloriesStr is null or empty
            calories = 0.0f;
        }

        float carb;
        if (CarbStr != null && !TextUtils.isEmpty(CarbStr)) {
            CarbStr = CarbStr.replaceAll("[^\\d.]", "");
            carb = Float.parseFloat(CarbStr);
        } else {
            // handle the case where CarbStr is null or empty
            carb = 0.0f;
        }

        float protein;
        if (ProteinStr != null && !TextUtils.isEmpty(ProteinStr)) {
            ProteinStr = ProteinStr.replaceAll("[^\\d.]", "");
            protein = Float.parseFloat(ProteinStr);
        } else {
            // handle the case where ProteinStr is null or empty
            protein = 0.0f;
        }

        float fat;
        if (FatStr != null && !TextUtils.isEmpty(FatStr)) {
            FatStr = FatStr.replaceAll("[^\\d.]", "");
            fat = Float.parseFloat(FatStr);
        } else {
            // handle the case where FatStr is null or empty
            fat = 0.0f;
        }

        float breakfastCalories;
        if (BreakfastCaloriesStr != null && !TextUtils.isEmpty(BreakfastCaloriesStr)) {
            BreakfastCaloriesStr = BreakfastCaloriesStr.replaceAll("[^\\d.]", "");
            breakfastCalories = Float.parseFloat(BreakfastCaloriesStr);
        } else {
            // handle the case where BreakfastCaloriesStr is null or empty
            breakfastCalories = 0.0f;
        }

        float breakfastCarb;
        if (BreakfastCarbStr != null && !TextUtils.isEmpty(BreakfastCarbStr)) {
            BreakfastCarbStr = BreakfastCarbStr.replaceAll("[^\\d.]", "");
            breakfastCarb = Float.parseFloat(BreakfastCarbStr);
        } else {
            // handle the case where BreakfastCarbStr is null or empty
            breakfastCarb = 0.0f;
        }

        float breakfastProtein;
        if (BreakfastProteinStr != null && !TextUtils.isEmpty(BreakfastProteinStr)) {
            BreakfastProteinStr = BreakfastProteinStr.replaceAll("[^\\d.]", "");
            breakfastProtein = Float.parseFloat(BreakfastProteinStr);
        } else {
            // handle the case where BreakfastProteinStr is null or empty
            breakfastProtein = 0.0f;
        }

        float breakfastFat;
        if (BreakfastFatStr != null && !TextUtils.isEmpty(BreakfastFatStr)) {
            BreakfastFatStr = BreakfastFatStr.replaceAll("[^\\d.]", "");
            breakfastFat = Float.parseFloat(BreakfastFatStr);
        } else {
            // handle the case where BreakfastFatStr is null or empty
            breakfastFat = 0.0f;
        }

        float lunchCalories;
        if (LunchCaloriesStr != null && !TextUtils.isEmpty(LunchCaloriesStr)) {
            LunchCaloriesStr = LunchCaloriesStr.replaceAll("[^\\d.]", "");
            lunchCalories = Float.parseFloat(LunchCaloriesStr);
        } else {
            // handle the case where LunchCaloriesStr is null or empty
            lunchCalories = 0.0f;
        }

        float lunchCarb;
        if (LunchCarbStr != null && !TextUtils.isEmpty(LunchCarbStr)) {
            LunchCarbStr = LunchCarbStr.replaceAll("[^\\d.]", "");
            lunchCarb = Float.parseFloat(LunchCarbStr);
        } else {
            // handle the case where LunchCarbStr is null or empty
            lunchCarb = 0.0f;
        }

        float lunchProtein;
        if (LunchProteinStr != null && !TextUtils.isEmpty(LunchProteinStr)) {
            LunchProteinStr = LunchProteinStr.replaceAll("[^\\d.]", "");
            lunchProtein = Float.parseFloat(LunchProteinStr);
        } else {
            // handle the case where LunchProteinStr is null or empty
            lunchProtein = 0.0f;
        }

        float lunchFat;
        if (LunchFatStr != null && !TextUtils.isEmpty(LunchFatStr)) {
            LunchFatStr = LunchFatStr.replaceAll("[^\\d.]", "");
            lunchFat = Float.parseFloat(LunchFatStr);
        } else {
            // handle the case where LunchFatStr is null or empty
            lunchFat = 0.0f;
        }

        float dinnerCalories;
        if (DinnerCaloriesStr != null && !TextUtils.isEmpty(DinnerCaloriesStr)) {
            DinnerCaloriesStr = DinnerCaloriesStr.replaceAll("[^\\d.]", "");
            dinnerCalories = Float.parseFloat(DinnerCaloriesStr);
        } else {
            // handle the case where DinnerCaloriesStr is null or empty
            dinnerCalories = 0.0f;
        }

        float dinnerCarb;
        if (DinnerCarbStr != null && !TextUtils.isEmpty(DinnerCarbStr)) {
            DinnerCarbStr = DinnerCarbStr.replaceAll("[^\\d.]", "");
            dinnerCarb = Float.parseFloat(DinnerCarbStr);
        } else {
            // handle the case where DinnerCarbStr is null or empty
            dinnerCarb = 0.0f;
        }

        float dinnerProtein;
        if (DinnerProteinStr != null && !TextUtils.isEmpty(DinnerProteinStr)) {
            DinnerProteinStr = DinnerProteinStr.replaceAll("[^\\d.]", "");
            dinnerProtein = Float.parseFloat(DinnerProteinStr);
        } else {
            // handle the case where DinnerProteinStr is null or empty
            dinnerProtein = 0.0f;
        }

        float dinnerFat;
        if (DinnerFatStr != null && !TextUtils.isEmpty(DinnerFatStr)) {
            DinnerFatStr = DinnerFatStr.replaceAll("[^\\d.]", "");
            dinnerFat = Float.parseFloat(DinnerFatStr);
        } else {
            // handle the case where DinnerFatStr is null or empty
            dinnerFat = 0.0f;
        }

        String dateCreated = DateUtils.getDateTime();

        // Save data to database
        NutritionLogData data = new NutritionLogData(-1,
                                username,
                                calories,
                                latestData.goalCalories,
                                carb,
                                protein,
                                fat,
                                breakfastCalories,
                                breakfastCarb,
                                breakfastProtein,
                                breakfastFat,
                                lunchCalories,
                                lunchCarb,
                                lunchProtein,
                                lunchFat,
                                dinnerCalories,
                                dinnerCarb,
                                dinnerProtein,
                                dinnerFat,
                                dateCreated);
        dbHelper.insertNutritionLogData(data);

        // Show success message
        Toast.makeText(this, "Nutrition logged successfully", Toast.LENGTH_SHORT).show();
    }
}
