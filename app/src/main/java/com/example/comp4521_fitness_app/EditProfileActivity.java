package com.example.comp4521_fitness_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class EditProfileActivity extends AppCompatActivity {

    private RadioGroup genderRadioGroup, activeRadioGroup, goalTypeRadioGroup;
    private RadioButton genderRadioButton, activeRadioButton, goalTypeRadioButton;
    private String gender, active, goalType;
    private String age, height, weight, weightGoal;
    private EditText ageInput, heightInput, weightInput, weightGoalInput;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        genderRadioGroup = findViewById(R.id.genderGroup);
        activeRadioGroup = findViewById(R.id.activeGroup);
        goalTypeRadioGroup = findViewById(R.id.goalTypeGroup);

        ageInput = findViewById(R.id.ageInput);
        heightInput = findViewById(R.id.heightInput);
        weightInput = findViewById(R.id.weightInput);
        weightGoalInput = findViewById(R.id.weightGoalInput);

        saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the selected radio button from the radio group
                int selectedGenderId = genderRadioGroup.getCheckedRadioButtonId();
                if(selectedGenderId != -1) {
                    // Get the radio button view
                    genderRadioButton = findViewById(selectedGenderId);

                    // Get the text of the radio button view
                    gender = genderRadioButton.getText().toString();

                    // Create an intent to start the ProfileActivity
                    Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                    intent.putExtra("gender", gender);
                    startActivity(intent);
                } else {
                    // No radio button is selected
                    Toast.makeText(EditProfileActivity.this, "Please select a gender", Toast.LENGTH_SHORT).show();
                }

                int selectedActiveId = activeRadioGroup.getCheckedRadioButtonId();
                if(selectedActiveId != -1) {
                    // Get the radio button view
                    activeRadioButton = findViewById(selectedActiveId);

                    // Get the text of the radio button view
                    active = activeRadioButton.getText().toString();
                } else {
                    // No radio button is selected
                    Toast.makeText(EditProfileActivity.this, "Please select a active level", Toast.LENGTH_SHORT).show();
                }

                int selectedGoalTypeId = goalTypeRadioGroup.getCheckedRadioButtonId();
                if(selectedGoalTypeId != -1) {
                    // Get the radio button view
                    goalTypeRadioButton = findViewById(selectedGoalTypeId);

                    // Get the text of the radio button view
                    goalType = goalTypeRadioButton.getText().toString();
                } else {
                    // No radio button is selected
                    Toast.makeText(EditProfileActivity.this, "Please select a goal type", Toast.LENGTH_SHORT).show();
                }

                // Validate the input fields
                age = ageInput.getText().toString();
                height = heightInput.getText().toString();
                weight = weightInput.getText().toString();
                weightGoal = weightGoalInput.getText().toString();

                // Create an intent to start the ProfileActivity
                Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                intent.putExtra("GENDER", gender);
                intent.putExtra("ACTIVE", active);
                intent.putExtra("GOAL_TYPE", goalType);
                intent.putExtra("AGE", Integer.parseInt(age));
                intent.putExtra("HEIGHT", Integer.parseInt(height));
                intent.putExtra("WEIGHT", Integer.parseInt(weight));
                intent.putExtra("WEIGHT_GOAL", Integer.parseInt(weightGoal));
                startActivity(intent);
            }
        });
    }
}
