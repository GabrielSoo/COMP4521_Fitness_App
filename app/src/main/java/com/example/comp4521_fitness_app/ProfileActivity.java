package com.example.comp4521_fitness_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.comp4521_fitness_app.R;
import com.example.comp4521_fitness_app.data.CurrentUser;
import com.example.comp4521_fitness_app.database.weightLog.WeightLogDBHelper;
import com.example.comp4521_fitness_app.database.weightLog.WeightLogData;
import com.example.comp4521_fitness_app.utilities.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;


public class ProfileActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner mSpinnerRedirect;
    private EditText heightEditText, actualWeightEditText, goalWeightEditText;
    private Spinner goalTypeSpinner;
    private Button saveButton;
    private TextView usernameText;
    private Button logoutButton;

    WeightLogData latestData;
    private String gender, active;
    private int age;
    private EditText ageEditText;
    private float bmr, amr;
    private String username;
    private WeightLogDBHelper dbHelper;
    private SharedPreferences nSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        dbHelper = new WeightLogDBHelper(this);

        // Initialize the views
        heightEditText = findViewById(R.id.heightInput);
        actualWeightEditText = findViewById(R.id.WeightInput);
        goalWeightEditText = findViewById(R.id.WeightGoalInput);
        goalTypeSpinner = findViewById(R.id.goalTypeSpinner);
        usernameText = findViewById(R.id.usernameText);
        saveButton = findViewById(R.id.saveButton);
        logoutButton = findViewById(R.id.logoutButton);

        mSpinnerRedirect = findViewById(R.id.spinner_redirect);
        mSpinnerRedirect.setOnItemSelectedListener(this);

        // Get the username from the previous activity
        username = CurrentUser.getInstance().getUsername();
        usernameText.setText(username);

        // Set the default selection to "Profile"
        String[] redirectOptions = getResources().getStringArray(R.array.redirect_options);
        int weightManagementIndex = Arrays.asList(redirectOptions).indexOf("Profile");
        mSpinnerRedirect.setSelection(weightManagementIndex);

        // Fill in fields with latest weight log data for this user
        latestData = dbHelper.getLatestWeightLogData(username);
        if (latestData != null) {
            heightEditText.setText(Float.toString(latestData.height));
            actualWeightEditText.setText(Float.toString(latestData.actualWeight));
            goalWeightEditText.setText(Float.toString(latestData.goalWeight));
            if (latestData.weightGoalType.equals("Muscle Growth")) {
                goalTypeSpinner.setSelection(1);
            } else {
                goalTypeSpinner.setSelection(0);
            }
        } else {
            mSpinnerRedirect.setVisibility(View.GONE);
        }

        nSharedPreferences = getSharedPreferences("ProfileData" + username, MODE_PRIVATE);
        // Load the values from SharedPreferences
        age = nSharedPreferences.getInt("AGE", 0);
        Log.d("Age", Integer.toString(age));
        gender = nSharedPreferences.getString("GENDER", null);
        active = nSharedPreferences.getString("ACTIVE", null);

        ageEditText = findViewById(R.id.ageInput);

        if (age != 0) {
            ageEditText.setText(Integer.toString(age));
        }
        else {
            ageEditText.setText("");
        }


        RadioGroup genderGroup = findViewById(R.id.genderGroup);
        if (gender != null) {
            if (gender.equals("Male")) {
                genderGroup.check(R.id.maleButton);
            } else if (gender.equals("Female")) {
                genderGroup.check(R.id.femaleButton);
            }
        }

        RadioGroup activeGroup = findViewById(R.id.activeGroup);
        if (active != null) {
            if (active.equals("0")) {
                activeGroup.check(R.id.activeButton1);
            } else if (active.equals("1-2")) {
                activeGroup.check(R.id.activeButton2);
            } else if (active.equals("3-5")) {
                activeGroup.check(R.id.activeButton3);
            } else if (active.equals("6-7")) {
                activeGroup.check(R.id.activeButton4);
            }
        }

        genderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selectedRadioButton = findViewById(checkedId);
                gender = selectedRadioButton.getText().toString();

                SharedPreferences nSharedPreferences = getSharedPreferences("ProfileData" + username, MODE_PRIVATE);

                SharedPreferences.Editor editor = nSharedPreferences.edit();
                Log.d("MyApp", "Gender value before saving to SharedPreferences: " + gender);
                editor.putString("GENDER", gender);
                editor.apply();
            }
        });

        activeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selectedRadioButton = findViewById(checkedId);
                active = selectedRadioButton.getText().toString();

                SharedPreferences nSharedPreferences = getSharedPreferences("ProfileData" + username, MODE_PRIVATE);

                SharedPreferences.Editor editor = nSharedPreferences.edit();
                editor.putString("ACTIVE", active);
                editor.apply();
            }
        });

        String heightStr = heightEditText.getText().toString();
        String actualWeightStr = actualWeightEditText.getText().toString();

        float height = 0;
        if (!heightStr.isEmpty()) {
            height = Float.parseFloat(heightStr);
        }
        float actualWeight = 0;
        if (!actualWeightStr.isEmpty()) {
            actualWeight = Float.parseFloat(actualWeightStr);
        }

        String ageStr = ageEditText.getText().toString();

        if (!ageStr.isEmpty()) {
            age = Integer.parseInt(ageStr);
        }

        // Calculate BMR based on gender
        if (gender != null && gender.equals("Male")) {
            bmr = 66.47f + (13.75f * (float) actualWeight) + (5.003f * (float) height) - (6.755f * (float) age);
        } else if (gender != null && gender.equals("Female")){
            bmr = 655.1f + (9.563f * (float) actualWeight) + (1.85f * (float) height) - (4.676f * (float) age);
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


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfile();
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform logout action
                logout();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        String redirectOption = adapterView.getItemAtPosition(position).toString();
        Intent intent;

        switch (redirectOption) {
            case "Weight management":
                intent = new Intent(this, WeightManagementActivity.class);
                startActivity(intent);
                break;
            case "Fitness management":
                intent = new Intent(this, FitnessManagementActivity.class);
                startActivity(intent);
                break;
            case "Nutrition management":
                intent = new Intent(this, NutritionManagementActivity.class);
                startActivity(intent);
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // Do nothing
    }

    private void saveProfile() {
        // Get input values
        int previousAge;
        String ageStr = ageEditText.getText().toString();
        String heightStr = heightEditText.getText().toString();
        String actualWeightStr = actualWeightEditText.getText().toString();
        String goalWeightStr = goalWeightEditText.getText().toString();
        String weightGoalType = goalTypeSpinner.getSelectedItem().toString();

        // Check if all input values are filled
        if (TextUtils.isEmpty(heightStr) || TextUtils.isEmpty(actualWeightStr)
                || TextUtils.isEmpty(goalWeightStr) || TextUtils.isEmpty(ageStr)) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        } else {
            previousAge = age;
            age = Integer.parseInt(ageStr);
        }

        SharedPreferences.Editor editor = nSharedPreferences.edit();
        editor.putString("GENDER", gender);
        editor.putString("ACTIVE", active);
        editor.putFloat("GOAL_VALUE", amr);
        editor.apply();


        float height = Float.parseFloat(heightStr);
        float actualWeight = Float.parseFloat(actualWeightStr);
        float goalWeight = Float.parseFloat(goalWeightStr);

        // Get the current date and time
        String dateCreated = DateUtils.getDateTime();

        // Check if data has changed
        WeightLogData data = new WeightLogData(-1, username, height, weightGoalType, actualWeight, goalWeight, dateCreated);
        if (latestData != null){
            if (data.actualWeight == latestData.actualWeight && data.goalWeight == latestData.goalWeight && data.weightGoalType.equals(latestData.weightGoalType) && data.height == latestData.height && previousAge == age) {
                return;
            }
        }

        // Update db and shared
        dbHelper.insertWeightLogData(data);
        editor = nSharedPreferences.edit();
        editor.putInt("AGE", age);
        editor.apply();

        if (mSpinnerRedirect.getVisibility() == View.GONE) {
            mSpinnerRedirect.setVisibility(View.VISIBLE);
        }

        // Show success message
        Toast.makeText(this, "Profile saved successfully", Toast.LENGTH_SHORT).show();
    }

    // Logout method
    private void logout() {
        // Clear the current user session or perform any other necessary logout actions

        // For example, you can clear the CurrentUser instance and navigate back to the login activity
        CurrentUser.getInstance().clear(); // Clear the current user instance

        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
        startActivity(intent);
        finish(); // Finish the profile activity to prevent going back to it when pressing the back button
    }

}