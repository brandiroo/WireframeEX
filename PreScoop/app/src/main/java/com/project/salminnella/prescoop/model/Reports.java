package com.project.salminnella.prescoop.model;

/**
 * Created by anthony on 5/17/16.
 */
public class Reports {
    private String mDate;
    private String mReportUrl;
    private String mTitle;

//    private SchoolsKey schools;
//
//    public Reports(String mDate, String mReportUrl, String mTitle, SchoolsKey schoolsKey) {
//        this.mDate = mDate;
//        this.mReportUrl = mReportUrl;
//        this.mTitle = mTitle;
//        this.schools = schoolsKey;
//    }

    public Reports(String mDate, String mReportUrl, String mTitle) {
        this.mDate = mDate;
        this.mReportUrl = mReportUrl;
        this.mTitle = mTitle;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public String getmReportUrl() {
        return mReportUrl;
    }

    public void setmReportUrl(String mReportUrl) {
        this.mReportUrl = mReportUrl;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

//    public SchoolsKey getSchools() {
//        return schools;
//    }
//
//    public void setSchools(SchoolsKey schools) {
//        this.schools = schools;
//    }
}
