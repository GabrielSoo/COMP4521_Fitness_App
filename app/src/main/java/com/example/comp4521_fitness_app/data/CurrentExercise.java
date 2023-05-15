package com.example.comp4521_fitness_app.data;

public class CurrentExercise {
    private static CurrentExercise instance = null;
    private int id;

    private CurrentExercise() {
        // Private constructor to prevent instantiation from outside
    }

    public static CurrentExercise getInstance() {
        if (instance == null) {
            instance = new CurrentExercise();
        }
        return instance;
    }

    public void setExerciseId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
