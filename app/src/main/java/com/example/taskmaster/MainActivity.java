package com.example.taskmaster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        try {

            Amplify.addPlugin(new AWSApiPlugin());
            Amplify.addPlugin(new AWSDataStorePlugin());
            Amplify.configure(getApplicationContext());
            Log.i("Tutorial", "Initialized Amplify");
        } catch (AmplifyException failure) {
            Log.e("Tutorial", "Could not initialize Amplify", failure);
        }


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


        Button sittings = findViewById(R.id.sittingsButton);
        sittings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToSettings = new Intent(MainActivity.this, Sittings.class);
                startActivity(goToSettings);

            }

        });





//        Amplify.DataStore.observe(Task.class,
//                started -> Log.i("Tutorial", "Observation began."),
//                change -> Log.i("Tutorial", change.item().toString()),
//                failure -> Log.e("Tutorial", "Observation failed.", failure),
//                () -> Log.i("Tutorial", "Observation complete.")
//        );


//        List<Task> allTasksData = new ArrayList<>();
//        allTasksData.add(new Task("visiting a doctor","general routine examination","complete"));
//        allTasksData.add(new Task("go to bank","make a new account for bills","new"));
//        allTasksData.add(new Task("pay the bills","Pay monthly bills","in progress"));

//        AppDatabase db = Room.databaseBuilder(getApplicationContext(),AppDatabase.class,"tasks").allowMainThreadQueries().build();
//        TaskDao userDao = db.taskDao();
//

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String team = sharedPreferences.getString("team", "team");


        RecyclerView allTaskRecyclerView = findViewById(R.id.taskRecyclerView);

        List<Task> allTasksData =new ArrayList<>();
        List<Team> teamsList =new ArrayList<>();



        Handler handler =new Handler(Looper.getMainLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                allTaskRecyclerView.getAdapter().notifyDataSetChanged();
                return false;
            }
        });

        allTaskRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        allTaskRecyclerView.setAdapter(new TaskAdapter(allTasksData));



//
        Amplify.API.query(
                ModelQuery.list(Team.class),
                response -> {
                    for (Team teamName : response.getData()) {
                        Log.i("MyAmplifyApp", teamName.getName());
                        Log.i("MyAmplifyApp", teamName.getId());

                        ///add new team
                        teamsList.add(teamName);
                    }
                    for (int i = 0; i < teamsList.size(); i++) {
                        if (teamsList.get(i).getName().equals(team)){


                            allTasksData.addAll(teamsList.get(i).getTasks());
                            break;
                        }
                    }

                    handler.sendEmptyMessage(1);
                    Log.i("MyAmplifyApp", "outside the loop");
                },
                error -> Log.e("MyAmplifyApp", "Query failure", error)
        );








        //---------------------------------------------------------------------


//        Button task1 = findViewById(R.id.task1);
//        task1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String visitADoc = task1.getText().toString();
//                Intent intentTask1 = new Intent(MainActivity.this, Task_Detail.class);
//                intentTask1.putExtra("title", visitADoc);
//                startActivity(intentTask1);
//            }
//        });
//
//
//        Button task2 = findViewById(R.id.task2);
//        task2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String goToBank = task2.getText().toString();
//                Intent intentTask2 = new Intent(MainActivity.this, Task_Detail.class);
//                intentTask2.putExtra("title", goToBank);
//                startActivity(intentTask2);
//            }
//        });
//
//        Button task3 = findViewById(R.id.task3);
//        task3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String payBells = task3.getText().toString();
//                Intent intentTask3 = new Intent(MainActivity.this, Task_Detail.class);
//                intentTask3.putExtra("title", payBells);
//                startActivity(intentTask3);
//            }
//        });




    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String userName = sharedPreferences.getString("userName","user");
        String teamName = sharedPreferences.getString("team","team");


        Toast.makeText(this, userName,Toast.LENGTH_LONG).show();

        TextView userNameField = findViewById(R.id.userNameTask);


        TextView teamNameField = findViewById(R.id.teamNameTask);

        String tasks = "'s Tasks";
        userNameField.setText(userName + " Task`s");
        teamNameField.setText(teamName);





    }
}