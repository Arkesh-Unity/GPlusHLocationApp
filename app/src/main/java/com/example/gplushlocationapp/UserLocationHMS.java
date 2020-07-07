package com.example.gplushlocationapp;

import android.app.Activity;

import androidx.core.app.ActivityCompat;

import com.huawei.hmf.tasks.Task;
import com.huawei.hms.location.FusedLocationProviderClient;
import com.huawei.hms.location.LocationCallback;
import com.huawei.hms.location.LocationRequest;
import com.huawei.hms.location.LocationResult;
import com.huawei.hms.location.LocationServices;
import com.huawei.hms.location.LocationSettingsRequest;
import com.huawei.hms.location.LocationSettingsResponse;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class UserLocationHMS {

    private Activity activity;
    private LocationCallInterface locationCallInterface;
    private LocationRequest mLocationRequest;
    public static final int locationRequestCode = 1000;
    public static final int USER_LOCATION = 300;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback locationCallback;

    public UserLocationHMS(Activity activity, LocationCallInterface locationCallInterface) {
        this.activity = activity;
        this.locationCallInterface = locationCallInterface;
        init();
    }

    private void init() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
        if (ActivityCompat.checkSelfPermission(activity, ACCESS_FINE_LOCATION) != PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(activity, ACCESS_COARSE_LOCATION) != PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION},
                    locationRequestCode);

        } else {
            createLocationRequest();
            myLocPren();
        }
    }

    public void createLocationRequest() {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(10);
        mLocationRequest.setFastestInterval(10);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    public void myLocPren() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(activity).checkLocationSettings(builder.build());

        result.addOnCompleteListener(task -> {
            try {
                LocationSettingsResponse locationSettingsResponse = task.getResult(/*ApiException.class*/);
                getLocationCallback();
                requestLocationUpdates(true);
            } catch (Exception exception) {

            }
        });
    }

    public void getLocationCallback() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (android.location.Location location : locationResult.getLocations()) {
                    if (location != null) {
                        locationCallInterface.getLocation(location);
                        requestLocationUpdates(false);
                    }
                }
            }
        };
    }

    public void requestLocationUpdates(boolean b) {
        if (ActivityCompat.checkSelfPermission(activity, ACCESS_FINE_LOCATION) == PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(activity, ACCESS_COARSE_LOCATION) == PERMISSION_GRANTED) {
            if (b)
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, locationCallback, null);
            else
                mFusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }

}