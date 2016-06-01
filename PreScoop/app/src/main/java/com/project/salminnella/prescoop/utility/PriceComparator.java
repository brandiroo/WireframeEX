package com.project.salminnella.prescoop.utility;

import com.project.salminnella.prescoop.model.PreSchool;

import java.util.Comparator;

/**
 * Allows sorting by price using Collections.sort in MainActivity
 */
public class PriceComparator implements Comparator<PreSchool> {
    @Override
    public int compare(PreSchool schoolOne, PreSchool schoolTwo) {
        return schoolOne.getPrice() - schoolTwo.getPrice();
    }
}
