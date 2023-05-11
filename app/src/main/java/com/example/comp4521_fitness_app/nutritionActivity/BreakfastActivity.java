package com.example.comp4521_fitness_app.nutritionActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.comp4521_fitness_app.NutritionManagementActivity;
import com.example.comp4521_fitness_app.R;

public class BreakfastActivity extends AppCompatActivity {

    private Button mBreakfastButton1;
    private Button mBreakfastButton2;
    private Button mBreakfastButton3;
    private Button mBreakfastButton4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breakfast);

        mBreakfastButton1 = findViewById(R.id.lunchButton1);
        mBreakfastButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int breakfastCalories = getResources().getInteger(R.integer.breakfast1_calories);
                int breakfastFats = getResources().getInteger(R.integer.breakfast1_fats);
                int breakfastCarbs = getResources().getInteger(R.integer.breakfast1_carbs);
                int breakfastProteins = getResources().getInteger(R.integer.breakfast1_proteins);
                Intent intent = new Intent(BreakfastActivity.this, NutritionManagementActivity.class);
                intent.putExtra("BREAKFAST_CALORIES", breakfastCalories);
                intent.putExtra("BREAKFAST_CARBS", breakfastCarbs);
                intent.putExtra("BREAKFAST_PROTEINS", breakfastProteins);
                intent.putExtra("BREAKFAST_FATS", breakfastFats);
                startActivity(intent);
            }
        });

        mBreakfastButton2 = findViewById(R.id.breakfastButton2);
        mBreakfastButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int breakfastCalories = getResources().getInteger(R.integer.breakfast2_calories);
                int breakfastFats = getResources().getInteger(R.integer.breakfast2_fats);
                int breakfastCarbs = getResources().getInteger(R.integer.breakfast2_carbs);
                int breakfastProteins = getResources().getInteger(R.integer.breakfast2_proteins);
                Intent intent = new Intent(BreakfastActivity.this, NutritionManagementActivity.class);
                intent.putExtra("BREAKFAST_CALORIES", breakfastCalories);
                intent.putExtra("BREAKFAST_CARBS", breakfastCarbs);
                intent.putExtra("BREAKFAST_PROTEINS", breakfastProteins);
                intent.putExtra("BREAKFAST_FATS", breakfastFats);
                startActivity(intent);
            }
        });

        mBreakfastButton3 = findViewById(R.id.breakfastButton3);
        mBreakfastButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int breakfastCalories = getResources().getInteger(R.integer.breakfast3_calories);
                int breakfastFats = getResources().getInteger(R.integer.breakfast3_fats);
                int breakfastCarbs = getResources().getInteger(R.integer.breakfast3_carbs);
                int breakfastProteins = getResources().getInteger(R.integer.breakfast3_proteins);
                Intent intent = new Intent(BreakfastActivity.this, NutritionManagementActivity.class);
                intent.putExtra("BREAKFAST_CALORIES", breakfastCalories);
                intent.putExtra("BREAKFAST_CARBS", breakfastCarbs);
                intent.putExtra("BREAKFAST_PROTEINS", breakfastProteins);
                intent.putExtra("BREAKFAST_FATS", breakfastFats);
                startActivity(intent);
            }
        });

        mBreakfastButton4 = findViewById(R.id.breakfastButton4);
        mBreakfastButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int breakfastCalories = getResources().getInteger(R.integer.breakfast4_calories);
                int breakfastFats = getResources().getInteger(R.integer.breakfast4_fats);
                int breakfastCarbs = getResources().getInteger(R.integer.breakfast4_carbs);
                int breakfastProteins = getResources().getInteger(R.integer.breakfast4_proteins);
                Intent intent = new Intent(BreakfastActivity.this, NutritionManagementActivity.class);
                intent.putExtra("BREAKFAST_CALORIES", breakfastCalories);
                intent.putExtra("BREAKFAST_CARBS", breakfastCarbs);
                intent.putExtra("BREAKFAST_PROTEINS", breakfastProteins);
                intent.putExtra("BREAKFAST_FATS", breakfastFats);
                startActivity(intent);
            }
        });
    }
}