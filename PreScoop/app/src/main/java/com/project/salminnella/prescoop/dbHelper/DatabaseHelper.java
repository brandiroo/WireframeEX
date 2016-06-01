package com.project.salminnella.prescoop.dbHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.project.salminnella.prescoop.model.PreSchool;

/**
 *  The database Builder, and search helper. Creates The Prescoop Database
 *  and preschools table, and all its columns.
 *  Also performs the search queries and returns either a cursor or PreSchool object
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    // region Constants
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "PRESCOOP";
    private static final String SQL_CREAT_TABLE = "CREATE TABLE IF NOT EXISTS ";
    private static final String SQL_PRIMARY_KEY = " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, ";
    private static final String SQL_OPEN_PARANETHESIS = " (";
    private static final String SQL_TEXT_FIELD = " TEXT, ";
    private static final String SQL_TEXT_FIELD_CLOSE = " TEXT );";
    private static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS ";
    private static final String SQL_PLACEHOLDER_AND = " = ? AND ";
    private static final String SQL_EQUALS_ONE = "1";
    private static final String EQUALS_TRUE = " = 1";
    // endregion Constants
    // region Member Variables
    private Cursor cursor;
    private SQLiteDatabase dbWrite = getWritableDatabase();
    private SQLiteDatabase dbRead = getReadableDatabase();
    // endregion Member Variables

    // makes sure there is only one instance of the database
    // if there isn't one, make it, otherwise return the one instance
    private static DatabaseHelper instance;

    public static DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context);
        }
        return instance;
    }

    // database constructor
    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // table and columns
    public static final String PRESCHOOL_TABLE_NAME = "preschools";
    public static final String COL_ID = "_id";
    public static final String COL_NAME = "name";
    public static final String COL_STREET_ADDRESS = "streetAddress";
    public static final String COL_CITY = "city";
    public static final String COL_STATE = "state";
    public static final String COL_ZIPCODE = "zipcode";
    public static final String COL_REGION = "region";
    public static final String COL_PHONE_NUM = "phoneNum";
    public static final String COL_FACILITY_NUM = "facilityNum";
    public static final String COL_CAPACITY = "capacity";
    public static final String COL_PRICE = "price";
    public static final String COL_TYPE = "type";
    public static final String COL_WEBSITE_URL = "webUrl";
    public static final String COL_IMAGE_URL = "imageUrl";
    public static final String COL_RANGE = "range";
    public static final String COL_SCHOOL_DESCRIPTION = "description";
    public static final String COL_RATING = "rating";
    public static final String COL_FAVORITE = "favorite";
    public static final String COL_NUM_VISITS = "numVisits";
    public static final String COL_VISIT_DATES = "visitDates";
    public static final String COL_CITATION_TYPE_A = "citationTypeA";
    public static final String COL_CITATION_TYPE_B = "citationTypeB";
    public static final String COL_INSPECT_NUM = "inspectNum";
    public static final String COL_INSPECT_DATES = "inspectDates";
    public static final String COL_INSPECT_TYPE_A = "inspectTypeA";
    public static final String COL_INSPECT_TYPE_B = "inspectTypeB";
    public static final String COL_COMPLAINT_TOTAL = "complaintTotal";
    public static final String COL_COMPLAINT_ALLEG_SUB = "complaintAllegSug";
    public static final String COL_COMPLAINT_ALLEG_INCON = "complaintAllegIncon";
    public static final String COL_TOTAL_COMPLAINT_TYPE_A_CITATION = "complaintTypeACitation";
    public static final String COL_TOTAL_COMPLAINT_TYPE_B_CITATION = "complaintTypeBCitation";
    public static final String COL_TOTAL_COMPLAINT_VISITS = "complaintTotalVisits";
    public static final String COL_COMPLAINT_DETAILS = "complaintDetails";
    public static final String COL_OTHER_VISITS = "otherVisits";
    public static final String COL_OTHER_VISIT_DATES = "otherVisitDates";
    public static final String COL_VISIT_TYPE_A_CITATION = "visitTypeACitation";
    public static final String COL_VISIT_TYPE_B_CITATION = "visitTypeBCitation";
    public static final String COL_REPORTS_LIST = "reportsList";
    public static final String COL_TOTAL_REPORTS = "totalReports";
    public static final String COL_LATITUDE = "latitude";
    public static final String COL_LONGITUDE = "longitude";
    public static final String COL_LICENSE_STATUS = "licenseStatus";
    public static final String COL_LICENSE_DATE = "licenseDate";

    // builds all columns in one array
    private static final String[] COLUMNS = {COL_ID, COL_NAME, COL_STREET_ADDRESS, COL_CITY, COL_STATE, COL_ZIPCODE, COL_REGION,
            COL_PHONE_NUM, COL_FACILITY_NUM, COL_CAPACITY, COL_PRICE, COL_TYPE, COL_WEBSITE_URL, COL_IMAGE_URL, COL_RANGE,
            COL_SCHOOL_DESCRIPTION, COL_RATING, COL_FAVORITE, COL_NUM_VISITS, COL_VISIT_DATES, COL_CITATION_TYPE_A,
            COL_CITATION_TYPE_B, COL_INSPECT_NUM, COL_INSPECT_DATES, COL_INSPECT_TYPE_A, COL_INSPECT_TYPE_B,
            COL_COMPLAINT_TOTAL, COL_COMPLAINT_ALLEG_SUB, COL_COMPLAINT_ALLEG_INCON, COL_TOTAL_COMPLAINT_TYPE_A_CITATION,
            COL_TOTAL_COMPLAINT_TYPE_B_CITATION, COL_TOTAL_COMPLAINT_VISITS, COL_COMPLAINT_DETAILS, COL_OTHER_VISITS,
            COL_OTHER_VISIT_DATES, COL_VISIT_TYPE_A_CITATION, COL_VISIT_TYPE_B_CITATION, COL_REPORTS_LIST, COL_TOTAL_REPORTS,
            COL_LATITUDE, COL_LONGITUDE, COL_LICENSE_STATUS, COL_LICENSE_DATE};

    // the actual sql statement to create the table
    private static final String CREATE_PRESCHOOLS_TABLE = SQL_CREAT_TABLE + PRESCHOOL_TABLE_NAME +
            SQL_OPEN_PARANETHESIS +
            COL_ID + SQL_PRIMARY_KEY +
            COL_NAME + SQL_TEXT_FIELD +
            COL_STREET_ADDRESS + SQL_TEXT_FIELD +
            COL_CITY + SQL_TEXT_FIELD +
            COL_STATE + SQL_TEXT_FIELD +
            COL_ZIPCODE + SQL_TEXT_FIELD +
            COL_REGION + SQL_TEXT_FIELD +
            COL_PHONE_NUM + SQL_TEXT_FIELD +
            COL_FACILITY_NUM + SQL_TEXT_FIELD +
            COL_CAPACITY + SQL_TEXT_FIELD +
            COL_PRICE + SQL_TEXT_FIELD +
            COL_TYPE + SQL_TEXT_FIELD +
            COL_WEBSITE_URL + SQL_TEXT_FIELD +
            COL_IMAGE_URL + SQL_TEXT_FIELD +
            COL_RANGE + SQL_TEXT_FIELD +
            COL_SCHOOL_DESCRIPTION + SQL_TEXT_FIELD +
            COL_RATING + SQL_TEXT_FIELD +
            COL_FAVORITE + SQL_TEXT_FIELD +
            COL_NUM_VISITS + SQL_TEXT_FIELD +
            COL_VISIT_DATES + SQL_TEXT_FIELD +
            COL_CITATION_TYPE_A + SQL_TEXT_FIELD +
            COL_CITATION_TYPE_B + SQL_TEXT_FIELD +
            COL_INSPECT_NUM + SQL_TEXT_FIELD +
            COL_INSPECT_DATES + SQL_TEXT_FIELD +
            COL_INSPECT_TYPE_A + SQL_TEXT_FIELD +
            COL_INSPECT_TYPE_B + SQL_TEXT_FIELD +
            COL_COMPLAINT_TOTAL + SQL_TEXT_FIELD +
            COL_COMPLAINT_ALLEG_SUB + SQL_TEXT_FIELD +
            COL_COMPLAINT_ALLEG_INCON + SQL_TEXT_FIELD +
            COL_TOTAL_COMPLAINT_TYPE_A_CITATION + SQL_TEXT_FIELD +
            COL_TOTAL_COMPLAINT_TYPE_B_CITATION + SQL_TEXT_FIELD +
            COL_TOTAL_COMPLAINT_VISITS + SQL_TEXT_FIELD +
            COL_COMPLAINT_DETAILS + SQL_TEXT_FIELD +
            COL_OTHER_VISITS + SQL_TEXT_FIELD +
            COL_OTHER_VISIT_DATES + SQL_TEXT_FIELD +
            COL_VISIT_TYPE_A_CITATION + SQL_TEXT_FIELD +
            COL_VISIT_TYPE_B_CITATION + SQL_TEXT_FIELD +
            COL_REPORTS_LIST + SQL_TEXT_FIELD +
            COL_TOTAL_REPORTS + SQL_TEXT_FIELD +
            COL_LATITUDE + SQL_TEXT_FIELD +
            COL_LONGITUDE + SQL_TEXT_FIELD +
            COL_LICENSE_STATUS + SQL_TEXT_FIELD +
            COL_LICENSE_DATE + SQL_TEXT_FIELD_CLOSE;


    /**
     * Create Database
     * @param db SQLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PRESCHOOLS_TABLE);
    }

    /**
     * If database version is newer, drop table and create a new one.
     * @param db SQLiteDatabase
     * @param oldVersion int
     * @param newVersion int
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DROP_TABLE + PRESCHOOL_TABLE_NAME);
        this.onCreate(db);
    }

    /**
     * Saves the PreSchool object to the database.  Stores a 1 in the Favorites column, and a GSON
     * generated string to represent an array list.
     * @param school PreSchool
     * @param reportsList String
     */
    public void insertSavedSchool(PreSchool school, String reportsList) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_NAME, school.getName());
        values.put(COL_STREET_ADDRESS, school.getStreetAddress());
        values.put(COL_CITY, school.getCity());
        values.put(COL_STATE, school.getState());
        values.put(COL_ZIPCODE, school.getZipCode());
        values.put(COL_REGION, school.getRegion());
        values.put(COL_PHONE_NUM, school.getPhoneNumber());
        values.put(COL_FACILITY_NUM, school.getFacilityNumber());
        values.put(COL_CAPACITY, school.getCapacity());
        values.put(COL_PRICE, school.getPrice());
        values.put(COL_TYPE, school.getType());
        values.put(COL_WEBSITE_URL, school.getWebsiteUrl());
        values.put(COL_IMAGE_URL, school.getImageUrl());
        values.put(COL_RANGE, school.getRange());
        values.put(COL_SCHOOL_DESCRIPTION, school.getSchoolDescription());
        values.put(COL_RATING, school.getRating());
        values.put(COL_FAVORITE, SQL_EQUALS_ONE);
        values.put(COL_NUM_VISITS, school.getNumVisits());
        values.put(COL_VISIT_DATES, school.getVisitDates());
        values.put(COL_CITATION_TYPE_A, school.getCitationTypeA());
        values.put(COL_CITATION_TYPE_B, school.getCitationTypeB());
        values.put(COL_INSPECT_NUM, school.getInspectionNum());
        values.put(COL_INSPECT_DATES, school.getInspectionDates());
        values.put(COL_INSPECT_TYPE_A, school.getInspectionTypeA());
        values.put(COL_INSPECT_TYPE_B, school.getInspectionTypeB());
        values.put(COL_COMPLAINT_TOTAL, school.getComplaintTotal());
        values.put(COL_COMPLAINT_ALLEG_SUB, school.getTotalComplaintAllegSub());
        values.put(COL_COMPLAINT_ALLEG_INCON, school.getTotalComplaintAllegIncon());
        values.put(COL_TOTAL_COMPLAINT_TYPE_A_CITATION, school.getTotalComplaintTypeACitation());
        values.put(COL_TOTAL_COMPLAINT_TYPE_B_CITATION, school.getTotalComplaintTypeBCitation());
        values.put(COL_TOTAL_COMPLAINT_VISITS, school.getTotalComplaintVisits());
        values.put(COL_COMPLAINT_DETAILS, school.getComplaintDetails());
        values.put(COL_OTHER_VISITS, school.getOtherVisits());
        values.put(COL_OTHER_VISIT_DATES, school.getOtherVisitDates());
        values.put(COL_VISIT_TYPE_A_CITATION, school.getVisitTypeACitation());
        values.put(COL_VISIT_TYPE_B_CITATION, school.getVisitTypeBCitation());
        values.put(COL_REPORTS_LIST, reportsList);
        values.put(COL_TOTAL_REPORTS, school.getTotalReports());
        values.put(COL_LATITUDE, school.getLatitude());
        values.put(COL_LONGITUDE, school.getLongitude());
        values.put(COL_LICENSE_STATUS, school.getLicenseStatus());
        values.put(COL_LICENSE_DATE, school.getLicenseDate());

        db.insert(PRESCHOOL_TABLE_NAME, null, values);
    }

    /**
     * Performs a find on the school name, and the favorites column for a value of 1.
     * Used to check if a school has already been saved as a favorite.
     * @param schoolName String
     * @return Cursor
     */
    public Cursor findSavedSchool(String schoolName) {
        cursor = dbRead.query(PRESCHOOL_TABLE_NAME, COLUMNS,
                COL_NAME + SQL_PLACEHOLDER_AND + COL_FAVORITE + EQUALS_TRUE,
                new String[]{schoolName},
                null,
                null,
                null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
        }

        return cursor;
    }

    /**
     * Deletes the school from the database, that matches the schoolName and favorites column
     * @param schoolName String
     */
    public void deleteSavedSchool(String schoolName) {
        dbWrite.delete(PRESCHOOL_TABLE_NAME,
                COL_NAME + SQL_PLACEHOLDER_AND + COL_FAVORITE + EQUALS_TRUE,
                new String[]{schoolName});
    }

    /**
     * Retrieves all saved schools from the database. Performs a find on the school name, and the
     * favorites column of a value of 1.
     * @return Cursor
     */
    public Cursor findAllSavedSchools() {
        cursor = dbRead.query(PRESCHOOL_TABLE_NAME, COLUMNS,
                COL_FAVORITE + EQUALS_TRUE,
                null,
                null,
                null,
                null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
        }

        return cursor;
    }
}
