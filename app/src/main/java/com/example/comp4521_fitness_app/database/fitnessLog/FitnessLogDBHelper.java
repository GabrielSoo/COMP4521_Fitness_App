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
                + COLUMN_ROUTINE_ID + " INTEGER,"
                + COLUMN_DATE_CREATED + " TEXT,"
                + "FOREIGN KEY(" + COLUMN_ROUTINE_ID + ") REFERENCES " + TABLE_ROUTINE + "(" + COLUMN_ROUTINE_ID + ")"
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

        SQLiteDatabase db = this.getReadableDatabase();

        // Retrieve all routine IDs for the current user
        String[] columns = {COLUMN_ROUTINE_ID};
        String selection = COLUMN_USERNAME + " = ?";
        String[] selectionArgs = {currentUser};
        Cursor routineCursor = db.query(TABLE_ROUTINE, columns, selection, selectionArgs, null, null, null);

        if (routineCursor.moveToFirst()) {
            do {
                int routineId = routineCursor.getInt(routineCursor.getColumnIndex(COLUMN_ROUTINE_ID));

                // Retrieve the routine name
                String[] nameColumns = {COLUMN_ROUTINE_NAME};
                String nameSelection = COLUMN_ROUTINE_ID + " = ?";
                String[] nameSelectionArgs = {String.valueOf(routineId)};
                Cursor nameCursor = db.query(TABLE_ROUTINE, nameColumns, nameSelection, nameSelectionArgs, null, null, null);

                String routineName = "";
                if (nameCursor.moveToFirst()) {
                    routineName = nameCursor.getString(nameCursor.getColumnIndex(COLUMN_ROUTINE_NAME));
                }
                nameCursor.close();

                // Retrieve the exercise IDs for the current routine
                String[] exerciseColumns = {COLUMN_EXERCISE_ID};
                String exerciseSelection = COLUMN_ROUTINE_ID + " = ?";
                String[] exerciseSelectionArgs = {String.valueOf(routineId)};
                Cursor exerciseCursor = db.query(TABLE_EXERCISE_ROUTINE, exerciseColumns, exerciseSelection, exerciseSelectionArgs, null, null, null);

                List<Integer> exerciseIds = new ArrayList<>();
                if (exerciseCursor.moveToFirst()) {
                    do {
                        int exerciseId = exerciseCursor.getInt(exerciseCursor.getColumnIndex(COLUMN_EXERCISE_ID));
                        exerciseIds.add(exerciseId);
                    } while (exerciseCursor.moveToNext());
                }
                exerciseCursor.close();

                // Create the RoutineData object and add it to the list
                RoutineData routine = new RoutineData(routineId, exerciseIds.stream().mapToInt(Integer::intValue).toArray(), routineName);
                routines.add(routine);

            } while (routineCursor.moveToNext());
        }
        routineCursor.close();
        db.close();

        return routines;
    }

    public RoutineData getRoutineById(int routineId) {
        RoutineData routine = null;
        SQLiteDatabase db = getReadableDatabase();

        // Retrieve the routine name
        String[] nameColumns = {COLUMN_ROUTINE_NAME};
        String nameSelection = COLUMN_ROUTINE_ID + " = ?";
        String[] nameSelectionArgs = {String.valueOf(routineId)};
        Cursor nameCursor = db.query(TABLE_ROUTINE, nameColumns, nameSelection, nameSelectionArgs, null, null, null);

        String routineName = "";
        if (nameCursor.moveToFirst()) {
            routineName = nameCursor.getString(nameCursor.getColumnIndex(COLUMN_ROUTINE_NAME));
        }
        nameCursor.close();

        // Retrieve the exercise IDs for the given routine
        String[] exerciseColumns = {COLUMN_EXERCISE_ID};
        String exerciseSelection = COLUMN_ROUTINE_ID + " = ?";
        String[] exerciseSelectionArgs = {String.valueOf(routineId)};
        Cursor exerciseCursor = db.query(TABLE_EXERCISE_ROUTINE, exerciseColumns, exerciseSelection, exerciseSelectionArgs, null, null, null);

        List<Integer> exerciseIds = new ArrayList<>();
        if (exerciseCursor.moveToFirst()) {
            do {
                int exerciseId = exerciseCursor.getInt(exerciseCursor.getColumnIndex(COLUMN_EXERCISE_ID));
                exerciseIds.add(exerciseId);
            } while (exerciseCursor.moveToNext());
        }
        exerciseCursor.close();

        // Create the RoutineData object
        if (!routineName.isEmpty() && !exerciseIds.isEmpty()) {
            routine = new RoutineData(routineId, exerciseIds.stream().mapToInt(Integer::intValue).toArray(), routineName);
        }

        db.close();

        return routine;
    }

    public List<ExerciseData> getExercisesByIds(int[] exerciseIds) {
        List<ExerciseData> exercises = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        // Create the selection criteria for exercise IDs
        StringBuilder selection = new StringBuilder();
        selection.append(COLUMN_EXERCISE_ID).append(" IN (");
        for (int i = 0; i < exerciseIds.length; i++) {
            selection.append(exerciseIds[i]);
            if (i < exerciseIds.length - 1) {
                selection.append(",");
            }
        }
        selection.append(")");

        // Query the exercise table with the selection criteria
        Cursor cursor = db.query(TABLE_EXERCISE, null, selection.toString(), null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int exerciseId = cursor.getInt(cursor.getColumnIndex(COLUMN_EXERCISE_ID));
                String exerciseName = cursor.getString(cursor.getColumnIndex(COLUMN_EXERCISE_NAME));
                String exerciseType = cursor.getString(cursor.getColumnIndex(COLUMN_EXERCISE_TYPE));

                ExerciseData exercise = new ExerciseData(exerciseId, exerciseName, exerciseType);
                exercises.add(exercise);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return exercises;
    }

    public void logRoutine(int routineId, ExerciseSetData[] exerciseSetDataArray) {
        SQLiteDatabase db = getWritableDatabase();

        // Insert activity log entry
        ContentValues activityLogValues = new ContentValues();
        activityLogValues.put(COLUMN_USERNAME, currentUser);
        activityLogValues.put(COLUMN_ROUTINE_ID, routineId);
        activityLogValues.put(COLUMN_DATE_CREATED, DateUtils.getDateTime());
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

    public List<ExerciseSetData> queryExerciseLogsByExerciseId(int exerciseId) {
        List<ExerciseSetData> exerciseLogs = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String[] columns = {
                TABLE_EXERCISE_SET + "." + COLUMN_LOG_ID,
                COLUMN_EXERCISE_ID,
                COLUMN_SETS,
                COLUMN_REPS,
                COLUMN_WEIGHT,
                COLUMN_CALORIES_BURNED,
                COLUMN_DATE_CREATED
        };

        String selection = TABLE_EXERCISE_SET + "." + COLUMN_EXERCISE_ID + " = ?";
        String[] selectionArgs = {String.valueOf(exerciseId)};
        String orderBy = COLUMN_DATE_CREATED + " ASC";

        String joinTable = TABLE_EXERCISE_SET + " INNER JOIN " + TABLE_ACTIVITY_LOG +
                " ON " + TABLE_EXERCISE_SET + "." + COLUMN_LOG_ID + " = " +
                TABLE_ACTIVITY_LOG + "." + COLUMN_LOG_ID;

        Cursor cursor = db.query(joinTable, columns, selection, selectionArgs, null, null, orderBy);

        if (cursor.moveToFirst()) {
            do {
                int logId = cursor.getInt(cursor.getColumnIndex(COLUMN_LOG_ID));
                int sets = cursor.getInt(cursor.getColumnIndex(COLUMN_SETS));
                int reps = cursor.getInt(cursor.getColumnIndex(COLUMN_REPS));
                int weight = cursor.getInt(cursor.getColumnIndex(COLUMN_WEIGHT));
                int caloriesBurned = cursor.getInt(cursor.getColumnIndex(COLUMN_CALORIES_BURNED));
                String dateCreated = cursor.getString(cursor.getColumnIndex(COLUMN_DATE_CREATED));

                ExerciseSetData exerciseSetData = new ExerciseSetData(-1, logId, exerciseId, sets, reps, weight, caloriesBurned);
                exerciseSetData.setDateCreated(dateCreated);
                exerciseLogs.add(exerciseSetData);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return exerciseLogs;
    }


    public List<ExerciseData> getAllExercises() {
        List<ExerciseData> exercises = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_EXERCISE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                int exerciseId = cursor.getInt(cursor.getColumnIndex(COLUMN_EXERCISE_ID));
                String exerciseName = cursor.getString(cursor.getColumnIndex(COLUMN_EXERCISE_NAME));
                String exerciseType = cursor.getString(cursor.getColumnIndex(COLUMN_EXERCISE_TYPE));

                ExerciseData exercise = new ExerciseData(exerciseId, exerciseName, exerciseType);
                exercises.add(exercise);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return exercises;
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

    public ExerciseData getExerciseById(int exerciseId) {
        ExerciseData exercise = null;
        SQLiteDatabase db = getReadableDatabase();

        String[] columns = {COLUMN_EXERCISE_ID, COLUMN_EXERCISE_NAME, COLUMN_EXERCISE_TYPE};
        String selection = COLUMN_EXERCISE_ID + " = ?";
        String[] selectionArgs = {String.valueOf(exerciseId)};
        Cursor cursor = db.query(TABLE_EXERCISE, columns, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            String exerciseName = cursor.getString(cursor.getColumnIndex(COLUMN_EXERCISE_NAME));
            String exerciseType = cursor.getString(cursor.getColumnIndex(COLUMN_EXERCISE_TYPE));

            exercise = new ExerciseData(exerciseId, exerciseName, exerciseType);
        }

        cursor.close();
        db.close();

        return exercise;
    }

}
