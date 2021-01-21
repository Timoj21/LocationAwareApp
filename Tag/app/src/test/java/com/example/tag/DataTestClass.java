package com.example.tag;

import org.junit.Before;
import org.junit.Test;
import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

public class DataTestClass {

    private Data data;

    @Before
    public void init(){
        data = Data.INSTANCE;
    }

    @Test
    public void testDistance(){
        data.setDistance(20.3);
        double expected = 20.3;
        assertEquals(expected, data.getDistance(), 0.01);
        data.setDistance(3.52);
        assertNotEquals(expected, data.getDistance());
        data.setDistance(null);
        assertNull(data.getDistance());

    }

    @Test
    public void testTimeWalked(){
        GeoPoint testGeopoint = new GeoPoint(12.34, 654.30);
        data.setGeoPoint(testGeopoint);
        assertEquals(testGeopoint, data.getGeoPoint());
        data.setGeoPoint(null);
        assertNull(data.getGeoPoint());
    }
    

    @Test
    public void testZoom(){
        data.setZoom(234.63);
        double expected = 234.63;
        assertEquals(expected, data.getZoom(), 0.01);
        data.setZoom(342.55);
        assertNotEquals(expected, data.getZoom());
    }

    @Test
    public void testGameID(){
       data.setGameId("TESTED");
       String expected = "TESTED";
       assertEquals(expected, data.getGameId());
       data.setGameId(null);
       assertNull(data.getGameId());
       assertNotEquals(expected, data.getGameId());
    }

    @Test
    public void testLocation(){
        android.location.Location testLocation = new android.location.Location("TEST");
        data.setLocation(testLocation);

        assertEquals(testLocation, data.getLocation());
        data.setLocation(null);
        assertNull(data.getLocation());
        assertNotEquals(testLocation, data.getLocation());
    }

    @Test
    public void testPlayer(){
        data.setPlayer("TEST");
        String expected = "TEST";
        assertEquals(expected, data.getPlayer());
        data.setPlayer(null);
        assertNotEquals(expected, data.getPlayer());
        assertNull(data.getPlayer());
    }

    @Test
    public void testPlayerID(){
        data.setPlayerId("TEST");
        String expected = "TEST";
        assertEquals(expected, data.getPlayerId());
        data.setPlayerId(null);
        assertNotEquals(expected, data.getPlayerId());
        assertNull(data.getPlayerId());
    }

    @Test
    public void testGeoPoints(){
        ArrayList<GeoPoint> testGeopoints = new ArrayList<>();
        GeoPoint geoPoint1 = new GeoPoint(32.54, 65.32);
        GeoPoint geoPoint2 = new GeoPoint(62.31, 12.45);
        GeoPoint geoPoint3 = new GeoPoint(73.32, 34.76);
        testGeopoints.add(geoPoint1);
        testGeopoints.add(geoPoint2);
        testGeopoints.add(geoPoint3);
        data.setGeoPoints(testGeopoints);

        assertEquals(testGeopoints, data.getGeoPoints());
    }


}
