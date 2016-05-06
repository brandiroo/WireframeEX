package com.project.salminnella.prescoop.firebase;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by anthony on 5/5/16.
 */
public class FireBasePrescoop extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
