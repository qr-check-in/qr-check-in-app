package com.example.qrcheckin.Event;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.qrcheckin.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.qrcheckin.databinding.ActivityMapsBinding;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private ArrayList<LatLng> locationArrayList = new ArrayList<>(); // Initialize the ArrayList

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Retrieve values passed by previous activity to determine
        locationArrayList = (ArrayList<LatLng>) getIntent().getSerializableExtra("AllGeoPoints");
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Set initial camera position to Edmonton city
        LatLng edmonton = new LatLng(53.5461, -113.4938);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(edmonton, 10)); // Zoom level can be adjusted as needed

        // Iterate over the locationArrayList up to its size
        for (int i = 0; i < locationArrayList.size(); i++) {
            mMap.addMarker(new MarkerOptions().position(locationArrayList.get(i)).title(locationArrayList.get(i).toString()));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(18.0f));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(locationArrayList.get(i)));
        }
    }
}