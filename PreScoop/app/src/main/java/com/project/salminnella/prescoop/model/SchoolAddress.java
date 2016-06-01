package com.project.salminnella.prescoop.model;

/**
 * To help reduce the number of fields in the PreSchool constructor, this class models fields related
 * to the school address.
 */
public class SchoolAddress {
    String name, streetAddress, city, state, zipCode, region;

    public SchoolAddress(String name, String streetAddress, String city, String state, String zipCode, String region) {
        this.name = name;
        this.streetAddress = streetAddress;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.region = region;
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

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
