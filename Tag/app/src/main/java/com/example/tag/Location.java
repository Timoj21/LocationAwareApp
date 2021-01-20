package com.example.tag;

import org.osmdroid.util.GeoPoint;

public class Location {

    /**
     * coordinates will be saved as for example: 2.434343,4.65656;3.656565,6.43434
     * so lat1,long1;lat2,long2
     */
    private String coordinates;
    private boolean visited;

    public Location(String coordinates) {
        this.coordinates = coordinates;

    }

    public Location(double latCoord, double longCoord) {
        this(getStringFromCoordinates(latCoord, longCoord));
    }

    public Location(String name, android.location.Location loc, String description) {
        this(getStringFromCoordinates(loc.getLatitude(), loc.getLongitude()));
    }




    public double[] getCoordinatesAsDoubles() {
        double[] res = new double[2];
        res[1] = getLat();
        res[0] = getLong();

        return res;
    }

    public double getLat() {
        return Double.parseDouble(this.coordinates.split(",")[0]);
    }

    public double getLong() {
        return Double.parseDouble(this.coordinates.split(",")[1]);
    }

    public static String getStringFromCoordinates(double lat1, double long1) {
        return long1 + "," + lat1;
    }

    public GeoPoint convertToGeoPoint() {
        return new GeoPoint(this.getLat(), this.getLong());
    }


    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }
}
