package com.example.comp4521_fitness_app.database.nutritionLog;

public class NutritionLogData {
    public int nutritionLogId;
    public String username;
    public float actualCalories;
    public float goalCalories;
    public String dateCreated;

    public NutritionLogData(int nutritionLogId, String username, float actualCalories, float goalCalories, String dateCreated) {
        this.nutritionLogId = nutritionLogId;
        this.username = username;
        this.actualCalories = actualCalories;
        this.goalCalories = goalCalories;
        this.dateCreated = dateCreated;
    }
}
