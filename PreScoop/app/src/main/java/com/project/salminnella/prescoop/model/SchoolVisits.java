package com.project.salminnella.prescoop.model;

/**
 * Created by anthony on 5/11/16.
 */
public class SchoolVisits {
    private int numVisits;
    private int otherVisits;
    private int visitTypeACitation;
    private int visitTypeBCitation;
    private int totalReports;
    private String visitDates;
    private String otherVisitDates;


    public SchoolVisits(int numVisits, int otherVisits, int visitTypeACitation, int visitTypeBCitation, int totalReports, String visitDates, String otherVisitDates) {
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

    public void setNumVisits(int numVisits) {
        this.numVisits = numVisits;
    }

    public int getOtherVisits() {
        return otherVisits;
    }

    public void setOtherVisits(int otherVisits) {
        this.otherVisits = otherVisits;
    }

    public int getVisitTypeACitation() {
        return visitTypeACitation;
    }

    public void setVisitTypeACitation(int visitTypeACitation) {
        this.visitTypeACitation = visitTypeACitation;
    }

    public int getVisitTypeBCitation() {
        return visitTypeBCitation;
    }

    public void setVisitTypeBCitation(int visitTypeBCitation) {
        this.visitTypeBCitation = visitTypeBCitation;
    }

    public int getTotalReports() {
        return totalReports;
    }

    public void setTotalReports(int totalReports) {
        this.totalReports = totalReports;
    }

    public String getVisitDates() {
        return visitDates;
    }

    public void setVisitDates(String visitDates) {
        this.visitDates = visitDates;
    }

    public String getOtherVisitDates() {
        return otherVisitDates;
    }

    public void setOtherVisitDates(String otherVisitDates) {
        this.otherVisitDates = otherVisitDates;
    }
}
