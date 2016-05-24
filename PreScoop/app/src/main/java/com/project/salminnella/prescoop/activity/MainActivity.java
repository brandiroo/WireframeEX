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
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.project.salminnella.prescoop.adapter.OnRvItemClickListener;
import com.project.salminnella.prescoop.dbHelper.DatabaseHelper;
import com.project.salminnella.prescoop.fragment.SchoolsMapFragment;
import com.project.salminnella.prescoop.model.PreSchool;
import com.project.salminnella.prescoop.utility.Constants;
import com.project.salminnella.prescoop.utility.NameComparator;
import com.project.salminnella.prescoop.utility.PriceComparator;
import com.project.salminnella.prescoop.utility.RatingComparator;
import com.project.salminnella.prescoop.utility.Utilities;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import static android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;


public class MainActivity extends AppCompatActivity implements OnRvItemClickListener{ //, DBCursorAdapter.OnItemClickListener
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
    private ProgressBar mProgressBar;
    private DatabaseHelper dbHelper;
    private Cursor cursor;
    private DBCursorAdapter dbCursorAdapter;
    private boolean isViewingSavedSchools;
    private ArrayList<PreSchool> filteredList;
    private SwipeRefreshLayout swipeContainer;
    private SearchView searchView;
    private MenuItem mSearchMenuItem;
    private String searchQuery;
    private Menu menu;



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
        initSwipeListener();

    }

    private void initViews() {
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.rvSchools);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
    }

    private void initSwipeListener() {
        // Setup refresh listener which triggers new data loading
        swipeContainer.setDistanceToTriggerSync(500);
        swipeContainer.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRecyclerView.setAdapter(mRecycleAdapter);
                mRecycleAdapter.swap(backupList);
                if (filteredList != null) {
                    filteredList.clear();
                }
                mSearchMenuItem.collapseActionView();
                isViewingSavedSchools = false;
                menu.getItem(1).setIcon(getResources().getDrawable(R.drawable.ic_favorite_border_white_24dp));
                swipeContainer.setRefreshing(false);
            }
        });
        // refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    private void buildBottomBar(Bundle savedInstanceState) {
        mBottomBar = BottomBar.attachShy((CoordinatorLayout) findViewById(R.id.coordinator_Layout_main),
                null, savedInstanceState);
        mBottomBar.noResizeGoodness();
        mBottomBar.noTabletGoodness();
        mBottomBar.setTextAppearance(R.style.CardView);
        mBottomBar.setItemsFromMenu(R.menu.menu_bottom_bar_main, new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                switchBottomBarTab(menuItemId);
            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {
                switchBottomBarTab(menuItemId);
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
                sortByName();
                break;
            case R.id.rating_sort_bottom_bar:
                sortByRating();
                break;
            case R.id.price_sort_bottom_bar:
                sortByPrice();
                break;
            case R.id.maps_bottom_bar:
                checkPermissions();
                break;

        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // restore the BottomBar's state, otherwise we would
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
    public void onListItemClick(PreSchool preschool) {
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

            searchQuery = intent.getStringExtra(SearchManager.QUERY);
            filteredList = Utilities.filterSchoolsList(searchQuery, mSchoolsList);
            if (filteredList.size() > 0) {
                mRecycleAdapter.swap(filteredList);
            } else {
                Toast.makeText(MainActivity.this, "No matches found.", Toast.LENGTH_SHORT).show();
            }

        }
    }
    // endregion FilterSearch

    // region SortMethods
    private void sortByPrice() {
        Collections.sort(mSchoolsList, new PriceComparator());
        mRecycleAdapter.notifyDataSetChanged();
        mRecyclerView.smoothScrollToPosition(0);
        Toast.makeText(MainActivity.this, "Sorted by Price", Toast.LENGTH_SHORT).show();
    }

    private void sortByRating() {
        Collections.sort(mSchoolsList, new RatingComparator());
        mRecycleAdapter.notifyDataSetChanged();
        mRecyclerView.smoothScrollToPosition(0);
        Toast.makeText(MainActivity.this, "Sorted by Rating", Toast.LENGTH_SHORT).show();
    }

    private void sortByName() {
        Collections.sort(mSchoolsList, new NameComparator());
        mRecycleAdapter.notifyDataSetChanged();
        mRecyclerView.smoothScrollToPosition(0);
        Toast.makeText(MainActivity.this, "Sorted Alphabetically", Toast.LENGTH_SHORT).show();
    }
    // endregion SortMethods

    private void showProgressBar() {
        if (mSchoolsList == null) {
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    private void removeProgressBar() {
        if (mSchoolsList.size() > 0) {
            mProgressBar.setVisibility(View.INVISIBLE);
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
        // the saved schools icon should change if list was refreshed
        this.menu = menu;

        // Associate searchable configuration with the SearchView
        mSearchMenuItem = menu.findItem(R.id.search_menu_item_main);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.search_menu_item_main).getActionView();
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
            case R.id.bookmarks_menu_item_main:
                swapListContents(item);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void swapListContents(MenuItem item) {
        if (isViewingSavedSchools) {
            mRecyclerView.setAdapter(mRecycleAdapter);
            if (filteredList != null && filteredList.size() > 0) {
                mRecycleAdapter.swap(filteredList);
                mSearchMenuItem.expandActionView();
                searchView.setQuery(searchQuery, false);
                searchView.clearFocus();
            } else {
                mRecycleAdapter.swap(backupList);
            }
            isViewingSavedSchools = false;
            item.setIcon(R.drawable.ic_favorite_border_white_24dp);
        } else {
            findSavedSchools();
            item.setIcon(R.drawable.ic_favorite_white_24dp);
            mSearchMenuItem.collapseActionView();

        }
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
            dbCursorAdapter = new DBCursorAdapter(MainActivity.this, cursor, this);
            mRecyclerView.setAdapter(dbCursorAdapter);
            isViewingSavedSchools = true;
        } else {
            Toast.makeText(MainActivity.this, "No Saved Schools Yet", Toast.LENGTH_SHORT).show();
        }
    }

    private HashMap<String, LatLng> buildMapMarkers() {
        HashMap<String, LatLng> mapMarkersHashMap = new HashMap<>();
        LatLng coordinates;
        if (mSchoolsList == null || cursor == null) {
            return mapMarkersHashMap;
        }
        if (isViewingSavedSchools) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++){
                String title = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_NAME));
                double latitude = cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.COL_LATITUDE));
                double longitude = cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.COL_LONGITUDE));
                coordinates = new LatLng(latitude, longitude);
                mapMarkersHashMap.put(title, coordinates);
                cursor.moveToNext();
            }
        } else {
            for (int i = 0; i < mSchoolsList.size(); i++) {
                String title = mSchoolsList.get(i).getName();
                double latitude = mSchoolsList.get(i).getLatitude();
                double longitude = mSchoolsList.get(i).getLongitude();
                coordinates = new LatLng(latitude, longitude);
                mapMarkersHashMap.put(title, coordinates);
            }
        }
        return mapMarkersHashMap;
    }

    // region Permission Check
    private void checkPermissions() {
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
    // endregion Permission Check



    @Override
    protected void onResume() {
        super.onResume();
        // clears the search filter focus to prevent keyboard from popping up when returning from
        // school details Activity.
        if (searchView != null) {
            searchView.clearFocus();
        }
        // refresh list with updated cursor contents if a saved school was removed
        if (cursor != null && isViewingSavedSchools) {
            findSavedSchools();
        }
    }
}
