package com.project.salminnella.prescoop;

import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.project.salminnella.prescoop.activity.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;

/**
 * Created by anthony on 6/10/16.
 */
@RunWith(AndroidJUnit4.class)
public class FirebaseTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    @Test
    public void testFirebase() {

        final FirebaseOperationIdlingResource pushIdlingResource = new FirebaseOperationIdlingResource();
        Espresso.registerIdlingResources(pushIdlingResource);

        final String item = "Hello World";

        Firebase firebase = new Firebase("https://prescoop.firebaseio.com/");
        firebase.push().setValue(item, new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase itemRef) {
                itemRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        pushIdlingResource.onOperationEnded();
                        assertEquals(item, dataSnapshot.getValue(String.class));
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        firebaseError.toException().printStackTrace();
                        pushIdlingResource.onOperationEnded();
                    }
                });
            }

        });
        pushIdlingResource.onOperationStarted();

        Espresso.unregisterIdlingResources(pushIdlingResource);
    }

}
