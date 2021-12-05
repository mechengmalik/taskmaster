package com.example.taskmaster;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.room.Room;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AddTask extends AppCompatActivity {
    // class level
    private FusedLocationProviderClient fusedLocationClient;
    String imgName = "";
    String loc;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

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

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }












        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {

                        if (location != null) {


                            Geocoder geocoder;
                            List<Address> addresses = new ArrayList<>();
                            geocoder = new Geocoder(AddTask.this, Locale.getDefault());

                            try {
                                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                            String city = addresses.get(0).getLocality();
                            String state = addresses.get(0).getAdminArea();
                            String  country = addresses.get(0).getCountryName();
                            String postalCode = addresses.get(0).getPostalCode();
                            String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

                            loc = city + "- " + country;


                            System.out.println("LLLLLLLLLLLLLLLLLLLLLLLLLLL" + loc);

                        }
                    }
                });



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
                String key =task.getId();

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(AddTask.this);
                sharedPreferences.edit().putString(key, loc).apply();


                Amplify.API.mutate(
                        ModelMutation.create(task),
                        response -> Log.i("lateeeeh", "Added Task with id: " + response.getData().getTeamId()),
                        error -> Log.e("MyAmplifyApp", "Create failed", error));



                Intent toHome = new Intent(AddTask.this,MainActivity.class);
                startActivity(toHome);


            }


        });
        System.out.println("GOOGLE GOOGLE "+   GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this));


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
            System.out.println("myApp"+data.getData().toString());

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