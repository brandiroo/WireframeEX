package com.project.salminnella.prescoop.utility;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by anthony on 5/7/16.
 * Holds methods that are used through out the application
 */
public class Utilities {

    /**
     * takes an address String, and returns a Latitude / Longitude that the Maps fragment
     * will use to add markers with
     * @param context Context
     * @param strAddress String
     * @return LatLng
     */
    public static LatLng getLocationFromAddress(Context context, String strAddress)
    {
        Geocoder coder= new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try
        {
            address = coder.getFromLocationName(strAddress, 5);
            if(address==null)
            {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return p1;
    }


    public static String buildAddressString(String streetAddress, String city, String state, String zipcode) {
        String strAddress = streetAddress + ", " + city + ", " + state + " " + zipcode;
        return strAddress;
    }


}
