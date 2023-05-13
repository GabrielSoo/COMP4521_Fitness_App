package com.example.comp4521_fitness_app.database.fitnessLog;

public class RoutineData {
    public int routineId;
    public int[] exerciseIds;
    public String routineName;

    public RoutineData(int routineId, int[] exerciseIds, String routineName) {
        this.routineId = routineId;
        this.exerciseIds = exerciseIds;
        this.routineName = routineName;
    }
}
