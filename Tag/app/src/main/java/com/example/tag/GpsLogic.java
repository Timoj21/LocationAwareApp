package com.example.tag;

import android.location.Location;
import android.location.LocationListener;

import androidx.annotation.NonNull;

public class GpsLogic implements LocationListener {

    private Thread checkingThread;
    private Location myLocation;
    private boolean on;

    public GpsLogic(){
        this.on = true;
    }


    @Override
    public void onLocationChanged(@NonNull Location location) {
        this.myLocation = new Location(location);
    }

    public void start(){
        this.checkingThread = new Thread(this::ConstantlyCheck);
        this.checkingThread.start();
    }

    public void stop(){
        this.on = false;
    }

    public void ConstantlyCheck(){
        //TODO check if you are within 5m of a player
    }
}
