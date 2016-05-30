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
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.project.salminnella.prescoop.R;
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

    // region Member Variables
    private TextView mPhoneNumber;
    private TextView mFacilityNumber;
    private TextView mFacilityCapacity;
    private TextView mFacilityType;
    private TextView mLicenseStatus;
    private TextView mLicenseDate;
    private TextView mSchoolPrice;
    private TextView mSchoolNeighborhood;
    private TextView mSchoolWebLink;
    private TextView mSchoolName;
    private TextView mSchoolAddress;
    private TextView mYelpTitleText;
    private TextView mYelpNumReviews;
    private TextView mYelpSnippet;
    private boolean saveSchool;
    private ImageView mSchoolRating;
    private ImageView mYelpRating;
    private ListView mYelpListView;
    private YelpAdapter mYelpAdapter;
    private Business mYelpSchoolMatch;
    private PreSchool mPreschool;
    private ViewPager viewPager;
    private DatabaseHelper databaseHelper;
    private FloatingActionButton mFab;
    // endregion Member Variables


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_details);

        databaseHelper = DatabaseHelper.getInstance(SchoolDetailsActivity.this);
        receiveIntent();
        initToolbar();
        initViews();
        adjustFabIcon();
        populateSchoolDetails();
        callYelpProvider();
        initTabLayout();
        setClickListeners();
    }

    private void adjustFabIcon() {
        if (isBookmarkAlreadySaved()) {
            mFab.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_white_24dp));
            saveSchool = true;
        }
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        if (collapsingToolbarLayout != null) {
            collapsingToolbarLayout.setTitle(mPreschool.getName());

            collapsingToolbarLayout.setExpandedTitleColor(Color.TRANSPARENT);
        }

        loadBackdrop();
    }


    private void initViews() {
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mSchoolName = (TextView) findViewById(R.id.school_name_text_details);
        mSchoolAddress = (TextView) findViewById(R.id.school_address_text_details);
        mYelpTitleText = (TextView) findViewById(R.id.yelp_title_text_details);
        mYelpRating = (ImageView) findViewById(R.id.yelp_rating);
        mYelpListView = (ListView) findViewById(R.id.yelp_response_list);
        mYelpNumReviews = (TextView) findViewById(R.id.yelp_num_reviews);
        mYelpSnippet = (TextView) findViewById(R.id.yelp_review_snippet_details);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        mPhoneNumber = (TextView) findViewById(R.id.school_phone_text_details);
        mFacilityNumber = (TextView) findViewById(R.id.facility_num_text_details);
        mFacilityCapacity = (TextView) findViewById(R.id.facility_capacity_text_details);
        mFacilityType = (TextView) findViewById(R.id.facility_type_text_details);
        mLicenseStatus = (TextView) findViewById(R.id.license_status_text_details);
        mLicenseDate = (TextView) findViewById(R.id.license_date_text_details);
        mSchoolPrice = (TextView) findViewById(R.id.school_price_text_details);
        mSchoolNeighborhood = (TextView) findViewById(R.id.school_neighborhood_text_details);
        mSchoolWebLink = (TextView) findViewById(R.id.school_weblink_text_details);
        mSchoolRating = (ImageView) findViewById(R.id.school_rating_image_details);
    }

    private void loadBackdrop() {
        final ImageView imageView = (ImageView) findViewById(R.id.backdrop);
        if (mPreschool.getImageUrl().matches("")) {
            Picasso.with(SchoolDetailsActivity.this).load(R.drawable.no_image).into(imageView);
        } else {
            Picasso.with(SchoolDetailsActivity.this).load(mPreschool.getImageUrl())
                    .into(imageView);
        }
    }

    private void receiveIntent() {
        Intent receiveIntent = getIntent();
        mPreschool = (PreSchool) receiveIntent.getSerializableExtra(Constants.SCHOOL_OBJECT_KEY);
    }


    private void setClickListeners() {
        mSchoolWebLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startIntentToWebView(mPreschool.getWebsiteUrl(), mPreschool.getName(), mPreschool);
            }
        });

        mYelpListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Business clickedYelpBusiness = (Business) mYelpListView.getItemAtPosition(position);
                startIntentToWebView(clickedYelpBusiness.mobileUrl(), Constants.YELP_REVIEWS_TITLE, mPreschool);
            }
        });

        if (mFab != null) {
            mFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (saveSchool) {
                        mFab.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_border_white_24dp));
                        removeFavoriteSchool();
                        saveSchool = false;
                        Toast.makeText(SchoolDetailsActivity.this, R.string.remove_favorites, Toast.LENGTH_SHORT).show();
                    } else {
                        mFab.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_white_24dp));
                        addFavoriteSchool();
                        saveSchool = true;
                        Toast.makeText(SchoolDetailsActivity.this, R.string.add_favorites, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void setYelpClickListener() {
        mYelpTitleText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startIntentToWebView(mYelpSchoolMatch.mobileUrl(), Constants.YELP_REVIEWS_TITLE, mPreschool);
            }
        });
    }

    @Override
    public void listItemClicked(View view, String url) {
        startIntentToWebView(url, Constants.SCHOOL_REPORT_TITLE, mPreschool);
    }

    private void removeFavoriteSchool() {
        if (isBookmarkAlreadySaved()) {
            databaseHelper.deleteSavedSchool(mPreschool.getName());
        }
    }

    private void addFavoriteSchool() {
        if (!isBookmarkAlreadySaved()) {
            String reportsList = arrayListAsString(mPreschool);
            databaseHelper.insertSavedSchool(mPreschool, reportsList);
        }
    }

    private void startIntentToWebView(String url, String title, PreSchool preschool) {
        Intent intentToWebView = new Intent(SchoolDetailsActivity.this, WebViewActivity.class);
        intentToWebView.putExtra(Constants.WEB_URL_KEY, url); // weburl
        intentToWebView.putExtra(Constants.WEB_VIEW_TITLE_KEY, title); // review title
        intentToWebView.putExtra(Constants.SCHOOL_OBJECT_KEY, preschool); // preschool object
        startActivityForResult(intentToWebView, Constants.WEB_REQUEST_CODE);

    }

    private void populateSchoolDetails() {
        mSchoolName.setText(mPreschool.getName());
        setSchoolAddressTextView();
        mPhoneNumber.setText(mPreschool.getPhoneNumber());
        mFacilityNumber.setText(String.valueOf(mPreschool.getFacilityNumber()));
        mFacilityCapacity.setText(String.valueOf(mPreschool.getCapacity()));
        mFacilityType.setText(mPreschool.getType());
        mLicenseStatus.setText(mPreschool.getLicenseStatus());
        mLicenseDate.setText(mPreschool.getLicenseDate());
        if (mPreschool.getPrice() == 999) {
            mSchoolPrice.setText(R.string.contact_school_label_details);
        } else {
            String price = "$" + mPreschool.getPrice();
            mSchoolPrice.setText(price);
        }
        mSchoolNeighborhood.setText(mPreschool.getRegion());
        mSchoolWebLink.setText(mPreschool.getWebsiteUrl());
        mSchoolWebLink.setPaintFlags(mSchoolWebLink.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        mSchoolRating.setImageResource(Utilities.getRatingImage(mPreschool.getRating()));
    }

    private void setSchoolAddressTextView() {
        mSchoolAddress.setText(Utilities.buildAddressString(mPreschool.getStreetAddress(),
                mPreschool.getCity(),
                mPreschool.getState(),
                mPreschool.getZipCode()));
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
        params.put("term", mPreschool.getName());
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

                    mYelpSchoolMatch = filterYelpResponse(searchResponse);
                    if (mYelpSchoolMatch != null) {
                        mYelpTitleText.setText(mYelpSchoolMatch.name());
                        Picasso.with(SchoolDetailsActivity.this).load(mYelpSchoolMatch.ratingImgUrlLarge()).into(mYelpRating);
                        String reviewText = String.valueOf(mYelpSchoolMatch.reviewCount()) + " Reviews";
                        mYelpNumReviews.setText(reviewText);
                        mYelpSnippet.setText(mYelpSchoolMatch.snippetText());
                        mYelpTitleText.setPaintFlags(mYelpTitleText.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                        mYelpTitleText.setTextColor(Color.parseColor("#000099"));
                        setYelpClickListener();
                    } else {
                        mYelpTitleText.setText(R.string.empty_yelp_response_title);
                        ArrayList<Business> businesses = searchResponse.businesses();
                        mYelpAdapter = new YelpAdapter(SchoolDetailsActivity.this, businesses);
                        mYelpListView.setAdapter(mYelpAdapter);

                    }
                }
            }
            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                // HTTP error happened, do something to handle it.
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
            int strContains = response.businesses().get(i).name().indexOf(mPreschool.getName());
            if (strContains != -1) {
                return response.businesses().get(i);
            }
        }

        return null;
    }


    private void initTabLayout() {
        // Set PagerAdapter so that it can display items
        viewPager.setAdapter(new TabLayoutAdapter(getSupportFragmentManager(),
                SchoolDetailsActivity.this, mPreschool));

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
            intentToMaps.putExtra(Constants.SCHOOL_MARKER_KEY, mPreschool);
            startActivity(intentToMaps);
        }

        return super.onOptionsItemSelected(item);
    }

    private String arrayListAsString(PreSchool preschool) {
        if (preschool.getReports() != null) {
            Reports[] reportsArray = preschool.getReports();
            Gson gson = new Gson();

            return gson.toJson(reportsArray);
        } else {
            return null;
            //TODO check this return
        }
    }


    private boolean isBookmarkAlreadySaved() {
        Cursor bookmarkCursor = databaseHelper.findSavedSchool(mPreschool.getName());
        return bookmarkCursor.getCount() != 0;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                mPreschool = (PreSchool) data.getSerializableExtra(Constants.SCHOOL_OBJECT_KEY);
            }
        }
    }
}