package com.example.comp4521_fitness_app.FitnessActivities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import com.example.comp4521_fitness_app.database.fitnessLog.ExerciseData;
import com.example.comp4521_fitness_app.database.fitnessLog.FitnessLogDBHelper;
import com.example.comp4521_fitness_app.database.fitnessLog.RoutineData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddRoutineActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner mSpinnerRedirect;
    private ListView listViewExercises;
    private EditText nameField;
    private Button addRoutineBtn;
    private FitnessLogDBHelper dbHelper;

    private List<ExerciseData> selectedExercises;
    private List<ExerciseData> allExercises;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_routine);

        mSpinnerRedirect = findViewById(R.id.spinner_redirect);
        listViewExercises = findViewById(R.id.listViewExercises);
        nameField = findViewById(R.id.editTextRoutineName);
        addRoutineBtn = findViewById(R.id.btn_add_routine);
        mSpinnerRedirect.setOnItemSelectedListener(this);

        dbHelper = new FitnessLogDBHelper(this);

        allExercises = dbHelper.getAllExercises();
        selectedExercises = new ArrayList<ExerciseData>();

        ExerciseAdapter adapter = new ExerciseAdapter(this, allExercises);
        listViewExercises.setAdapter(adapter);

        String[] redirectOptions = getResources().getStringArray(R.array.redirect_options);
        int weightManagementIndex = Arrays.asList(redirectOptions).indexOf("Fitness management");
        mSpinnerRedirect.setSelection(weightManagementIndex);

        addRoutineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the routine name from the nameField EditText
                String routineName = nameField.getText().toString();
                if (routineName.isEmpty()) {
                    Toast.makeText(AddRoutineActivity.this, "Please Add Routine Name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (selectedExercises.size()<=0) {
                    Toast.makeText(AddRoutineActivity.this, "Please select at least 1 exercise", Toast.LENGTH_SHORT).show();
                    return;
                }

                int[] exerciseIds = new int[selectedExercises.size()];
                for (int i = 0; i < selectedExercises.size(); i++) {
                    exerciseIds[i] = selectedExercises.get(i).exerciseId;
                }

                // Create a new RoutineData object
                RoutineData routineData = new RoutineData(-1, exerciseIds, routineName);

                // Call dbHelper.addRoutine(db, routineData)
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                dbHelper.addRoutine(db, routineData);

                // Close the database
                db.close();

                // Display a success message or navigate to another activity
                Toast.makeText(AddRoutineActivity.this, "Routine added successfully", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(AddRoutineActivity.this, FitnessManagementActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private class ExerciseAdapter extends ArrayAdapter<ExerciseData> {
        private LayoutInflater inflater;
        private boolean[] checkedStates;  // Array to store the checked states

        public ExerciseAdapter(Context context, List<ExerciseData> exercises) {
            super(context, R.layout.list_item_exercise, exercises);
            inflater = LayoutInflater.from(context);
            checkedStates = new boolean[exercises.size()];  // Initialize the checkedStates array
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.list_item_exercise, parent, false);
                holder = new ViewHolder();
                holder.checkBoxExercise = convertView.findViewById(R.id.checkBoxExercise);
                holder.textViewExerciseName = convertView.findViewById(R.id.textViewExerciseName);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final ExerciseData exercise = getItem(position);

            holder.checkBoxExercise.setOnCheckedChangeListener(null);  // Remove previous listener to avoid interference
            holder.checkBoxExercise.setChecked(selectedExercises.contains(exercise));

            // Reset the state of the checkbox when it is unchecked
            if (!selectedExercises.contains(exercise)) {
                holder.checkBoxExercise.setChecked(false);
            }

            holder.checkBoxExercise.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        selectedExercises.add(exercise);
                    } else {
                        selectedExercises.remove(exercise);
                    }
                }
            });

            holder.textViewExerciseName.setText(exercise.exerciseName);

            return convertView;
        }

        private class ViewHolder {
            CheckBox checkBoxExercise;
            TextView textViewExerciseName;
        }
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