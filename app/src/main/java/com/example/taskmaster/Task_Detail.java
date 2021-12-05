package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.amplifyframework.core.Amplify;

import java.io.File;

public class Task_Detail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        Intent intent = getIntent();

        TextView titleText = findViewById(R.id.mainTaskTitle);
        TextView stateText = findViewById(R.id.stateView);
        TextView bodyText = findViewById(R.id.bodyView);

        titleText.setText(intent.getExtras().getString("title"));

        stateText.setText(intent.getExtras().getString("state"));


        bodyText.setText(intent.getExtras().getString("body"));

        TextView location = findViewById(R.id.location);
        String key = getIntent().getStringExtra("key");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Task_Detail.this);
        String locationData = sharedPreferences.getString(key,"No Location Found");

        location.setText(locationData);


        Amplify.Storage.downloadFile(
                getIntent().getExtras().getString("imgName"),
                new File(getApplicationContext().getFilesDir() + "/download.jpg"),
                result -> {
                    ImageView taskImageView = findViewById(R.id.TaskImgView);
                    String newImg = result.getFile().getPath();
                    taskImageView.setImageBitmap(BitmapFactory.decodeFile(newImg));

                    Log.i("MyAmplifyApp", "Successfully downloaded: " + result.getFile());},
                error -> Log.e("MyAmplifyApp",  "Download Failure", error)
        );

    }
}