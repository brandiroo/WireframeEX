package com.project.salminnella.prescoop.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
    private static final String TAG = "WebViewActivity";
    private WebView mWebview;
    String url;
    ProgressBar progressBar;
    PreSchool mPreschoolHolder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar_web_viewer);
        progressBar.setVisibility(View.VISIBLE);

        initToolbar();
        receiveIntent();
        loadWebview();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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
        url = receiveIntent.getStringExtra(Constants.WEB_URL_KEY);
        mPreschoolHolder = (PreSchool) receiveIntent.getSerializableExtra(Constants.SCHOOL_OBJECT_KEY);

    }

    private void loadWebview() {
        mWebview  = (WebView) findViewById(R.id.web_viewer);
//        mWebview  = new WebView(this);
        mWebview.getSettings().setJavaScriptEnabled(true); // enable javascript

        final Activity activity = this;
        mWebview.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(activity, description, Toast.LENGTH_SHORT).show();
            }
        });

        mWebview.loadUrl(url);
        Log.i(TAG, "loadWebview: " + url);
//        setContentView(mWebview);
        progressBar.setVisibility(View.INVISIBLE);
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_web_view_activity, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
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
}
