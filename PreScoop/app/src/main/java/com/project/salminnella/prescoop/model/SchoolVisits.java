package com.project.salminnella.prescoop.model;

/**
 * To help reduce the number of fields in the PreSchool constructor, this class models fields related
 * to the inspection visits information in the School Visits tab of the SchoolDetailsActivity.
 */
public class SchoolVisits {
    private int numVisits, otherVisits, visitTypeACitation, visitTypeBCitation, totalReports;
    private String visitDates, otherVisitDates;


    public SchoolVisits(int numVisits, int otherVisits, int visitTypeACitation, int visitTypeBCitation,
                        int totalReports, String visitDates, String otherVisitDates) {
        this.numVisits = numVisits;
        this.otherVisits = otherVisits;
        this.visitTypeACitation = visitTypeACitation;
        this.visitTypeBCitation = visitTypeBCitation;
        this.totalReports = totalReports;
        this.visitDates = visitDates;
        this.otherVisitDates = otherVisitDates;
    }

    public int getNumVisits() {
        return numVisits;
    }

    public int getOtherVisits() {
        return otherVisits;
    }

    public int getVisitTypeACitation() {
        return visitTypeACitation;
    }

    public int getVisitTypeBCitation() {
        return visitTypeBCitation;
    }

    public int getTotalReports() {
        return totalReports;
    }

    public String getVisitDates() {
        return visitDates;
    }

    public String getOtherVisitDates() {
        return otherVisitDates;
    }
}
