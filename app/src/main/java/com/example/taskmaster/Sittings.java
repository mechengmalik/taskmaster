package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.prefs.Preferences;

public class Sittings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sittings);

        Button saveUserName = findViewById(R.id.saveButton);
        saveUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"saved",Toast.LENGTH_LONG).show();
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Sittings.this);
                SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();

                EditText userNameField = findViewById(R.id.userNameField);
                String userName = userNameField.getText().toString();

                RadioButton course = findViewById(R.id.sittingCourse);
                RadioButton home     = findViewById(R.id.sittingHome);
                RadioButton work      = findViewById(R.id.sittingWork);

                if (course.isChecked()){
                    sharedPreferencesEditor.putString("team",course.getText().toString());

                }else if (home.isChecked()){
                    sharedPreferencesEditor.putString("team",home.getText().toString());

                }else if (work.isChecked()){
                    sharedPreferencesEditor.putString("team",work.getText().toString());

                }


                sharedPreferencesEditor.putString("userName",userName);
                sharedPreferencesEditor.apply();

                Intent toHome = new Intent(Sittings.this,MainActivity.class);
                startActivity(toHome);
            }
        });

    }
}