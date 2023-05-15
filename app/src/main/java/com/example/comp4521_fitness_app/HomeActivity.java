package com.example.comp4521_fitness_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.example.comp4521_fitness_app.R;

import java.util.Arrays;

public class HomeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner mSpinnerRedirect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mSpinnerRedirect = findViewById(R.id.spinner_redirect);
        mSpinnerRedirect.setOnItemSelectedListener(this);

        // Set the default selection to "Weight management"
        String[] redirectOptions = getResources().getStringArray(R.array.redirect_options);
        int homePageIndex = Arrays.asList(redirectOptions).indexOf("Home page");
        mSpinnerRedirect.setSelection(homePageIndex);
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
}