package com.example.comp4521_fitness_app.database.fitnessLog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.comp4521_fitness_app.data.CurrentUser;
import com.example.comp4521_fitness_app.utilities.DateUtils;

import java.util.ArrayList;
import java.util.List;

public class FitnessLogDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "fitness_log.db";
    private static final int DATABASE_VERSION = 1;

    // Table names
    private static final String TABLE_ACTIVITY_LOG = "activity_log";
    private static final String TABLE_ROUTINE = "routine";
    private static final String TABLE_EXERCISE_ROUTINE = "exercise_routine";
    private static final String TABLE_EXERCISE = "exercise";
    private static final String TABLE_EXERCISE_SET = "exercise_set";


    // Activity_log table columns
    private static final String COLUMN_LOG_ID = "log_id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_ROUTINE_ID = "routine_id";
    private static final String COLUMN_DATE_CREATED = "date_created";

    // Routine table columns
    private static final String COLUMN_ROUTINE_NAME = "routine_name";
    private static final String COLUMN_EXERCISE_ID = "exercise_id";

    // Exercise table columns
    private static final String COLUMN_EXERCISE_NAME = "exercise_name";
    private static final String COLUMN_EXERCISE_TYPE = "exercise_type";

    // Exercise routine table columns
    private static final String COLUMN_EXERCISE_ROUTINE_ID = "exercise_routine_id";

    // Exercise_set table columns
    private static final String COLUMN_EXERCISE_SET_ID = "exercise_set_id";
    private static final String COLUMN_SETS = "sets";
    private static final String COLUMN_REPS = "reps";
    private static final String COLUMN_WEIGHT = "weight";
    private static final String COLUMN_CALORIES_BURNED = "calories_burned";

    private String currentUser = CurrentUser.getInstance().getUsername();

    public FitnessLogDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        addDefaultExercises(getWritableDatabase());
        addDefaultRoutine(getWritableDatabase());
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create tables
        String CREATE_ACTIVITY_LOG_TABLE = "CREATE TABLE " + TABLE_ACTIVITY_LOG + "("
                + COLUMN_LOG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USERNAME + " TEXT,"
                + COLUMN_EXERCISE_ROUTINE_ID + " INTEGER,"
                + COLUMN_DATE_CREATED + " TEXT,"
                + "FOREIGN KEY(" + COLUMN_EXERCISE_ROUTINE_ID + ") REFERENCES " + TABLE_EXERCISE_ROUTINE + "(" + COLUMN_EXERCISE_ROUTINE_ID + ")"
                + ")";
        db.execSQL(CREATE_ACTIVITY_LOG_TABLE);

        String CREATE_EXERCISE_ROUTINE_TABLE = "CREATE TABLE " + TABLE_EXERCISE_ROUTINE + "("
                + COLUMN_EXERCISE_ROUTINE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_ROUTINE_ID + " INTEGER, "
                + COLUMN_EXERCISE_ID + " INTEGER,"
                + "FOREIGN KEY(" + COLUMN_ROUTINE_ID + ") REFERENCES " + TABLE_ROUTINE + "(" + COLUMN_ROUTINE_ID + "), "
                + "FOREIGN KEY(" + COLUMN_EXERCISE_ID + ") REFERENCES " + TABLE_EXERCISE + "(" + COLUMN_EXERCISE_ID + ")"
                + ")";
        db.execSQL(CREATE_EXERCISE_ROUTINE_TABLE);

        String CREATE_ROUTINE_TABLE = "CREATE TABLE " + TABLE_ROUTINE + "("
                + COLUMN_ROUTINE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USERNAME + " TEXT,"
                + COLUMN_ROUTINE_NAME + " TEXT"
                + ")";
        db.execSQL(CREATE_ROUTINE_TABLE);

        String CREATE_EXERCISE_TABLE = "CREATE TABLE " + TABLE_EXERCISE + "("
                + COLUMN_EXERCISE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_EXERCISE_NAME + " TEXT,"
                + COLUMN_EXERCISE_TYPE + " STRING"
                + ")";
        db.execSQL(CREATE_EXERCISE_TABLE);

        String CREATE_EXERCISE_SET_TABLE = "CREATE TABLE " + TABLE_EXERCISE_SET + "("
                + COLUMN_EXERCISE_SET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_LOG_ID + " INTEGER,"
                + COLUMN_EXERCISE_ID + " INTEGER,"
                + COLUMN_SETS + " INTEGER,"
                + COLUMN_REPS + " INTEGER,"
                + COLUMN_WEIGHT + " INTEGER,"
                + COLUMN_CALORIES_BURNED + " INTEGER,"
                + "FOREIGN KEY(" + COLUMN_LOG_ID + ") REFERENCES " + TABLE_ACTIVITY_LOG + "(" + COLUMN_LOG_ID + "),"
                + "FOREIGN KEY(" + COLUMN_EXERCISE_ID + ") REFERENCES " + TABLE_EXERCISE + "(" + COLUMN_EXERCISE_ID + ")"
                + ")";
        db.execSQL(CREATE_EXERCISE_SET_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older tables if they exist
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACTIVITY_LOG);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROUTINE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXERCISE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXERCISE_ROUTINE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXERCISE_SET);

        // Create new tables
        onCreate(db);
    }

    private void addDefaultExercises(SQLiteDatabase db) {
        String countQuery = "SELECT COUNT(*) FROM " + TABLE_EXERCISE;
        Cursor cursor = db.rawQuery(countQuery, null);
        int exerciseCount = 0;

        if (cursor.moveToFirst()) {
            exerciseCount = cursor.getInt(0);
        }

        cursor.close();

        if (exerciseCount == 0) {
            String[] weightExercises = ExerciseListData.getWeightExercises();
            String[] cardioExercises = ExerciseListData.getCardioExercises();

            for (String exercise : weightExercises) {
                addExercise(db, new ExerciseData(-1, exercise, "weights"));
            }

            for (String exercise : cardioExercises) {
                addExercise(db, new ExerciseData(-1, exercise, "cardio"));
            }
        }
    }

    private void addDefaultRoutine(SQLiteDatabase db) {
        // Check if the Routine table is empty for the current user
        String countQuery = "SELECT COUNT(*) FROM " + TABLE_ROUTINE +
                " WHERE " + COLUMN_USERNAME + " = ?";
        Cursor cursor = db.rawQuery(countQuery, new String[]{currentUser});
        cursor.moveToFirst();
        int rowCount = cursor.getInt(0);
        cursor.close();

        if (rowCount == 0) {
            // Add the default routine
            RoutineData routineData = new RoutineData(-1, new int[]{1, 2, 3}, "Default Routine");
            addRoutine(db, routineData);
        }
    }


    private void addExercise(SQLiteDatabase db, ExerciseData data) {
        ContentValues exerciseValues = new ContentValues();
        exerciseValues.put(COLUMN_EXERCISE_NAME, data.exerciseName);
        exerciseValues.put(COLUMN_EXERCISE_TYPE, data.exerciseType);

        db.insert(TABLE_EXERCISE, null, exerciseValues);
    }


    public void addRoutine(SQLiteDatabase db, RoutineData data) {
        // Insert the routine data into ROUTINE_TABLE
        ContentValues routineValues = new ContentValues();
        routineValues.put(COLUMN_USERNAME, currentUser);
        routineValues.put(COLUMN_ROUTINE_NAME, data.routineName);
        long routineId = db.insert(TABLE_ROUTINE, null, routineValues);

        // Insert the exercises into EXERCISE_ROUTINE_TABLE
        if (routineId != -1) { // Check if routine data was inserted successfully
            for (int exerciseId : data.exerciseIds) {
                ContentValues exerciseRoutineValues = new ContentValues();
                exerciseRoutineValues.put(COLUMN_ROUTINE_ID, routineId);
                exerciseRoutineValues.put(COLUMN_EXERCISE_ID, exerciseId);
                db.insert(TABLE_EXERCISE_ROUTINE, null, exerciseRoutineValues);
            }
        }
    }


    public List<RoutineData> getAllRoutines() {
        List<RoutineData> routines = new ArrayList<>();

        String selectQuery = "SELECT " +
                "r." + COLUMN_ROUTINE_ID + ", " +
                "r." + COLUMN_ROUTINE_NAME + ", " +
                "e." + COLUMN_EXERCISE_ID +
                " FROM " + TABLE_ROUTINE + " r" +
                " INNER JOIN " + TABLE_EXERCISE_ROUTINE + " er" +
                " ON r." + COLUMN_ROUTINE_ID + " = er." + COLUMN_ROUTINE_ID +
                " INNER JOIN " + TABLE_EXERCISE + " e" +
                " ON er." + COLUMN_EXERCISE_ID + " = e." + COLUMN_EXERCISE_ID +
                " WHERE r." + COLUMN_USERNAME + " = ?";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{currentUser});
        if (cursor.moveToFirst()) {
            int currentRoutineId = -1;
            List<Integer> exerciseIds = new ArrayList<>();
            String routineName;

            do {
                int routineId = cursor.getInt(cursor.getColumnIndex(COLUMN_ROUTINE_ID));
                routineName = cursor.getString(cursor.getColumnIndex(COLUMN_ROUTINE_NAME));
                int exerciseId = cursor.getInt(cursor.getColumnIndex(COLUMN_EXERCISE_ID));

                if (currentRoutineId != routineId) {
                    if (currentRoutineId != -1) {
                        RoutineData routine = new RoutineData(currentRoutineId, exerciseIds.stream().mapToInt(Integer::intValue).toArray(), routineName);
                        routines.add(routine);
                        exerciseIds.clear();
                    }

                    currentRoutineId = routineId;
                }

                exerciseIds.add(exerciseId);
            } while (cursor.moveToNext());

            // Add the last routine
            if (currentRoutineId != -1) {
                RoutineData routine = new RoutineData(currentRoutineId, exerciseIds.stream().mapToInt(Integer::intValue).toArray(), routineName);
                routines.add(routine);
            }
        }

        cursor.close();
        db.close();

        return routines;
    }

    public RoutineData getRoutineById(int routineId) {
        RoutineData routine = null;

        String selectQuery = "SELECT " +
                "r." + COLUMN_ROUTINE_NAME + ", " +
                "e." + COLUMN_EXERCISE_ID +
                " FROM " + TABLE_ROUTINE + " r" +
                " INNER JOIN " + TABLE_EXERCISE_ROUTINE + " er" +
                " ON r." + COLUMN_ROUTINE_ID + " = er." + COLUMN_ROUTINE_ID +
                " INNER JOIN " + TABLE_EXERCISE + " e" +
                " ON er." + COLUMN_EXERCISE_ID + " = e." + COLUMN_EXERCISE_ID +
                " WHERE r." + COLUMN_ROUTINE_ID + " = ?";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(routineId)});

        if (cursor.moveToFirst()) {
            String routineName = cursor.getString(cursor.getColumnIndex(COLUMN_ROUTINE_NAME));
            List<Integer> exerciseIds = new ArrayList<>();

            do {
                int exerciseId = cursor.getInt(cursor.getColumnIndex(COLUMN_EXERCISE_ID));
                exerciseIds.add(exerciseId);
            } while (cursor.moveToNext());

            routine = new RoutineData(routineId, exerciseIds.stream().mapToInt(Integer::intValue).toArray(), routineName);
        }

        cursor.close();
        db.close();

        return routine;
    }

    public void logActivity(ExerciseSetData[] exerciseSetDataArray) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues activityLogValues = new ContentValues();
        activityLogValues.put(COLUMN_USERNAME, currentUser);
        activityLogValues.put(COLUMN_DATE_CREATED, DateUtils.getDateTime()); // Define getCurrentDateTime() method to get the current date and time
        long logId = db.insert(TABLE_ACTIVITY_LOG, null, activityLogValues);

        if (logId != -1) { // Check if activity log data was inserted successfully
            for (ExerciseSetData exerciseSetData : exerciseSetDataArray) {
                ContentValues exerciseSetValues = new ContentValues();
                exerciseSetValues.put(COLUMN_LOG_ID, logId);
                exerciseSetValues.put(COLUMN_EXERCISE_ID, exerciseSetData.exerciseId);
                exerciseSetValues.put(COLUMN_SETS, exerciseSetData.sets);
                exerciseSetValues.put(COLUMN_REPS, exerciseSetData.reps);
                exerciseSetValues.put(COLUMN_WEIGHT, exerciseSetData.weight);
                exerciseSetValues.put(COLUMN_CALORIES_BURNED, exerciseSetData.caloriesBurned);
                db.insert(TABLE_EXERCISE_SET, null, exerciseSetValues);
            }
        }

        db.close();
    }
    public int[] getAllExerciseIds() {
        int[] exerciseIds = new int[0];

        String selectQuery = "SELECT " + COLUMN_EXERCISE_ID + " FROM " + TABLE_EXERCISE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            exerciseIds = new int[cursor.getCount()];
            int index = 0;

            do {
                exerciseIds[index++] = cursor.getInt(cursor.getColumnIndex(COLUMN_EXERCISE_ID));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return exerciseIds;
    }

    public int getRoutinesTableLength() {
        String countQuery = "SELECT COUNT(*) FROM " + TABLE_ROUTINE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = 0;

        if (cursor.moveToFirst()) {
            rowCount = cursor.getInt(0);
        }

        cursor.close();
        db.close();

        return rowCount;
    }

}
