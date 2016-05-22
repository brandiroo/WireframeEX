package com.project.salminnella.prescoop.utility;

import android.content.Context;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.project.salminnella.prescoop.dbHelper.DatabaseHelper;
import com.project.salminnella.prescoop.model.PreSchool;
import com.project.salminnella.prescoop.model.Reports;

import java.lang.reflect.Type;
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

    public static PreSchool buildPreschoolObject(Cursor cursor) {
        PreSchool preschool = new PreSchool();
        preschool.setName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_NAME)));
        preschool.setStreetAddress(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_STREET_ADDRESS)));
        preschool.setCity(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_CITY)));
        preschool.setState(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_STATE)));
        preschool.setZipCode(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_ZIPCODE)));
        preschool.setRegion(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_REGION)));
        preschool.setPhoneNumber(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_PHONE_NUM)));
        preschool.setFacilityNumber(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COL_FACILITY_NUM)));
        preschool.setCapacity(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COL_CAPACITY)));
        preschool.setPrice(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COL_PRICE)));
        preschool.setType(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TYPE)));
        preschool.setWebsiteUrl(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_WEBSITE_URL)));
        preschool.setImageUrl(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_IMAGE_URL)));
        preschool.setRange(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COL_RANGE)));
        preschool.setSchoolDescription(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_SCHOOL_DESCRIPTION)));
        preschool.setRating(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COL_RATING)));
        //preschool.setName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_FAVORITE)));
        preschool.setNumVisits(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COL_NUM_VISITS)));
        preschool.setVisitDates(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_VISIT_DATES)));
        preschool.setCitationTypeA(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COL_CITATION_TYPE_A)));
        preschool.setCitationTypeB(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COL_CITATION_TYPE_B)));
        preschool.setInspectionNum(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COL_INSPECT_NUM)));
        preschool.setInspectionDates(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_INSPECT_DATES)));
        preschool.setInspectionTypeA(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COL_INSPECT_TYPE_A)));
        preschool.setInspectionTypeB(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COL_INSPECT_TYPE_B)));
        preschool.setComplaintTotal(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COL_COMPLAINT_TOTAL)));
        preschool.setTotalComplaintAllegSub(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COL_COMPLAINT_ALLEG_SUB)));
        preschool.setTotalComplaintAllegIncon(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COL_COMPLAINT_ALLEG_INCON)));
        preschool.setTotalComplaintTypeACitation(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COL_TOTAL_COMPLAINT_TYPE_A_CITATION)));
        preschool.setTotalComplaintTypeBCitation(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COL_TOTAL_COMPLAINT_TYPE_B_CITATION)));
        preschool.setTotalComplaintVisits(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COL_TOTAL_COMPLAINT_VISITS)));
        preschool.setComplaintDetails(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_COMPLAINT_DETAILS)));
        preschool.setOtherVisits(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COL_OTHER_VISITS)));
        preschool.setOtherVisitDates(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_OTHER_VISIT_DATES)));
        preschool.setVisitTypeACitation(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COL_VISIT_TYPE_A_CITATION)));
        preschool.setVisitTypeBCitation(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COL_VISIT_TYPE_B_CITATION)));
        preschool.setTotalReports(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COL_TOTAL_REPORTS)));
        preschool.setReportDates(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_REPORT_DATES)));
        preschool.setReportUrl(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_REPORT_URL)));
        preschool.setLatitude(cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.COL_LATITUDE)));
        preschool.setLongitude(cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.COL_LONGITUDE)));
        preschool.setLicenseStatus(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_LICENSE_STATUS)));
        preschool.setLicenseDate(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_LICENSE_DATE)));

        // get json object from string in the database
        String outputString = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_REPORTS_LIST));
        Reports[] reportsList = getReportsArrayList(outputString);

        preschool.setReports(reportsList);

        return preschool;
    }


    private static Reports[] getReportsArrayList(String outputList) {
        Gson gson = new Gson();
        Type type = new TypeToken<Reports[]>() {}.getType();

        Reports[] finalOutputList = gson.fromJson(outputList, type);

        return finalOutputList;
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
