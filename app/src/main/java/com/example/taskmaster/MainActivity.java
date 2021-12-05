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
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;
import com.amplifyframework.storage.s3.AWSS3StoragePlugin;

import java.util.ArrayList;
import java.util.List;
import static com.amazonaws.mobile.client.internal.oauth2.OAuth2Client.TAG;


public class MainActivity extends AppCompatActivity {
//    Handler handler;
    RecyclerView allTaskRecyclerView;
    List<Task> allTasksData =new ArrayList<>();
    List<Team> teamsList =new ArrayList<>();
    List<Task> tasks = new ArrayList<>();

    private String teamName;
    private Team teamFilter;
    // sign in
    public void login(){
        Amplify.Auth.signInWithWebUI(
                this,
                result -> Log.i("AuthQuickStart", result.toString()),
                error -> Log.e("AuthQuickStart", error.toString())
        );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        try {
            Amplify.addPlugin(new AWSApiPlugin());
            Amplify.addPlugin(new AWSS3StoragePlugin());

            Amplify.addPlugin(new AWSCognitoAuthPlugin());
            Amplify.configure(getApplicationContext());

            Log.i("MyAmplifyApp", "Initialized Amplify");

        } catch (AmplifyException failure) {
            Log.e("MyAmplifyApp", "Could not initialize Amplify", failure);
        }

        login();

        //----------to take the userName from AWS Auth----------
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
//        String userName = sharedPreferences.getString("userName","user");

//        Toast.makeText(this, userName,Toast.LENGTH_LONG).show();
        TextView userNameField = findViewById(R.id.userNameTask);
        userNameField.setText(com.amazonaws.mobile.client.AWSMobileClient.getInstance().getUsername() + "'s Tasks");

        //--logout button----
        Button logoutButton =  findViewById(R.id.signout);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Amplify.Auth.signOut(
                        () -> {
                            login();
                            Log.i("AuthQuickstart", "Signed out successfully");
                        },
                        error -> Log.e("AuthQuickstart", error.toString())
                );
            }
        });




        //------to add a task--------
        Button addTask = findViewById(R.id.addTask);
        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddTask.class);
                startActivity(intent);
            }
        });

        //------show all task button-------
        Button showTask = findViewById(R.id.showTaskButton);
        showTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AllTask.class);
                startActivity(intent);
            }
        });

        //----------setting button--------
        Button sittings = findViewById(R.id.sittingsButton);
        sittings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToSettings = new Intent(MainActivity.this, Sittings.class);
                startActivity(goToSettings);

            }

        });




        //-------checks the task query ---------//
//        Amplify.DataStore.observe(Task.class,
//                started -> Log.i("Tutorial", "Observation began."),
//                change -> Log.i("Tutorial", change.item().toString()),
//                failure -> Log.e("Tutorial", "Observation failed.", failure),
//                () -> Log.i("Tutorial", "Observation complete.")
//        );


        //----------hardCoded task---------//
//        List<Task> allTasksData = new ArrayList<>();
//        allTasksData.add(new Task("visiting a doctor","general routine examination","complete"));
//        allTasksData.add(new Task("go to bank","make a new account for bills","new"));
//        allTasksData.add(new Task("pay the bills","Pay monthly bills","in progress"));

//        AppDatabase db = Room.databaseBuilder(getApplicationContext(),AppDatabase.class,"tasks").allowMainThreadQueries().build();
//        TaskDao userDao = db.taskDao();
//




        allTaskRecyclerView = findViewById(R.id.taskRecyclerView);
        String team = sharedPreferences.getString("team","team");





            //---------to handle the threads with data change --------------
//        handler =new Handler(Looper.getMainLooper(), new Handler.Callback() {
//            @Override
//            public boolean handleMessage(@NonNull Message msg) {
//                allTaskRecyclerView.getAdapter().notifyDataSetChanged();
//                return false;
//            }
//        });





        //---------fetch the data -------------








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
        Handler handler;

        handler =new Handler(Looper.getMainLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                allTaskRecyclerView.getAdapter().notifyDataSetChanged();
                return false;
            }
        });



        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String teamName = sharedPreferences.getString("team", "team");


        this.teamName=teamName;
        TextView teamNameField = findViewById(R.id.teamNameTask);


        teamNameField.setText(teamName);


        Amplify.API.query(
                ModelQuery.list(Team.class,Team.NAME.contains(teamName)),
                response -> {
                    for (Team teamN : response.getData()) {
//                        teamFilter = teamN;
//                        teamsList.add(teamN);
                        tasks = teamN.getTasks();
                        Log.i("MyAmplifyApp", tasks.toString());

//                        Log.i("MyAmplifyApp", tasks.getId());
                        ///add new team
//                        allTasksData.add(teamFilter.getTasks());
//                        Log.i("MyAmplifyApp", teamsList.toString());
                    }


                    handler.sendEmptyMessage(1);
                    Log.i("MyAmplifyApp", "outside the loop");

                },
                error -> Log.e("MyAmplifyApp", "Query failure", error)

        );
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        allTaskRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        tasks = teamsList.get(0).getTasks();

        allTaskRecyclerView.setAdapter(new TaskAdapter(tasks));



    }
}