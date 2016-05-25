package com.project.salminnella.prescoop.model;

/**
 * Created by anthony on 5/25/16.
 */
public class SchoolLicense {
    private String licenseDate, licenseStatus;
    private int capacity, rating;
    private long facilityNumber;

    public SchoolLicense(String licenseDate, String licenseStatus, int capacity, int rating, long facilityNumber) {
        this.licenseDate = licenseDate;
        this.licenseStatus = licenseStatus;
        this.capacity = capacity;
        this.rating = rating;
        this.facilityNumber = facilityNumber;
    }

    public String getLicenseDate() {
        return licenseDate;
    }

    public void setLicenseDate(String licenseDate) {
        this.licenseDate = licenseDate;
    }

    public String getLicenseStatus() {
        return licenseStatus;
    }

    public void setLicenseStatus(String licenseStatus) {
        this.licenseStatus = licenseStatus;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public long getFacilityNumber() {
        return facilityNumber;
    }

    public void setFacilityNumber(long facilityNumber) {
        this.facilityNumber = facilityNumber;
    }
}
