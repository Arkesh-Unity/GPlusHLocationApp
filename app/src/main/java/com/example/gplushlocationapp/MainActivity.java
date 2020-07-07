package com.example.gplushlocationapp;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MainActivity extends AppCompatActivity implements LocationCallInterface {
    private UserLocation userLocation;
    private UserLocationHMS userLocationHMS;

    private Location location;
    private boolean gAvailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GlobalEnvSetting.init(MainActivity.this);
        if(GlobalEnvSetting.isHms()){
            userLocationHMS = new UserLocationHMS(MainActivity.this, this);
        }
        else{
            userLocation = new UserLocation(MainActivity.this, this);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == UserLocation.locationRequestCode) {
            if (grantResults.length > 0
                    && grantResults[0] == PERMISSION_GRANTED) {
                if (GlobalEnvSetting.isHms()) {
                    userLocationHMS.createLocationRequest();
                    userLocationHMS.myLocPren();
                } else {
                    userLocation.createLocationRequest();
                    userLocation.myLocPren();
                }
            } else {
                Toast.makeText(this, "Location not provided", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void getLocation(Location location) {
        this.location = location;
        TextView textView = findViewById(R.id.crash_btn);
        textView.setText("Location: " + location.getLatitude() + ", " + location.getLongitude());
        Toast.makeText(this, "Location: " + location.getLatitude() + ", " + location.getLongitude(), Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case UserLocation.USER_LOCATION:
                switch (resultCode) {
                    case RESULT_OK:
                        userLocation.getLocationCallback();
                        userLocation.requestLocationUpdates(true);
                        break;
                }
                break;
        }
    }
}