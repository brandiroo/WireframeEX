package com.project.salminnella.prescoop.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * To help reduce the number of fields in the PreSchool constructor, this class models fields related
 * to the school reports.  It is a nested object within each school in Firebase.  
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Reports implements Serializable {
    private String mDate;
    private String mReportUrl;
    private String mTitle;

    // empty constructor for firebase
    public Reports() {
    }

    public Reports(String mDate, String mReportUrl, String mTitle) {
        this.mDate = mDate;
        this.mReportUrl = mReportUrl;
        this.mTitle = mTitle;
    }

    public String getmDate() {
        return mDate;
    }

    public String getmReportUrl() {
        return mReportUrl;
    }

    public String getmTitle() {
        return mTitle;
    }
}
