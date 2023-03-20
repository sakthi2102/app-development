package com.example.trackmyroute;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;

import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    Location currentLocation;
    GoogleMap gmap;
    Marker marker;
    SearchView searchView;


    public boolean isMyLocationButtonEnabled() {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        searchView = findViewById(R.id.search);
        searchView.clearFocus();

        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapview);
        supportMapFragment.getMapAsync(this);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String loc = searchView.getQuery().toString();
                if (loc == null) {
                    Toast.makeText(MapsActivity.this, "Location Not Found", Toast.LENGTH_SHORT).show();
                } else {
                    Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
                    try {
                        List<Address> addressList = geocoder.getFromLocationName(loc, 1);
                        if (addressList.size() > 0) {
                            LatLng latLng = new LatLng(addressList.get(0).getLatitude(), addressList.get(0).getLongitude());
                            if (marker != null) {
                                marker.remove();
                            }
                            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(loc);
                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 10);
                            gmap.animateCamera(cameraUpdate);
                            marker = gmap.addMarker(markerOptions);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        gmap = googleMap;
        LatLng latLng = new LatLng(0, 0);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.title("Current Location");
        markerOptions.position(latLng);
        gmap.addMarker(markerOptions);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
        gmap.animateCamera(cameraUpdate);
        gmap.getUiSettings().setAllGesturesEnabled(true);
        gmap.getUiSettings().setCompassEnabled(true);
        gmap.getUiSettings().setZoomControlsEnabled(true);
        gmap.getUiSettings().setZoomGesturesEnabled(true);
        gmap.getUiSettings().setScrollGesturesEnabled(true);
        gmap.getUiSettings().setRotateGesturesEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        gmap.setMyLocationEnabled(true);
    }
}