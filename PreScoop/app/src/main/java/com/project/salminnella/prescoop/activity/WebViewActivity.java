package com.project.salminnella.prescoop.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
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
    String url;
    ProgressBar progressBar;
    PreSchool mPreschoolHolder;

    private static final String LOCATION_PERMISSION = Manifest.permission.READ_CONTACTS;
    private static final int PERMISSION_REQUEST_CODE = 12345;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar_web_viewer);
        progressBar.setVisibility(View.VISIBLE);

        initToolbar();
        receiveIntent();
        //loadWebview();
        checkPerms();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentToDetailsActivity = new Intent(WebViewActivity.this, SchoolDetails.class);
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
//        setContentView(mWebview);
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void checkPerms() {
        if (permissionExists()){
            loadWebview();
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
                    loadWebview();
                } else {
                    // contacts permission was denied, lets warn the user that we need this permission!
                    Toast.makeText(getApplicationContext(), "You need to grant location permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.maps_menu_item_web) {
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intentToDetailsActivity = new Intent(WebViewActivity.this, SchoolDetails.class);
        intentToDetailsActivity.putExtra(Constants.SCHOOL_OBJECT_KEY, mPreschoolHolder);
        setResult(RESULT_OK, intentToDetailsActivity);
        finish();
    }
}
