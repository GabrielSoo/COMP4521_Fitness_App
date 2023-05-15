package com.example.comp4521_fitness_app.FitnessActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.comp4521_fitness_app.FitnessManagementActivity;
import com.example.comp4521_fitness_app.NutritionManagementActivity;
import com.example.comp4521_fitness_app.ProfileActivity;
import com.example.comp4521_fitness_app.R;
import com.example.comp4521_fitness_app.WeightManagementActivity;
import com.example.comp4521_fitness_app.data.CurrentExercise;
import com.example.comp4521_fitness_app.data.CurrentRoutine;
import com.example.comp4521_fitness_app.database.fitnessLog.ExerciseData;
import com.example.comp4521_fitness_app.database.fitnessLog.FitnessLogDBHelper;
import com.example.comp4521_fitness_app.database.fitnessLog.RoutineData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RoutineActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner mSpinnerRedirect;
    private FitnessLogDBHelper dbHelper;
    private ListView listViewExercises;
    private ArrayAdapter<String> exercisesAdapter;
    private TextView routineName;
    private Button logBtn;
    private Button deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine);

        mSpinnerRedirect = findViewById(R.id.spinner_redirect);
        listViewExercises = findViewById(R.id.list_exercises);
        routineName = findViewById(R.id.routineName);
        logBtn = findViewById(R.id.btn_log_routines);
        deleteButton = findViewById(R.id.btn_delete_routine);

        dbHelper = new FitnessLogDBHelper(this);
        mSpinnerRedirect.setOnItemSelectedListener(this);

        RoutineData routine = dbHelper.getRoutineById(CurrentRoutine.getInstance().getId());
        List<ExerciseData> exerciseDataList = dbHelper.getExercisesByIds(routine.exerciseIds);
        routineName.setText(routine.routineName);

        List<String> exerciseNames = new ArrayList<>();

        for (ExerciseData exercise : exerciseDataList) {
            exerciseNames.add(exercise.exerciseName);
        }

        exercisesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, exerciseNames);
        listViewExercises.setAdapter(exercisesAdapter);

        // Set the default selection to "Weight management"
        String[] redirectOptions = getResources().getStringArray(R.array.redirect_options);
        int fitnessManagementIndex = Arrays.asList(redirectOptions).indexOf("Fitness management");
        mSpinnerRedirect.setSelection(fitnessManagementIndex);

        logBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RoutineActivity.this, LogRoutineActivity.class);
                startActivity(intent);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the deleteRoutine() function to delete the routine
                deleteRoutine();
            }
        });

        listViewExercises.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ExerciseData exerciseData = exerciseDataList.get(position);
                int exerciseId = exerciseData.exerciseId;
                String exerciseType = exerciseData.exerciseType;

                // Set the exerciseId in the CurrentExercise singleton
                CurrentExercise.getInstance().setExerciseId(exerciseId);

                // Navigate to the appropriate activity based on the exercise type
                if (exerciseType.equals("cardio")) {
                    Intent intent = new Intent(RoutineActivity.this, CardioActivity.class);
                    startActivity(intent);
                } else if (exerciseType.equals("weights")) {
                    Intent intent = new Intent(RoutineActivity.this, WeightsActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        String redirectOption = adapterView.getItemAtPosition(position).toString();
        Intent intent;

        switch (redirectOption) {
            case "Nutrition management":
                intent = new Intent(this, NutritionManagementActivity.class);
                startActivity(intent);
                break;
            case "Weight management":
                intent = new Intent(this, WeightManagementActivity.class);
                startActivity(intent);
                break;
            case "Profile":
                intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void deleteRoutine() {
        // Retrieve the routine ID or any other necessary information to identify the routine to delete

        // Call the deleteRoutineById() method from your FitnessLogDBHelper to delete the routine
        FitnessLogDBHelper dbHelper = new FitnessLogDBHelper(this);
        dbHelper.deleteRoutineById(CurrentRoutine.getInstance().getId());

        // Route back to the FitnessManagementActivity
        Intent intent = new Intent(RoutineActivity.this, FitnessManagementActivity.class);
        startActivity(intent);
        finish(); // Optional: Close the current activity to prevent going back to it using the back button
    }
}