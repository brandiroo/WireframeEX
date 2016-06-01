package com.project.salminnella.prescoop.adapter;

import com.project.salminnella.prescoop.model.PreSchool;

/**
 * Interface method for list item click in the Recycler View. Captures the selected PreSchool
 * object.
 */
public interface OnRvItemClickListener {
    void onListItemClick(PreSchool preschool);
}
