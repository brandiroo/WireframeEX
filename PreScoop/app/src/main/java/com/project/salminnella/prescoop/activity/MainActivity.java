package com.project.salminnella.prescoop.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.maps.model.LatLng;
import com.project.salminnella.prescoop.R;
import com.project.salminnella.prescoop.adapter.DBCursorAdapter;
import com.project.salminnella.prescoop.adapter.ListAdapter;
import com.project.salminnella.prescoop.dbHelper.DatabaseHelper;
import com.project.salminnella.prescoop.fragment.SchoolsMapFragment;
import com.project.salminnella.prescoop.model.PreSchool;
import com.project.salminnella.prescoop.utility.Constants;
import com.project.salminnella.prescoop.utility.NameComparator;
import com.project.salminnella.prescoop.utility.PriceComparator;
import com.project.salminnella.prescoop.utility.RatingComparator;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements ListAdapter.OnItemClickListener {
    private static final String TAG = "MainActivity";

    private static final String LOCATION_PERMISSION = Manifest.permission.READ_CONTACTS;
    private static final int PERMISSION_REQUEST_CODE = 12345;
    //TODO markers for maps can be turned into an object class
    //TODO use firebase UI for recycler view instead of the onchild overrides
    //TODO move the sort and search methods to Utilities
    private ArrayList<PreSchool> mSchoolsList;
    private Firebase mFireBaseRoot, mFirebasePreschoolRef;
    private PreSchool mPreschool;
    private RecyclerView mRecyclerView;
    private ListAdapter mRecycleAdapter;
    private BottomBar mBottomBar;
    private ArrayList<PreSchool> backupList;
    private ProgressBar progressBar;
    private DatabaseHelper dbHelper;
    private DBCursorAdapter cursorAdapter;
    private Cursor cursor;
    private ListView cursorListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = DatabaseHelper.getInstance(MainActivity.this);
        initViews();
        initToolbar();
        showProgressBar();
        initFirebase();
        if (mSchoolsList == null) {
            queryFirebase();
        }
        createRecycler();
        handleSearchFilterIntent(getIntent());
        buildBottomBar(savedInstanceState);
        setCursorListItemListener();
    }

    private void setCursorListItemListener() {
        cursorListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intentToDetails = new Intent(MainActivity.this, SchoolDetailsActivity.class);
                intentToDetails.putExtra(Constants.SCHOOL_OBJECT_KEY, mPreschool);
                startActivity(intentToDetails);
            }
        });
    }


    private void initViews() {
        progressBar = (ProgressBar) findViewById(R.id.progress_bar_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.rvSchools);
        cursorListView = (ListView) findViewById(R.id.cursor_list_view);
    }

    private void buildBottomBar(Bundle savedInstanceState) {
        mBottomBar = BottomBar.attach(this, savedInstanceState);
        mBottomBar.setItemsFromMenu(R.menu.menu_bottom_bar_main, new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                switchBottomBarTab(menuItemId);
            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {
                switchBottomBarTab(menuItemId);
                if (menuItemId == R.id.refresh_bottom_bar) {
                    // The user reselected refresh item
                    mRecycleAdapter.swap(backupList);
                    mRecyclerView.smoothScrollToPosition(0);
                }
            }
        });

        // Setting colors for different tabs when there's more than three of them.
        // You can set colors for tabs in three different ways as shown below.
        mBottomBar.mapColorForTab(0, ContextCompat.getColor(this, R.color.colorAccent));
        mBottomBar.mapColorForTab(1, 0xFF5D4037);
        mBottomBar.mapColorForTab(2, "#7B1FA2");
        mBottomBar.mapColorForTab(3, "#FF5252");
    }

    private void switchBottomBarTab(int menuItemId) {
        switch (menuItemId) {
            case R.id.abc_sort_bottom_bar:
                Toast.makeText(MainActivity.this, "Sorted alphabetically", Toast.LENGTH_SHORT).show();
                sortByName();
                break;
            case R.id.rating_sort_bottom_bar:
                Toast.makeText(MainActivity.this, "Sorted By Rating", Toast.LENGTH_SHORT).show();
                sortByRating();
                break;
            case R.id.price_sort_bottom_bar:
                Toast.makeText(MainActivity.this, "Sort By Price", Toast.LENGTH_SHORT).show();
                sortByPrice();
                break;
            case R.id.refresh_bottom_bar:
                mRecycleAdapter.swap(backupList);
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Necessary to restore the BottomBar's state, otherwise we would
        // lose the current tab on orientation change.
        mBottomBar.onSaveInstanceState(outState);
    }

    // region RecyclerView
    private void createRecycler() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        mRecycleAdapter = new ListAdapter(mSchoolsList, this);
        mRecyclerView.setAdapter(mRecycleAdapter);
    }

    @Override
    public void onItemClick(PreSchool preschool) {
        Intent intentToDetails = new Intent(MainActivity.this, SchoolDetailsActivity.class);
        intentToDetails.putExtra(Constants.SCHOOL_OBJECT_KEY, preschool);
        startActivity(intentToDetails);
    }
    // endregion RecyclerView

    // region FilterSearch
    @Override protected void onNewIntent(Intent intent) {
        handleSearchFilterIntent(intent);
    }

    private void handleSearchFilterIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            filterSchoolsList(query);
            Toast.makeText(MainActivity.this,"Searching for "+query,Toast.LENGTH_SHORT).show();
        }
    }

    private void filterSchoolsList(String query) {
        if (!query.equals("")) {
            char first = query.charAt(0);
            if (first >= '0' && first <= '9') {
                searchByZipCode(query);
            } else if (Character.isLetter(first)) {
                searchByNeighborhood(query);
            } else {
                searchByPriceRange(query);
            }
        }
    }

    private void searchByZipCode(String query) {
        ArrayList<PreSchool> filteredList = new ArrayList<>();
        for (int i = 0; i < mSchoolsList.size(); i++) {
            if (mSchoolsList.get(i).getZipCode().equals(query)) {
                filteredList.add(mSchoolsList.get(i));
            }
        }
        mRecycleAdapter.swap(filteredList);
    }

    private void searchByNeighborhood(String query) {
        ArrayList<PreSchool> filteredList = new ArrayList<>();
        String queryLowerCase = query.toLowerCase();
        for (int i = 0; i < mSchoolsList.size(); i++) {
            String regionLowerCase = mSchoolsList.get(i).getRegion().toLowerCase();
            if (regionLowerCase.contains(queryLowerCase)) {
                filteredList.add(mSchoolsList.get(i));
            }
        }
        mRecycleAdapter.swap(filteredList);
    }

    private void searchByPriceRange(String query) {
        ArrayList<PreSchool> filteredList = new ArrayList<>();
        int min = 0;
        int max = 0;
        if (query.equals("$")) {
            min = 0;
            max = 1000;
        } else if (query.equals("$$")) {
            min = 1001;
            max = 2000;
        } else if (query.equals("$$$")) {
            min = 2001;
            max = 5000;
        }

        for (int i = 0; i < mSchoolsList.size(); i++) {
            if (mSchoolsList.get(i).getPrice() >= min && mSchoolsList.get(i).getPrice() <= max) {
                filteredList.add(mSchoolsList.get(i));
            }
        }
        mRecycleAdapter.swap(filteredList);
    }
    // endregion FilterSearch

    // region SortMethods
    private void sortByPrice() {
        Collections.sort(mSchoolsList, new PriceComparator());
        for (int i = 0; i < mSchoolsList.size(); i++) {
            Log.i(TAG, "sortByPrice: " + mSchoolsList.get(i).getName() + "price: " + mSchoolsList.get(i).getPrice());
        }
        mRecycleAdapter.notifyDataSetChanged();
        mRecyclerView.smoothScrollToPosition(0);
    }

    private void sortByRating() {
        Collections.sort(mSchoolsList, new RatingComparator());
        for (int i = 0; i < mSchoolsList.size(); i++) {
            Log.i(TAG, "sortByrating: " + mSchoolsList.get(i).getName() + "rating: " + mSchoolsList.get(i).getRating());
        }
        mRecycleAdapter.notifyDataSetChanged();
        mRecyclerView.smoothScrollToPosition(0);
    }

    private void sortByName() {
        Collections.sort(mSchoolsList, new NameComparator());
        mRecycleAdapter.notifyDataSetChanged();
        mRecyclerView.smoothScrollToPosition(0);
    }
    // endregion SortMethods

    private void showProgressBar() {
        if (mSchoolsList == null) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    private void removeProgressBar() {
        if (mSchoolsList.size() > 0) {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void initFirebase(){
        mFireBaseRoot = new Firebase(Constants.FIREBASE_ROOT_URL);
        mFirebasePreschoolRef = mFireBaseRoot.child(Constants.FIREBASE_ROOT_CHILD);
    }


    private void queryFirebase(){
        mSchoolsList = new ArrayList<>();
        backupList = new ArrayList<>();
        Query queryRef = mFirebasePreschoolRef.orderByChild(Constants.ORDER_BY_NAME);
        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                removeProgressBar();
            }
            public void onCancelled(FirebaseError firebaseError) { }
        });
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                mPreschool = snapshot.getValue(PreSchool.class);
                mSchoolsList.add(mPreschool);
                backupList.add(mPreschool);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search_menu_item_main).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.maps_menu_item_main:
                checkPerms();
                //buildIntentToMap();
                break;
            case R.id.saved_schools_menu_main:
                findSavedSchools();
                break;
            case R.id.all_schools_menu_main:
                cursorListView.setVisibility(View.INVISIBLE);
                mRecyclerView.setVisibility(View.VISIBLE);
                Toast.makeText(MainActivity.this, "all", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void buildIntentToMap() {
        HashMap<String, LatLng> markersHashMap = buildMapMarkers();
        Intent intentToMaps = new Intent(MainActivity.this, SchoolsMapFragment.class);
        intentToMaps.putExtra(Constants.ADDRESS_LIST_KEY, markersHashMap);
        intentToMaps.putExtra(Constants.SCHOOLS_LIST_KEY, mSchoolsList);
        startActivity(intentToMaps);
    }

    private void findSavedSchools() {
        cursor = dbHelper.findAllSavedSchools();
        if (cursor.getCount() > 0) {
            cursorAdapter = new DBCursorAdapter(MainActivity.this, cursor, 0);
            cursorListView.setAdapter(cursorAdapter);
            cursorListView.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.INVISIBLE);
            //cursor.close();
        } else {
            Toast.makeText(MainActivity.this, "No Saved Schools Yet", Toast.LENGTH_SHORT).show();
        }
    }

    private HashMap<String, LatLng> buildMapMarkers() {
        HashMap<String, LatLng> mapMarkersHashMap = new HashMap<>();
        LatLng coordinates;
        if (mSchoolsList == null) {
            return mapMarkersHashMap;
            //TODO return something else
        }
        for (int i = 0; i < mSchoolsList.size(); i++) {
            coordinates = new LatLng(mSchoolsList.get(i).getLatitude(), mSchoolsList.get(i).getLongitude());
            mapMarkersHashMap.put(mSchoolsList.get(i).getName(), coordinates);
        }

        return mapMarkersHashMap;
    }

    private void checkPerms() {
        if (permissionExists()){
            buildIntentToMap();
        } else {
            requestUserForPermission();
        }
    }

    /**
     * Returns true if the permission is granted. False otherwise.
     *
     * NOTE: If we detect that this phone is an older OS then Android M, we assume
     * the permission is true because they are granted at INSTALL time.
     *
     * @return
     */
    @TargetApi(23)
    private boolean permissionExists(){
        int currentApiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentApiVersion < Build.VERSION_CODES.M){

            // Permissions are already granted during INSTALL TIME for older OS version
            return true;
        }

        int granted = checkSelfPermission(LOCATION_PERMISSION);
        if (granted == PackageManager.PERMISSION_GRANTED){
            return true;
        }
        return false;
    }

    @TargetApi(23)
    private void requestUserForPermission(){
        int currentApiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentApiVersion < Build.VERSION_CODES.M){
            // This OS version is lower then Android M, therefore we have old permission model and should not ask for permission
            return;
        }

        // request the location!
        String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
        requestPermissions(permissions, PERMISSION_REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode){
            case PERMISSION_REQUEST_CODE:
                if (permissions.length < 0){
                    return; // no permissions were returned, nothing to process here
                }
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    // contacts permission was granted! Lets now grab contacts or show them!
                    buildIntentToMap();
                } else {
                    // contacts permission was denied, lets warn the user that we need this permission!
                    Toast.makeText(getApplicationContext(), "You need to grant location permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

}
