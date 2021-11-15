package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button addTask = findViewById(R.id.addTask);
        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddTask.class);
                startActivity(intent);
            }
        });


        Button showTask = findViewById(R.id.showTaskButton);
        showTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AllTask.class);
                startActivity(intent);
            }
        });


        Button task1 = findViewById(R.id.task1);
        task1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String visitADoc = task1.getText().toString();
                Intent intentTask1 = new Intent(MainActivity.this, Task_Detail.class);
                intentTask1.putExtra("title", visitADoc);
                startActivity(intentTask1);
            }
        });


        Button task2 = findViewById(R.id.task2);
        task2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String goToBank = task2.getText().toString();
                Intent intentTask2 = new Intent(MainActivity.this, Task_Detail.class);
                intentTask2.putExtra("title", goToBank);
                startActivity(intentTask2);
            }
        });

        Button task3 = findViewById(R.id.task3);
        task3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String payBells = task3.getText().toString();
                Intent intentTask3 = new Intent(MainActivity.this, Task_Detail.class);
                intentTask3.putExtra("title", payBells);
                startActivity(intentTask3);
            }
        });


        Button sittings = findViewById(R.id.sittingsButton);
        sittings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToSettings = new Intent(MainActivity.this, Sittings.class);
                startActivity(goToSettings);

            }

        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String userName = sharedPreferences.getString("userName","user");
        Toast.makeText(this, userName,Toast.LENGTH_LONG).show();

        TextView userNameField = findViewById(R.id.userNameTask);

        String tasks = "'s Tasks";
        userNameField.setText(userName +"Task`s");





    }
}