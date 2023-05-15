package com.example.comp4521_fitness_app.FitnessActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.comp4521_fitness_app.FitnessManagementActivity;
import com.example.comp4521_fitness_app.NutritionManagementActivity;
import com.example.comp4521_fitness_app.ProfileActivity;
import com.example.comp4521_fitness_app.R;
import com.example.comp4521_fitness_app.WeightManagementActivity;
import com.example.comp4521_fitness_app.data.CurrentRoutine;
import com.example.comp4521_fitness_app.database.fitnessLog.ExerciseData;
import com.example.comp4521_fitness_app.database.fitnessLog.ExerciseSetData;
import com.example.comp4521_fitness_app.database.fitnessLog.FitnessLogDBHelper;
import com.example.comp4521_fitness_app.database.fitnessLog.RoutineData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LogRoutineActivity extends AppCompatActivity {
    private ListView listView;
    private TextView routineName;
    private List<ExerciseData> exerciseList;
    private Button logButton;
    private ExerciseListAdapter adapter;
    private FitnessLogDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_routine);

        routineName = findViewById(R.id.text_routine_name);
        listView = findViewById(R.id.list_exercises);
        logButton = findViewById(R.id.button_log);

        dbHelper = new FitnessLogDBHelper(this);

        // Get the exercise list from your database or any other source
        RoutineData routine = dbHelper.getRoutineById(CurrentRoutine.getInstance().getId());
        exerciseList = dbHelper.getExercisesByIds(routine.exerciseIds);
        routineName.setText(routine.routineName);

        // Create and set the adapter
        adapter = new ExerciseListAdapter(this, exerciseList);
        listView.setAdapter(adapter);

        // Set the click listener for the log button
        logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logRoutine();
            }
        });
    }

    private class ExerciseListAdapter extends ArrayAdapter<ExerciseData> {
        private LayoutInflater inflater;

        public ExerciseListAdapter(Context context, List<ExerciseData> exerciseList) {
            super(context, 0, exerciseList);
            inflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ExerciseData exercise = getItem(position);

            if (convertView == null) {
                // Inflate the appropriate layout file based on exercise type
                if (exercise.exerciseType.equals("weights")) {
                    convertView = inflater.inflate(R.layout.list_item_weights, parent, false);
                } else if (exercise.exerciseType.equals("cardio")) {
                    convertView = inflater.inflate(R.layout.list_item_cardio, parent, false);
                }
            }

            // Bind the data to the views in the list item layout
            if (exercise.exerciseType.equals("weights")) {
                TextView textExerciseName = convertView.findViewById(R.id.text_exercise_name_weights);
                EditText editSets = convertView.findViewById(R.id.edit_sets);
                EditText editReps = convertView.findViewById(R.id.edit_reps);
                EditText editWeight = convertView.findViewById(R.id.edit_weight);

                textExerciseName.setText(exercise.exerciseName);

            } else if (exercise.exerciseType.equals("cardio")) {
                TextView textExerciseName = convertView.findViewById(R.id.text_exercise_name_cardio);
                EditText editCaloriesBurned = convertView.findViewById(R.id.edit_calories_burned);

                textExerciseName.setText(exercise.exerciseName);
            }

            return convertView;
        }
    }
    private void logRoutine() {
        ExerciseSetData[] exerciseSetDataArray = new ExerciseSetData[exerciseList.size()];

        for (int i = 0; i < exerciseList.size(); i++) {
            ExerciseData exercise = exerciseList.get(i);
            View listItemView = listView.getChildAt(i);

            if (listItemView != null) {
                if (exercise.exerciseType.equals("weights")) {
                    EditText editSets = listItemView.findViewById(R.id.edit_sets);
                    EditText editReps = listItemView.findViewById(R.id.edit_reps);
                    EditText editWeight = listItemView.findViewById(R.id.edit_weight);

                    // Check if any fields are empty
                    if (editSets.getText().toString().isEmpty() || editReps.getText().toString().isEmpty() || editWeight.getText().toString().isEmpty()) {
                        Toast.makeText(LogRoutineActivity.this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    int sets = Integer.parseInt(editSets.getText().toString());
                    int reps = Integer.parseInt(editReps.getText().toString());
                    int weight = Integer.parseInt(editWeight.getText().toString());

                    ExerciseSetData exerciseSetData = new ExerciseSetData(-1, -1, exercise.exerciseId, sets, reps, weight, null);
                    exerciseSetDataArray[i] = exerciseSetData;

                } else if (exercise.exerciseType.equals("cardio")) {
                    EditText editCaloriesBurned = listItemView.findViewById(R.id.edit_calories_burned);

                    // Check if the calories burned field is empty
                    if (editCaloriesBurned.getText().toString().isEmpty()) {
                        Toast.makeText(LogRoutineActivity.this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    int caloriesBurned = Integer.parseInt(editCaloriesBurned.getText().toString());

                    ExerciseSetData exerciseSetData = new ExerciseSetData(-1, -1, exercise.exerciseId, null, null, null, caloriesBurned);
                    exerciseSetDataArray[i] = exerciseSetData;
                }
            }
        }

        dbHelper.logRoutine(CurrentRoutine.getInstance().getId(), exerciseSetDataArray);
        Toast.makeText(LogRoutineActivity.this, "Routine logged successfully!", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(LogRoutineActivity.this, FitnessManagementActivity.class);
        startActivity(intent);
    }
}
