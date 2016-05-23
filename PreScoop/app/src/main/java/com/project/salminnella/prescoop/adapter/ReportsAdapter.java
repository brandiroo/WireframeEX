package com.project.salminnella.prescoop.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.project.salminnella.prescoop.R;
import com.project.salminnella.prescoop.model.Reports;

/**
 * Created by anthony on 5/20/16.
 */
public class ReportsAdapter extends ArrayAdapter<Reports> {

    Reports[] mObjects;
    Context context;

    public ReportsAdapter(Context context, Reports[] mObjects ) {
        super(context, -1, mObjects);
        this.mObjects = mObjects;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reports_list_items, parent, false);
        TextView reportDate = (TextView) view.findViewById(R.id.report_date_list_item);
        TextView reportTitle = (TextView) view.findViewById(R.id.report_title_list_item);
        reportTitle.setTextColor(Color.BLUE);
        reportTitle.setPaintFlags(reportTitle.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        Reports reports = mObjects[position];

        reportDate.setText(reports.getmDate());
        reportTitle.setText(reports.getmTitle());

        return view;
    }
}