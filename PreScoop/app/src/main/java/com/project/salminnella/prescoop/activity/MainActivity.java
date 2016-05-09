package com.project.salminnella.prescoop.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.project.salminnella.prescoop.R;
import com.project.salminnella.prescoop.adapter.ListAdapter;
import com.project.salminnella.prescoop.fragment.SchoolsMapFragment;
import com.project.salminnella.prescoop.model.PreSchool;
import com.project.salminnella.prescoop.utility.Constants;
import com.project.salminnella.prescoop.utility.Utilities;

import java.util.HashMap;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity implements ListAdapter.OnItemClickListener {
    private static final String TAG = "MainActivity";

    LinkedList<PreSchool> mSchoolsList;
    Firebase mFireBaseRoot, mFirebasePreschoolRef;
    PreSchool mPreschool;
    RecyclerView mRecyclerView;
    ListAdapter mRecycleAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbar();
        setFAB();
        initFirebase();
        if (mSchoolsList == null) {
            queryFirebase();
        }
        createRecycler();
        handleSearchFilterIntent(getIntent());
    }

    private void createRecycler() {
        mRecyclerView = (RecyclerView) findViewById(R.id.rvSchools);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        mRecycleAdapter = new ListAdapter(mSchoolsList, this);
        mRecyclerView.setAdapter(mRecycleAdapter);
    }

    @Override protected void onNewIntent(Intent intent) {
        handleSearchFilterIntent(intent);
    }

    private void handleSearchFilterIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Toast.makeText(MainActivity.this,"Searching for "+query,Toast.LENGTH_SHORT).show();
        }
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
        mFireBaseRoot = new Firebase(Constants.FIREBASE_ROOT_URL);
        mFirebasePreschoolRef = mFireBaseRoot.child(Constants.FIREBASE_ROOT_CHILD);
    }


    private void queryFirebase(){
        mSchoolsList = new LinkedList<>();
        Query queryRef = mFirebasePreschoolRef.orderByChild(Constants.ORDER_BY_NAME);
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                mPreschool = snapshot.getValue(PreSchool.class);
                mSchoolsList.add(mPreschool);
                mRecycleAdapter.notifyDataSetChanged();
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
        Intent intentToDetails = new Intent(MainActivity.this, SchoolDetails.class);
        intentToDetails.putExtra(Constants.SCHOOL_TITLE_KEY, preschool.getName());
        startActivity(intentToDetails);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

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
            intentToMaps.putExtra(Constants.ADDRESS_LIST_KEY, addressList);
            startActivity(intentToMaps);
        }

        return super.onOptionsItemSelected(item);
    }

    private HashMap buildAddressListHash() {
        HashMap<String, String> addressListHashMap = new HashMap<>();

        if (mSchoolsList == null) {
            return addressListHashMap;
            //TODO return something else
        }
        for (int i = 0; i < mSchoolsList.size(); i++) {
            String stringAddress = Utilities.buildAddressString(mSchoolsList.get(i).getStreetAddress(),
                    mSchoolsList.get(i).getCity(),
                    mSchoolsList.get(i).getState(),
                    mSchoolsList.get(i).getZipCode());
            addressListHashMap.put(mSchoolsList.get(i).getName(), stringAddress);
        }

        return addressListHashMap;
    }
}
