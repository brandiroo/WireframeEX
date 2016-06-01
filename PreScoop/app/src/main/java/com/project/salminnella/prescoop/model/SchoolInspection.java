package com.project.salminnella.prescoop.model;

/**
 * To help reduce the number of fields in the PreSchool constructor, this class models fields related
 * to the school inspection information.
 */
public class SchoolInspection {
    private int inspectionNum, inspectionTypeA, inspectionTypeB, citationTypeA, citationTypeB;
    private String inspectionDates;

    public SchoolInspection(int inspectionNum, int inspectionTypeA, int inspectionTypeB, int citationTypeA,
                            int citationTypeB, String inspectionDates) {
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

    public int getInspectionTypeA() {
        return inspectionTypeA;
    }

    public int getInspectionTypeB() {
        return inspectionTypeB;
    }

    public int getCitationTypeA() {
        return citationTypeA;
    }

    public int getCitationTypeB() {
        return citationTypeB;
    }

    public String getInspectionDates() {
        return inspectionDates;
    }
}
