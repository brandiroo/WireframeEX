package com.project.salminnella.prescoop.adapter;



import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.salminnella.prescoop.R;
import com.project.salminnella.prescoop.model.PreSchool;

/**
 * Provide a reference to the views for each data item.  Since i'm currently using 2 adapters for the
 * RecyclerView, i moved this view holder class out so that both adapters can use it.
 *
 */
public class ListViewHolder extends RecyclerView.ViewHolder {
    public TextView schoolNameTextView;
    public ImageView schoolImageView;
    public TextView schoolPriceTextView;
    public ImageView schoolRatingImageView;

    // Constructor that accepts the entire item row and does the view lookups to find each subview
    public ListViewHolder(View itemView) {
        // Stores the itemView in a public final member variable that can be used
        // to access the context from any CursorViewHolder instance.
        super(itemView);
        schoolNameTextView = (TextView) itemView.findViewById(R.id.school_name_items);
        schoolPriceTextView = (TextView) itemView.findViewById(R.id.school_price_items);
        schoolImageView = (ImageView) itemView.findViewById(R.id.school_image_items);
        schoolRatingImageView = (ImageView) itemView.findViewById(R.id.school_rating_items);
    }

    // Binds the item click listener for the Recycler View.
    public void bind(final PreSchool preschool, final OnRvItemClickListener listener){
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onListItemClick(preschool);
            }
        });
    }
}