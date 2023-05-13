package com.example.comp4521_fitness_app.database.fitnessLog;

public class ExerciseData {
    public int exerciseId;
    public String exerciseName;
    public String exerciseType;

    public ExerciseData(int exercise_id, String exercise_name, String exercise_type) {
        this.exerciseId = exercise_id;
        this.exerciseName = exercise_name;
        this.exerciseType = exercise_type;
    }
}
