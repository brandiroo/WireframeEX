package com.project.salminnella.prescoop.fragment;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.salminnella.prescoop.R;
import com.project.salminnella.prescoop.model.PreSchool;
import com.project.salminnella.prescoop.utility.Constants;

/**
 * Created by anthony on 5/9/16.
 */
public class TabLayoutFragment extends Fragment {
    private static final String TAG = "TabFragment";

    private int mPage;
    static PreSchool preschool;
    XmlClickable textClickListener;

    public interface XmlClickable {
        void clickMethod(View view, String url);
    }

    public static TabLayoutFragment newInstance(int page, PreSchool schoolData) {
        Bundle args = new Bundle();
        args.putInt(Constants.ARG_PAGE, page);
        TabLayoutFragment fragment = new TabLayoutFragment();
        fragment.setArguments(args);
        preschool = schoolData;
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            textClickListener = (XmlClickable) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + " must implement textClickListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(Constants.ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (mPage == 1) {
            View viewAllVisits = inflater.inflate(R.layout.fragment_all_visits, container, false);

            TextView numVisits = (TextView) viewAllVisits.findViewById(R.id.num_visits_text_frag);
            numVisits.setText(String.valueOf(preschool.getNumVisits()));
            TextView visitDates = (TextView) viewAllVisits.findViewById(R.id.visit_dates_text_frag);
            visitDates.setText(preschool.getVisitDates());

            return viewAllVisits;

        } else if (mPage == 2) {
            View citations = inflater.inflate(R.layout.fragment_citations, container, false);

            TextView citationA = (TextView) citations.findViewById(R.id.citations_type_a_text_frag);
            TextView citationB = (TextView) citations.findViewById(R.id.citations_type_b_text_frag);

            citationA.setText(String.valueOf(preschool.getCitationTypeA()));
            citationB.setText(String.valueOf(preschool.getCitationTypeA()));

            return citations;

        } else if (mPage == 3) {
            View view = inflater.inflate(R.layout.fragment_inspections, container, false);

            TextView textView = (TextView) view.findViewById(R.id.inspections_frag_text);
            textView.setText("Inspections" + mPage);

            return view;

        } else if (mPage == 4) {
            View complaints = inflater.inflate(R.layout.fragment_complaints, container, false);

            TextView totalComplaints = (TextView) complaints.findViewById(R.id.total_complaint_text_frag);
            TextView totalComplaintAllegSub = (TextView) complaints.findViewById(R.id.total_alleg_sub_text_frag);
            TextView totalComplaintAllegIncon = (TextView) complaints.findViewById(R.id.total_alleg_incon_text_frag);
            TextView totalComplaintTypeACitation = (TextView) complaints.findViewById(R.id.total_type_a_citation_text_frag);
            TextView totalComplaintTypeBCitation = (TextView) complaints.findViewById(R.id.total_type_b_citation_text_frag);
            TextView totalComplaintVisits = (TextView) complaints.findViewById(R.id.total_complaint_visits_text_frag);

            totalComplaints.setText(String.valueOf(preschool.getComplaintTotal()));
            totalComplaintAllegSub.setText(String.valueOf(preschool.getTotalComplaintAllegSub()));
            totalComplaintAllegIncon.setText(String.valueOf(preschool.getTotalComplaintAllegIncon()));
            totalComplaintTypeACitation.setText(String.valueOf(preschool.getTotalComplaintTypeACitation()));
            totalComplaintTypeBCitation.setText(String.valueOf(preschool.getTotalComplaintTypeBCitation()));
            totalComplaintVisits.setText(String.valueOf(preschool.getTotalComplaintVisits()));

            return complaints;

        } else if (mPage == 5) {
            View view = inflater.inflate(R.layout.fragment_other_visits, container, false);
            TextView textView = (TextView) view.findViewById(R.id.other_visits_frag_text);
            textView.setText("other visits" + mPage);

            return view;

        } else if (mPage == 6) {
            final View reports = inflater.inflate(R.layout.fragment_reports, container, false);

            TextView totalReports = (TextView) reports.findViewById(R.id.total_reports_text_frag);
            TextView totalReportsDate = (TextView) reports.findViewById(R.id.reports_date_text_frag);

            totalReports.setText(String.valueOf(preschool.getTotalReports()));
            totalReportsDate.setText(preschool.getReportDates());
            if (preschool.getReportUrl().contains("http")) {
                totalReportsDate.setPaintFlags(totalReportsDate.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                totalReportsDate.setTextColor(getResources().getColor(R.color.htmlLink));
                totalReportsDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        textClickListener.clickMethod(reports, preschool.getReportUrl());
                    }
                });
            }
            return reports;
        }

        return null;
    }
}
