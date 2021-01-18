package com.example.tag;

import android.Manifest;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tag.gui.LoginActivity;
import com.example.tag.gui.MainActivity;

import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;


public class PlayFragment extends Fragment {
    private static final String TAG = PlayFragment.class.getSimpleName();

    private AppCompatButton settingsButton;
    private AppCompatButton centerButton;

    private MainActivity mainActivity;

    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;

    private MapView mapView;

    private MyLocationNewOverlay locationOverlay;
    private MapController mapController;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestPermissions(new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION
        }, REQUEST_PERMISSIONS_REQUEST_CODE);
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.mapView = getActivity().findViewById(R.id.mapView);
        this.mapView.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.NEVER);
        this.mapView.setMultiTouchControls(true);
        this.mapView.setTileSource(TileSourceFactory.MAPNIK);

        this.locationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(getActivity().getApplicationContext()), this.mapView);
        this.locationOverlay.enableMyLocation();
        this.locationOverlay.enableFollowLocation();
        this.mapView.getOverlays().add(this.locationOverlay);
        this.mapController = new MapController(this.mapView);
        this.mapController.setCenter(locationOverlay.getMyLocation());
        this.mapController.animateTo(locationOverlay.getMyLocation());
        this.mapController.zoomTo(19);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //TODO if not given permission block app
    }
}