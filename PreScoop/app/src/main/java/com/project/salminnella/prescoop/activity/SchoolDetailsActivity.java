package com.project.salminnella.prescoop.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.project.salminnella.prescoop.R;
import com.project.salminnella.prescoop.adapter.ListAdapter;
import com.project.salminnella.prescoop.adapter.TabLayoutAdapter;
import com.project.salminnella.prescoop.adapter.YelpAdapter;
import com.project.salminnella.prescoop.dbHelper.DatabaseHelper;
import com.project.salminnella.prescoop.fragment.SchoolsMapFragment;
import com.project.salminnella.prescoop.fragment.TabLayoutFragment;
import com.project.salminnella.prescoop.model.PreSchool;
import com.project.salminnella.prescoop.model.Reports;
import com.project.salminnella.prescoop.utility.Constants;
import com.project.salminnella.prescoop.utility.Utilities;
import com.squareup.picasso.Picasso;
import com.yelp.clientlib.connection.YelpAPI;
import com.yelp.clientlib.connection.YelpAPIFactory;
import com.yelp.clientlib.entities.Business;
import com.yelp.clientlib.entities.SearchResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SchoolDetailsActivity extends AppCompatActivity implements TabLayoutFragment.ListItemClickable {
    private static final String TAG = "SchoolDetailsActivity";
    private TextView mSchoolName;
    private TextView mSchoolAddress;
    private PreSchool mPreschoolMain;
    private TextView mYelpTitleText;
    private TextView mYelpNumReviews;
    private ImageView mYelpRating;
    private ListView mYelpListView;
    private ViewPager viewPager;
    private YelpAdapter mYelpAdapter;
    private boolean saveSchool;
    private DatabaseHelper databaseHelper;
    private Business schoolMatch;
    private FloatingActionButton fab;
    private TextView mPhoneNumber;
    private TextView mFacilityNumber;
    private TextView mFacilityCapacity;
    private TextView mFacilityType;
    private TextView mLicenseStatus;
    private TextView mLicenseDate;

    private RecyclerView mRecyclerView;
    private ListAdapter mRecycleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_details);

        databaseHelper = DatabaseHelper.getInstance(SchoolDetailsActivity.this);
        receiveIntent();
        initToolbar();
        initViews();
        setFab();
        adjustFabIcon();
        populateSchoolDetails();
        callYelpProvider();
        initTabLayout();

        setYelpListClickListener();

    }

    private void setYelpListClickListener() {
        mYelpListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Business clickedYelpBusiness = (Business) mYelpListView.getItemAtPosition(position);
                Intent intentToWebView = new Intent(SchoolDetailsActivity.this, WebViewActivity.class);
                intentToWebView.putExtra(Constants.WEB_URL_KEY, clickedYelpBusiness.mobileUrl());
                intentToWebView.putExtra(Constants.SCHOOL_OBJECT_KEY, mPreschoolMain);
                startActivityForResult(intentToWebView, Constants.WEB_REQUEST_CODE);
            }
        });
    }

    private void setFab() {
        fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i(TAG, "onClick: fab - saveSchool = " + saveSchool);
                    if (saveSchool) {
                        fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_bookmark));
                        saveSchool = false;
                    } else {
                        fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_bookmark_selected));
                        saveSchool = true;
                    }
                    Log.i(TAG, "onClick: fab - saveSchool after click = " + saveSchool);
                }
            });
        }
    }

    private void adjustFabIcon() {
        Log.i(TAG, "adjustFabIcon: before if - saveSchool = " + saveSchool);
        if (isBookmarkAlreadySaved()) {
            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_bookmark_selected));
            saveSchool = true;
        }
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        if (collapsingToolbarLayout != null) {
            collapsingToolbarLayout.setTitle(mPreschoolMain.getName());

            collapsingToolbarLayout.setExpandedTitleColor(Color.TRANSPARENT);
        }

        loadBackdrop();
    }


    private void initViews() {
        mSchoolName = (TextView) findViewById(R.id.school_name_text_details);
        mSchoolAddress = (TextView) findViewById(R.id.school_address_text_details);
        mYelpTitleText = (TextView) findViewById(R.id.yelp_title_text_details);
        mYelpRating = (ImageView) findViewById(R.id.yelp_rating);
        mYelpListView = (ListView) findViewById(R.id.yelp_response_list);
        //mRecyclerView = (RecyclerView) findViewById(R.id.rvSchoolsYelp);
        mYelpNumReviews = (TextView) findViewById(R.id.yelp_num_reviews);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        mPhoneNumber = (TextView) findViewById(R.id.school_phone_text_details);
        mFacilityNumber = (TextView) findViewById(R.id.facility_num_text_details);
        mFacilityCapacity = (TextView) findViewById(R.id.facility_capacity_text_details);
        mFacilityType = (TextView) findViewById(R.id.facility_type_text_details);
        mLicenseStatus = (TextView) findViewById(R.id.license_status_text_details);
        mLicenseDate = (TextView) findViewById(R.id.license_date_text_details);
    }

    private void loadBackdrop() {
        final ImageView imageView = (ImageView) findViewById(R.id.backdrop);
        if (mPreschoolMain.getImageUrl().matches("")) {
            Picasso.with(SchoolDetailsActivity.this).load(R.drawable.no_image_available).into(imageView);
        } else {
            Picasso.with(SchoolDetailsActivity.this).load(mPreschoolMain.getImageUrl())
                    .into(imageView);
        }
    }

    private void receiveIntent() {
        Intent receiveIntent = getIntent();
        mPreschoolMain = (PreSchool) receiveIntent.getSerializableExtra(Constants.SCHOOL_OBJECT_KEY);
    }

    private void createRecycler() {
    }


    private void populateSchoolDetails() {
        mSchoolName.setText(mPreschoolMain.getName());
        setSchoolAddressTextView();
        mPhoneNumber.setText(mPreschoolMain.getPhoneNumber());
        mFacilityNumber.setText(String.valueOf(mPreschoolMain.getFacilityNumber()));
        mFacilityCapacity.setText(String.valueOf(mPreschoolMain.getCapacity()));
        mFacilityType.setText(mPreschoolMain.getType());
        mLicenseStatus.setText(mPreschoolMain.getLicenseStatus());
        mLicenseDate.setText(mPreschoolMain.getLicenseDate());
    }

    private void setSchoolAddressTextView() {
        mSchoolAddress.setText(Utilities.buildAddressString(mPreschoolMain.getStreetAddress(),
                mPreschoolMain.getCity(),
                mPreschoolMain.getState(),
                mPreschoolMain.getZipCode()));
    }

    private void callYelpProvider() {
        YelpAPIFactory apiFactory = new YelpAPIFactory(
                Constants.YELP_CONSUMER_KEY,
                Constants.YELP_CONSUMER_SECRET,
                Constants.YELP_TOKEN,
                Constants.YELP_TOKEN_SECRET);

        YelpAPI yelpAPI = apiFactory.createAPI();

        Map<String, String> params = new HashMap<>();

        // general params
        params.put("term", mPreschoolMain.getName());
        params.put("limit", Constants.YELP_RESPONSE_LIMIT_STRING);
        params.put("category_filter", "preschools");
        params.put("sort", "0");

        Call<SearchResponse> call = yelpAPI.search("San Francisco", params);

        Callback<SearchResponse> callback = new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                SearchResponse searchResponse = response.body();
                if (searchResponse.total() == 0) {
                    mYelpTitleText.setText(R.string.empty_yelp_response);
                } else {
                    // Update UI text with the searchResponse.

                    schoolMatch = filterYelpResponse(searchResponse);
                    if (schoolMatch != null) {
                        mYelpTitleText.setText(schoolMatch.name());
                        Picasso.with(SchoolDetailsActivity.this).load(schoolMatch.ratingImgUrlLarge()).into(mYelpRating);
                        String reviewText = String.valueOf(schoolMatch.reviewCount()) + " Reviews";
                        mYelpNumReviews.setText(reviewText);
                        mYelpTitleText.setPaintFlags(mYelpTitleText.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                        mYelpTitleText.setTextColor(Color.parseColor("#000099"));
                        mYelpTitleText.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intentToWebView = new Intent(SchoolDetailsActivity.this, WebViewActivity.class);
                                intentToWebView.putExtra(Constants.WEB_URL_KEY, schoolMatch.mobileUrl());
                                intentToWebView.putExtra(Constants.SCHOOL_MARKER_KEY, mPreschoolMain);
                                startActivityForResult(intentToWebView, Constants.WEB_REQUEST_CODE);
                            }
                        });
                    } else {
                        mYelpTitleText.setText(R.string.empty_yelp_response_title);
                        ArrayList<Business> businesses = searchResponse.businesses();
                        mYelpAdapter = new YelpAdapter(SchoolDetailsActivity.this, businesses);
                        mYelpListView.setAdapter(mYelpAdapter);
//
//
//                        mRecyclerView.setLayoutManager(new LinearLayoutManager(SchoolDetailsActivity.this));
//                        mRecycleAdapter = new ListAdapter(businesses);
//                        mRecyclerView.setAdapter(mRecycleAdapter);
                    }
                }
            }
            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                // HTTP error happened, do something to handle it.
                Log.i(TAG, "on Yelp Failure: ");
            }
        };

        call.enqueue(callback);
    }

    private Business filterYelpResponse(SearchResponse response) {
        int limit = response.total();
        if (Constants.YELP_RESPONSE_LIMIT_INT < response.total()) {
            limit = Constants.YELP_RESPONSE_LIMIT_INT;
        }
        for (int i = 0; i < limit; i++) {
            int strContains = response.businesses().get(i).name().indexOf(mPreschoolMain.getName());
            if (strContains != -1) {
                Log.i(TAG, response.businesses().get(i).name() + " contains " + mPreschoolMain.getName());
                return response.businesses().get(i);
            }
        }

        return null;
    }


    private void initTabLayout() {
        // Set PagerAdapter so that it can display items
        viewPager.setAdapter(new TabLayoutAdapter(getSupportFragmentManager(),
                SchoolDetailsActivity.this, mPreschoolMain));

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        if (tabLayout != null) {
            tabLayout.setupWithViewPager(viewPager);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_school_details_activity, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.maps_menu_item_details) {
            Intent intentToMaps = new Intent(SchoolDetailsActivity.this, SchoolsMapFragment.class);
            intentToMaps.putExtra(Constants.SCHOOL_MARKER_KEY, mPreschoolMain);
            startActivity(intentToMaps);
        }

        return super.onOptionsItemSelected(item);
    }

    private String arrayListAsString(PreSchool preschool) {
//        JSONObject json = new JSONObject();
//        ArrayList<Reports> reportsArrayList = new ArrayList<>();
//        reportsArrayList.addAll(preschool.getReportsData());
//        try {
//            json.put("schoolReports", new JSONArray(reportsArrayList));
//            Log.i(TAG, "arrayListAsString: " + json);
//            for (int i = 0; i < preschool.getReportsData().size(); i++) {
//                Log.i(TAG, "arrayListAsString: url " + preschool.getReportsData().get(i).getmReportUrl());
//                Log.i(TAG, "arrayListAsString: date " + preschool.getReportsData().get(i).getmDate());
//                Log.i(TAG, "arrayListAsString: title " + preschool.getReportsData().get(i).getmTitle());
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        String arrayList = json.toString();
//        Log.i(TAG, "arrayListAsString: " + arrayList);
//
//        return arrayList;
        return null;
    }

    private String arrayListAsGSONString(PreSchool preschool) {
        if (preschool.getReports() != null) {
            Reports[] reportsArray = preschool.getReports();
            Gson gson = new Gson();
            String inputString = gson.toJson(reportsArray);
            Log.i(TAG, "arrayListAsString: " + inputString);

            return inputString;
        } else {
            return null;
            //TODO check this return
        }
    }



    private boolean isBookmarkAlreadySaved() {
        Cursor bookmarkCursor = databaseHelper.findSavedSchool(mPreschoolMain.getName());
        return bookmarkCursor.getCount() != 0;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (saveSchool) {
            if (!isBookmarkAlreadySaved()) {
                String reportsList = arrayListAsGSONString(mPreschoolMain);
                databaseHelper.insertSavedSchool(mPreschoolMain, reportsList);
            }
        } else if (isBookmarkAlreadySaved()) {
            databaseHelper.deleteSavedSchool(mPreschoolMain.getName());
        }
    }

    @Override
    public void listItemClicked(View view, String url) {
        Intent intentWebView = new Intent(SchoolDetailsActivity.this, WebViewActivity.class);
        intentWebView.putExtra(Constants.WEB_URL_KEY, url);
        startActivity(intentWebView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                mPreschoolMain = (PreSchool) data.getSerializableExtra(Constants.SCHOOL_OBJECT_KEY);
            }
        }
    }
}






