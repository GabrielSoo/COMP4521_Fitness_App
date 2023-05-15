package com.example.comp4521_fitness_app.database.fitnessLog;

public class ActivityLogData {
    public int logId;
    public String username;
    public int exerciseRoutineId;
    public String dateCreated;

    public ActivityLogData(int logId, String username, int exerciseRoutineId, String dateCreated) {
        this.logId = logId;
        this.username = username;
        this.exerciseRoutineId = exerciseRoutineId;
        this.dateCreated = dateCreated;
    }
}
