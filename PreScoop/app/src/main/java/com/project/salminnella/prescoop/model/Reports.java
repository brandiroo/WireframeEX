package com.project.salminnella.prescoop.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by anthony on 5/17/16.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Reports implements Serializable {
    private String mDate;
    private String mReportUrl;
    private String mTitle;

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

}
