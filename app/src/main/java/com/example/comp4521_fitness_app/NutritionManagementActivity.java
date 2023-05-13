package com.example.comp4521_fitness_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
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
        dailyCaloriesTextView = findViewById(R.id.text_daily_calories);
        goalCaloriesTextView = findViewById(R.id.text_goal_calories);
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

        // Initialize SharedPreferences
        mSharedPreferences = getSharedPreferences("NutritionData", MODE_PRIVATE);

        mSharedPreferences.edit().clear().apply();

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
            mCaloriesValue = Integer.parseInt(dailyCaloriesTextView.getText().toString());
            mCarbsValue = Integer.parseInt(mCarbsTextView.getText().toString());
            mProteinValue = Integer.parseInt(mProteinTextView.getText().toString());
            mFatValue = Integer.parseInt(mFatsTextView.getText().toString());

            // Update the mBreakfastCaloriesValue with the value from the Intent
            mBreakfastCaloriesValue = Integer.parseInt(breakfastCaloriesTextView.getText().toString());
            mBreakfastCarbValue = Integer.parseInt(breakfastCarbTextView.getText().toString());
            mBreakfastProteinValue = Integer.parseInt(breakfastProteinTextView.getText().toString());
            mBreakfastFatValue = Integer.parseInt(breakfastFatTextView.getText().toString());
            mBreakfastCaloriesValue += breakfastCalories;
            mBreakfastCarbValue += breakfastCarbs;
            mBreakfastProteinValue += breakfastProteins;
            mBreakfastFatValue += breakfastFats;

            // Update the mLunchCaloriesValue with the value from the Intent
            mLunchCaloriesValue = Integer.parseInt(lunchCaloriesTextView.getText().toString());
            mLunchCarbValue = Integer.parseInt(lunchCarbTextView.getText().toString());
            mLunchProteinValue = Integer.parseInt(lunchProteinTextView.getText().toString());
            mLunchFatValue = Integer.parseInt(lunchFatTextView.getText().toString());
            mLunchCaloriesValue += lunchCalories;
            mLunchCarbValue += lunchCarbs;
            mLunchProteinValue += lunchProteins;
            mLunchFatValue += lunchFats;

            // Update the mLunchCaloriesValue with the value from the Intent
            mDinnerCaloriesValue = Integer.parseInt(dinnerCaloriesTextView.getText().toString());
            mDinnerCarbValue = Integer.parseInt(dinnerCarbTextView.getText().toString());
            mDinnerProteinValue = Integer.parseInt(dinnerProteinTextView.getText().toString());
            mDinnerFatValue = Integer.parseInt(dinnerFatTextView.getText().toString());
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

        float calories = Float.parseFloat(CaloriesStr);
        float carbs = Float.parseFloat(CarbStr);
        float protein = Float.parseFloat(ProteinStr);
        float fat = Float.parseFloat(FatStr);
        float breakfastCalories = Float.parseFloat(BreakfastCaloriesStr);
        float breakfastCarb = Float.parseFloat(BreakfastCarbStr);
        float breakfastProtein = Float.parseFloat(BreakfastProteinStr);
        float breakfastFat = Float.parseFloat(BreakfastFatStr);
        float lunchCalories = Float.parseFloat(LunchCaloriesStr);
        float lunchCarb = Float.parseFloat(LunchCarbStr);
        float lunchProtein = Float.parseFloat(LunchProteinStr);
        float lunchFat = Float.parseFloat(LunchFatStr);
        float dinnerCalories = Float.parseFloat(DinnerCaloriesStr);
        float dinnerCarb = Float.parseFloat(DinnerCarbStr);
        float dinnerProtein = Float.parseFloat(DinnerProteinStr);
        float dinnerFat = Float.parseFloat(DinnerFatStr);

        String dateCreated = DateUtils.getDateTime();

        // Save data to database
        NutritionLogData data = new NutritionLogData(-1,
                                username,
                                calories,
                                latestData.goalCalories,
                                carbs,
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
