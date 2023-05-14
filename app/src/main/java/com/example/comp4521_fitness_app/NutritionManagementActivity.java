package com.example.comp4521_fitness_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

    private float mCaloriesValue, mGoalValue = 3000, mCarbsValue, mProteinValue, mFatValue;
    private float mBreakfastCaloriesValue, mBreakfastCarbValue, mBreakfastProteinValue, mBreakfastFatValue;
    private float mLunchCaloriesValue, mLunchCarbValue, mLunchProteinValue, mLunchFatValue;
    private float mDinnerCaloriesValue, mDinnerCarbValue, mDinnerProteinValue, mDinnerFatValue;
    private SharedPreferences mSharedPreferences;
    private String username;
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

        // Get the saved nutrition data from SharedPreferences
        mCaloriesValue = mSharedPreferences.getFloat("CALORIES_VALUE", 0);
        mCarbsValue = mSharedPreferences.getFloat("CARBS_VALUE", 0);
        mProteinValue = mSharedPreferences.getFloat("PROTEIN_VALUE", 0);
        mFatValue = mSharedPreferences.getFloat("FAT_VALUE", 0);
        mBreakfastCaloriesValue = mSharedPreferences.getFloat("BREAKFAST_CALORIES_VALUE", 0);
        mBreakfastCarbValue = mSharedPreferences.getFloat("BREAKFAST_CARB_VALUE", 0);
        mBreakfastProteinValue = mSharedPreferences.getFloat("BREAKFAST_PROTEIN_VALUE", 0);
        mBreakfastFatValue = mSharedPreferences.getFloat("BREAKFAST_FAT_VALUE", 0);
        mLunchCaloriesValue = mSharedPreferences.getFloat("LUNCH_CALORIES_VALUE", 0);
        mLunchCarbValue = mSharedPreferences.getFloat("LUNCH_CARB_VALUE", 0);
        mLunchProteinValue = mSharedPreferences.getFloat("LUNCH_PROTEIN_VALUE", 0);
        mLunchFatValue = mSharedPreferences.getFloat("LUNCH_FAT_VALUE", 0);
        mDinnerCaloriesValue = mSharedPreferences.getFloat("DINNER_CALORIES_VALUE", 0);
        mDinnerCarbValue = mSharedPreferences.getFloat("DINNER_CARB_VALUE", 0);
        mDinnerProteinValue = mSharedPreferences.getFloat("DINNER_PROTEIN_VALUE", 0);
        mDinnerFatValue = mSharedPreferences.getFloat("DINNER_FAT_VALUE", 0);

        // Get the username from the previous activity
        username = CurrentUser.getInstance().getUsername();
        logButton = findViewById(R.id.logButton);


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
        editor.putFloat("CALORIES_VALUE", mCaloriesValue);
        editor.putFloat("CARBS_VALUE", mCarbsValue);
        editor.putFloat("PROTEIN_VALUE", mProteinValue);
        editor.putFloat("FAT_VALUE", mFatValue);
        editor.putFloat("BREAKFAST_CALORIES_VALUE", mBreakfastCaloriesValue);
        editor.putFloat("BREAKFAST_CARB_VALUE", mBreakfastCarbValue);
        editor.putFloat("BREAKFAST_PROTEIN_VALUE", mBreakfastProteinValue);
        editor.putFloat("BREAKFAST_FAT_VALUE", mBreakfastFatValue);
        editor.putFloat("LUNCH_CALORIES_VALUE", mLunchCaloriesValue);
        editor.putFloat("LUNCH_CARB_VALUE", mLunchCarbValue);
        editor.putFloat("LUNCH_PROTEIN_VALUE", mLunchProteinValue);
        editor.putFloat("LUNCH_FAT_VALUE", mLunchFatValue);
        editor.putFloat("DINNER_CALORIES_VALUE", mDinnerCaloriesValue);
        editor.putFloat("DINNER_CARB_VALUE", mDinnerCarbValue);
        editor.putFloat("DINNER_PROTEIN_VALUE", mDinnerProteinValue);
        editor.putFloat("DINNER_FAT_VALUE", mDinnerFatValue);
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

        logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //DO NOTHING
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
}