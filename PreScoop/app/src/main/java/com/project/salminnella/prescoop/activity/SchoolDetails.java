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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.project.salminnella.prescoop.R;
import com.project.salminnella.prescoop.adapter.TabLayoutAdapter;
import com.project.salminnella.prescoop.model.PreSchool;
import com.project.salminnella.prescoop.utility.Constants;
import com.project.salminnella.prescoop.utility.Utilities;
import com.yelp.clientlib.connection.YelpAPI;
import com.yelp.clientlib.connection.YelpAPIFactory;
import com.yelp.clientlib.entities.Review;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_details);

        initToolbar();
        receiveIntent();
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
            collapsingToolbarLayout.setTitle("Top Stories");
        }

        loadBackdrop();
    }


    private void initViews() {
        mSchoolName = (TextView) findViewById(R.id.school_name_text_details);
        mSchoolAddress = (TextView) findViewById(R.id.school_address_text_details);
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
        params.put("limit", Constants.YELP_RESPONSE_LIMIT);
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
                if (searchResponse.total() == 0) {
                    Log.i(TAG, "onResponse: nothing found from yelp");
                } else {
                    //TODO if response is more then one, need to figure out which
                    // one to display
                    filterYelpResponse(searchResponse);
                    String schoolName = response.body().businesses().get(0).name();
                    String ratingImgUrl = response.body().businesses().get(0).ratingImgUrl();
                    String snippetText = response.body().businesses().get(0).snippetText();
                    ArrayList<Review> reviews = response.body().businesses().get(0).reviews();
                    String mobileUrl = response.body().businesses().get(0).mobileUrl();
                    // Update UI text with the searchResponse.
//                    Log.i(TAG, "response total: " + searchResponse.total());
//                    Log.i(TAG, "search response " + schoolName);
//                    Log.i(TAG, "rating image " + ratingImgUrl);
//                    Log.i(TAG, "snippet " + snippetText);
//                    Log.i(TAG, "reviews " + reviews);
//                    Log.i(TAG, "mobile url " + mobileUrl);
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

    private void filterYelpResponse(SearchResponse response) {
        Log.i(TAG, "filterYelpResponse: searching for " + mPreschoolMain.getName());
        Log.i(TAG, "filterYelpResponse: response total " + response.total());
        int limit = response.total();
        if (Integer.parseInt(Constants.YELP_RESPONSE_LIMIT) < response.total()) {
            limit = Integer.parseInt(Constants.YELP_RESPONSE_LIMIT);
        }
        for (int i = 0; i < limit; i++) {
            Log.i(TAG, "filterYelpResponse: name " + response.businesses().get(i).name());
        }
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
