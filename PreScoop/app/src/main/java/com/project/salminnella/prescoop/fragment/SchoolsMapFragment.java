package com.project.salminnella.prescoop.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.project.salminnella.prescoop.R;
import com.project.salminnella.prescoop.activity.SchoolDetailsActivity;
import com.project.salminnella.prescoop.model.PreSchool;
import com.project.salminnella.prescoop.utility.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This holds the google map, it extends FragmentActivity and implements OnMapReadyCallback.
 * Displays markers for each school being viewed in the list from the
 * MainActivity.  Also receives a single PreSchool object when navigating from the SchoolDetailsPage
 */
public class SchoolsMapFragment extends FragmentActivity implements OnMapReadyCallback {

    // region Member Variables
    private HashMap<String, LatLng> mMarkersList;
    private PreSchool mPreschool;
    private ArrayList<PreSchool> mSchoolsList;
    // endregion Member Variables

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_schools_map);
        receiveIntentFromMain();
        loadMap();
    }

    private void loadMap() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_all_schools);
        mapFragment.getMapAsync(this);
    }


    /**
     * Receives a HashMap containing the map markers for each school, and a list of each school object
     * from the MainActivity
     * The list of school objects is needed so that the user can click on a marker and see all info
     * on the SchoolDetailsActivity
     */
    private void receiveIntentFromMain() {
        Intent receiveIntent = getIntent();
        // Received from MainActivity, holds each marker to the map representing each school
        mMarkersList = (HashMap<String, LatLng>) receiveIntent.getSerializableExtra(Constants.ADDRESS_LIST_KEY);
        // Holds each school object, so user can go to the SchoolDetailsActivity from the Map
        mSchoolsList = (ArrayList<PreSchool>) receiveIntent.getSerializableExtra(Constants.SCHOOLS_LIST_KEY);
        // this is sent from the SchoolDetailsActivity. to view the single school on the map
        mPreschool = (PreSchool) receiveIntent.getSerializableExtra(Constants.SCHOOL_MARKER_KEY);
    }

    /**
     * Manipulates the map once available
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.setMyLocationEnabled(true);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        
        LatLng latLng;
        // place markers on the map
        // mSchoolsList will be null if coming from the SchoolDetailsActivity, so it uses the PreSchool Object
        if (mSchoolsList == null) {
            latLng = new LatLng(mPreschool.getLatitude(), mPreschool.getLongitude());
            googleMap.addMarker(new MarkerOptions().position(latLng).title(mPreschool.getName()));
        } else {
            for (Map.Entry<String, LatLng> entry : mMarkersList.entrySet()) {
                String infoWindowTitle = entry.getKey();
                latLng = entry.getValue();
                googleMap.addMarker(new MarkerOptions().position(latLng).title(infoWindowTitle));
            }
        }

        // sets the bounds for the camera view
        LatLngBounds sanFrancisco = new LatLngBounds(new LatLng(37.657785, -122.521568), new LatLng(37.825296, -122.354369));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sanFrancisco.getCenter(), 11.8f));

        // sets the info window click listener to go to the SchoolDetailsActivity
        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent intentToSchoolDetails = new Intent(SchoolsMapFragment.this, SchoolDetailsActivity.class);
                if (mSchoolsList != null) {
                    PreSchool filteredPreschool = selectSchoolForIntent(marker.getTitle());
                    intentToSchoolDetails.putExtra(Constants.SCHOOL_OBJECT_KEY, filteredPreschool);
                } else {
                    intentToSchoolDetails.putExtra(Constants.SCHOOL_OBJECT_KEY, mPreschool);
                }

                startActivity(intentToSchoolDetails);
            }
        });
    }

    /**
     * Finds the selected school in the list, and sends that school object to SchoolDetailsActivity
     * @param title String
     * @return PreSchool
     */
    private PreSchool selectSchoolForIntent(String title) {
        PreSchool schoolForIntent = null;
        if (mSchoolsList != null) {
            for(int i = 0; i < mSchoolsList.size(); i++) {
                if (title.equals(mSchoolsList.get(i).getName())) {
                    schoolForIntent = mSchoolsList.get(i);
                    break;
                }
            }
        }

        return schoolForIntent;
    }
}
