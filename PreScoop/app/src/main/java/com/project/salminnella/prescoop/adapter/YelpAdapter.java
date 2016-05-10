package com.project.salminnella.prescoop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.project.salminnella.prescoop.R;
import com.yelp.clientlib.entities.Business;

import java.util.List;

/**
 * Created by anthony on 5/9/16.
 */
public class YelpAdapter extends ArrayAdapter<Business> {
    private static final String TAG = "YelpAdapter";
    List<Business> mData;

    public YelpAdapter(Context context, List<Business> objects) {
        super(context, -1, objects);
        this.mData = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.yelp_list_items, parent, false);
        TextView yelpTitle = (TextView) rowItem.findViewById(R.id.yelp_list_title);

        Business business = mData.get(position);
        yelpTitle.setText(business.name());

        return rowItem;
    }
}
