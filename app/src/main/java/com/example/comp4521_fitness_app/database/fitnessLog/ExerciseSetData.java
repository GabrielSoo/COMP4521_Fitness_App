package com.example.comp4521_fitness_app.database.fitnessLog;

public class ExerciseSetData {
    public int exerciseSetId;
    public int logId;
    public int exerciseId;
    public int sets;
    public int reps;
    public int weight;
    public int caloriesBurned;

    public ExerciseSetData(int exerciseSetId, int logId, int exerciseId, int sets, int reps, int weight, int caloriesBurned) {
        this.exerciseSetId = exerciseSetId;
        this.logId = logId;
        this.exerciseId = exerciseId;
        this.sets = sets;
        this.reps = reps;
        this.weight = weight;
        this.caloriesBurned = caloriesBurned;
    }
}
