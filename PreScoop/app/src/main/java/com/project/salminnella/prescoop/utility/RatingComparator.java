package com.project.salminnella.prescoop.utility;

import com.project.salminnella.prescoop.model.PreSchool;

import java.util.Comparator;

/**
 * Created by anthony on 5/10/16.
 */
public class RatingComparator implements Comparator<PreSchool> {
    @Override
    public int compare(PreSchool schoolOne, PreSchool schoolTwo) {
        // descending order (ascending order would be:
        // o1.getGrade()-o2.getGrade())
        return schoolTwo.getRating() - schoolOne.getRating();
    }
}
