package com.project.salminnella.prescoop;

import android.support.test.espresso.IdlingResource;

/**
 * Created by anthony on 6/10/16.
 */
public class FirebaseOperationIdlingResource implements IdlingResource {

    private boolean idleNow = true;
    private ResourceCallback callback;

    @Override
    public String getName() {
        return "FirebaseOperationIdlingResource";
    }

    public void onOperationStarted() {
        idleNow = false;
    }

    public void onOperationEnded() {
        idleNow = true;
        if (callback != null) {
            callback.onTransitionToIdle();
        }
    }

    @Override
    public boolean isIdleNow() {
        return idleNow;
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        this.callback = callback;
    }
}
