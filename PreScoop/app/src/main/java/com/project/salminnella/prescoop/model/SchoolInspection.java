package com.project.salminnella.prescoop.model;

/**
 * Created by anthony on 5/25/16.
 */
public class SchoolInspection {
    private int inspectionNum, inspectionTypeA, inspectionTypeB, citationTypeA, citationTypeB;
    private String inspectionDates;

    public SchoolInspection(int inspectionNum, int inspectionTypeA, int inspectionTypeB, int citationTypeA, int citationTypeB, String inspectionDates) {
        this.inspectionNum = inspectionNum;
        this.inspectionTypeA = inspectionTypeA;
        this.inspectionTypeB = inspectionTypeB;
        this.citationTypeA = citationTypeA;
        this.citationTypeB = citationTypeB;
        this.inspectionDates = inspectionDates;
    }

    public int getInspectionNum() {
        return inspectionNum;
    }

    public void setInspectionNum(int inspectionNum) {
        this.inspectionNum = inspectionNum;
    }

    public int getInspectionTypeA() {
        return inspectionTypeA;
    }

    public void setInspectionTypeA(int inspectionTypeA) {
        this.inspectionTypeA = inspectionTypeA;
    }

    public int getInspectionTypeB() {
        return inspectionTypeB;
    }

    public void setInspectionTypeB(int inspectionTypeB) {
        this.inspectionTypeB = inspectionTypeB;
    }

    public int getCitationTypeA() {
        return citationTypeA;
    }

    public void setCitationTypeA(int citationTypeA) {
        this.citationTypeA = citationTypeA;
    }

    public int getCitationTypeB() {
        return citationTypeB;
    }

    public void setCitationTypeB(int citationTypeB) {
        this.citationTypeB = citationTypeB;
    }

    public String getInspectionDates() {
        return inspectionDates;
    }

    public void setInspectionDates(String inspectionDates) {
        this.inspectionDates = inspectionDates;
    }
}
