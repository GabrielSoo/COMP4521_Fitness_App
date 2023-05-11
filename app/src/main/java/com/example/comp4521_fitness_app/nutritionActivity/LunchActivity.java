package com.example.comp4521_fitness_app.nutritionActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.comp4521_fitness_app.NutritionManagementActivity;
import com.example.comp4521_fitness_app.R;

public class LunchActivity extends AppCompatActivity {

    private Button mLunchButton1;
    private Button mLunchButton2;
    private Button mLunchButton3;
    private Button mLunchButton4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lunch);

        mLunchButton1 = findViewById(R.id.lunchButton1);
        mLunchButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int lunchCalories = getResources().getInteger(R.integer.lunch1_calories);
                int lunchFats = getResources().getInteger(R.integer.lunch1_fats);
                int lunchCarbs = getResources().getInteger(R.integer.lunch1_carbs);
                int lunchProteins = getResources().getInteger(R.integer.lunch1_proteins);
                Intent intent = new Intent(LunchActivity.this, NutritionManagementActivity.class);
                intent.putExtra("LUNCH_CALORIES", lunchCalories);
                intent.putExtra("LUNCH_CARBS", lunchCarbs);
                intent.putExtra("LUNCH_PROTEINS", lunchProteins);
                intent.putExtra("LUNCH_FATS", lunchFats);
                startActivity(intent);
            }
        });

        mLunchButton2 = findViewById(R.id.lunchButton2);
        mLunchButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int lunchCalories = getResources().getInteger(R.integer.lunch2_calories);
                int lunchFats = getResources().getInteger(R.integer.lunch2_fats);
                int lunchCarbs = getResources().getInteger(R.integer.lunch2_carbs);
                int lunchProteins = getResources().getInteger(R.integer.lunch2_proteins);
                Intent intent = new Intent(LunchActivity.this, NutritionManagementActivity.class);
                intent.putExtra("LUNCH_CALORIES", lunchCalories);
                intent.putExtra("LUNCH_CARBS", lunchCarbs);
                intent.putExtra("LUNCH_PROTEINS", lunchProteins);
                intent.putExtra("LUNCH_FATS", lunchFats);
                startActivity(intent);
            }
        });

        mLunchButton3 = findViewById(R.id.lunchButton3);
        mLunchButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int lunchCalories = getResources().getInteger(R.integer.lunch3_calories);
                int lunchFats = getResources().getInteger(R.integer.lunch3_fats);
                int lunchCarbs = getResources().getInteger(R.integer.lunch3_carbs);
                int lunchProteins = getResources().getInteger(R.integer.lunch3_proteins);
                Intent intent = new Intent(LunchActivity.this, NutritionManagementActivity.class);
                intent.putExtra("LUNCH_CALORIES", lunchCalories);
                intent.putExtra("LUNCH_CARBS", lunchCarbs);
                intent.putExtra("LUNCH_PROTEINS", lunchProteins);
                intent.putExtra("LUNCH_FATS", lunchFats);
                startActivity(intent);
            }
        });

        mLunchButton4 = findViewById(R.id.lunchButton4);
        mLunchButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int lunchCalories = getResources().getInteger(R.integer.lunch4_calories);
                int lunchFats = getResources().getInteger(R.integer.lunch4_fats);
                int lunchCarbs = getResources().getInteger(R.integer.lunch4_carbs);
                int lunchProteins = getResources().getInteger(R.integer.lunch4_proteins);
                Intent intent = new Intent(LunchActivity.this, NutritionManagementActivity.class);
                intent.putExtra("LUNCH_CALORIES", lunchCalories);
                intent.putExtra("LUNCH_CARBS", lunchCarbs);
                intent.putExtra("LUNCH_PROTEINS", lunchProteins);
                intent.putExtra("LUNCH_FATS", lunchFats);
                startActivity(intent);
            }
        });
    }
}