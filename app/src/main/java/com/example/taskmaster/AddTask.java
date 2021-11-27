package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;

import java.util.ArrayList;
import java.util.List;

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


        List<Team> teams = new ArrayList<>();

        Amplify.API.query(
                ModelQuery.list(Team.class),
                response -> {
                    for (Team task : response.getData()) {
                        Log.i("MyAmplifyApp", task.getName());
                        teams.add(task);

                    }
                },
                error -> Log.e("MyAmplifyApp", "Query failure", error)
        );



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

                RadioButton course = findViewById(R.id.course);
                RadioButton home = findViewById(R.id.homee);
                RadioButton work = findViewById(R.id.work);

                String teamName ="";
                if (course.isChecked()){

                    teamName ="course";

                }else if (home.isChecked()){
                    teamName= "home";
                }else if (work.isChecked()){
                    teamName= "work";
                }

                Team team = null;

                for (int i = 0; i<teams.size(); i++){
                    if (teams.get(i).getName().equals(teamName)){
                        team= teams.get(i);
                    }
                }

                Task task = Task.builder()
                        .title(taskTitle.getText().toString())
                        .body(taskBody.getText().toString())
                        .state(taskState.getText().toString())
                        .team(team)
                        .build();

                Amplify.API.mutate(
                        ModelMutation.create(task),
                        response -> Log.i("MyAmplifyApp", "Added Todo with id: " + response.getData().getId()),
                        error -> Log.e("MyAmplifyApp", "Create failed", error));



                Intent toHome = new Intent(AddTask.this,MainActivity.class);
                startActivity(toHome);

            }
        });

    }
}