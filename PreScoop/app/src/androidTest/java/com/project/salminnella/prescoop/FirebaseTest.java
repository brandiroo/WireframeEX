package com.project.salminnella.prescoop;

import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.project.salminnella.prescoop.activity.MainActivity;
import com.project.salminnella.prescoop.model.PreSchool;
import com.project.salminnella.prescoop.utility.Constants;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

/**
 * Created by anthony on 6/10/16.
 */
@RunWith(AndroidJUnit4.class)
public class FirebaseTest {

    Firebase firebase = new Firebase(Constants.FIREBASE_ROOT_URL);
    PreSchool mPreschool;

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<MainActivity>(MainActivity.class);

//    @Test
//    public void testFirebase() {
//
//        final FirebaseOperationIdlingResource pushIdlingResource = new FirebaseOperationIdlingResource();
//        Espresso.registerIdlingResources(pushIdlingResource);
//
//        final String item = "Hello World";
//
//        firebase.push().setValue(item, new Firebase.CompletionListener() {
//            @Override
//            public void onComplete(FirebaseError firebaseError, Firebase itemRef) {
//                itemRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        pushIdlingResource.onOperationEnded();
//                        assertEquals(item, dataSnapshot.getValue(String.class));
//                    }
//
//                    @Override
//                    public void onCancelled(FirebaseError firebaseError) {
//                        firebaseError.toException().printStackTrace();
//                        pushIdlingResource.onOperationEnded();
//                    }
//                });
//            }
//
//        });
//        pushIdlingResource.onOperationStarted();
//
//        Espresso.unregisterIdlingResources(pushIdlingResource);
//    }

    @Test
    public void testReceiveSchools() {
        final FirebaseOperationIdlingResource receiveIdlingResource = new FirebaseOperationIdlingResource();
        Espresso.registerIdlingResources(receiveIdlingResource);

        //final ArrayList<PreSchool> schoolsList = new ArrayList<>();

        Query queryRef = firebase.orderByChild(Constants.ORDER_BY_NAME);
        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                receiveIdlingResource.onOperationEnded();
                mPreschool = dataSnapshot.getValue(PreSchool.class);
                System.out.println("onDataChange: mPreschool " + mPreschool.getName());
//                System.out.println("onDataChange: name is " + schoolsList.get(0).getName());
//                System.out.println("onDataChange: snapshot is " + dataSnapshot.getValue());
                //testPrintSchoolName(schoolsList);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                firebaseError.toException().printStackTrace();
                receiveIdlingResource.onOperationEnded();
            }
        });
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                mPreschool = dataSnapshot.getValue(PreSchool.class);
                ArrayList<PreSchool> schoolsList = new ArrayList<>();
                schoolsList.add(mPreschool);
                System.out.println("onChildAdded: snapshot is " + dataSnapshot.getValue());
                System.out.println("onChildAdded: mPreschool name is " + mPreschool.getName());
                System.out.println("onChildAdded: schoolList " + schoolsList.size());
                System.out.println("onChildAdded: schoolList " + schoolsList.get(0).getName());
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

        receiveIdlingResource.onOperationStarted();
        Espresso.unregisterIdlingResources(receiveIdlingResource);
    }

    @Test
    public void testSort() {

    }

    public void testPrintSchoolName(ArrayList<PreSchool> data) {
        for (int i = 0; i < data.size(); i++) {
            System.out.println("from test print - name is: " + data.get(0).getName());
        }
    }
}
