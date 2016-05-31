package com.project.salminnella.prescoop.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
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

/**
 * This activity displays all the details about the school the user has selected.  Contains
 * a tab layout with pageviewer holding all licensing, citation, and inspection report information.
 * Calls yelps API, and filters the list for a match to the school name.
 *
 * An interface for a clickable textview in the Reports tab allows fragment to activity communication.
 * This allows the user to see the web page of the selected report on the school inspection.
 */
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
    private ImageView mSchoolRating;
    private ImageView mYelpRating;
    private ListView mYelpListView;
    private YelpAdapter mYelpAdapter;
    private Business mYelpSchoolMatch;
    private PreSchool mPreschool;
    private ViewPager mViewPager;
    private DatabaseHelper databaseHelper;
    private FloatingActionButton mFab;
    private boolean saveSchool;
    // endregion Member Variables


    /**
     * Activity is created
     * @param savedInstanceState Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_details);
        databaseHelper = DatabaseHelper.getInstance(SchoolDetailsActivity.this);
        receiveIntent();
        setupToolbar();
        initViews();
        adjustFabIcon();
        populateSchoolDetails();
        callYelpProvider();
        initTabLayout();
        setClickListeners();
    }

    /**
     * Receives Intent data from MainActivity or the Map Fragment.  The Preschool object is used
     * to populate the rest of the school details.
     */
    private void receiveIntent() {
        Intent receiveIntent = getIntent();
        mPreschool = (PreSchool) receiveIntent.getSerializableExtra(Constants.SCHOOL_OBJECT_KEY);
    }

    /**
     * Initializes, and sets up the collapsing toolbar, then loads the backdrop image.
     */
    private void setupToolbar() {
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

    /**
     * Loads the bacdrop image using Picasso, but not all schools have images.
     * When there isn't an image available, the backdrop loads
     * a default 'no image' available instead.
     */
    private void loadBackdrop() {
        final ImageView imageView = (ImageView) findViewById(R.id.backdrop);
        if (mPreschool.getImageUrl().matches("")) {
            Picasso.with(SchoolDetailsActivity.this).load(R.drawable.no_image).into(imageView);
        } else {
            Picasso.with(SchoolDetailsActivity.this).load(mPreschool.getImageUrl())
                    .into(imageView);
        }
    }

    /**
     * Initialize all views in the activity.
     */
    private void initViews() {
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mSchoolName = (TextView) findViewById(R.id.school_name_text_details);
        mSchoolAddress = (TextView) findViewById(R.id.school_address_text_details);
        mYelpTitleText = (TextView) findViewById(R.id.yelp_title_text_details);
        mYelpRating = (ImageView) findViewById(R.id.yelp_rating);
        mYelpListView = (ListView) findViewById(R.id.yelp_response_list);
        mYelpNumReviews = (TextView) findViewById(R.id.yelp_num_reviews);
        mYelpSnippet = (TextView) findViewById(R.id.yelp_review_snippet_details);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
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

    /**
     * When loading the activity, this checks if the school is currently saved as a favorite, and sets
     * it icon on the fab button to match. Full heart if it is a favorite, just an outline if it isn't.
     */
    private void adjustFabIcon() {
        if (isSchoolAlreadySaved()) {
            mFab.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_white_24dp));
            saveSchool = true;
        }
    }

    /**
     * Sets the click listeners.  There is a web url link, a list of Yelp reviews, and the fab icon
     */
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

    /**
     * This is called from within the API call to yelp. Is not included in above set click listeners
     * because there isn't always results to set the listener on.  Only needed when a resulting list from
     * yelp is created.
     */
    private void setYelpClickListener() {
        mYelpTitleText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startIntentToWebView(mYelpSchoolMatch.mobileUrl(), Constants.YELP_REVIEWS_TITLE, mPreschool);
            }
        });
    }

    /**
     * Interface method from TabLayoutFragment.ListItemClickable. The reports available to view for
     * each school are shown through a list in the Reports tablayout. This allows the fragment
     * to activity communication.
     * @param view View
     * @param url String
     */
    @Override
    public void listItemClicked(View view, String url) {
        startIntentToWebView(url, Constants.SCHOOL_REPORT_TITLE, mPreschool);
    }

    /**
     * Called from the Fab button click. If user wants to remove the school from their favorites
     */
    private void removeFavoriteSchool() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                if (isSchoolAlreadySaved()) {
                    databaseHelper.deleteSavedSchool(mPreschool.getName());
                }
                return null;
            }
        }.execute();
    }
    /**
     * Called from the Fab button click. If user wants to add the school to their favorites.
     * Also stores the array list of reports in one column as a GSON string
     */
    private void addFavoriteSchool() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                if (!isSchoolAlreadySaved()) {
                    String reportsList = arrayListAsString(mPreschool);
                    databaseHelper.insertSavedSchool(mPreschool, reportsList);
                }
                return null;
            }
        }.execute();
    }

    /**
     * Uses the reports list from the Preschool object, and converts it to a string to be easily
     * stored in the SQLite database on the device.
     * @param preschool Preschool
     * @return String
     */
    private String arrayListAsString(PreSchool preschool) {
        if (preschool.getReports() != null) {
            Reports[] reportsArray = preschool.getReports();
            Gson gson = new Gson();

            return gson.toJson(reportsArray);
        } else {
            return null;
        }
    }

    /**
     * This intent takes the url to load into the webview in the WebViewActivity.  The title is
     * for the toolbar, and the preschool object is used to reload the school details when the user
     * comes back.
     * @param url String
     * @param title String
     * @param preschool PreSchool
     */
    private void startIntentToWebView(String url, String title, PreSchool preschool) {
        Intent intentToWebView = new Intent(SchoolDetailsActivity.this, WebViewActivity.class);
        intentToWebView.putExtra(Constants.WEB_URL_KEY, url); // weburl
        intentToWebView.putExtra(Constants.WEB_VIEW_TITLE_KEY, title); // review title
        intentToWebView.putExtra(Constants.SCHOOL_OBJECT_KEY, preschool); // preschool object
        startActivityForResult(intentToWebView, Constants.WEB_REQUEST_CODE);

    }

    /**
     * Populates all school details that are not in the TabLayout
     */
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

    /**
     * The street address, city etc, are in separate fields. This puts them into one string
     */
    private void setSchoolAddressTextView() {
        mSchoolAddress.setText(Utilities.buildAddressString(mPreschool.getStreetAddress(),
                mPreschool.getCity(),
                mPreschool.getState(),
                mPreschool.getZipCode()));
    }

    /**
     * Calls Yelp API
     */
    // TODO obfuscate api keys
    private void callYelpProvider() {
        YelpAPIFactory apiFactory = new YelpAPIFactory(
                Constants.YELP_CONSUMER_KEY,
                Constants.YELP_CONSUMER_SECRET,
                Constants.YELP_TOKEN,
                Constants.YELP_TOKEN_SECRET);

        YelpAPI yelpAPI = apiFactory.createAPI();

        Map<String, String> params = new HashMap<>();

        // general params
        params.put(Constants.YELP_SEARCH_PARAM_TERMS, mPreschool.getName());
        params.put(Constants.YELP_SEARCH_PARAM_LIMIT, Constants.YELP_RESPONSE_LIMIT_STRING);
        params.put(Constants.YELP_SEARCH_PARAM_CATEGORY, Constants.YELP_CATEGORY);
        params.put(Constants.YELP_SEARCH_PARAM_SORT, Constants.YELP_SORT);

        Call<SearchResponse> call = yelpAPI.search(Constants.YELP_SEARCH_PARAM_LOCATION, params);

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
                        String reviewText = String.valueOf(mYelpSchoolMatch.reviewCount()) + getString(R.string.yelp_reviews_text);
                        mYelpNumReviews.setText(reviewText);
                        mYelpSnippet.setText(mYelpSchoolMatch.snippetText());
                        mYelpTitleText.setPaintFlags(mYelpTitleText.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                        mYelpTitleText.setTextColor(Color.parseColor(getString(R.string.html_color_num_blue)));
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
                // Handle HTTP error
            }
        };

        call.enqueue(callback);
    }

    /**
     * There is always a list of businesses in respose to calling Yelps API.  This one finds the best
     * match and uses it to populate the reviews.
     * Loops through the response to see if the name of the preschool is in there.
     * @param response SearchResponse
     * @return Business - model provided by yelp.
     */
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


    /**
     * Initializes and sets up the TabLayout. The Preschool object is passed into the constructor of
     * the adapter to fill in all necessary licensing and reports information.
     */
    private void initTabLayout() {
        // Set PagerAdapter so that it can display items
        mViewPager.setAdapter(new TabLayoutAdapter(getSupportFragmentManager(),
                SchoolDetailsActivity.this, mPreschool));

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        if (tabLayout != null) {
            tabLayout.setupWithViewPager(mViewPager);
        }
    }

    /**
     * Inflate options menu for the Toolbar
     * @param menu Menu
     * @return Boolean
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_school_details_activity, menu);

        return true;
    }

    /**
     * Handle menu item selections
     * @param item MenuItem
     * @return Boolean
     */
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

    /**
     * Used to confirm if the school has already been saved to the database.
     * @return Boolean
     */
    private boolean isSchoolAlreadySaved() {
        Cursor bookmarkCursor = databaseHelper.findSavedSchool(mPreschool.getName());
        return bookmarkCursor.getCount() != 0;
    }

    /** When returning from the web activity, the preschool objects needs to sent and returned in order
     * to populate the school details again.
     *
     * @param requestCode int
     * @param resultCode int
     * @param data Intent
     */
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