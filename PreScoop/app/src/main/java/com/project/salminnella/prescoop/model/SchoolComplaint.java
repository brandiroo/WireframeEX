package com.project.salminnella.prescoop.model;

/**
 * To help reduce the number of fields in the PreSchool constructor, this class models fields related
 * to the school inspection information.
 */
public class SchoolComplaint {
    private int complaintTotal, totalComplaintAllegSub, totalComplaintAllegIncon, totalComplaintTypeACitation,
                totalComplaintTypeBCitation,  totalComplaintVisits;
    private String complaintDetails;

    public SchoolComplaint(int complaintTotal, int totalComplaintAllegSub, int totalComplaintAllegIncon,
                           int totalComplaintTypeACitation, int totalComplaintTypeBCitation,
                           int totalComplaintVisits, String complaintDetails) {
        this.complaintTotal = complaintTotal;
        this.totalComplaintAllegSub = totalComplaintAllegSub;
        this.totalComplaintAllegIncon = totalComplaintAllegIncon;
        this.totalComplaintTypeACitation = totalComplaintTypeACitation;
        this.totalComplaintTypeBCitation = totalComplaintTypeBCitation;
        this.totalComplaintVisits = totalComplaintVisits;
        this.complaintDetails = complaintDetails;
    }

    public int getComplaintTotal() {
        return complaintTotal;
    }

    public int getTotalComplaintAllegSub() {
        return totalComplaintAllegSub;
    }

    public int getTotalComplaintAllegIncon() {
        return totalComplaintAllegIncon;
    }

    public int getTotalComplaintTypeACitation() {
        return totalComplaintTypeACitation;
    }

    public int getTotalComplaintTypeBCitation() {
        return totalComplaintTypeBCitation;
    }

    public int getTotalComplaintVisits() {
        return totalComplaintVisits;
    }

    public String getComplaintDetails() {
        return complaintDetails;
    }
}
