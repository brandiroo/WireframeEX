package com.project.salminnella.prescoop;

import android.test.ApplicationTestCase;

import com.firebase.client.Firebase;
import com.project.salminnella.prescoop.activity.MainActivity;
import com.project.salminnella.prescoop.firebase.FireBasePrescoop;
import com.project.salminnella.prescoop.model.PreSchool;

import org.junit.Test;

import java.util.ArrayList;

/**
 * Created by anthony on 6/2/16.
 */
public class MainActivityTest extends ApplicationTestCase<FireBasePrescoop> {
    private Firebase mRef;
    public static FireBasePrescoop application;
    private ArrayList<PreSchool> mSchoolsList;
    private ArrayList<PreSchool> mBackupList;
    private Firebase mFirebasePreschoolRef;
    private PreSchool mPreschool;

    public MainActivityTest() {
        super(FireBasePrescoop.class);
    }

    @Test
    public void testQueryFirebase() {

        PreSchool preschool = new PreSchool();
        MainActivity mainActivity = new MainActivity();
        mainActivity.queryFirebase();
    }

//    @Override
//    public void setUp() throws Exception {
//        super.setUp();
//        if (application == null) {
//            application = getApplication();
//        }
//        if (application == null) {
//            application = (FireBasePrescoop) getContext().getApplicationContext();
//            assertNotNull(application);
//            long start = System.currentTimeMillis();
//            while (!application.isInitialized()){
//                Thread.sleep(300);  //wait until FireBase is totally initialized
//                if ( (System.currentTimeMillis() - start ) >= 1000 )
//                    throw new TimeoutException(this.getClass().getName() +"Setup timeOut");
//            }
//        }
//    }


//    @Test
//    public void testWrite(){
//        Firebase cloud = new Firebase("https://tesing-8f8da.firebaseio.com/");
//        cloud.child("message").setValue("Do you have data? You'll love Firebase.");
//    }



//    @Override
//    protected void setUp() throws Exception {
//        super.setUp();
//        if (application == null) {
//            application = getApplication();
//        }
//        if (application == null) {
//            application = (FireBasePrescoop) getContext().getApplicationContext();
//            assertNotNull(application);
//            long start = System.currentTimeMillis();
//            while (!application.isInitialized()){
//                Thread.sleep(300);  //wait until FireBase is totally initialized
//                if ( (System.currentTimeMillis() - start ) >= 1000 )
//                    throw new TimeoutException(this.getClass().getName() +"Setup timeOut");
//            }
//        }
//    }

//    @Before
//    public void setUp() throws Exception {
////        activity = getActivity();
////        mContext = activity;
//        application = (FireBasePrescoop) getContext().getApplicationContext();
//        Firebase.setAndroidContext(application.getApplicationContext());
//        Firebase mFireBaseRoot = new Firebase(Constants.FIREBASE_ROOT_URL);
//        mRef = mFireBaseRoot.child(Constants.FIREBASE_ROOT_CHILD);
//        mRef.removeValue();
//        runAndWaitUntil(mRef, new Runnable() {
//            public void run() {
//                mSchoolsList = new ArrayList<>();
//                mBackupList = new ArrayList<>();
//                Query queryRef = mFirebasePreschoolRef.orderByChild(Constants.ORDER_BY_NAME);
//                queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                    }
//                    public void onCancelled(FirebaseError firebaseError) { }
//                });
//                queryRef.addChildEventListener(new ChildEventListener() {
//                    @Override
//                    public void onChildAdded(DataSnapshot snapshot, String previousChild) {
//                        mPreschool = snapshot.getValue(PreSchool.class);
//                        mSchoolsList.add(mPreschool);
//                    }
//
//                    @Override
//                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//                    }
//
//                    @Override
//                    public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//                    }
//
//                    @Override
//                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//                    }
//
//                    @Override
//                    public void onCancelled(FirebaseError firebaseError) {
//
//                    }
//                });
//            }
//        }, new Callable<Boolean>() {
//            public Boolean call() throws Exception {
//                return true;
//                return mArray.getCount() == 3;
//            }
//        });
//    }
//
//    @After
//    public void tearDown() throws Exception {
//        mRef.removeValue();
//    }

//    public static void runAndWaitUntil(final Firebase ref, Runnable task, Callable<Boolean> done) throws InterruptedException {
//        final java.util.concurrent.Semaphore semaphore = new java.util.concurrent.Semaphore(0);
//        Firebase mFireBaseRoot = new Firebase(Constants.FIREBASE_ROOT_URL);
//        Firebase mFirebasePreschoolRef = mFireBaseRoot.child(Constants.FIREBASE_ROOT_CHILD);
//        Query queryRef = mFirebasePreschoolRef.orderByChild(Constants.ORDER_BY_NAME);
//
//        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                semaphore.release();
//            }
//            public void onCancelled(FirebaseError firebaseError) { }
//        });
//        task.run();
//        boolean isDone = false;
//        long startedAt = System.currentTimeMillis();
//        while (!isDone && System.currentTimeMillis() - startedAt < 5000) {
//            semaphore.tryAcquire(1, TimeUnit.SECONDS);
//            try {
//                isDone = done.call();
//            } catch (Exception e) {
//                e.printStackTrace();
//                // and we're not done
//            }
//        }
//        if (!isDone) {
//            throw new AssertionFailedError();
//        }
//        //array.setOnChangedListener(null);
//    }


//    @Override
//    protected void setUp() throws Exception {
//        super.setUp();
//
//        application = (FireBasePrescoop) getApplication().getApplicationContext();
//        mContext = application;
//        Firebase.setAndroidContext(application);
////        Firebase.setAndroidContext(getApplication().getApplicationContext());
//        //isInitialized = true;
//            Firebase mFireBaseRoot = new Firebase(Constants.FIREBASE_ROOT_URL);
//            mFirebasePreschoolRef = mFireBaseRoot.child(Constants.FIREBASE_ROOT_CHILD);
//    }

//    @Before
//
//
//
//    @Test
//    public void testReceiveSchools(){
//        mSchoolsList = new ArrayList<>();
//        mBackupList = new ArrayList<>();
//        Firebase mFireBaseRoot = new Firebase(Constants.FIREBASE_ROOT_URL);
//        mFirebasePreschoolRef = mFireBaseRoot.child(Constants.FIREBASE_ROOT_CHILD);
//        Query queryRef = mFirebasePreschoolRef.orderByChild(Constants.ORDER_BY_NAME);
//        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            public void onDataChange(DataSnapshot dataSnapshot) {
//            }
//            public void onCancelled(FirebaseError firebaseError) { }
//        });
//        queryRef.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot snapshot, String previousChild) {
//                mPreschool = snapshot.getValue(PreSchool.class);
//                mSchoolsList.add(mPreschool);
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//
//            }
//        });
//    }
}
