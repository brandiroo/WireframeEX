package com.project.salminnella.prescoop.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.project.salminnella.prescoop.R;
import com.project.salminnella.prescoop.adapter.ReportsAdapter;
import com.project.salminnella.prescoop.model.PreSchool;
import com.project.salminnella.prescoop.model.Reports;
import com.project.salminnella.prescoop.utility.Constants;

/**
 * Builds the layout fragments for each tab in the SchoolDetailsActivity. Contains all the licensing
 * and citation information
 */
public class TabLayoutFragment extends Fragment {

    // region Member Variables
    private int mPage;
    static PreSchool mPreschool;
    private ListItemClickable listener;
    // endregion Member Variables

    // the reports tab has a list of reports, that are clickable to go to the WebViewActivity
    public interface ListItemClickable {
        void listItemClicked(View view, String url);
    }

    public static TabLayoutFragment newInstance(int page, PreSchool schoolData) {
        Bundle args = new Bundle();
        args.putInt(Constants.ARG_PAGE, page);
        TabLayoutFragment fragment = new TabLayoutFragment();
        fragment.setArguments(args);
        mPreschool = schoolData;
        return fragment;
    }

    /**
     * Attaches click listener to fragment
     * @param context Context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (ListItemClickable) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + context.getString(R.string.implement_click_listener));
        }
    }

    /**
     * Gets the bundle arguments for the which tab to setup
     * @param savedInstanceState Bundle
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(Constants.ARG_PAGE);
    }

    /**
     * Switches on the mPage to determine which tab to inflate
     * @param inflater LayoutInflater
     * @param container ViewGroup
     * @param savedInstanceState Bundle
     * @return View group containing school details
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        switch (mPage) {
            case 1: // All Visits Tab
                View viewAllVisits = inflater.inflate(R.layout.fragment_all_visits, container, false);
                inflateAllVisits(viewAllVisits);

                return viewAllVisits;
            case 2: // Citations Tab
                View citations = inflater.inflate(R.layout.fragment_citations, container, false);
                inflateCitations(citations);

                return citations;
            case 3: //Inspections Tab
                View inspections = inflater.inflate(R.layout.fragment_inspections, container, false);
                inflateInspections(inspections);

                return inspections;
            case 4: // Complaints Tab
                View complaints = inflater.inflate(R.layout.fragment_complaints, container, false);
                inflateComplaints(complaints);

                return complaints;
            case 5: // Other Visits Tab
                View otherVisits = inflater.inflate(R.layout.fragment_other_visits, container, false);
                inflateOtherVisits(otherVisits);

                return otherVisits;
            case 6: // Reports Tab
                final View reports = inflater.inflate(R.layout.fragment_reports, container, false);
                inflateReports(reports);

                return reports;
        }

        return null;
    }

    /**
     * Initializes and sets views for number of inspections and inspection dates
     * @param viewAllVisits View
     * @return View
     */
    private View inflateAllVisits(View viewAllVisits) {
        TextView numVisits = (TextView) viewAllVisits.findViewById(R.id.num_visits_text_frag);
        TextView visitDates = (TextView) viewAllVisits.findViewById(R.id.visit_dates_text_frag);

        numVisits.setText(String.valueOf(mPreschool.getNumVisits()));
        visitDates.setText(mPreschool.getVisitDates());

        return viewAllVisits;
    }

    /**
     * Initializes and sets views for number of citations
     * @param citations View
     * @return View
     */
    private View inflateCitations(View citations) {

        TextView citationA = (TextView) citations.findViewById(R.id.citations_type_a_text_frag);
        TextView citationB = (TextView) citations.findViewById(R.id.citations_type_b_text_frag);

        citationA.setText(String.valueOf(mPreschool.getCitationTypeA()));
        citationB.setText(String.valueOf(mPreschool.getCitationTypeA()));

        return citations;
    }

    /**
     * Initializes and sets views for inspection types and dates
     * @param inspections View
     * @return View
     */
    private View inflateInspections(View inspections) {
        TextView numInspections = (TextView) inspections.findViewById(R.id.num_inspections_text_frag);
        TextView inspectionDates = (TextView) inspections.findViewById(R.id.inspection_dates_text_frag);
        TextView citationA = (TextView) inspections.findViewById(R.id.inspection_citation_a_text_frag);
        TextView citationB = (TextView) inspections.findViewById(R.id.inspection_citation_b_text_frag);

        numInspections.setText(String.valueOf(mPreschool.getInspectionNum()));
        citationA.setText(String.valueOf(mPreschool.getInspectionTypeA()));
        citationB.setText(String.valueOf(mPreschool.getInspectionTypeB()));
        if (mPreschool.getInspectionDates() == null) {
            inspectionDates.setText(R.string.not_available);
        } else {
            inspectionDates.setText(mPreschool.getInspectionDates());
        }

        return inspections;
    }

    /**
     * Initializes and sets views for total complaint types
     * @param complaints View
     * @return View
     */
    private View inflateComplaints(View complaints) {
        TextView totalComplaints = (TextView) complaints.findViewById(R.id.total_complaint_text_frag);
        TextView totalComplaintAllegSub = (TextView) complaints.findViewById(R.id.total_alleg_sub_text_frag);
        TextView totalComplaintAllegIncon = (TextView) complaints.findViewById(R.id.total_alleg_incon_text_frag);
        TextView totalComplaintTypeACitation = (TextView) complaints.findViewById(R.id.total_type_a_citation_text_frag);
        TextView totalComplaintTypeBCitation = (TextView) complaints.findViewById(R.id.total_type_b_citation_text_frag);
        TextView totalComplaintVisits = (TextView) complaints.findViewById(R.id.total_complaint_visits_text_frag);

        totalComplaints.setText(String.valueOf(mPreschool.getComplaintTotal()));
        totalComplaintAllegSub.setText(String.valueOf(mPreschool.getTotalComplaintAllegSub()));
        totalComplaintAllegIncon.setText(String.valueOf(mPreschool.getTotalComplaintAllegIncon()));
        totalComplaintTypeACitation.setText(String.valueOf(mPreschool.getTotalComplaintTypeACitation()));
        totalComplaintTypeBCitation.setText(String.valueOf(mPreschool.getTotalComplaintTypeBCitation()));
        totalComplaintVisits.setText(String.valueOf(mPreschool.getTotalComplaintVisits()));

        return complaints;
    }

    /**
     * Initializes and sets views for inspection citation
     * @param otherVisits View
     * @return View
     */
    private View inflateOtherVisits(View otherVisits) {
        TextView numVisitInspection = (TextView) otherVisits.findViewById(R.id.num_other_visits_text_frag);
        TextView inspectionDates = (TextView) otherVisits.findViewById(R.id.other_visit_dates_text_frag);
        TextView visitCitationA = (TextView) otherVisits.findViewById(R.id.other_visits_citation_a_text_frag);
        TextView visitCitationB = (TextView) otherVisits.findViewById(R.id.other_visits_citation_b_text_frag);

        numVisitInspection.setText(String.valueOf(mPreschool.getInspectionNum()));
        visitCitationA.setText(String.valueOf(mPreschool.getVisitTypeACitation()));
        visitCitationB.setText(String.valueOf(mPreschool.getVisitTypeBCitation()));
        if (mPreschool.getInspectionDates() == null) {
            inspectionDates.setText(R.string.not_available);
        } else {
            inspectionDates.setText(mPreschool.getInspectionDates());
        }

        return otherVisits;
    }

    /**
     * Initializes and sets views for total reports, and the list holding the web links to view them
     * sets onItemClick listener for each item in the list
     * @param reports View
     * @return View
     */
    private View inflateReports(final View reports) {

        TextView totalReports = (TextView) reports.findViewById(R.id.total_reports_text_frag);
        ListView reportsListView = (ListView) reports.findViewById(R.id.reports_list_view_frag);

        totalReports.setText(String.valueOf(mPreschool.getTotalReports()));
        Reports[] reportsList = mPreschool.getReports();
        if (reportsList != null) {
            ReportsAdapter reportsAdapter = new ReportsAdapter(getActivity(), reportsList);
            reportsListView.setAdapter(reportsAdapter);
            reportsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    listener.listItemClicked(reports, mPreschool.getReports()[position].getmReportUrl());
                }
            });
        }

        return reports;
    }
}
