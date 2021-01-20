package com.example.tag.gui.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tag.Data;
import com.example.tag.R;
import com.example.tag.Service;
import com.example.tag.geofencing.GeofenceInitalizer;
import com.example.tag.gui.activity.MainActivity;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class PlayFragment extends Fragment implements LocationListener {
    private static final String TAG = PlayFragment.class.getSimpleName();

    private AppCompatButton settingsButton;
    private AppCompatButton centerButton;

    private MainActivity mainActivity;

    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;

    private MapView mapView;

    private TextView tagCounterTextView;

    private MyLocationNewOverlay locationOverlay;
    private MapController mapController;

    private Location currentLocation;
    private Overlay allGeoPointsOverlay;
    //private GeofenceInitalizer initializer;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Context ctx = getContext();
        //Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        requestPermissionsIfNecessary(new String[]{
                // if you need to show the current location request FINE_LOCATION permission
                Manifest.permission.ACCESS_FINE_LOCATION,
                // WRITE_EXTERNAL_STORAGE is required in order to show the map
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_play, container, false);

        this.mainActivity = (MainActivity) getContext();

        this.tagCounterTextView = view.findViewById(R.id.tagCounterTextView);
        this.tagCounterTextView.setText(String.valueOf(Data.INSTANCE.getTagCounter()));


        this.settingsButton = view.findViewById(R.id.settingsButton);
        this.centerButton = view.findViewById(R.id.centerButton);

        this.settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.toSettingsFragment();
            }
        });

        this.centerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapController.setCenter(locationOverlay.getMyLocation());
                mapController.animateTo(locationOverlay.getMyLocation());
                mapController.zoomTo(19);
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        this.mapView.onResume();
        this.locationOverlay.onResume();

        this.mapController.setCenter(locationOverlay.getMyLocation());
        this.mapController.animateTo(locationOverlay.getMyLocation());
        this.mapController.zoomTo(19);
    }

    @Override
    public void onStop() {
        super.onStop();
        this.mapView.onPause();
        this.locationOverlay.onPause();
        //getActivity().stopService(new Intent(getActivity(), Service.class));
    }

    @Override
    public void onResume() {
        super.onResume();
        //getActivity().startService(new Intent(getActivity(), Service.class));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Configuration.getInstance().setUserAgentValue("com.example.tag");

        //initializer = new GeofenceInitalizer(requireContext(), requireActivity());
        this.mapView = view.findViewById(R.id.mapView);
        this.mapView.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.NEVER);
        this.mapView.setMultiTouchControls(true);
        this.mapView.setTileSource(TileSourceFactory.MAPNIK);

        GpsMyLocationProvider gpsMyLocationProvider = new GpsMyLocationProvider(getContext());

        this.locationOverlay = new MyLocationNewOverlay(gpsMyLocationProvider, this.mapView);
        this.locationOverlay.enableFollowLocation();
        this.locationOverlay.enableMyLocation();
        this.mapView.getOverlays().add(this.locationOverlay);

        this.mapController = new MapController(this.mapView);
        this.mapController.setZoom(19);


        Location location = new Location(String.valueOf(gpsMyLocationProvider));
        Data.INSTANCE.setLocation(location);

        // add location manager and set the start point
        LocationManager locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);

        try {

            // request location updates for the distance checking
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) this);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) this);

            // get the current location and set it as center
            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Data.INSTANCE.setLocation(location1);
            if (currentLocation == null) {
                currentLocation = location1;
            }

            if (location1 != null) {
                GeoPoint start = new GeoPoint(currentLocation.getLatitude(), currentLocation.getLongitude());
                mapController.setCenter(start);
            }

        } catch (SecurityException e) {
            Log.d(TAG, "onViewCreated: exception while getting location: " + e.getLocalizedMessage());

            requestPermissionsIfNecessary(new String[]{
                    // if you need to show the current location request FINE_LOCATION permission
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    // WRITE_EXTERNAL_STORAGE is required in order to show the map
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            });


        }
        GeoPoint geoPoint = new GeoPoint(Double.parseDouble("51.58634557563859"), Double.parseDouble("4.776964947099206"));
        Data.INSTANCE.setGeoPoint(geoPoint);
        Data.INSTANCE.getGeoPoints().add(geoPoint);

        DrawWayPoints();
    }

    public void DrawWayPoints() {
        //initializer.removeGeoFences();


        final ArrayList<OverlayItem> items = new ArrayList<>(Data.INSTANCE.getGeoPoints().size());
        // marker icon

        // add all locations to the overlay itemss

        OverlayItem item = new OverlayItem(Data.INSTANCE.getGeoPoint().toDoubleString(), Data.INSTANCE.getGeoPoint().toIntString(), Data.INSTANCE.getGeoPoint());
        Drawable marker = null;

        Log.d(TAG, "addLocations: geopoint " + Data.INSTANCE.getGeoPoint());
        marker = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_not_listed_location_24);
        marker.setAlpha(255);

        item.setMarker(marker);
        items.add(item);

        allGeoPointsOverlay = new ItemizedIconOverlay<>(items, new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
            @Override
            public boolean onItemSingleTapUp(int index, OverlayItem item) {
                return false;
            }

            @Override
            public boolean onItemLongPress(int index, OverlayItem item) {
                return false;
            }
        }, requireContext());

        mapView.getOverlays().add(allGeoPointsOverlay);
//                List<GeoPoint> list = new ArrayList<>(Data.INSTANCE.getGeoPoints().values());
//                List<com.example.tag.Location> locations = new ArrayList<>();
//                for (GeoPoint g : list ){
//                    double lat = g.getLatitude();
//                    double lon = g.getLongitude();
//                    com.example.tag.Location location = new com.example.tag.Location(lat, lon);
//                    locations.add(location);
//                }
//                addGeofences(locations);


//
//        Data.INSTANCE.getGeoPoints().forEach((k, v) -> {
//            Marker marker = new Marker(mapView);
//            marker.setIcon(getResources().getDrawable(R.drawable.ic_baseline_not_listed_location_24));
//            marker.setTitle(k);
//            marker.setPosition(Data.INSTANCE.getGeoPoints().get(k));
//            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
//            mapView.getOverlays().add(marker);
//        });
    }

//    private void addGeofences(List<com.example.tag.Location> locations) {
//        Log.d(TAG, "addGeofences: adding geofences!");
//        initializer.init(locations);
//    }

    private void requestPermissionsIfNecessary(String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this.getContext(), permission)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                permissionsToRequest.add(permission);
            }
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this.getActivity(),
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }


    @Override
    public void onLocationChanged(@NonNull Location location) {
        Data.INSTANCE.setLocation(location);
        if (isAdded()) {
            checkTarget(location);
        }

    }

    public void checkTarget(Location l) {
//        Data.INSTANCE.getDistances().forEach((k, v) -> {
//            if(v < 0.005 && !Data.INSTANCE.isTargetReached()){
//                Toast.makeText(mainActivity, "you reached something", Toast.LENGTH_LONG).show();
//                //Data.INSTANCE.getGeoPoints().replace(k, makeRandomGeoPoint());
//                Data.INSTANCE.setTagCounter(Data.INSTANCE.getTagCounter() + 1);
//                this.tagCounterTextView.setText(String.valueOf(Data.INSTANCE.getTagCounter()));
//                Data.INSTANCE.setTargetReached(true);
//                DrawWayPoints();
//            }
//        });
        if (Data.INSTANCE.getGeoPoint() != null) {
            double lon1 = l.getLongitude();
            double lat1 = l.getLatitude();


            double lon2 = Data.INSTANCE.getGeoPoint().getLongitude();
            double lat2 = Data.INSTANCE.getGeoPoint().getLatitude();


            lat1 = Math.toRadians(lat1);
            lat2 = Math.toRadians(lat2);
            lon1 = Math.toRadians(lon1);
            lon2 = Math.toRadians(lon2);

            double dlon = lon2 - lon1;
            double dlat = lat2 - lat1;
            double a = Math.pow(Math.sin(dlat / 2), 2)
                    + Math.cos(lat1) * Math.cos(lat2)
                    * Math.pow(Math.sin(dlon / 2), 2);

            double c = 2 * Math.asin(Math.sqrt(a));

            double distance;
            // Radius of earth in kilometers. Use 3956
            // for miles

            //double r = 3956;
            //distance = c * r;


            double r = 6371;
            distance = c * r;

            System.out.println(distance);
            if (distance < 0.03) {
                Data.INSTANCE.setGeoPoint(makeRandomGeoPoint());
                Data.INSTANCE.setTagCounter(Data.INSTANCE.getTagCounter() + 1);
                Toast.makeText(mainActivity, "Reached Point, new point has been made", Toast.LENGTH_SHORT).show();
                DrawWayPoints();
            }
        } else {
            GeoPoint geoPoint = new GeoPoint(4.776964947099206, 51.58634557563859);
            Data.INSTANCE.setGeoPoint(geoPoint);
            DrawWayPoints();
        }

    }

    public GeoPoint makeRandomGeoPoint() {
        Random random = new Random();
        double lowLat = 51.57835075376575;
        double highLat = 51.594681317352745;

        double lowLon = 4.764797137382507;
        double highLon = 4.787660921269813;

        double randomLat = ((highLat - lowLat) * random.nextDouble()) + lowLat;
        double randomLon = ((highLon - lowLon) * random.nextDouble()) + lowLon;

        GeoPoint geoPoint = new GeoPoint(randomLat, randomLon);
        return geoPoint;
    }

}