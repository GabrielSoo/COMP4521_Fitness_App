package com.example.comp4521_fitness_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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
import com.example.comp4521_fitness_app.utilities.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class WeightManagementActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner mSpinnerRedirect;

    private TextView currentWeightValueTextView;
    private TextView goalWeightValueTextView;
    private TextView weightToGoalTextView;
    private TextView lastLoggedTextView;
    private EditText weightInput;
    private Button logButton;
    private WeightLogData latestData;
    private String username;
    private WeightLogDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_management);

        dbHelper = new WeightLogDBHelper(this);

        mSpinnerRedirect = findViewById(R.id.spinner_redirect);
        mSpinnerRedirect.setOnItemSelectedListener(this);

        // Initialize the TextViews
        currentWeightValueTextView = findViewById(R.id.currentWeightValueTextView);
        goalWeightValueTextView = findViewById(R.id.goalWeightValueTextView);
        weightToGoalTextView = findViewById(R.id.weightToGoalTextView);
        lastLoggedTextView = findViewById(R.id.lastLogged);
        weightInput = findViewById(R.id.weightInputEditText);
        logButton = findViewById(R.id.logButton);

        // Get the username from the previous activity
        username = CurrentUser.getInstance().getUsername();

        // Set the default selection to "Weight management"
        String[] redirectOptions = getResources().getStringArray(R.array.redirect_options);
        int weightManagementIndex = Arrays.asList(redirectOptions).indexOf("Weight management");
        mSpinnerRedirect.setSelection(weightManagementIndex);

        updateUI();

        logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logWeight();
            }
        });
    };

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
            case "Fitness management":
                intent = new Intent(this, FitnessManagementActivity.class);
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

    public void updateUI() {
        // Fill in fields with latest weight log data for this user
        latestData = dbHelper.getLatestWeightLogData(username);
        if (latestData != null) {
            currentWeightValueTextView.setText(String.format("%.1f kg", latestData.actualWeight));
            goalWeightValueTextView.setText(String.format("%.1f kg", latestData.goalWeight));
            float difference = latestData.actualWeight - latestData.goalWeight;
            if (difference >= 0) {
                weightToGoalTextView.setText(String.format("%.1f kg to lose", difference));
                weightToGoalTextView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.red));
            }
            else {

                weightToGoalTextView.setText(String.format("%.1f kg to gain", difference*-1));
                weightToGoalTextView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
            }
            lastLoggedTextView.setText("(Last logged - " + DateUtils.getRelativeDate(latestData.dateCreated) + ")");
        }
    }

    public void logWeight(){
        String weightStr = weightInput.getText().toString();

        if (TextUtils.isEmpty(weightStr)){
            Toast.makeText(this, "Please enter the weight", Toast.LENGTH_SHORT).show();
            return;
        }

        float weight = Float.parseFloat(weightStr);

        String dateCreated = DateUtils.getDateTime();

        // Save data to database
        WeightLogData data = new WeightLogData(-1, username, latestData.height, latestData.weightGoalType, weight, latestData.goalWeight, dateCreated);
        dbHelper.insertWeightLogData(data);

        // Show success message
        Toast.makeText(this, "Weight logged successfully", Toast.LENGTH_SHORT).show();
        updateUI();
    }
}
