package com.example.comp4521_fitness_app;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.comp4521_fitness_app.FitnessActivities.AddRoutineActivity;
import com.example.comp4521_fitness_app.FitnessActivities.RoutineActivity;
import com.example.comp4521_fitness_app.R;
import com.example.comp4521_fitness_app.data.CurrentRoutine;
import com.example.comp4521_fitness_app.database.fitnessLog.ExerciseData;
import com.example.comp4521_fitness_app.database.fitnessLog.FitnessLogDBHelper;
import com.example.comp4521_fitness_app.database.fitnessLog.RoutineData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FitnessManagementActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner mSpinnerRedirect;
    private FitnessLogDBHelper dbHelper;
    private ListView listViewRoutines;
    private ArrayAdapter<String> routinesAdapter;
    private FloatingActionButton addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitness_management);

        mSpinnerRedirect = findViewById(R.id.spinner_redirect);
        listViewRoutines = findViewById(R.id.list_routines);
        addButton = findViewById(R.id.addButton);

        mSpinnerRedirect.setOnItemSelectedListener(this);

        dbHelper = new FitnessLogDBHelper(this);

        List<RoutineData> routines = dbHelper.getAllRoutines();
        int x = dbHelper.getRoutinesTableLength();
        List<String> routineNames = new ArrayList<>();

        for (RoutineData routine : routines) {
            Log.d("routineName", routine.routineName);
            routineNames.add(routine.routineName);
        }

        routinesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, routineNames);
        listViewRoutines.setAdapter(routinesAdapter);

        // Set the default selection to "Weight management"
        String[] redirectOptions = getResources().getStringArray(R.array.redirect_options);
        int weightManagementIndex = Arrays.asList(redirectOptions).indexOf("Fitness management");
        mSpinnerRedirect.setSelection(weightManagementIndex);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FitnessManagementActivity.this, AddRoutineActivity.class);
                startActivity(intent);
            }
        });

        listViewRoutines.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RoutineData routine = routines.get(position);
                int routineId = routine.routineId;
                CurrentRoutine.getInstance().setRoutineId(routineId);

                // Start the routine activity here
                Intent intent = new Intent(FitnessManagementActivity.this, RoutineActivity.class);
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
        // Do nothing
    }
}