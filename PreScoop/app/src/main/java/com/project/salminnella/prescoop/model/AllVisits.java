package com.project.salminnella.prescoop.model;

/**
 * Created by anthony on 5/11/16.
 */
public class AllVisits {
    private int numVisits;
    private String visitDates;

    public AllVisits(int numVisits, String visitDates) {
        this.numVisits = numVisits;
        this.visitDates = visitDates;
    }

    public int getNumVisits() {
        return numVisits;
    }

    public void setNumVisits(int numVisits) {
        this.numVisits = numVisits;
    }

    public String getVisitDates() {
        return visitDates;
    }

    public void setVisitDates(String visitDates) {
        this.visitDates = visitDates;
    }
}
