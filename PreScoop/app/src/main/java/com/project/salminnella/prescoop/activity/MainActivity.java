package com.project.salminnella.prescoop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.project.salminnella.prescoop.R;
import com.project.salminnella.prescoop.adapter.ListAdapter;
import com.project.salminnella.prescoop.fragment.SchoolsMapFragment;
import com.project.salminnella.prescoop.model.PreSchool;
import com.project.salminnella.prescoop.utility.Utilities;

import java.util.HashMap;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity implements ListAdapter.OnItemClickListener {
    private static final String TAG = "MainActivity";
    public static final String ADDRESS_LIST_KEY = "addressList";

    LinkedList<PreSchool> mSchools;

    Firebase mFireBaseRoot, mFirebasePreschoolRef;
    PreSchool preschool;
    RecyclerView rvSchools;
    ListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbar();
        setFAB();
        initFirebase();

        // Lookup the recyclerview in activity layout
        rvSchools = (RecyclerView) findViewById(R.id.rvSchools);
        mSchools = new LinkedList<>();
        mAdapter = new ListAdapter(mSchools, this);
        rvSchools.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        result();

        rvSchools.setAdapter(mAdapter);
    }

    public void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }


    private void setFAB() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
        }
    }

    private void initFirebase(){
        mFireBaseRoot = new Firebase("https://prescoop.firebaseio.com/");
        mFirebasePreschoolRef = mFireBaseRoot.child("Facility");
    }


    private void result(){
        Query queryRef = mFirebasePreschoolRef.orderByChild("zipCode");
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                preschool = snapshot.getValue(PreSchool.class);
                mSchools.add(preschool);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }

        });
    }

    @Override
    public void onItemClick(PreSchool preschool) {
        Log.d(TAG, "onItemClick: please work");
        Intent intentToDetails = new Intent(MainActivity.this, SchoolDetails.class);
        startActivity(intentToDetails);
    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.maps_menu_item) {
            HashMap<String, String> addressList = buildAddressListHash();
            Intent intentToMaps = new Intent(MainActivity.this, SchoolsMapFragment.class);
            intentToMaps.putExtra(ADDRESS_LIST_KEY, addressList);
            startActivity(intentToMaps);
        }

        return super.onOptionsItemSelected(item);
    }

//    private ArrayList buildAddressList() {
//        ArrayList<String> addressList = new ArrayList<>();
//
//        if (mSchools == null) {
//            return addressList;
////            return null;
//        }
//        for (int i = 0; i < mSchools.size(); i++) {
//            String streetAdd = mSchools.get(i).getStreetAddress();
//            String city = mSchools.get(i).getCity();
//            String state = mSchools.get(i).getState();
//            String zipcode = mSchools.get(i).getZipCode();
//
//            String stringAddress = Utilities.buildAddressString(streetAdd, city, state, zipcode);
//            addressList.add(stringAddress);
//        }
//
//        return addressList;
//    }

    private HashMap buildAddressListHash() {
        HashMap<String, String> addressListHashMap = new HashMap<>();

        if (mSchools == null) {
            return addressListHashMap;
//            return null;
        }
        for (int i = 0; i < mSchools.size(); i++) {
            String streetAdd = mSchools.get(i).getStreetAddress();
            String city = mSchools.get(i).getCity();
            String state = mSchools.get(i).getState();
            String zipcode = mSchools.get(i).getZipCode();

            String stringAddress = Utilities.buildAddressString(streetAdd, city, state, zipcode);
            addressListHashMap.put(mSchools.get(i).getName(), stringAddress);
        }

        return addressListHashMap;
    }
}
