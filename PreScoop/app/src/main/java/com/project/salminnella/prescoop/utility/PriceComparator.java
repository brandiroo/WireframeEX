package com.project.salminnella.prescoop.utility;

import com.project.salminnella.prescoop.model.PreSchool;

import java.util.Comparator;

/**
 * Created by anthony on 5/10/16.
 */
public class PriceComparator implements Comparator<PreSchool> {
    @Override
    public int compare(PreSchool schoolOne, PreSchool schoolTwo) {

        return schoolOne.getPrice() - schoolTwo.getPrice();
    }
}
