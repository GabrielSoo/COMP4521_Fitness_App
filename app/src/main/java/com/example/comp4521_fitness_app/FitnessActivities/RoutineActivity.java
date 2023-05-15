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

import com.example.comp4521_fitness_app.NutritionManagementActivity;
import com.example.comp4521_fitness_app.ProfileActivity;
import com.example.comp4521_fitness_app.R;
import com.example.comp4521_fitness_app.WeightManagementActivity;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine);

        mSpinnerRedirect = findViewById(R.id.spinner_redirect);
        listViewExercises = findViewById(R.id.list_exercises);
        routineName = findViewById(R.id.routineName);
        logBtn = findViewById(R.id.btn_log_routines);

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
}