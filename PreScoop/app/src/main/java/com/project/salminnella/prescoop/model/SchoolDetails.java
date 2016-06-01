package com.project.salminnella.prescoop.model;

/**
 * To help reduce the number of fields in the PreSchool constructor, this class models fields related
 * to the school's website, latitude longitude, phone, monthly price and school type.
 */
public class SchoolDetails {

    private String phoneNumber, type, websiteUrl, imageUrl;
    private int price;
    private double pLongitude, pLatitude;

    public SchoolDetails(String phoneNumber, String type, String websiteUrl, String imageUrl,
                         int price, double pLongitude, double pLatitude) {
        this.phoneNumber = phoneNumber;
        this.type = type;
        this.websiteUrl = websiteUrl;
        this.imageUrl = imageUrl;
        this.price = price;
        this.pLongitude = pLongitude;
        this.pLatitude = pLatitude;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getType() {
        return type;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getPrice() {
        return price;
    }

    public double getpLongitude() {
        return pLongitude;
    }

    public double getpLatitude() {
        return pLatitude;
    }
}
