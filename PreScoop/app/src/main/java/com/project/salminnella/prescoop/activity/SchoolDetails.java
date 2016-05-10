package com.project.salminnella.prescoop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.project.salminnella.prescoop.R;
import com.project.salminnella.prescoop.adapter.TabLayoutAdapter;
import com.project.salminnella.prescoop.adapter.YelpAdapter;
import com.project.salminnella.prescoop.model.PreSchool;
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

public class SchoolDetails extends AppCompatActivity {
    private static final String TAG = "SchoolDetails";
    TextView mSchoolName;
    TextView mSchoolAddress;
    PreSchool mPreschoolMain;
    TextView mYelpTitleText;
    ImageView mYelpRating;
    ListView mYelpListView;
    YelpAdapter mYelpAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_details);

        receiveIntent();
        initToolbar();
        initViews();
        setFab();
        populateSchoolDetails();
        callYelpProvider();
        initTabLayout();

    }

    private void setFab() {
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

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        if (collapsingToolbarLayout != null) {
            collapsingToolbarLayout.setTitle(mPreschoolMain.getName());
        }

        loadBackdrop();
    }


    private void initViews() {
        mSchoolName = (TextView) findViewById(R.id.school_name_text_details);
        mSchoolAddress = (TextView) findViewById(R.id.school_address_text_details);
        mYelpTitleText = (TextView) findViewById(R.id.yelp_title_text_details);
        mYelpRating = (ImageView) findViewById(R.id.yelp_rating);
        mYelpListView = (ListView) findViewById(R.id.yelp_response_list);
    }

    private void loadBackdrop() {
        final ImageView imageView = (ImageView) findViewById(R.id.backdrop);
        Glide.with(this).load(R.drawable.argonne_playground).centerCrop().into(imageView);
    }

    private void receiveIntent() {
        Intent receiveIntent = getIntent();
        mPreschoolMain = (PreSchool) receiveIntent.getSerializableExtra(Constants.SCHOOL_OBJECT_KEY);

    }


    private void populateSchoolDetails() {
        mSchoolName.setText(mPreschoolMain.getName());
        setSchoolAddressTextView();
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
//
//        Response<SearchResponse> response = null;
//        try {
//            response = call.execute();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        Callback<SearchResponse> callback = new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                SearchResponse searchResponse = response.body();
                Log.i(TAG, "onResponse: " + searchResponse);
                if (searchResponse.total() == 0) {
                    Log.i(TAG, "onResponse: nothing found from yelp");
                } else {
                    // Update UI text with the searchResponse.
                    Business schoolMatch = filterYelpResponse(searchResponse);
                    if (schoolMatch != null) {
                        mYelpTitleText.setText(schoolMatch.name());
                        Picasso.with(SchoolDetails.this).load(schoolMatch.ratingImgUrlLarge()).into(mYelpRating);
                    } else {
                        ArrayList<Business> businesses = searchResponse.businesses();
                        mYelpAdapter = new YelpAdapter(SchoolDetails.this, businesses);
                        mYelpListView.setAdapter(mYelpAdapter);
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
        Log.i(TAG, "filterYelpResponse: searching for " + mPreschoolMain.getName());
        Log.i(TAG, "filterYelpResponse: response total " + response.total());
        int limit = response.total();
        if (Constants.YELP_RESPONSE_LIMIT_INT < response.total()) {
            limit = Constants.YELP_RESPONSE_LIMIT_INT;
        }
        for (int i = 0; i < limit; i++) {
            Log.i(TAG, "filterYelpResponse: name " + response.businesses().get(i).name());
//            if (mPreschoolMain.getName().equals(response.businesses().get(i).name())) {
//                Log.i(TAG, "filterYelpResponse: found a match");
//            }
            int strContains = response.businesses().get(i).name().indexOf(mPreschoolMain.getName());
            if (strContains != -1) {
                Log.i(TAG, response.businesses().get(i).name() + " contains " + mPreschoolMain.getName());
                return response.businesses().get(i);
            }
        }

        return null;
    }


    private void initTabLayout() {
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new TabLayoutAdapter(getSupportFragmentManager(),
                SchoolDetails.this, mPreschoolMain));

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        //if (tabLayout != null) {
        tabLayout.setupWithViewPager(viewPager);
        //}
    }
}
