package com.project.salminnella.prescoop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.salminnella.prescoop.R;
import com.squareup.picasso.Picasso;
import com.yelp.clientlib.entities.Business;

import java.util.List;

/**
 * The yelp reviews section holds a list of related search results to the curently viewed school
 * This list is populated when there are no exact matches for the school in the response from yelp.
 */
public class YelpAdapter extends ArrayAdapter<Business> {
    private List<Business> mData;
    private Context context;

    public YelpAdapter(Context context, List<Business> objects) {
        super(context, -1, objects);
        this.mData = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.yelp_list_items, parent, false);

        TextView yelpTitle = (TextView) rowItem.findViewById(R.id.yelp_list_title);
        TextView yelpNumReviews = (TextView) rowItem.findViewById(R.id.yelp_list_num_reviews);
        TextView yelpReviewSnippet = (TextView) rowItem.findViewById(R.id.yelp_list_snippet);
        ImageView yelpRating = (ImageView) rowItem.findViewById(R.id.yelp_list_rating);
        ImageView yelpImage = (ImageView) rowItem.findViewById(R.id.yelp_list_image);

        Business business = mData.get(position);

        yelpTitle.setText(business.name());
        String reviewText = String.valueOf(business.reviewCount()) + " Reviews";
        yelpNumReviews.setText(reviewText);
        yelpReviewSnippet.setText(business.snippetText());
        Picasso.with(context).load(business.ratingImgUrlLarge()).into(yelpRating);
        Picasso.with(context).load(business.imageUrl()).into(yelpImage);

        return rowItem;
    }
}
