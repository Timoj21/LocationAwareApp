package com.example.tag.gui.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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

import com.example.tag.Data;
import com.example.tag.R;
import com.example.tag.gui.activity.MainActivity;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;


public class PlayFragment extends Fragment implements LocationListener {
    private static final String TAG = PlayFragment.class.getSimpleName();

    private AppCompatButton settingsButton;
    private AppCompatButton centerButton;

    private MainActivity mainActivity;

    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;

    private MapView mapView;

    private MyLocationNewOverlay locationOverlay;
    private MapController mapController;

    private Location currentLocation;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Context ctx = getContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        requestPermissionsIfNecessary(new String[] {
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
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Configuration.getInstance().setUserAgentValue("com.example.tag");

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

        Location location = new Location (String.valueOf(gpsMyLocationProvider));
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

            requestPermissionsIfNecessary(new String[] {
                    // if you need to show the current location request FINE_LOCATION permission
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    // WRITE_EXTERNAL_STORAGE is required in order to show the map
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            });

        }
    }

    public void DrawWayPoints(){
        Data.INSTANCE.getGeoPoints().forEach((k, v) -> {
            Marker marker = new Marker(mapView);
            marker.setIcon(getResources().getDrawable(R.drawable.ic_baseline_not_listed_location_24));
            marker.setTitle(k);
            marker.setPosition(Data.INSTANCE.getGeoPoints().get(k));
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            mapView.getOverlays().add(marker);
        });
    }

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
        DrawWayPoints();
    }
}