package com.example.comp4521_fitness_app.database.fitnessLog;

public class ExerciseSetData {
    public int exerciseSetId;
    public int logId;
    public int exerciseId;
    public Integer sets;
    public Integer reps;
    public Integer weight;
    public Integer caloriesBurned;
    public String dateCreated;

    public ExerciseSetData(int exerciseSetId, int logId, int exerciseId, Integer sets, Integer reps, Integer weight, Integer caloriesBurned) {
        this.exerciseSetId = exerciseSetId;
        this.logId = logId;
        this.exerciseId = exerciseId;
        this.sets = sets;
        this.reps = reps;
        this.weight = weight;
        this.caloriesBurned = caloriesBurned;
        dateCreated = null;
    }

    public void setDateCreated(String date) {
        this.dateCreated = date;
    }
}
