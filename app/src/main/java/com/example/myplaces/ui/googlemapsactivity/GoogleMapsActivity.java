package com.example.myplaces.ui.googlemapsactivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.example.myplaces.AboutActivity;
import com.example.myplaces.R;
import com.example.myplaces.data.MyPlace;
import com.example.myplaces.data.MyPlacesData;
import com.example.myplaces.databinding.ActivityGoogleMapsBinding;
import com.example.myplaces.ui.editmyplaceactivity.EditMyPlaceActivity;
import com.example.myplaces.ui.viewmyplaceactivity.ViewMyPlaceActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;

public class GoogleMapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityGoogleMapsBinding binding;
    static int NEW_PLACE_REQUEST = 1;
    static final int PERMISSION_ACCESS_FINE_LOCATION_REQUEST = 1;

    public static final int SHOW_MAP = 0;
    public static final int CENTER_PLACE_ON_MAP = 1;
    public static final int SELECT_COORDINATES = 2;

    private int state = 0;
    private boolean selCoorsEnabled = false;
    private LatLng placeLoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            Intent mapIntent = getIntent();
            Bundle mapBundle = mapIntent.getExtras();
            if(mapBundle != null) {
                state = mapBundle.getInt("state");
                if(state == CENTER_PLACE_ON_MAP) {
                    String placeLat = mapBundle.getString("lat");
                    String placeLon = mapBundle.getString("lon");
                    placeLoc = new LatLng(Double.parseDouble(placeLat), Double.parseDouble(placeLon));
                }
            }
        } catch(Exception e) {
            Log.d("Error", "Error reading state");
        }

        binding = ActivityGoogleMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        FloatingActionButton fab = findViewById(R.id.fab);
        if(state != SELECT_COORDINATES) {
            fab.setOnClickListener(view -> {
                Intent i = new Intent(GoogleMapsActivity.this, EditMyPlaceActivity.class);
                startActivityForResult(i, NEW_PLACE_REQUEST);
            });
        } else {
            ViewGroup layout = (ViewGroup) fab.getParent();
            if(layout != null)
                layout.removeView(fab);
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(state == SELECT_COORDINATES && !selCoorsEnabled) {
            menu.add(0,1,1,"Select Coordinates");
            menu.add(0,2,2, "Cancel");
            return super.onCreateOptionsMenu(menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_my_places_maps, menu);
            return true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        Intent i;

        if(state == SELECT_COORDINATES && !selCoorsEnabled) {
            if(id == 1) {
                selCoorsEnabled = true;
                item.setEnabled(false);
                Toast.makeText(this, "Select coordinates", Toast.LENGTH_LONG).show();
            } else if (id == 2) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        } else {
            switch (id) {
                case R.id.new_place_item:
                    i = new Intent(this, EditMyPlaceActivity.class);
                    startActivityForResult(i, NEW_PLACE_REQUEST);
                    break;
                case R.id.about_item:
                    i = new Intent(this, AboutActivity.class);
                    startActivity(i);
                    break;
                case android.R.id.home:
                    finish();
                    break;
                case 2:
                    setResult(Activity.RESULT_CANCELED);
                    finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ACCESS_FINE_LOCATION_REQUEST);
        else {
            if(state == SHOW_MAP)
                mMap.setMyLocationEnabled(true);
            else if(state == CENTER_PLACE_ON_MAP)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(placeLoc, 15));
            else
                setOnMapClickListener();
            addMyPlaceMarkers();
        }
    }

    private HashMap<Marker, Integer> markerPlaceIdMap;

    private void addMyPlaceMarkers() {
        ArrayList<MyPlace> places = MyPlacesData.getInstance().getMyPlaces();
        markerPlaceIdMap = new HashMap<>();
        for(int i=0; i<places.size(); i++) {
            MyPlace place = places.get(i);
            String lat = place.latitude;
            String lon = place.longitude;
            LatLng loc = new LatLng(Double.parseDouble(lat), Double.parseDouble(lon));
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(loc);
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin));
            markerOptions.title(place.Name);
            Marker m = mMap.addMarker(markerOptions);
            markerPlaceIdMap.put(m, i);
        }

        mMap.setOnMarkerClickListener(marker -> {
            Intent intent = new Intent(GoogleMapsActivity.this, ViewMyPlaceActivity.class);
            int i = markerPlaceIdMap.get(marker);
            intent.putExtra("position", i);
            startActivity(intent);
            return true;
        });
    }

    private void setOnMapClickListener() {
        if(mMap != null) {
            mMap.setOnMapClickListener(latLng -> {
                if(state == SELECT_COORDINATES && selCoorsEnabled) {
                    String lon = Double.toString(latLng.longitude);
                    String lat = Double.toString(latLng.latitude);
                    Intent locationIntent = new Intent();
                    locationIntent.putExtra("lon", lon);
                    locationIntent.putExtra("lat", lat);
                    setResult(Activity.RESULT_OK, locationIntent);
                    finish();
                }
            });
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch(requestCode) {
            case PERMISSION_ACCESS_FINE_LOCATION_REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                    setOnMapClickListener();
                }
                return;
            }
        }
    }
}