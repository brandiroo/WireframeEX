package com.project.salminnella.prescoop.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.salminnella.prescoop.R;
import com.project.salminnella.prescoop.model.PreSchool;
import com.squareup.picasso.Picasso;
import com.yelp.clientlib.entities.Business;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anthony on 5/2/16.
 */
public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private static final String TAG = "ListAdapter";
    private Context context;
    // Store a member variable for the schools
    private List<PreSchool> mSchools;
    private List<Business> yelpBusiness;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(PreSchool preschool);
    }


    // constructor
    public ListAdapter(List<PreSchool> mSchools, OnItemClickListener listener) {
        this.mSchools = mSchools;
        this.listener = listener;
    }

    // constructor
    public ListAdapter(List<Business> yelpBusiness) {
        this.yelpBusiness = yelpBusiness;
        //this.listener = listener;
    }


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView schoolNameTextView;
        public ImageView schoolImageView;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            schoolNameTextView = (TextView) itemView.findViewById(R.id.school_name_items);
            schoolImageView = (ImageView) itemView.findViewById(R.id.school_image_items);
        }
        public void bind(final PreSchool preschool, final OnItemClickListener listener){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(preschool);
                }
            });
        }

    }

    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View schoolView = inflater.inflate(R.layout.recycler_view_items, parent, false);

        // Return a new holder instance
        return new ViewHolder(schoolView);
    }

    @Override
    public void onBindViewHolder(ListAdapter.ViewHolder holder, final int position) {
        // Get the data model based on position
        PreSchool preSchool = mSchools.get(position);

        // Set item views based on the data model
        TextView schoolNameTextView = holder.schoolNameTextView;
        schoolNameTextView.setText(preSchool.getName());

        if (preSchool.getImageUrl().matches("")) {
            Picasso.with(context).load(R.drawable.no_image_available).into(holder.schoolImageView);
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
}
