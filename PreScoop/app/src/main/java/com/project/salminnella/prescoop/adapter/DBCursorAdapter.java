package com.project.salminnella.prescoop.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.salminnella.prescoop.R;
import com.project.salminnella.prescoop.dbHelper.DatabaseHelper;
import com.squareup.picasso.Picasso;

/**
 * Created by anthony on 5/15/16.
 */
public class DBCursorAdapter extends RecyclerView.Adapter<ListViewHolder> {

    // Because RecyclerView.Adapter in its current form doesn't natively
    // support cursors, we wrap a Cursor Adapter that will do all the job
    // for us.
    CursorAdapter mCursorAdapter;
    Context mContext;

    public DBCursorAdapter(Context context, Cursor c) {

        mContext = context;
        mCursorAdapter = new android.support.v4.widget.CursorAdapter(mContext, c, 0) {

            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                // Inflate the view here
                return LayoutInflater.from(context).inflate(R.layout.recycler_view_items,parent,false);
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                // Binding operations
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
        };
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {
        // Passing the binding operation to cursor loader
        mCursorAdapter.getCursor().moveToPosition(position); //EDITED: added this line as suggested in the comments below, thanks :)
        mCursorAdapter.bindView(holder.itemView, mContext, mCursorAdapter.getCursor());
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Passing the inflater job to the cursor-adapter
        View v = mCursorAdapter.newView(mContext, mCursorAdapter.getCursor(), parent);
        return new ListViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return mCursorAdapter.getCount();
    }
}
