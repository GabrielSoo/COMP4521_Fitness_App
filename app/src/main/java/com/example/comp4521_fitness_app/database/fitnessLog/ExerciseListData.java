package com.example.comp4521_fitness_app.database.fitnessLog;

public class ExerciseListData {
    private static final String[] weightExercises = {
            "Bench Press",
            "Dumbbell Press",
            "Bicep Curls",
            "Squats",
            "Deadlifts",
            "Push-ups",
            "Pull-ups",
            "Plank",
            "Lunges",
            "Lat Pulldowns",
            "Shoulder Press",
            "Tricep Dips",
            "Leg Press",
            "Crunches",
            "Russian Twists",
            "Hip Thrusts",
            "Calf Raises",
            "Incline Bench Press",
            "Hammer Curls",
            "Leg Extensions",
            "Romanian Deadlifts",
            "Side Planks",
            "Dips",
            "Inverted Rows",
            "Hip Abduction",
            "Seated Row",
            "Side Lateral Raises",
            "Leg Curl",
            "Hanging Leg Raises",
            "Cable Flyes",
            "Cable Crunches"
    };

    private static final String[] cardioExercises = {
            "Running",
            "Cycling",
            "Swimming",
            "Treadmill",
            "Burpees",
            "Jumping Jacks",
            "Mountain Climbers",
            "Jump Rope",
            "Box Jumps",
            "Rowing Machine",
            "Elliptical Trainer",
            "Stair Climber",
            "Kettlebell Swings",
            "High Knees",
            "Sprints",
            "Battle Ropes",
            "Flutter Kicks",
            "Bicycle Crunch"
    };

    public static String[] getWeightExercises() {
        return weightExercises;
    }

    public static String[] getCardioExercises() {
        return cardioExercises;
    }
}

