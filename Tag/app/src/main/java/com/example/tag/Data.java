package com.example.tag;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
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
    private GeoPoint geoPoint = new GeoPoint(Double.parseDouble("51.58634557563859"), Double.parseDouble("4.776964947099206"));
    private ArrayList<GeoPoint> geoPoints = new ArrayList();
    private HashMap<String, Double> distances = new HashMap<>();

    private Double distance;
    private SharedPreferences.Editor editor;


    private int tagCounter = 0;
    private boolean targetReached = false;

    private String player;
    private String playerId;
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

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public HashMap<String, Double> getDistances() {
        return distances;
    }

    public void setDistances(HashMap<String, Double> distances) {
        this.distances = distances;
    }

    public int getTagCounter() {
        return tagCounter;
    }

    public void setTagCounter(int tagCounter) {
        this.tagCounter = tagCounter;
        editor.putInt("tagCounter", tagCounter);
        editor.apply();
    }

    public boolean isTargetReached() {
        return targetReached;
    }

    public void setTargetReached(boolean targetReached) {
        this.targetReached = targetReached;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
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
        if (editor != null)
        editor.putString("geoPoint", geoPoint.toString());
    }

    public ArrayList<GeoPoint> getGeoPoints() {
        return geoPoints;
    }

    public void setGeoPoints(ArrayList<GeoPoint> geoPoints) {
        this.geoPoints = geoPoints;
    }

    public void load(){
        SharedPreferences prefs = context.getSharedPreferences("Data", Context.MODE_PRIVATE);
        this.editor = prefs.edit();
        this.tagCounter = prefs.getInt("tagCounter", 0);
        GeoPoint geoPointStandard = new GeoPoint(Double.parseDouble("51.58634557563859"), Double.parseDouble("4.776964947099206"));
        GeoPoint geoPointLoaded = GeoPoint.fromDoubleString(prefs.getString("geoPoint", String.valueOf(geoPointStandard)), ',') ;
        this.geoPoint = geoPointLoaded;

    }

    public void loadNewGame(){
        SharedPreferences prefs = context.getSharedPreferences("Data", Context.MODE_PRIVATE);
        this.editor = prefs.edit();
        this.editor.clear();
        this.editor.apply();

    }
}
