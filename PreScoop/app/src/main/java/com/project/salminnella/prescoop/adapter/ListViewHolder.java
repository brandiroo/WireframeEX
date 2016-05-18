package com.project.salminnella.prescoop.adapter; /**
 * Created by anthony on 5/17/16.
 */

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.salminnella.prescoop.R;
import com.project.salminnella.prescoop.model.PreSchool;

// Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
public class ListViewHolder extends RecyclerView.ViewHolder {
    // Your holder should contain a member variable
    // for any view that will be set as you render a row
    public TextView schoolNameTextView;
    public ImageView schoolImageView;

    // We also create a constructor that accepts the entire item row
    // and does the view lookups to find each subview
    public ListViewHolder(View itemView) {
        // Stores the itemView in a public final member variable that can be used
        // to access the context from any CursorViewHolder instance.
        super(itemView);
        schoolNameTextView = (TextView) itemView.findViewById(R.id.school_name_items);
        schoolImageView = (ImageView) itemView.findViewById(R.id.school_image_items);
    }

    public void bind(final PreSchool preschool, final ListAdapter.OnItemClickListener listener){
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(preschool);
            }
        });
    }
}