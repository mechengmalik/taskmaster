package com.example.taskmaster;

import androidx.annotation.Nullable;
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

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class AddTask extends AppCompatActivity {
    String imgName = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

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
        TextView textView = findViewById(R.id.taskInput);



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

        Button uploadImg = findViewById(R.id.uploadFile);
        uploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileChoose();
            }
        });



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

                assert team != null;

                Task task = Task.builder()
                        .title(taskTitle.getText().toString())
                        .body(taskBody.getText().toString())
                        .state(taskState.getText().toString())
                        .teamId(team.getId())
                        .imgName(imgName)
                        .build();

                Amplify.API.mutate(
                        ModelMutation.create(task),
                        response -> Log.i("lateeeeh", "Added Task with id: " + response.getData().getTeamId()),
                        error -> Log.e("MyAmplifyApp", "Create failed", error));



                Intent toHome = new Intent(AddTask.this,MainActivity.class);
                startActivity(toHome);


            }


        });


    }
    public void fileChoose(){
        Intent fileChoose=new Intent(Intent.ACTION_GET_CONTENT);
        fileChoose.setType("*/*");
        fileChoose=Intent.createChooser(fileChoose,"choose file");
        startActivityForResult(fileChoose,1111);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        try {
            InputStream exampleInputStream = getContentResolver().openInputStream(data.getData());
            System.out.println("hhhhhhhh"+data.getData().toString());

            imgName = data.getData().toString();


            Amplify.Storage.uploadInputStream(
                    data.getData().toString(),
                    exampleInputStream,
                    result -> Log.i("MyAmplifyApp", "Successfully uploaded: " + result.getKey()),
                    storageFailure -> Log.e("MyAmplifyApp", "Upload failed", storageFailure)
            );
        }  catch (FileNotFoundException error) {
            Log.e("MyAmplifyApp", "Could not find file to open for input stream.", error);
        }
    }

}