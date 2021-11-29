package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.BitmapFactory;
import android.os.Bundle;
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

        TextView titleText = findViewById(R.id.mainTaskTitle);
        String title = getIntent().getExtras().get("title").toString();
        titleText.setText(title);


        TextView state = findViewById(R.id.stateView);
        state.setText(getIntent().getExtras().getString("body"));

        TextView body = findViewById(R.id.bodyView);
        body.setText(getIntent().getExtras().getString("state"));

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