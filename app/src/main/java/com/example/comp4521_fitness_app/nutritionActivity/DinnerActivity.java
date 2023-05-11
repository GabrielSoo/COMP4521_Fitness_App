package com.example.comp4521_fitness_app.nutritionActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.comp4521_fitness_app.NutritionManagementActivity;
import com.example.comp4521_fitness_app.R;

public class DinnerActivity extends AppCompatActivity {

    private Button mDinnerButton1;
    private Button mDinnerButton2;
    private Button mDinnerButton3;
    private Button mDinnerButton4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dinner);

        mDinnerButton1 = findViewById(R.id.dinnerButton1);
        mDinnerButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int dinnerCalories = getResources().getInteger(R.integer.dinner1_calories);
                int dinnerFats = getResources().getInteger(R.integer.dinner1_fats);
                int dinnerCarbs = getResources().getInteger(R.integer.dinner1_carbs);
                int dinnerProteins = getResources().getInteger(R.integer.dinner1_proteins);
                Intent intent = new Intent(DinnerActivity.this, NutritionManagementActivity.class);
                intent.putExtra("DINNER_CALORIES", dinnerCalories);
                intent.putExtra("DINNER_CARBS", dinnerCarbs);
                intent.putExtra("DINNER_PROTEINS", dinnerProteins);
                intent.putExtra("DINNER_FATS", dinnerFats);
                startActivity(intent);
            }
        });

        mDinnerButton2 = findViewById(R.id.dinnerButton2);
        mDinnerButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int dinnerCalories = getResources().getInteger(R.integer.dinner2_calories);
                int dinnerFats = getResources().getInteger(R.integer.dinner2_fats);
                int dinnerCarbs = getResources().getInteger(R.integer.dinner2_carbs);
                int dinnerProteins = getResources().getInteger(R.integer.dinner2_proteins);
                Intent intent = new Intent(DinnerActivity.this, NutritionManagementActivity.class);
                intent.putExtra("DINNER_CALORIES", dinnerCalories);
                intent.putExtra("DINNER_CARBS", dinnerCarbs);
                intent.putExtra("DINNER_PROTEINS", dinnerProteins);
                intent.putExtra("DINNER_FATS", dinnerFats);
                startActivity(intent);
            }
        });

        mDinnerButton3 = findViewById(R.id.dinnerButton3);
        mDinnerButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int dinnerCalories = getResources().getInteger(R.integer.dinner3_calories);
                int dinnerFats = getResources().getInteger(R.integer.dinner3_fats);
                int dinnerCarbs = getResources().getInteger(R.integer.dinner3_carbs);
                int dinnerProteins = getResources().getInteger(R.integer.dinner3_proteins);
                Intent intent = new Intent(DinnerActivity.this, NutritionManagementActivity.class);
                intent.putExtra("DINNER_CALORIES", dinnerCalories);
                intent.putExtra("DINNER_CARBS", dinnerCarbs);
                intent.putExtra("DINNER_PROTEINS", dinnerProteins);
                intent.putExtra("DINNER_FATS", dinnerFats);
                startActivity(intent);
            }
        });

        mDinnerButton4 = findViewById(R.id.dinnerButton4);
        mDinnerButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int dinnerCalories = getResources().getInteger(R.integer.dinner4_calories);
                int dinnerFats = getResources().getInteger(R.integer.dinner4_fats);
                int dinnerCarbs = getResources().getInteger(R.integer.dinner4_carbs);
                int dinnerProteins = getResources().getInteger(R.integer.dinner4_proteins);
                Intent intent = new Intent(DinnerActivity.this, NutritionManagementActivity.class);
                intent.putExtra("DINNER_CALORIES", dinnerCalories);
                intent.putExtra("DINNER_CARBS", dinnerCarbs);
                intent.putExtra("DINNER_PROTEINS", dinnerProteins);
                intent.putExtra("DINNER_FATS", dinnerFats);
                startActivity(intent);
            }
        });
    }
}