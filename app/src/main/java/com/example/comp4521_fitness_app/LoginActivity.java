package com.example.comp4521_fitness_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.comp4521_fitness_app.data.CurrentUser;
import com.example.comp4521_fitness_app.database.DBHelper;
import com.example.comp4521_fitness_app.database.weightLog.WeightLogDBHelper;
import com.example.comp4521_fitness_app.database.weightLog.WeightLogData;

public class LoginActivity extends AppCompatActivity {

    EditText username, password;
    Button btnlogin;
    DBHelper DB;
    WeightLogDBHelper WeightLogDB;
    // declare the CurrentUser instance
    private CurrentUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // initialize the CurrentUser instance
        currentUser = CurrentUser.getInstance();

        username = (EditText) findViewById(R.id.username1);
        password = (EditText) findViewById(R.id.password1);
        btnlogin = (Button) findViewById(R.id.btnsignin1);
        DB = new DBHelper(this);
        WeightLogDB = new WeightLogDBHelper(this);

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String user = username.getText().toString();
                String pass = password.getText().toString();

                if(user.equals("")||pass.equals("")) {
                    Toast.makeText(LoginActivity.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                }
                else {
                    Boolean checkUserPass = DB.checkUsernamePassword(user, pass);
                    if(checkUserPass == true) {
                        // set the current user's username
                        currentUser.setUsername(user);

                        Toast.makeText(LoginActivity.this, "Sign-in successful", Toast.LENGTH_SHORT).show();
                        WeightLogData latest = WeightLogDB.getLatestWeightLogData(user);
                        Intent intent;
                        intent = new Intent(getApplicationContext(), ProfileActivity.class);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(LoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}