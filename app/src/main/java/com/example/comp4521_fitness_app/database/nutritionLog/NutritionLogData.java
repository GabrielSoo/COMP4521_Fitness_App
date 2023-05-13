package com.example.comp4521_fitness_app.database.nutritionLog;

import java.util.Objects;

public class NutritionLogData {
    public int CaloriesLogId;
    public String username;
    public float actualCalories;
    public float goalCalories;
    public float carbsValue;
    public float proteinValue;
    public float fatValue;
    public float breakfastCaloriesValue;
    public float breakfastCarbValue;
    public float breakfastProteinValue;
    public float breakfastFatValue;
    public float lunchCaloriesValue;
    public float lunchCarbValue;
    public float lunchProteinValue;
    public float lunchFatValue;
    public float dinnerCaloriesValue;
    public float dinnerCarbValue;
    public float dinnerProteinValue;
    public float dinnerFatValue;
    public String dateCreated;

    public NutritionLogData(int CaloriesLogId, String username, float actualCalories, float goalCalories, float carbsValue, float proteinValue, float fatValue, float breakfastCaloriesValue, float breakfastCarbValue, float breakfastProteinValue, float breakfastFatValue, float lunchCaloriesValue, float lunchCarbValue, float lunchProteinValue, float lunchFatValue, float dinnerCaloriesValue, float dinnerCarbValue, float dinnerProteinValue, float dinnerFatValue, String dateCreated) {
        this.CaloriesLogId = CaloriesLogId;
        this.username = username;
        this.actualCalories = actualCalories;
        this.goalCalories = goalCalories;
        this.carbsValue = carbsValue;
        this.proteinValue = proteinValue;
        this.fatValue = fatValue;
        this.breakfastCaloriesValue = breakfastCaloriesValue;
        this.breakfastCarbValue = breakfastCarbValue;
        this.breakfastProteinValue = breakfastProteinValue;
        this.breakfastFatValue = breakfastFatValue;
        this.lunchCaloriesValue = lunchCaloriesValue;
        this.lunchCarbValue = lunchCarbValue;
        this.lunchProteinValue = lunchProteinValue;
        this.lunchFatValue = lunchFatValue;
        this.dinnerCaloriesValue = dinnerCaloriesValue;
        this.dinnerCarbValue = dinnerCarbValue;
        this.dinnerProteinValue = dinnerProteinValue;
        this.dinnerFatValue = dinnerFatValue;
        this.dateCreated = dateCreated;
    }
}

