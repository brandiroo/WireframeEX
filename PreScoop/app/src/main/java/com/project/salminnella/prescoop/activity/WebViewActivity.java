package com.project.salminnella.prescoop.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.project.salminnella.prescoop.R;
import com.project.salminnella.prescoop.model.PreSchool;
import com.project.salminnella.prescoop.utility.Constants;

/**
 * This activity displays the URL's that are selected from the SchoolDetailsActivity
 * DIsplays either the schools website, the yelp link to reviews, or the schools inspection report
 */
public class WebViewActivity extends AppCompatActivity {
    // region Member Variables
    private WebView mWebview;
    private String mUrl;
    private String mTitle;
    private ProgressBar mProgressBar;
    private PreSchool mPreschoolHolder;
    // endregion Member Variables

    /**
     * Creates Activity
     * @param savedInstanceState Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar_web_viewer);
        receiveIntent();
        setupToolbar();
        loadWebview();
    }

    /**
     * Initialize and setup the toolbar.  The up navigation needs to send back the preschool
     * object to populates its details.
     */
    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        assert toolbar != null;
        toolbar.setTitle(mTitle);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToDetailsActivity = new Intent(WebViewActivity.this, SchoolDetailsActivity.class);
                intentToDetailsActivity.putExtra(Constants.SCHOOL_OBJECT_KEY, mPreschoolHolder);
                setResult(RESULT_OK, intentToDetailsActivity);
                finish();
            }
        });
    }

    /**
     * Receives the intent from SchoolDetailsActivity.  Includes the Url for the web view,
     * the currently viewed preschool object to send back, and the title for the toolbar.
     */
    private void receiveIntent() {
        Intent receiveIntent = getIntent();
        mUrl = receiveIntent.getStringExtra(Constants.WEB_URL_KEY);
        mPreschoolHolder = (PreSchool) receiveIntent.getSerializableExtra(Constants.SCHOOL_OBJECT_KEY);
        mTitle = receiveIntent.getStringExtra(Constants.WEB_VIEW_TITLE_KEY);
    }

    /**
     *
     */
    private void loadWebview() {
        mWebview  = (WebView) findViewById(R.id.web_viewer);
        if (mWebview != null) {
            mWebview.getSettings().setJavaScriptEnabled(true); // enable javascript
        }

        final Activity activity = this;
        mWebview.setWebViewClient(new WebClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(activity, description, Toast.LENGTH_SHORT).show();
            }
        });

        mWebview.loadUrl(mUrl);
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    /**
     * Overrides the back press button to send an intent with the preschool object for
     * SchoolDetailsActivity to recreate.
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intentToDetailsActivity = new Intent(WebViewActivity.this, SchoolDetailsActivity.class);
        intentToDetailsActivity.putExtra(Constants.SCHOOL_OBJECT_KEY, mPreschoolHolder);
        setResult(RESULT_OK, intentToDetailsActivity);
        finish();
    }

    /**
     * Inner class WebClient, to allow some overrides for the progress bar to display while the page
     * is loading. Overrides shouldOverrideUrlLoading and onPageFinished
     */
    public class WebClient extends WebViewClient
    {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            mProgressBar.setVisibility(View.VISIBLE);
            view.loadUrl(url);
            return true;

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            mProgressBar.setVisibility(View.GONE);
        }
    }
}
