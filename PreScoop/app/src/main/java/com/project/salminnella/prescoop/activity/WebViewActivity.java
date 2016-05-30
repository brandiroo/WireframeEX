package com.project.salminnella.prescoop.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.project.salminnella.prescoop.R;
import com.project.salminnella.prescoop.model.PreSchool;
import com.project.salminnella.prescoop.utility.Constants;

public class WebViewActivity extends AppCompatActivity {
    private WebView mWebview;
    private String mUrl;
    private String mTitle;
    private ProgressBar mProgressBar;
    private PreSchool mPreschoolHolder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar_web_viewer);

        receiveIntent();
        initToolbar();
        loadWebview();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(mTitle);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (toolbar != null) {
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
    }

    private void receiveIntent() {
        Intent receiveIntent = getIntent();
        mUrl = receiveIntent.getStringExtra(Constants.WEB_URL_KEY);
        mPreschoolHolder = (PreSchool) receiveIntent.getSerializableExtra(Constants.SCHOOL_OBJECT_KEY);
        mTitle = receiveIntent.getStringExtra(Constants.WEB_VIEW_TITLE_KEY);
    }

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



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_web_view_activity, menu);


        return true;
    }

    /**
     *  Handle action bar item clicks here.
     * @param item MenuItem
     * @return Boolean
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intentToDetailsActivity = new Intent(WebViewActivity.this, SchoolDetailsActivity.class);
        intentToDetailsActivity.putExtra(Constants.SCHOOL_OBJECT_KEY, mPreschoolHolder);
        setResult(RESULT_OK, intentToDetailsActivity);
        finish();
    }

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
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);

            mProgressBar.setVisibility(View.GONE);
        }
    }
}
