package com.example.comp4521_fitness_app.database.weightLog;

import java.util.Objects;

public class WeightLogData {
    public int weightLogId;
    public String username;
    public float height;
    public String weightGoalType;
    public float actualWeight;
    public float goalWeight;
    public String dateCreated;

    public WeightLogData(int weightLogId, String username, float height, String weightGoalType, float actualWeight, float goalWeight, String dateCreated) {
        this.weightLogId = weightLogId;
        this.username = username;
        this.height = height;
        this.weightGoalType = weightGoalType;
        this.actualWeight = actualWeight;
        this.goalWeight = goalWeight;
        this.dateCreated = dateCreated;
    }
}
