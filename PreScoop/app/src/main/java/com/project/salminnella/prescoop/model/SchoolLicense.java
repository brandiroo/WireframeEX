package com.project.salminnella.prescoop.model;

/**
 * To help reduce the number of fields in the PreSchool constructor, this class models fields related
 * to the school licensing information.
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
    
    public String getLicenseStatus() {
        return licenseStatus;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getRating() {
        return rating;
    }

    public long getFacilityNumber() {
        return facilityNumber;
    }
}
