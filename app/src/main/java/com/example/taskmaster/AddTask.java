package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;

public class AddTask extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        TextView textView = findViewById(R.id.taskInput);

//        Button button = findViewById(R.id.button1);
//        button.setOnClickListener(new View.OnClickListener() {
//
//
//            @Override
//            public void onClick(View v) {
//
//                Toast.makeText(getApplicationContext(), "Task Added",Toast.LENGTH_LONG).show();
//
//
//            }
//        });

    }
    @Override
    protected  void onStart() {

        super.onStart();
//        AppDatabase db = Room.databaseBuilder(getApplicationContext(),AppDatabase.class,"tasks").allowMainThreadQueries().build();
//        TaskDao taskDao = db.taskDao();
        Button addTask = findViewById(R.id.addButton);
        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Task Added",Toast.LENGTH_LONG).show();
                EditText taskTitle = findViewById(R.id.taskTitle);
                EditText taskBody = findViewById(R.id.taskInput);
                EditText taskState = findViewById(R.id.taskState);

                Task item = Task.builder()
                        .title(taskTitle.getText().toString())
                        .body(taskBody.getText().toString())
                        .state(taskState.getText().toString())
                        .build();
                Amplify.DataStore.save(
                        item,
                        success -> Log.i("Amplify", "Saved item: " + success.item().getId()),
                        error -> Log.e("Amplify", "Could not save item to DataStore", error)
                );

                Intent toHome = new Intent(AddTask.this,MainActivity.class);
                startActivity(toHome);

            }
        });


    }
}