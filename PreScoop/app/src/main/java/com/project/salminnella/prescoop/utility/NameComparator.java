package com.project.salminnella.prescoop.utility;

import com.project.salminnella.prescoop.model.PreSchool;

import java.util.Comparator;

/**
 * Created by anthony on 5/10/16.
 */
public class NameComparator implements Comparator<PreSchool> {
    @Override
    public int compare(PreSchool schoolOne, PreSchool schoolTwo) {

        String nameOne = schoolOne.getName();
        String nameTwo = schoolTwo.getName();
        return nameOne.compareTo(nameTwo);
    }
}
