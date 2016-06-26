package com.project.salminnella.prescoop.utility;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.project.salminnella.prescoop.R;
import com.project.salminnella.prescoop.adapter.ListAdapter;
import com.project.salminnella.prescoop.dbHelper.DatabaseHelper;
import com.project.salminnella.prescoop.model.PreSchool;
import com.project.salminnella.prescoop.model.Reports;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Holds common methods that are used through out the application.
 */
public final class Utilities {

    /**
     * Creates a string for the complete address of the school.
     * @param streetAddress String
     * @param city String
     * @param state String
     * @param zipcode String
     * @return String format of the complete school address
     */
    public static String buildAddressString(String streetAddress, String city, String state, String zipcode) {
        return streetAddress + ", " + city + ", " + state + " " + zipcode;
    }

    /**
     * This first checks the first character of the search query.  Depending on that character,
     * a different search filter will occur
     * @param query String
     * @param schoolsList List of PreSchool objects
     * @return ArrayList of PreSchool objects
     */
    public static ArrayList<PreSchool> filterSchoolsList(String query, List<PreSchool> schoolsList) {
        ArrayList<PreSchool> filteredList = new ArrayList<>();
        char firstChar = query.charAt(0);

        if (query.equals("")) {
            return filteredList;
        }

        if (Character.isLetter(firstChar)) {
            filteredList = searchByDistrict(query, schoolsList);
        } else if (firstChar >= '0' && firstChar <= '9') {
            filteredList = searchByZipCode(query, schoolsList);
        } else {
            filteredList = searchByPriceRange(query, schoolsList);
        }
//
//        if (firstChar >= '0' && firstChar <= '9') {
//            filteredList = searchByZipCode(query, schoolsList);
//        } else if (Character.isLetter(firstChar)) {
//            filteredList = searchByDistrict(query, schoolsList);
//        } else {
//            filteredList = searchByPriceRange(query, schoolsList);
//        }


        if (filteredList.size() == 0) {
            filteredList = searchByName(query, schoolsList);
        }

        return filteredList;
    }

    /**
     * Filters the array list of PreSchool objects by zipcode.
     * @param query String
     * @param schoolsList List of PreSchool objects
     * @return ArrayList of PreSchool objects
     */
    private static ArrayList<PreSchool> searchByZipCode(String query, List<PreSchool> schoolsList) {
        ArrayList<PreSchool> filteredListZipcode = new ArrayList<>();
        for (PreSchool school : schoolsList) {
            if (school.getZipCode().equals(query)) {
                filteredListZipcode.add(school);
            }
        }

        return filteredListZipcode;
    }

    /**
     * Filters the array list of PreSchool objects by neighborhood.
     * @param query String
     * @param schoolsList List of PreSchool objects
     * @return ArrayList of PreSchool objects
     */
    private static ArrayList<PreSchool> searchByDistrict(String query, List<PreSchool> schoolsList) {
        ArrayList<PreSchool> filteredListHood = new ArrayList<>();
        String queryLowerCase = query.toLowerCase();
        for (PreSchool school : schoolsList) {
            String regionLowerCase = school.getRegion().toLowerCase();
            if (regionLowerCase.contains(queryLowerCase)) {
                filteredListHood.add(school);
            }
        }
        return filteredListHood;
    }

    private static ArrayList<PreSchool> searchByName(String query, List<PreSchool> schoolsList) {
        ArrayList<PreSchool> filteredListName = new ArrayList<>();
        String queryLowerCase = query.toLowerCase();
        for (PreSchool school : schoolsList) {
            String nameLowerCase = school.getName().toLowerCase();
            if (nameLowerCase.contains(queryLowerCase)){
                filteredListName.add(school);
            }
        }

        return filteredListName;
    }

    /**
     * Filters the array list of PreSchool objects by price range.
     * @param query String
     * @param schoolsList List of PreSchool objects
     * @return ArrayList of PreSchool objects
     */
    private static ArrayList<PreSchool> searchByPriceRange(String query, List<PreSchool> schoolsList) {
        ArrayList<PreSchool> filteredListPrice = new ArrayList<>();
        int min = 0;
        int max = 0;
        switch (query) {
            case "$":
                min = 0;
                max = 1000;
                break;
            case "$$":
                min = 1001;
                max = 2000;
                break;
            case "$$$":
                min = 2001;
                max = 5000;
                break;
        }

        for (PreSchool school : schoolsList) {
            if (school.getPrice() >= min && school.getPrice() <= max) {
                filteredListPrice.add(school);
            }
        }
        return filteredListPrice;
    }


    public static void sortByPrice(List<PreSchool> schoolsList, ListAdapter recyclerAdapter,
                                   RecyclerView recyclerView, Context context) {
        Collections.sort(schoolsList, new PriceComparator());
        recyclerAdapter.notifyDataSetChanged();
        recyclerView.smoothScrollToPosition(0);
        Toast.makeText(context, R.string.price_sort, Toast.LENGTH_SHORT).show();
    }


    public static void sortByRating(List<PreSchool> schoolsList, ListAdapter recyclerAdapter,
                              RecyclerView recyclerView, Context context) {
        Collections.sort(schoolsList, new RatingComparator());
        recyclerAdapter.notifyDataSetChanged();
        recyclerView.smoothScrollToPosition(0);
        Toast.makeText(context, R.string.rating_sort, Toast.LENGTH_SHORT).show();
    }

    public static void sortByName(List<PreSchool> schoolsList, ListAdapter recyclerAdapter,
                            RecyclerView recyclerView, Context context) {
        Collections.sort(schoolsList, new NameComparator());
        recyclerAdapter.notifyDataSetChanged();
        recyclerView.smoothScrollToPosition(0);
        Toast.makeText(context, R.string.abc_sort, Toast.LENGTH_SHORT).show();
    }

    /**
     * From a list item click in the Recycler View, this builds the preschool object
     * from cursor data, to send to SchoolDetailsActivity
     * @param cursor Cursor
     * @return PreSchool object
     */
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
        preschool.setLatitude(cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.COL_LATITUDE)));
        preschool.setLongitude(cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.COL_LONGITUDE)));
        preschool.setLicenseStatus(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_LICENSE_STATUS)));
        preschool.setLicenseDate(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_LICENSE_DATE)));

        // get gson object from string in the database
        String outputString = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_REPORTS_LIST));
        Reports[] reportsList = getReportsArrayList(outputString);

        preschool.setReports(reportsList);

        return preschool;
    }

    /**
     * Receives the String element from the database, and converts it to array using Gson
     * @param outputList String
     * @return Reports array
     */
    private static Reports[] getReportsArrayList(String outputList) {
        Gson gson = new Gson();
        Type type = new TypeToken<Reports[]>() {}.getType();

        return gson.fromJson(outputList, type);
    }

    /**
     * Retrieves the matching image for the school rating on a range of 1-5
     * @param rating int
     * @return int -- drawable image id
     */
    public static int getRatingImage(int rating){
        switch(rating){
            case 1:
                return R.drawable.one_star;
            case 2:
                return R.drawable.two_stars;
            case 3:
                return R.drawable.three_stars;
            case 4:
                return R.drawable.four_stars;
            case 5:
                return R.drawable.five_stars;
            default:
                return 0;
        }
    }
}
