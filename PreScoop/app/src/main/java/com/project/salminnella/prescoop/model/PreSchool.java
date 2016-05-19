package com.project.salminnella.prescoop.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by anthony on 5/5/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PreSchool implements Serializable {

    private String name;
    private String streetAddress;
    private String city;
    private String state;
    private String zipCode;
    private String region;
    private String phoneNumber;
    private long facilityNumber;
    private int capacity;
    private int price;
    private String type;
    private String websiteUrl;
    private String imageUrl;

    private int range;
    private String schoolDescription;
    private int rating;
    private int favorite;

    private int[] images;
    private String[] imageDescription;

    private int numVisits;
    private String visitDates;
    private int citationTypeA;
    private int citationTypeB;

    private int inspectionNum;
    private String inspectionDates;
    private int inspectionTypeA;
    private int inspectionTypeB;

    private int complaintTotal;
    private int totalComplaintAllegSub;
    private int totalComplaintAllegIncon;
    private int totalComplaintTypeACitation;
    private int totalComplaintTypeBCitation;
    private int totalComplaintVisits;
    private String complaintDetails;

    private int otherVisits;
    private String otherVisitDates;
    private int visitTypeACitation;
    private int visitTypeBCitation;

    private int totalReports;
    private String reportDates;
    private String reportUrl;

    private double latitude, longitude;

    private String licenseStatus;
    private String licenseDate;
    private Reports reports;

    public PreSchool() {
    }

    public PreSchool(String name, String address, String phoneNumber, String zipcode) {
        this.name = name;
        this.streetAddress = address;
        this.phoneNumber = phoneNumber;
        this.zipCode = zipcode;
    }

    public PreSchool(SchoolLocation schoolLocation, String phoneNumber, long facilityNumber, int capacity, int price, String type, String websiteUrl,
                     String imageUrl, int rating, AllVisits allVisits, int citationTypeA, int citationTypeB,
                     int inspectionNum, String inspectionDates, int inspectionTypeA, int inspectionTypeB, int complaintTotal,
                     int totalComplaintAllegSub, int totalComplaintAllegIncon, int totalComplaintTypeACitation,
                     int totalComplaintTypeBCitation, int totalComplaintVisits, String complaintDetails, int otherVisits,
                     String otherVisitDates, int visitTypeACitation, int visitTypeBCitation, int totalReports,
                     String reportDates, String reportUrl, double pLongitude, double pLatitude, String licenseDate,
                     String licenseStatus, Reports reports) {
        this.name = schoolLocation.getName();
        this.streetAddress = schoolLocation.getStreetAddress();
        this.city = schoolLocation.getCity();
        this.state = schoolLocation.getState();
        this.zipCode = schoolLocation.getZipCode();
        this.region = schoolLocation.getRegion();
        this.phoneNumber = phoneNumber;
        this.facilityNumber = facilityNumber;
        this.capacity = capacity;
        this.price = price;
        this.type = type;
        this.websiteUrl = websiteUrl;
        this.imageUrl = imageUrl;
        this.rating = rating;
        this.numVisits = allVisits.getNumVisits();
        this.visitDates = allVisits.getVisitDates();
        this.citationTypeA = citationTypeA;
        this.citationTypeB = citationTypeB;
        this.inspectionNum = inspectionNum;
        this.inspectionDates = inspectionDates;
        this.inspectionTypeA = inspectionTypeA;
        this.inspectionTypeB = inspectionTypeB;
        this.complaintTotal = complaintTotal;
        this.totalComplaintAllegSub = totalComplaintAllegSub;
        this.totalComplaintAllegIncon = totalComplaintAllegIncon;
        this.totalComplaintTypeACitation = totalComplaintTypeACitation;
        this.totalComplaintTypeBCitation = totalComplaintTypeBCitation;
        this.totalComplaintVisits = totalComplaintVisits;
        this.complaintDetails = complaintDetails;
        this.otherVisits = otherVisits;
        this.otherVisitDates = otherVisitDates;
        this.visitTypeACitation = visitTypeACitation;
        this.visitTypeBCitation = visitTypeBCitation;
        this.totalReports = totalReports;
        this.reportDates = reportDates;
        this.reportUrl = reportUrl;
        this.latitude = pLatitude;
        this.longitude = pLongitude;
        this.licenseDate = licenseDate;
        this.licenseStatus = licenseStatus;
        this.reports = reports;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public long getFacilityNumber() {
        return facilityNumber;
    }

    public void setFacilityNumber(long facilityNumber) {
        this.facilityNumber = facilityNumber;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public String getSchoolDescription() {
        return schoolDescription;
    }

    public void setSchoolDescription(String schoolDescription) {
        this.schoolDescription = schoolDescription;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }

    public int[] getImages() {
        return images;
    }

    public void setImages(int[] images) {
        this.images = images;
    }

    public String[] getImageDescription() {
        return imageDescription;
    }

    public void setImageDescription(String[] imageDescription) {
        this.imageDescription = imageDescription;
    }

    public int getNumVisits() {
        return numVisits;
    }

    public void setNumVisits(int numVisits) {
        this.numVisits = numVisits;
    }

    public String getVisitDates() {
        return visitDates;
    }

    public void setVisitDates(String visitDates) {
        this.visitDates = visitDates;
    }

    public int getCitationTypeA() {
        return citationTypeA;
    }

    public void setCitationTypeA(int citationTypeA) {
        this.citationTypeA = citationTypeA;
    }

    public int getCitationTypeB() {
        return citationTypeB;
    }

    public void setCitationTypeB(int citationTypeB) {
        this.citationTypeB = citationTypeB;
    }

    public int getInspectionNum() {
        return inspectionNum;
    }

    public void setInspectionNum(int inspectionNum) {
        this.inspectionNum = inspectionNum;
    }

    public String getInspectionDates() {
        return inspectionDates;
    }

    public void setInspectionDates(String inspectionDates) {
        this.inspectionDates = inspectionDates;
    }

    public int getInspectionTypeA() {
        return inspectionTypeA;
    }

    public void setInspectionTypeA(int inspectionTypeA) {
        this.inspectionTypeA = inspectionTypeA;
    }

    public int getInspectionTypeB() {
        return inspectionTypeB;
    }

    public void setInspectionTypeB(int inspectionTypeB) {
        this.inspectionTypeB = inspectionTypeB;
    }

    public int getComplaintTotal() {
        return complaintTotal;
    }

    public void setComplaintTotal(int complaintTotal) {
        this.complaintTotal = complaintTotal;
    }

    public int getTotalComplaintAllegSub() {
        return totalComplaintAllegSub;
    }

    public void setTotalComplaintAllegSub(int totalComplaintAllegSub) {
        this.totalComplaintAllegSub = totalComplaintAllegSub;
    }

    public int getTotalComplaintAllegIncon() {
        return totalComplaintAllegIncon;
    }

    public void setTotalComplaintAllegIncon(int totalComplaintAllegIncon) {
        this.totalComplaintAllegIncon = totalComplaintAllegIncon;
    }

    public int getTotalComplaintTypeACitation() {
        return totalComplaintTypeACitation;
    }

    public void setTotalComplaintTypeACitation(int totalComplaintTypeACitation) {
        this.totalComplaintTypeACitation = totalComplaintTypeACitation;
    }

    public int getTotalComplaintTypeBCitation() {
        return totalComplaintTypeBCitation;
    }

    public void setTotalComplaintTypeBCitation(int totalComplaintTypeBCitation) {
        this.totalComplaintTypeBCitation = totalComplaintTypeBCitation;
    }

    public int getTotalComplaintVisits() {
        return totalComplaintVisits;
    }

    public void setTotalComplaintVisits(int totalComplaintVisits) {
        this.totalComplaintVisits = totalComplaintVisits;
    }

    public String getComplaintDetails() {
        return complaintDetails;
    }

    public void setComplaintDetails(String complaintDetails) {
        this.complaintDetails = complaintDetails;
    }

    public int getOtherVisits() {
        return otherVisits;
    }

    public void setOtherVisits(int otherVisits) {
        this.otherVisits = otherVisits;
    }

    public String getOtherVisitDates() {
        return otherVisitDates;
    }

    public void setOtherVisitDates(String otherVisitDates) {
        this.otherVisitDates = otherVisitDates;
    }

    public int getVisitTypeACitation() {
        return visitTypeACitation;
    }

    public void setVisitTypeACitation(int visitTypeACitation) {
        this.visitTypeACitation = visitTypeACitation;
    }

    public int getVisitTypeBCitation() {
        return visitTypeBCitation;
    }

    public void setVisitTypeBCitation(int visitTypeBCitation) {
        this.visitTypeBCitation = visitTypeBCitation;
    }

    public int getTotalReports() {
        return totalReports;
    }

    public void setTotalReports(int totalReports) {
        this.totalReports = totalReports;
    }

    public String getReportDates() {
        return reportDates;
    }

    public void setReportDates(String reportDates) {
        this.reportDates = reportDates;
    }

    public String getReportUrl() {
        return reportUrl;
    }

    public void setReportUrl(String reportUrl) {
        this.reportUrl = reportUrl;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getLicenseStatus() {
        return licenseStatus;
    }

    public void setLicenseStatus(String licenseStatus) {
        this.licenseStatus = licenseStatus;
    }

    public String getLicenseDate() {
        return licenseDate;
    }

    public void setLicenseDate(String licenseDate) {
        this.licenseDate = licenseDate;
    }

    public Reports getReports() {
        return reports;
    }

    public void setReports(Reports reports) {
        this.reports = reports;
    }
}
