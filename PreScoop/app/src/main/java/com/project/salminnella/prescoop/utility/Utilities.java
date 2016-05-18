package com.project.salminnella.prescoop.utility;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;
import com.project.salminnella.prescoop.model.PreSchool;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anthony on 5/7/16.
 * Holds methods that are used through out the application
 */
public final class Utilities {

    /**
     * takes an address String, and returns a Latitude / Longitude that the Maps fragment
     * will use to add markers with
     * @param context Context
     * @param strAddress String
     * @return LatLng
     */
    public static LatLng getLocationFromAddress(Context context, String strAddress)
    {
        Geocoder coder= new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try
        {
            address = coder.getFromLocationName(strAddress, 5);
            if(address==null)
            {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return p1;
    }


    public static String buildAddressString(String streetAddress, String city, String state, String zipcode) {
        String strAddress = streetAddress + ", " + city + ", " + state + " " + zipcode;
        return strAddress;
    }




    public static ArrayList<PreSchool> filterSchoolsList(String query, List<PreSchool> schoolsList) {
        ArrayList<PreSchool> filteredList = new ArrayList<>();
        if (!query.equals("")) {
            char first = query.charAt(0);
            if (first >= '0' && first <= '9') {
                filteredList = searchByZipCode(query, schoolsList);
            } else if (Character.isLetter(first)) {
                filteredList = searchByNeighborhood(query, schoolsList);
            } else {
                filteredList = searchByPriceRange(query, schoolsList);
            }
        }

        return filteredList;
    }

    private static ArrayList<PreSchool> searchByZipCode(String query, List<PreSchool> schoolsList) {
        ArrayList<PreSchool> filteredListZipcode = new ArrayList<>();
        for (int i = 0; i < schoolsList.size(); i++) {
            if (schoolsList.get(i).getZipCode().equals(query)) {
                filteredListZipcode.add(schoolsList.get(i));
            }
        }
        return filteredListZipcode;
    }

    private static ArrayList<PreSchool> searchByNeighborhood(String query, List<PreSchool> schoolsList) {
        ArrayList<PreSchool> filteredListHood = new ArrayList<>();
        String queryLowerCase = query.toLowerCase();
        for (int i = 0; i < schoolsList.size(); i++) {
            String regionLowerCase = schoolsList.get(i).getRegion().toLowerCase();
            if (regionLowerCase.contains(queryLowerCase)) {
                filteredListHood.add(schoolsList.get(i));
            }
        }
        return filteredListHood;
    }

    private static ArrayList<PreSchool> searchByPriceRange(String query, List<PreSchool> schoolsList) {
        ArrayList<PreSchool> filteredListPrice = new ArrayList<>();
        int min = 0;
        int max = 0;
        if (query.equals("$")) {
            min = 0;
            max = 1000;
        } else if (query.equals("$$")) {
            min = 1001;
            max = 2000;
        } else if (query.equals("$$$")) {
            min = 2001;
            max = 5000;
        }

        for (int i = 0; i < schoolsList.size(); i++) {
            if (schoolsList.get(i).getPrice() >= min && schoolsList.get(i).getPrice() <= max) {
                filteredListPrice.add(schoolsList.get(i));
            }
        }
        return filteredListPrice;
    }







//    // region SortMethods
//    private void sortByPrice() {
//        Collections.sort(mSchoolsList, new PriceComparator());
//        for (int i = 0; i < mSchoolsList.size(); i++) {
//            Log.i(TAG, "sortByPrice: " + mSchoolsList.get(i).getName() + "price: " + mSchoolsList.get(i).getPrice());
//        }
//        mRecycleAdapter.notifyDataSetChanged();
//        mRecyclerView.smoothScrollToPosition(0);
//    }
//
//    private void sortByRating() {
//        Collections.sort(mSchoolsList, new RatingComparator());
//        for (int i = 0; i < mSchoolsList.size(); i++) {
//            Log.i(TAG, "sortByrating: " + mSchoolsList.get(i).getName() + "rating: " + mSchoolsList.get(i).getRating());
//        }
//        mRecycleAdapter.notifyDataSetChanged();
//        mRecyclerView.smoothScrollToPosition(0);
//    }
//
//    private void sortByName() {
//        Collections.sort(mSchoolsList, new NameComparator());
//        mRecycleAdapter.notifyDataSetChanged();
//        mRecyclerView.smoothScrollToPosition(0);
//    }
//    // endregion SortMethods

}
