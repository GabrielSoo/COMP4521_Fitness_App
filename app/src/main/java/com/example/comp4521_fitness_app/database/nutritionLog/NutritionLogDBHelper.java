package com.example.comp4521_fitness_app.database.nutritionLog;

import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class NutritionLogDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "nutritionlog.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "nutritionlog";
    public static final String COL_ID = "nutrition_log_id";
    public static final String COL_USERNAME = "username";
    public static final String COL_ACTUAL_CALORIES = "actual_calories";
    public static final String COL_GOAL_CALORIES = "goal_calories";
    public static final String COL_CARB_VALUE = "carb_value";
    public static final String COL_PROTEIN_VALUE = "protein_value";
    public static final String COL_FAT_VALUE = "fat_value";
    public static final String COL_BREAKFAST_CALORIES = "breakfast_calories";
    public static final String COL_BREAKFAST_CARB_VALUE = "breakfast_carb_value";
    public static final String COL_BREAKFAST_PROTEIN_VALUE = "breakfast_protein_value";
    public static final String COL_BREAKFAST_FAT_VALUE = "breakfast_fat_value";
    public static final String COL_LUNCH_CALORIES = "lunch_calories";
    public static final String COL_LUNCH_CARB_VALUE = "lunch_carb_value";
    public static final String COL_LUNCH_PROTEIN_VALUE = "lunch_protein_value";
    public static final String COL_LUNCH_FAT_VALUE = "lunch_fat_value";
    public static final String COL_DINNER_CALORIES = "dinner_calories";
    public static final String COL_DINNER_CARB_VALUE = "dinner_carb_value";
    public static final String COL_DINNER_PROTEIN_VALUE = "dinner_protein_value";
    public static final String COL_DINNER_FAT_VALUE = "dinner_fat_value";
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
                COL_CARB_VALUE + " REAL, " +
                COL_PROTEIN_VALUE + " REAL, " +
                COL_FAT_VALUE + " REAL, " +
                COL_BREAKFAST_CALORIES + " REAL, " +
                COL_BREAKFAST_CARB_VALUE + " REAL, " +
                COL_BREAKFAST_PROTEIN_VALUE + " REAL, " +
                COL_BREAKFAST_FAT_VALUE + " REAL, " +
                COL_LUNCH_CALORIES + " REAL, " +
                COL_LUNCH_CARB_VALUE + " REAL, " +
                COL_LUNCH_PROTEIN_VALUE + " REAL, " +
                COL_LUNCH_FAT_VALUE + " REAL, " +
                COL_DINNER_CALORIES + " REAL, " +
                COL_DINNER_CARB_VALUE + " REAL, " +
                COL_DINNER_PROTEIN_VALUE + " REAL, " +
                COL_DINNER_FAT_VALUE + " REAL, " +
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
        values.put(COL_CARB_VALUE, data.carbsValue);
        values.put(COL_PROTEIN_VALUE, data.proteinValue);
        values.put(COL_FAT_VALUE, data.fatValue);
        values.put(COL_BREAKFAST_CALORIES, data.breakfastCaloriesValue);
        values.put(COL_BREAKFAST_CARB_VALUE, data.breakfastCarbValue);
        values.put(COL_BREAKFAST_PROTEIN_VALUE, data.breakfastProteinValue);
        values.put(COL_BREAKFAST_FAT_VALUE, data.breakfastFatValue);
        values.put(COL_LUNCH_CALORIES, data.lunchCaloriesValue);
        values.put(COL_LUNCH_CARB_VALUE, data.lunchCarbValue);
        values.put(COL_LUNCH_PROTEIN_VALUE, data.lunchProteinValue);
        values.put(COL_LUNCH_FAT_VALUE, data.lunchFatValue);
        values.put(COL_DINNER_CALORIES, data.dinnerCaloriesValue);
        values.put(COL_DINNER_CARB_VALUE, data.dinnerCarbValue);
        values.put(COL_DINNER_PROTEIN_VALUE, data.dinnerProteinValue);
        values.put(COL_DINNER_FAT_VALUE, data.dinnerFatValue);
        values.put(COL_DATE_CREATED, data.dateCreated);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }


    public NutritionLogData getLatestNutritionLogData(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{
                        COL_ID,
                        COL_ACTUAL_CALORIES,
                        COL_GOAL_CALORIES,
                        COL_CARB_VALUE,
                        COL_PROTEIN_VALUE,
                        COL_FAT_VALUE,
                        COL_BREAKFAST_CALORIES,
                        COL_BREAKFAST_CARB_VALUE,
                        COL_BREAKFAST_PROTEIN_VALUE,
                        COL_BREAKFAST_FAT_VALUE,
                        COL_LUNCH_CALORIES,
                        COL_LUNCH_CARB_VALUE,
                        COL_LUNCH_PROTEIN_VALUE,
                        COL_LUNCH_FAT_VALUE,
                        COL_DINNER_CALORIES,
                        COL_DINNER_CARB_VALUE,
                        COL_DINNER_PROTEIN_VALUE,
                        COL_DINNER_FAT_VALUE,
                        COL_DATE_CREATED},
                COL_USERNAME + "=?", new String[]{username}, null, null, COL_DATE_CREATED + " DESC", "1");
        if (cursor.moveToFirst()) {
            NutritionLogData nutritionLogData = new NutritionLogData(
                    cursor.getInt(0),   // nutritionLogId
                    username,
                    cursor.getFloat(1), // actualCalories
                    cursor.getFloat(2), // goalCalories
                    cursor.getFloat(3), // carbValue
                    cursor.getFloat(4), // proteinValue
                    cursor.getFloat(5), // fatValue
                    cursor.getFloat(6), // breakfastCalories
                    cursor.getFloat(7), // breakfastCarbValue
                    cursor.getFloat(8), // breakfastProteinValue
                    cursor.getFloat(9), // breakfastFatValue
                    cursor.getFloat(10), // lunchCalories
                    cursor.getFloat(11), // lunchCarbValue
                    cursor.getFloat(12), // lunchProteinValue
                    cursor.getFloat(13), // lunchFatValue
                    cursor.getFloat(14), // dinnerCalories
                    cursor.getFloat(15), // dinnerCarbValue
                    cursor.getFloat(16), // dinnerProteinValue
                    cursor.getFloat(17), // dinnerFatValue
                    cursor.getString(18) // dateCreated
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

    public List<NutritionLogData> getAllNutritionLogData(String username) {
        List<NutritionLogData> nutritionLogDataList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{
                        COL_ID,
                        COL_ACTUAL_CALORIES,
                        COL_GOAL_CALORIES,
                        COL_CARB_VALUE,
                        COL_PROTEIN_VALUE,
                        COL_FAT_VALUE,
                        COL_BREAKFAST_CALORIES,
                        COL_BREAKFAST_CARB_VALUE,
                        COL_BREAKFAST_PROTEIN_VALUE,
                        COL_BREAKFAST_FAT_VALUE,
                        COL_LUNCH_CALORIES,
                        COL_LUNCH_CARB_VALUE,
                        COL_LUNCH_PROTEIN_VALUE,
                        COL_LUNCH_FAT_VALUE,
                        COL_DINNER_CALORIES,
                        COL_DINNER_CARB_VALUE,
                        COL_DINNER_PROTEIN_VALUE,
                        COL_DINNER_FAT_VALUE,
                        COL_DATE_CREATED},
                COL_USERNAME + "=?", new String[]{username}, null, null, COL_DATE_CREATED + " ASC");

        if (cursor.moveToFirst()) {
            do {
                NutritionLogData nutritionLogData = new NutritionLogData(
                        cursor.getInt(0),   // nutritionLogId
                        username,
                        cursor.getFloat(1), // actualCalories
                        cursor.getFloat(2), // goalCalories
                        cursor.getFloat(3), // carbValue
                        cursor.getFloat(4), // proteinValue
                        cursor.getFloat(5), // fatValue
                        cursor.getFloat(6), // breakfastCalories
                        cursor.getFloat(7), // breakfastCarbValue
                        cursor.getFloat(8), // breakfastProteinValue
                        cursor.getFloat(9), // breakfastFatValue
                        cursor.getFloat(10), // lunchCalories
                        cursor.getFloat(11), // lunchCarbValue
                        cursor.getFloat(12), // lunchProteinValue
                        cursor.getFloat(13), // lunchFatValue
                        cursor.getFloat(14), // dinnerCalories
                        cursor.getFloat(15), // dinnerCarbValue
                        cursor.getFloat(16), // dinnerProteinValue
                        cursor.getFloat(17), // dinnerFatValue
                        cursor.getString(18) // dateCreated
                );
                nutritionLogDataList.add(nutritionLogData);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return nutritionLogDataList;
    }


}
