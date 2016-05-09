package com.project.salminnella.prescoop.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.project.salminnella.prescoop.R;
import com.project.salminnella.prescoop.activity.SchoolDetails;
import com.project.salminnella.prescoop.utility.Constants;
import com.project.salminnella.prescoop.utility.Utilities;

import java.util.HashMap;
import java.util.Map;

public class SchoolsMapFragment extends FragmentActivity implements OnMapReadyCallback {
    private static final String TAG = "MapFragment";
    private GoogleMap mMap;
    // Create a LatLngBounds that includes Australia.
    private LatLngBounds sanFrancisco = new LatLngBounds(
            new LatLng(37.657785, -122.521568), new LatLng(37.825296, -122.354369));
    HashMap<String, String> addressList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_schools_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_all_schools);
        mapFragment.getMapAsync(this);

        receiveIntentFromMain();



    }

    private void receiveIntentFromMain() {
        Intent intentFromMain = getIntent();
        addressList = (HashMap<String, String>) intentFromMain.getSerializableExtra(Constants.ADDRESS_LIST_KEY);
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
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.setMyLocationEnabled(true);

        //LatLng currentLocation = new LatLng(mLatitude, mLongitude);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        for (Map.Entry<String,String> entry : addressList.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            LatLng address = Utilities.getLocationFromAddress(this, value);
            if (address != null) {
                mMap.addMarker(new MarkerOptions().position(address).title(key));
            } else {
                Log.i(TAG, "onMapReady - null location: " + entry);
            }
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sanFrancisco.getCenter(), 11.8f));

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Log.i(TAG, "onInfoWindowClick: " + marker.getTitle());
                Intent intent = new Intent(SchoolsMapFragment.this, SchoolDetails.class);
                intent.putExtra(Constants.SCHOOL_TITLE_KEY, marker.getTitle());
                startActivity(intent);
            }
        });
    }



}
