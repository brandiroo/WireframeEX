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
import com.project.salminnella.prescoop.model.PreSchool;
import com.project.salminnella.prescoop.utility.Utilities;
import com.squareup.picasso.Picasso;

/**
 * Used to fill recycler view with data received from the SQLite database in a cursor.
 * The ViewHolder wraps the cursor adapter in order to fill the recycler view
 */
public class DBCursorAdapter extends RecyclerView.Adapter<ListViewHolder> implements View.OnClickListener{

    // region Member Variables
    private CursorAdapter mCursorAdapter;
    private Context mContext;
    private OnRvItemClickListener onRvClickListener;
    // endregion Member Variables

    //constructor creates the cursor adapter, and listener for item click in recycler view
    public DBCursorAdapter(Context context, Cursor c, OnRvItemClickListener listener) {

        mContext = context;
        setOnItemClickListener(listener);

        mCursorAdapter = new android.support.v4.widget.CursorAdapter(mContext, c, 0) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                // Inflate the view here
                return LayoutInflater.from(context).inflate(R.layout.recycler_view_items,parent,false);
            }
            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                // Binding operations
                ImageView schoolImageView = (ImageView) view.findViewById(R.id.school_image_items);
                ImageView schoolRatingImageView = (ImageView) view.findViewById(R.id.school_rating_items);
                TextView schoolNameTextView = (TextView) view.findViewById(R.id.school_name_items);
                TextView schoolPriceTextView = (TextView) view.findViewById(R.id.school_price_items);


                schoolNameTextView.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_NAME)));
                String price = context.getString(R.string.money_sign) + cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_PRICE)) + context.getString(R.string.per_month);
                schoolPriceTextView.setText(price);
                schoolRatingImageView.setImageResource(Utilities.getRatingImage(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COL_RATING))));
                String url = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_IMAGE_URL));
                if (url.matches("")) {
                    Picasso.with(context).load(R.drawable.no_image).into(schoolImageView);
                } else {
                    Picasso.with(context).load(url).into(schoolImageView);
                }
            }
        };
    }

    /**
     * Creates a new ViewHolder, and initialize on click listener
     * @param parent ViewGroup
     * @param viewType int
     * @return ListViewHolder
     */
    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Passing the inflater job to the cursor-adapter
        View v = mCursorAdapter.newView(mContext, mCursorAdapter.getCursor(), parent);
        v.setOnClickListener(this);

        return new ListViewHolder(v);
    }

    /**
     * Uses the cursor adapter bindView to bind to the ListViewHolder group
     * @param holder ListViewHolder
     * @param position int
     */
    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {
        // Passing the binding operation to cursor loader
        mCursorAdapter.getCursor().moveToPosition(position);
        mCursorAdapter.bindView(holder.itemView, mContext, mCursorAdapter.getCursor());
    }

    /**
     * Captures the list item click from the Recycler View
     * @param onRvItemClickListener OnRvItemClickListener
     */
    public void setOnItemClickListener(final OnRvItemClickListener onRvItemClickListener)
    {
        this.onRvClickListener = onRvItemClickListener;
    }

    /**
     * Captures the list item click from the Recycler View.
     * Builds the preschool object from cursor data, to send to SchoolDetailsActivity
     * @param view View
     */
    @Override
    public void onClick(final View view)
    {
        if (this.onRvClickListener != null)
        {
            final RecyclerView recyclerView = (RecyclerView) view.getParent();
            final int position = recyclerView.getChildLayoutPosition(view);
            if (position != RecyclerView.NO_POSITION)
            {
                final Cursor cursor = (Cursor) this.mCursorAdapter.getItem(position);
                PreSchool preschool = Utilities.buildPreschoolObject(cursor);
                this.onRvClickListener.onListItemClick(preschool);
            }
        }
    }

    /**
     * Returns number of item in the list
     * @return int
     */
    @Override
    public int getItemCount() {
        return mCursorAdapter.getCount();
    }
}
