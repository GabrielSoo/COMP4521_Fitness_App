package com.example.comp4521_fitness_app.database.nutritionLog;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.comp4521_fitness_app.database.weightLog.WeightLogData;

public class NutritionLogDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "nutritionlog.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "nutritionlog";
    public static final String COL_ID = "nutrition_log_id";
    public static final String COL_USERNAME = "username";
    public static final String COL_ACTUAL_CALORIES = "actual_calories";
    public static final String COL_GOAL_CALORIES = "goal_calories";
    public static final String COL_DATE_CREATED = "date_created";

    public NutritionLogDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USERNAME + " TEXT, " +
                COL_ACTUAL_CALORIES + " REAL, " +
                COL_GOAL_CALORIES + " REAL, " +
                COL_DATE_CREATED + " TEXT" +
                ");";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertNutritionLogData(NutritionLogData data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_USERNAME, data.username);
        values.put(COL_ACTUAL_CALORIES, data.actualCalories);
        values.put(COL_GOAL_CALORIES, data.goalCalories);
        values.put(COL_DATE_CREATED, data.dateCreated);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public NutritionLogData getLatestWeightLogData(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COL_ID, COL_ACTUAL_CALORIES, COL_GOAL_CALORIES, COL_DATE_CREATED},
                COL_USERNAME + "=?", new String[]{username}, null, null, COL_DATE_CREATED + " DESC", "1");
        if (cursor.moveToFirst()) {
            NutritionLogData nutritionLogData = new NutritionLogData(
                    cursor.getInt(0),   // nutritionLogId
                    username,
                    cursor.getFloat(1), // actualCalories
                    cursor.getFloat(2), // goalCalories
                    cursor.getString(3) // dateCreated
            );
            cursor.close();
            db.close();
            return nutritionLogData;
        } else {
            cursor.close();
            db.close();
            return null;
        }
    }
}
