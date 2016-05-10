package com.project.salminnella.prescoop.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.salminnella.prescoop.R;
import com.project.salminnella.prescoop.model.PreSchool;

/**
 * Created by anthony on 5/9/16.
 */
public class TabLayoutFragment extends Fragment {
    private static final String TAG = "TabFragment";
    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;
    TextView num_visits;
    static PreSchool preschool;
    View viewAllVisits;

    public static TabLayoutFragment newInstance(int page, PreSchool schoolData) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        TabLayoutFragment fragment = new TabLayoutFragment();
        fragment.setArguments(args);
        preschool = schoolData;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (mPage == 1) {
            viewAllVisits = inflater.inflate(R.layout.fragment_all_visits, container, false);
            num_visits = (TextView) viewAllVisits.findViewById(R.id.all_visits_frag_text);
            num_visits.setText(preschool.getName());
            return viewAllVisits;
        } else if (mPage == 2) {
            View view = inflater.inflate(R.layout.fragment_citations, container, false);
            TextView textView = (TextView) view.findViewById(R.id.citations_frag_text);
            textView.setText("Citations" + mPage);
            return view;
        } else if (mPage == 3) {
            View view = inflater.inflate(R.layout.fragment_inspections, container, false);
            TextView textView = (TextView) view.findViewById(R.id.inspections_frag_text);
            textView.setText("Inspections" + mPage);
            return view;
        } else if (mPage == 4) {
            View view = inflater.inflate(R.layout.fragment_complaints, container, false);
            TextView textView = (TextView) view.findViewById(R.id.complaints_fragment_text);
            textView.setText("complaints" + mPage);
            return view;
        } else if (mPage == 5) {
            View view = inflater.inflate(R.layout.fragment_other_visits, container, false);
            TextView textView = (TextView) view.findViewById(R.id.other_visits_frag_text);
            textView.setText("other visits" + mPage);
            return view;
        } else if (mPage == 6) {
            View view = inflater.inflate(R.layout.fragment_reports, container, false);
            TextView textView = (TextView) view.findViewById(R.id.reports_frag_text);
            textView.setText("reports" + mPage);
            return view;
        }

        return null;
    }
}
