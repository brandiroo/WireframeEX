package com.project.salminnella.prescoop.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.salminnella.prescoop.R;
import com.project.salminnella.prescoop.dbHelper.DatabaseHelper;
import com.squareup.picasso.Picasso;

/**
 * Created by anthony on 5/12/16.
 */
public class DBCursorAdapter extends android.widget.CursorAdapter {

    public DBCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.recycler_view_items,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView schoolImage = (ImageView) view.findViewById(R.id.school_image_items);
        TextView schoolName = (TextView) view.findViewById(R.id.school_name_items);

        schoolName.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_NAME)));

        String url = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_IMAGE_URL));

        if (url.matches("")) {
            Picasso.with(context).load(R.drawable.no_image_available).into(schoolImage);
        } else {
            Picasso.with(context).load(url).into(schoolImage);
        }
    }
}
