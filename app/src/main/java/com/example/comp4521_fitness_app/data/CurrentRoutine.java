package com.example.comp4521_fitness_app.data;

public class CurrentRoutine {
    private static CurrentRoutine instance = null;
    private int id;

    private CurrentRoutine() {
        // Private constructor to prevent instantiation from outside
    }

    public static CurrentRoutine getInstance() {
        if (instance == null) {
            instance = new CurrentRoutine();
        }
        return instance;
    }

    public void setRoutineId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
