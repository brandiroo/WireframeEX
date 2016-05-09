package com.project.salminnella.prescoop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.project.salminnella.prescoop.R;
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
    String mSchoolKey;
    TextView mSchoolName;
    TextView mSchoolAddress;
    Firebase mFireBaseRoot, mFirebasePreschoolRef;
    PreSchool mPreschool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_details);

        initToolbar();
        receiveIntent();
        initViews();
        initFirebase();
        queryFirebase();
        //setFab();

        YelpAPIFactory apiFactory = new YelpAPIFactory(
                Constants.YELP_CONSUMER_KEY,
                Constants.YELP_CONSUMER_SECRET,
                Constants.YELP_TOKEN,
                Constants.YELP_TOKEN_SECRET);

        YelpAPI yelpAPI = apiFactory.createAPI();

        Map<String, String> params = new HashMap<>();

        // general params
        params.put("term", "Little Bee Daycare & Preschool");
        params.put("limit", "3");

//        // locale params
//        params.put("lang", "en");

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
                String schoolName = response.body().businesses().get(0).name();
                String ratingImgUrl = response.body().businesses().get(0).ratingImgUrl();
                String snippetText = response.body().businesses().get(0).snippetText();
                ArrayList<Review> reviews = response.body().businesses().get(0).reviews();
                String mobileUrl = response.body().businesses().get(0).mobileUrl();
                // Update UI text with the searchResponse.
                Log.i(TAG, "search response " + schoolName);
                Log.i(TAG, "rating image " + ratingImgUrl);
                Log.i(TAG, "snippet " + snippetText);
                Log.i(TAG, "reviews " + reviews);
                Log.i(TAG, "mobile url " + mobileUrl);
            }
            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                // HTTP error happened, do something to handle it.
            }
        };

        call.enqueue(callback);


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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setFab() {

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

    private void initFirebase(){
        mFireBaseRoot = new Firebase(Constants.FIREBASE_ROOT_URL);
        mFirebasePreschoolRef = mFireBaseRoot.child(Constants.FIREBASE_ROOT_CHILD);
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
        mSchoolKey = receiveIntent.getStringExtra(Constants.SCHOOL_TITLE_KEY);
    }

    private void queryFirebase(){
        Query queryRef = mFirebasePreschoolRef.orderByChild(Constants.ORDER_BY_NAME).equalTo(mSchoolKey);
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                mPreschool = snapshot.getValue(PreSchool.class);
                populateSchoolDetails();
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

    private void populateSchoolDetails() {
        mSchoolName.setText(mPreschool.getName());
        setSchoolAddressTextView();
    }



    private void setSchoolAddressTextView() {
        mSchoolAddress.setText(Utilities.buildAddressString(mPreschool.getStreetAddress(),
                mPreschool.getCity(),
                mPreschool.getState(),
                mPreschool.getZipCode()));
    }


}
