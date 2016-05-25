package com.project.salminnella.prescoop.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.salminnella.prescoop.R;
import com.project.salminnella.prescoop.model.PreSchool;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anthony on 5/2/16.
 */
public class ListAdapter extends RecyclerView.Adapter<ListViewHolder> {
    private Context context;
    private List<PreSchool> mSchools;
    private OnRvItemClickListener listener;

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
        holder.schoolRatingImageView.setImageResource(getRatingImage(preSchool.getRating()));

        if (preSchool.getPrice() == 999) {
            schoolPriceTextView.setText(R.string.contact_school_price_label);
        } else {
            String price = "$" + preSchool.getPrice() + " /mo";
            schoolPriceTextView.setText(price);
        }

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

    public void swap(ArrayList<PreSchool> schools){
        mSchools.clear();
        mSchools.addAll(schools);
        notifyDataSetChanged();
    }

    private int getRatingImage(int rating){
        switch(rating){
            case 1:
                return R.drawable.one_star;
            case 2:
                return R.drawable.two_stars;
            case 3:
                return R.drawable.three_stars;
            case 4:
                return R.drawable.four_stars;
            case 5:
                return R.drawable.five_stars;
            default:
                return 0;
        }
    }
}
