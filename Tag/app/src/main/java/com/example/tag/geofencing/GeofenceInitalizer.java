package com.example.tag.geofencing;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.tag.Location;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;

import org.osmdroid.util.GeoPoint;

import java.util.List;

public class GeofenceInitalizer {
    private final Activity activity;
    private GeofencingClient geofencingClient;
    private GeoFencingHelper geoFencingHelper;
    private final Context context;
    private final String TAG = GeofenceInitalizer.class.getCanonicalName();
    private List<Location> locations;
    private int BACKGROUND_LOCATION_ACCESS_REQUEST_CODE = 10002;

    public GeofenceInitalizer(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    public void init(List<Location> locations) {
        if (!checkFineLocationPermission()) return;

        geofencingClient = LocationServices.getGeofencingClient(context);
        geoFencingHelper = new GeoFencingHelper(context);
        this.locations = locations;
        if (Build.VERSION.SDK_INT >= 29) {
            //If API is higher then 29 we need background permission

            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                addFences();
            } else {
                //Permission is not granted!! Need to request it..
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
                    //We show a dialog and ask for permission
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, BACKGROUND_LOCATION_ACCESS_REQUEST_CODE);
                } else {
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, BACKGROUND_LOCATION_ACCESS_REQUEST_CODE);

                }
            }
        } else {
            addFences();
        }
    }

    private void addFences() {
        for (Location location : locations) {
            GeoPoint t = new GeoPoint(location.getLat(), location.getLong());
            addGeofence(t);
        }
    }

    public void removeGeoFences() {
        geofencingClient = LocationServices.getGeofencingClient(context);
        geoFencingHelper = new GeoFencingHelper(context);

        PendingIntent pendingIntent = geoFencingHelper.getPendingIntent();

        geofencingClient.removeGeofences(pendingIntent)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Geofence is removed... "))
                .addOnFailureListener(e -> Log.d(TAG, e.getLocalizedMessage()));
        if (this.locations != null) this.locations.clear();

    }

    private void addGeofence(GeoPoint p) {
        if (!checkFineLocationPermission()) return;

        Geofence geofence = geoFencingHelper.getGeofence("Player", p, 10);
        GeofencingRequest geofencingRequest = geoFencingHelper.getGeoFencingRequest(geofence);
        PendingIntent pendingIntent = geoFencingHelper.getPendingIntent();

        geofencingClient.addGeofences(geofencingRequest, pendingIntent).addOnSuccessListener(v -> {
            Log.i(TAG, "addGeofence: added geofence");
        }).addOnFailureListener(v -> {
            Log.e(TAG, "addGeofence: failure adding geofence " + v.getMessage());
        });
        Log.i(TAG, "addGeofence: added geofence to client");
    }

    private boolean checkFineLocationPermission() {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }
}
