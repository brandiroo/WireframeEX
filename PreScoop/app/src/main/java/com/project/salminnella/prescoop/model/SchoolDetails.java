package com.project.salminnella.prescoop.model;

/**
 * Created by anthony on 5/25/16.
 */
public class SchoolDetails {

    private String phoneNumber, type, websiteUrl, imageUrl;
    private int price;
    private double pLongitude, pLatitude;

    public SchoolDetails(String phoneNumber, String type, String websiteUrl, String imageUrl, int price, double pLongitude, double pLatitude) {
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

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public double getpLongitude() {
        return pLongitude;
    }

    public void setpLongitude(double pLongitude) {
        this.pLongitude = pLongitude;
    }

    public double getpLatitude() {
        return pLatitude;
    }

    public void setpLatitude(double pLatitude) {
        this.pLatitude = pLatitude;
    }
}
