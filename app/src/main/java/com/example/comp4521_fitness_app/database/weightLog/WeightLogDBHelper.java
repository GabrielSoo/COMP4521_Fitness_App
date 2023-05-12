package com.example.comp4521_fitness_app.database.weightLog;

import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class WeightLogDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "weightlog.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "weightlog";
    public static final String COL_ID = "weight_log_id";
    public static final String COL_USERNAME = "username";
    public static final String COL_HEIGHT = "height";
    public static final String COL_WEIGHT_GOAL_TYPE = "weight_goal_type";
    public static final String COL_ACTUAL_WEIGHT = "actual_weight";
    public static final String COL_GOAL_WEIGHT = "goal_weight";
    public static final String COL_DATE_CREATED = "date_created";

    public WeightLogDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USERNAME + " TEXT, " +
                COL_HEIGHT + " REAL, " +
                COL_WEIGHT_GOAL_TYPE + " TEXT, " +
                COL_ACTUAL_WEIGHT + " REAL, " +
                COL_GOAL_WEIGHT + " REAL, " +
                COL_DATE_CREATED + " TEXT" +
                ");";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertWeightLogData(WeightLogData data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_USERNAME, data.username);
        values.put(COL_HEIGHT, data.height);
        values.put(COL_WEIGHT_GOAL_TYPE, data.weightGoalType);
        values.put(COL_ACTUAL_WEIGHT, data.actualWeight);
        values.put(COL_GOAL_WEIGHT, data.goalWeight);
        values.put(COL_DATE_CREATED, data.dateCreated);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public WeightLogData getLatestWeightLogData(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COL_ID, COL_HEIGHT, COL_WEIGHT_GOAL_TYPE, COL_ACTUAL_WEIGHT, COL_GOAL_WEIGHT, COL_DATE_CREATED},
                COL_USERNAME + "=?", new String[]{username}, null, null, COL_DATE_CREATED + " DESC", "1");
        if (cursor.moveToFirst()) {
            WeightLogData weightLogData = new WeightLogData(
                    cursor.getInt(0),   // weightLogId
                    username,
                    cursor.getFloat(1), // height
                    cursor.getString(2), // weightGoalType
                    cursor.getFloat(3), // actualWeight
                    cursor.getFloat(4), // goalWeight
                    cursor.getString(5) // dateCreated
            );
            cursor.close();
            db.close();
            return weightLogData;
        } else {
            cursor.close();
            db.close();
            return null;
        }
    }
    public List<WeightLogData> getAllWeightLogData(String username) {
        List<WeightLogData> weightLogDataList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COL_ID, COL_HEIGHT, COL_WEIGHT_GOAL_TYPE, COL_ACTUAL_WEIGHT, COL_GOAL_WEIGHT, COL_DATE_CREATED},
                COL_USERNAME + "=?", new String[]{username}, null, null, COL_DATE_CREATED + " ASC");

        if (cursor.moveToFirst()) {
            do {
                WeightLogData weightLogData = new WeightLogData(
                        cursor.getInt(0),   // weightLogId
                        username,
                        cursor.getFloat(1), // height
                        cursor.getString(2), // weightGoalType
                        cursor.getFloat(3), // actualWeight
                        cursor.getFloat(4), // goalWeight
                        cursor.getString(5) // dateCreated
                );
                weightLogDataList.add(weightLogData);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return weightLogDataList;
    }

}
