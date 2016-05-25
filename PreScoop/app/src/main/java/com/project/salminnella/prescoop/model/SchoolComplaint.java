package com.project.salminnella.prescoop.model;

/**
 * Created by anthony on 5/25/16.
 */
public class SchoolComplaint {
    private int complaintTotal, totalComplaintAllegSub, totalComplaintAllegIncon, totalComplaintTypeACitation,
    totalComplaintTypeBCitation,  totalComplaintVisits;
    private String complaintDetails;

    public SchoolComplaint(int complaintTotal, int totalComplaintAllegSub, int totalComplaintAllegIncon, int totalComplaintTypeACitation, int totalComplaintTypeBCitation, int totalComplaintVisits, String complaintDetails) {
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

    public void setComplaintTotal(int complaintTotal) {
        this.complaintTotal = complaintTotal;
    }

    public int getTotalComplaintAllegSub() {
        return totalComplaintAllegSub;
    }

    public void setTotalComplaintAllegSub(int totalComplaintAllegSub) {
        this.totalComplaintAllegSub = totalComplaintAllegSub;
    }

    public int getTotalComplaintAllegIncon() {
        return totalComplaintAllegIncon;
    }

    public void setTotalComplaintAllegIncon(int totalComplaintAllegIncon) {
        this.totalComplaintAllegIncon = totalComplaintAllegIncon;
    }

    public int getTotalComplaintTypeACitation() {
        return totalComplaintTypeACitation;
    }

    public void setTotalComplaintTypeACitation(int totalComplaintTypeACitation) {
        this.totalComplaintTypeACitation = totalComplaintTypeACitation;
    }

    public int getTotalComplaintTypeBCitation() {
        return totalComplaintTypeBCitation;
    }

    public void setTotalComplaintTypeBCitation(int totalComplaintTypeBCitation) {
        this.totalComplaintTypeBCitation = totalComplaintTypeBCitation;
    }

    public int getTotalComplaintVisits() {
        return totalComplaintVisits;
    }

    public void setTotalComplaintVisits(int totalComplaintVisits) {
        this.totalComplaintVisits = totalComplaintVisits;
    }

    public String getComplaintDetails() {
        return complaintDetails;
    }

    public void setComplaintDetails(String complaintDetails) {
        this.complaintDetails = complaintDetails;
    }
}
