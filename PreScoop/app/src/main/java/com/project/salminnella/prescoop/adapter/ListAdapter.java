package com.project.salminnella.prescoop.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.salminnella.prescoop.R;
import com.project.salminnella.prescoop.model.PreSchool;
import com.project.salminnella.prescoop.utility.Utilities;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter to bind to recycler view list
 */
public class ListAdapter extends RecyclerView.Adapter<ListViewHolder> {

    // region Member Variables
    private Context context;
    private List<PreSchool> mSchools;
    private OnRvItemClickListener listener;
    // endregion Member Variables

    // constructor
    public ListAdapter(List<PreSchool> mSchools, OnRvItemClickListener listener) {
        this.mSchools = mSchools;
        this.listener = listener;
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View schoolView = inflater.inflate(R.layout.recycler_view_items, parent, false);

        // Return a new holder instance
        return new ListViewHolder(schoolView);
    }


    @Override
    public void onBindViewHolder(ListViewHolder holder, final int position) {
        // Get the data model based on position
        PreSchool preSchool = mSchools.get(position);

        // Set item views based on the data model
        TextView schoolNameTextView = holder.schoolNameTextView;
        TextView schoolPriceTextView = holder.schoolPriceTextView;
        schoolNameTextView.setText(preSchool.getName());
        holder.schoolRatingImageView.setImageResource(Utilities.getRatingImage(preSchool.getRating()));

        // checks the school price, the $999 value is default right now for an unknown price per month
        if (preSchool.getPrice() == 999) {
            schoolPriceTextView.setText(R.string.contact_school_price_label);
        } else {
            String price = context.getString(R.string.money_sign) + preSchool.getPrice() + context.getString(R.string.per_month);
            schoolPriceTextView.setText(price);
        }

        // checks if there is no image for the school, displays a default image if not.
        if (preSchool.getImageUrl().matches("")) {
            Picasso.with(context).load(R.drawable.no_image).into(holder.schoolImageView);
        } else {
            Picasso.with(context).load(preSchool.getImageUrl()).into(holder.schoolImageView);
        }

        holder.bind(mSchools.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return mSchools.size();
    }


    /**
     * Changes the list set in the RecyclerView list. Helps manager the full list, the favorites list
     * and the search filter results.
     * @param schools ArrayList of PreSchool objects
     */
    public void swap(ArrayList<PreSchool> schools){
        mSchools.clear();
        mSchools.addAll(schools);
        notifyDataSetChanged();
    }
}
