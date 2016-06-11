package com.project.salminnella.prescoop.utility;

import android.Manifest;

/**
 * Home for all application constants. The database helper class however, has its own constants
 */
public final class Constants {

    // region Application Constants
    public static final String ADDRESS_LIST_KEY = "addressList";
    public static final String SCHOOL_OBJECT_KEY = "schoolTitle";
    public static final String FIREBASE_ROOT_URL = "https://prescoop.firebaseio.com/";
    public static final String FIREBASE_ROOT_CHILD = "Facility";
    public static final String ORDER_BY_NAME = "name";
    public static final String SCHOOLS_LIST_KEY = "schoolsList";
    public static final String SCHOOL_MARKER_KEY = "schoolMarker";
    public static final String SCHOOL_REPORT_TITLE = "School Report";
    public static final String WEB_URL_KEY = "webUrl";
    public static final String ARG_PAGE = "ARG_PAGE";
    public static final String LOCATION_PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final int PERMISSION_REQUEST_CODE = 12345;
    public static final int WEB_REQUEST_CODE = 49;
    // endregion Application Constants

    // region Yelp Constants
//    public static final String YELP_CONSUMER_KEY = "qjt368tSTOpOje3NIbdfyg";
//    public static final String YELP_CONSUMER_SECRET = "XSD1VcfmQLSH5ZM_a3mzvRDr49E";
//    public static final String YELP_TOKEN = "imPIqEPxV0hayvDlVLJzEZw2pYHbY6GY";
//    public static final String YELP_TOKEN_SECRET = "vduvdOiuqUVxce3NIBpU8DZHDN8";

    public static final String YELP_SEARCH_PARAM_TERMS = "term";
    public static final String YELP_SEARCH_PARAM_LIMIT = "limit";
    public static final String YELP_SEARCH_PARAM_CATEGORY = "category_filter";
    public static final String YELP_SEARCH_PARAM_SORT = "sort";
    public static final String YELP_SEARCH_PARAM_LOCATION = "San Francisco";
    public static final String YELP_CATEGORY = "preschools";
    public static final String YELP_SORT = "0";
    public static final String YELP_RESPONSE_LIMIT_STRING = "19";
    public static final String YELP_WEBVIEW_TITLE = "Yelp Reviews";
    public static final String YELP_WEBVIEW_TITLE_KEY = "yelpReviews";
    public static final int YELP_RESPONSE_LIMIT_INT = 19;
    // endregion Yelp Constants
}
