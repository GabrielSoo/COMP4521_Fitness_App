package com.example.comp4521_fitness_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Button;
import android.widget.TextView;

import com.example.comp4521_fitness_app.data.CurrentUser;

import java.util.Arrays;

public class ProfileActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner mSpinnerRedirect;
    private String gender, active, goalType;
    private int age = 0, height = 0, weight = 0, weightGoal = 0;
    private float bmr = 0.0f, amr = 0.0f;
    private TextView usernameTextView, nutritionGoalTextView, weightGoalTypeTextView, weightCurrentTextView, weightGoalTextView;
    private Button editProfileButton;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mSpinnerRedirect = findViewById(R.id.spinner_redirect);
        mSpinnerRedirect.setOnItemSelectedListener(this);

        usernameTextView = findViewById(R.id.usernameText);
        nutritionGoalTextView = findViewById(R.id.nutritionGoal);
        weightGoalTypeTextView = findViewById(R.id.weightGoalType);
        weightCurrentTextView = findViewById(R.id.weightCurrent);
        weightGoalTextView = findViewById(R.id.weightGoal);

        // Get the username from the previous activity
        username = CurrentUser.getInstance().getUsername();
        usernameTextView.setText(username);

        // Set the default selection to "Weight management"
        String[] redirectOptions = getResources().getStringArray(R.array.redirect_options);
        int weightManagementIndex = Arrays.asList(redirectOptions).indexOf("Profile");
        mSpinnerRedirect.setSelection(weightManagementIndex);

        Intent intent = getIntent();
        if (intent != null) {
            age = intent.getIntExtra("AGE", 0);
            height = intent.getIntExtra("HEIGHT", 0);
            gender = intent.getStringExtra("GENDER");
            active = intent.getStringExtra("ACTIVE");

            goalType = intent.getStringExtra("GOAL_TYPE");
            weightGoalTypeTextView.setText("Weight Goal Type: " + goalType);

            weight = intent.getIntExtra("WEIGHT", 0);
            weightCurrentTextView.setText("Current Weight: " + weight + "kg");

            weightGoal = intent.getIntExtra("WEIGHT_GOAL", 0);
            weightGoalTextView.setText("Weight Goal: " + weightGoal + "kg");

            // Calculate BMR based on gender
            if (gender != null && gender.equals("Male")) {
                bmr = 66.47f + (13.75f * (float) weight) + (5.003f * (float) height) - (6.755f * (float) age);
            } else if (gender != null && gender.equals("Female")){
                bmr = 655.1f + (9.563f * (float) weight) + (1.85f * (float) height) - (4.676f * (float) age);
            }

            // Calculate AMR based on activity level
            if (active != null && active.equals("0")) {
                amr = bmr * 1.2f;
            } else if (active != null && active.equals("1-2")) {
                amr = bmr * 1.375f;
            } else if (active != null && active.equals("3-5")) {
                amr = bmr * 1.55f;
            } else if (active != null && active.equals("6-7")) {
                amr = bmr * 1.725f;
            }

            nutritionGoalTextView.setText("Nutrition Goal: " + amr + "kCal");
        }

        SharedPreferences preferences = getSharedPreferences("NutritionData", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putFloat("GOAL_VALUE", amr);
        editor.apply();

        editProfileButton = findViewById(R.id.editProfileButton);
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        String redirectOption = adapterView.getItemAtPosition(position).toString();
        Intent intent;

        switch (redirectOption) {
            case "Home Page":
                intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
                break;
            case "Nutrition management":
                intent = new Intent(this, NutritionManagementActivity.class);
                startActivity(intent);
                break;
            case "Weight management":
                intent = new Intent(this, WeightManagementActivity.class);
                startActivity(intent);
                break;
            case "Fitness management":
                intent = new Intent(this, FitnessManagementActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // Do nothing
    }
}