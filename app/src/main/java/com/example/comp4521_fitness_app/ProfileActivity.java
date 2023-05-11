package com.example.comp4521_fitness_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.comp4521_fitness_app.R;
import com.example.comp4521_fitness_app.data.CurrentUser;
import com.example.comp4521_fitness_app.database.weightLog.WeightLogDBHelper;
import com.example.comp4521_fitness_app.database.weightLog.WeightLogData;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;


public class ProfileActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner mSpinnerRedirect;
    private EditText heightEditText, actualWeightEditText, goalWeightEditText;
    private Spinner goalTypeSpinner;
    private Button saveButton;
    private TextView usernameText;

    WeightLogData latestData;
    private String username;
    private WeightLogDBHelper dbHelper;
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
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfile();
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
            case "Weight management":
                intent = new Intent(this, WeightManagementActivity.class);
                startActivity(intent);
                break;
            case "Fitness management":
                intent = new Intent(this, FitnessManagementActivity.class);
                startActivity(intent);
                break;
            case "Nutrition Management":
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
        String heightStr = heightEditText.getText().toString();
        String actualWeightStr = actualWeightEditText.getText().toString();
        String goalWeightStr = goalWeightEditText.getText().toString();
        String weightGoalType = goalTypeSpinner.getSelectedItem().toString();

        // Check if all input values are filled
        if (TextUtils.isEmpty(heightStr) || TextUtils.isEmpty(actualWeightStr)
                || TextUtils.isEmpty(goalWeightStr)) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        float height = Float.parseFloat(heightStr);
        float actualWeight = Float.parseFloat(actualWeightStr);
        float goalWeight = Float.parseFloat(goalWeightStr);

        // Get the current date and time
        Date now = new Date();

        // Format the date and time as a string
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateCreated = format.format(now);

        // Save data to database
        WeightLogData data = new WeightLogData(-1, username, height, weightGoalType, actualWeight, goalWeight, dateCreated);
        if (data.actualWeight == latestData.actualWeight && data.goalWeight == latestData.goalWeight && data.weightGoalType.equals(latestData.weightGoalType) && data.height == latestData.height) {
            return;
        }
        dbHelper.insertWeightLogData(data);

        // Show success message
        Toast.makeText(this, "Profile saved successfully", Toast.LENGTH_SHORT).show();
    }
}