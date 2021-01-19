package com.example.tag;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.util.Log;

import org.osmdroid.util.GeoPoint;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * singleton to keep track of different global data
 */
public enum Data {
    INSTANCE;

    private final String TAG = Data.class.getCanonicalName();

    private double zoom = 0;
    private Context context;
    private LocationProximityListener locationProximityListener;
    private android.location.Location location;
    private GeoPoint geoPoint;
    private HashMap<String, GeoPoint> geoPoints = new HashMap();

    private String player;
    private String gameId;

    public void setContext(Context context) {
        this.context = context;
    }


    public LocationProximityListener getLocationProximityListener() {
        return locationProximityListener;
    }

    public void setLocationProximityListener(LocationProximityListener locationProximityListener) {
        this.locationProximityListener = locationProximityListener;
    }



    public double getZoom() {
        return zoom;
    }

    public void setZoom(double zoom) {
        this.zoom = zoom;
    }

    private ArrayList<String> visitedNames = new ArrayList<>();

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }


    @FunctionalInterface
    public interface LocationProximityListener {
        void onLocationVisited(Location location);
    }



    public android.location.Location getLocation() {
        return this.location;
    }

    public void setLocation(android.location.Location location) {
        this.location = location;
    }

    public GeoPoint getGeoPoint() {
        return geoPoint;
    }

    public void setGeoPoint(GeoPoint geoPoint) {
        this.geoPoint = geoPoint;
    }

    public HashMap<String, GeoPoint> getGeoPoints() {
        return geoPoints;
    }

    public void setGeoPoints(HashMap<String, GeoPoint> geoPoints) {
        this.geoPoints = geoPoints;
    }
}
