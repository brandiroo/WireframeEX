package com.project.salminnella.prescoop.utility;

import com.project.salminnella.prescoop.model.PreSchool;

import java.util.Comparator;

/**
 * Allows sorting by rating using Collections.sort in MainActivity
 */
public class RatingComparator implements Comparator<PreSchool> {
    @Override
    public int compare(PreSchool schoolOne, PreSchool schoolTwo) {
        return schoolTwo.getRating() - schoolOne.getRating();
    }
}
