package com.project.salminnella.prescoop.utility;

import com.project.salminnella.prescoop.model.PreSchool;

import java.util.Comparator;

/**
 * Allows alphabetical sorting based on the school name using Collections.sort in MainActivity
 */
public class NameComparator implements Comparator<PreSchool> {
    @Override
    public int compare(PreSchool schoolOne, PreSchool schoolTwo) {
        String nameOne = schoolOne.getName();
        String nameTwo = schoolTwo.getName();
        return nameOne.compareTo(nameTwo);
    }
}
