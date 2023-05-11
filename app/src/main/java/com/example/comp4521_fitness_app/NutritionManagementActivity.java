package com.example.comp4521_fitness_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.comp4521_fitness_app.nutritionActivity.BreakfastActivity;
import com.example.comp4521_fitness_app.nutritionActivity.DinnerActivity;
import com.example.comp4521_fitness_app.nutritionActivity.LunchActivity;

import java.util.Arrays;

public class NutritionManagementActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner mSpinnerRedirect;
    private TextView dailyCaloriesTextView;
    private TextView goalCaloriesTextView;
    private ProgressBar calories;
    private TextView mCarbsTextView;
    private TextView mProteinTextView;
    private TextView mFatsTextView;
    private ProgressBar mCarbsProgressBar;
    private ProgressBar mProteinProgressBar;
    private ProgressBar mFatsProgressBar;
    private Button mBreakfastButton;
    private Button mLunchButton;
    private Button mDinnerButton;

    private int mCaloriesValue = 0;
    private int mGoalValue = 3000;
    private int mCarbsValue = 0;
    private int mProteinValue = 0;
    private int mFatValue = 0;
    private int mBreakfastCaloriesValue = 0;
    private int mBreakfastCarbValue = 0;
    private int mBreakfastProteinValue = 0;
    private int mBreakfastFatValue = 0;
    private int mLunchCaloriesValue = 0;
    private int mLunchCarbValue = 0;
    private int mLunchProteinValue = 0;
    private int mLunchFatValue = 0;
    private int mDinnerCaloriesValue = 0;
    private int mDinnerCarbValue = 0;
    private int mDinnerProteinValue = 0;
    private int mDinnerFatValue = 0;


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

        //mSharedPreferences.edit().clear().apply();

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

            // Get the saved nutrition data from SharedPreferences
            mCaloriesValue = mSharedPreferences.getInt("CALORIES_VALUE", 0);
            mCarbsValue = mSharedPreferences.getInt("CARBS_VALUE", 0);
            mProteinValue = mSharedPreferences.getInt("PROTEIN_VALUE", 0);
            mFatValue = mSharedPreferences.getInt("FAT_VALUE", 0);

            // Update the mBreakfastCaloriesValue with the value from the Intent
            mBreakfastCaloriesValue = mSharedPreferences.getInt("BREAKFAST_CALORIES_VALUE", 0);
            mBreakfastCarbValue = mSharedPreferences.getInt("BREAKFAST_CARB_VALUE", 0);
            mBreakfastProteinValue = mSharedPreferences.getInt("BREAKFAST_PROTEIN_VALUE", 0);
            mBreakfastFatValue = mSharedPreferences.getInt("BREAKFAST_FAT_VALUE", 0);
            mBreakfastCaloriesValue += breakfastCalories;
            mBreakfastCarbValue += breakfastCarbs;
            mBreakfastProteinValue += breakfastProteins;
            mBreakfastFatValue += breakfastFats;

            // Update the mLunchCaloriesValue with the value from the Intent
            mLunchCaloriesValue = mSharedPreferences.getInt("LUNCH_CALORIES_VALUE", 0);
            mLunchCarbValue = mSharedPreferences.getInt("LUNCH_CARB_VALUE", 0);
            mLunchProteinValue = mSharedPreferences.getInt("LUNCH_PROTEIN_VALUE", 0);
            mLunchFatValue = mSharedPreferences.getInt("LUNCH_FAT_VALUE", 0);
            mLunchCaloriesValue += lunchCalories;
            mLunchCarbValue += lunchCarbs;
            mLunchProteinValue += lunchProteins;
            mLunchFatValue += lunchFats;

            // Update the mLunchCaloriesValue with the value from the Intent
            mDinnerCaloriesValue = mSharedPreferences.getInt("DINNER_CALORIES_VALUE", 0);
            mDinnerCarbValue = mSharedPreferences.getInt("DINNER_CARB_VALUE", 0);
            mDinnerProteinValue = mSharedPreferences.getInt("DINNER_PROTEIN_VALUE", 0);
            mDinnerFatValue = mSharedPreferences.getInt("DINNER_FAT_VALUE", 0);
            mDinnerCaloriesValue += dinnerCalories;
            mDinnerCarbValue += dinnerCarbs;
            mDinnerProteinValue += dinnerProteins;
            mDinnerFatValue += dinnerFats;

            mCaloriesValue += breakfastCalories + lunchCalories + dinnerCalories;
            mCarbsValue += breakfastCarbs + lunchCarbs + dinnerCarbs;
            mProteinValue += breakfastProteins + lunchProteins + dinnerProteins;
            mFatValue += breakfastFats + lunchFats + dinnerFats;

            // Save the updated nutrition data to SharedPreferences
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putInt("CALORIES_VALUE", mCaloriesValue);
            editor.putInt("CARBS_VALUE", mCarbsValue);
            editor.putInt("PROTEIN_VALUE", mProteinValue);
            editor.putInt("FAT_VALUE", mFatValue);
            editor.putInt("BREAKFAST_CALORIES_VALUE", mBreakfastCaloriesValue);
            editor.putInt("BREAKFAST_CARB_VALUE", mBreakfastCarbValue);
            editor.putInt("BREAKFAST_PROTEIN_VALUE", mBreakfastProteinValue);
            editor.putInt("BREAKFAST_FAT_VALUE", mBreakfastFatValue);
            editor.putInt("LUNCH_CALORIES_VALUE", mLunchCaloriesValue);
            editor.putInt("LUNCH_CARB_VALUE", mLunchCarbValue);
            editor.putInt("LUNCH_PROTEIN_VALUE", mLunchProteinValue);
            editor.putInt("LUNCH_FAT_VALUE", mLunchFatValue);
            editor.putInt("DINNER_CALORIES_VALUE", mDinnerCaloriesValue);
            editor.putInt("DINNER_CARB_VALUE", mDinnerCarbValue);
            editor.putInt("DINNER_PROTEIN_VALUE", mDinnerProteinValue);
            editor.putInt("DINNER_FAT_VALUE", mDinnerFatValue);
            editor.apply();

            // Update the TextView with the new value
            TextView breakfastCaloriesTextView = findViewById(R.id.breakfastCalories);
            breakfastCaloriesTextView.setText(Integer.toString(mBreakfastCaloriesValue) + "kCal");

            TextView breakfastCarbTextView = findViewById(R.id.breakfastCarbs);
            breakfastCarbTextView.setText(Integer.toString(mBreakfastCarbValue) + " g\nCarb");

            TextView breakfastProteinTextView = findViewById(R.id.breakfastProteins);
            breakfastProteinTextView.setText(Integer.toString(mBreakfastProteinValue) + " g\nProtein");

            TextView breakfastFatTextView = findViewById(R.id.breakfastFats);
            breakfastFatTextView.setText(Integer.toString(mBreakfastFatValue) + " g\nFat");

            // Update the TextView with the new value
            TextView lunchCaloriesTextView = findViewById(R.id.lunchCalories);
            lunchCaloriesTextView.setText(Integer.toString(mLunchCaloriesValue) + "kCal");

            TextView lunchCarbTextView = findViewById(R.id.lunchCarbs);
            lunchCarbTextView.setText(Integer.toString(mLunchCarbValue) + " g\nCarb");

            TextView lunchProteinTextView = findViewById(R.id.lunchProteins);
            lunchProteinTextView.setText(Integer.toString(mLunchProteinValue) + " g\nProtein");

            TextView lunchFatTextView = findViewById(R.id.lunchFats);
            lunchFatTextView.setText(Integer.toString(mLunchFatValue) + " g\nFat");

            // Update the TextView with the new value
            TextView dinnerCaloriesTextView = findViewById(R.id.dinnerCalories);
            dinnerCaloriesTextView.setText(Integer.toString(mDinnerCaloriesValue) + "kCal");

            TextView dinnerCarbTextView = findViewById(R.id.dinnerCarbs);
            dinnerCarbTextView.setText(Integer.toString(mDinnerCarbValue) + " g\nCarb");

            TextView dinnerProteinTextView = findViewById(R.id.dinnerProteins);
            dinnerProteinTextView.setText(Integer.toString(mDinnerProteinValue) + " g\nProtein");

            TextView dinnerFatTextView = findViewById(R.id.dinnerFats);
            dinnerFatTextView.setText(Integer.toString(mDinnerFatValue) + " g\nFat");
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
