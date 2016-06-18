package com.smartcity.getitdoneandroid.complaint;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.smartcity.getitdoneandroid.R;

import java.util.List;

public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    private final List<ComplaintList> mValues;
    private Context mContext;

    public MyItemRecyclerViewAdapter(Context context, List<ComplaintList> items) {
        mValues = items;
        mContext = context;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.wall_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mContentView.setText(holder.mItem.title);
        holder.descriptionTV.setText(holder.mItem.description);
        holder.corporatorNameTV.setText(holder.mItem.corporator_details != null ? "#"+holder.mItem.corporator_details.first_name: "" );

        Glide.with(mContext)
                .load(holder.mItem.file_link!=null? holder.mItem.file_link:"")
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mContentView;
        public ComplaintList mItem;
        public final TextView descriptionTV;
        public final TextView corporatorNameTV;
        public final ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mContentView = (TextView) view.findViewById(R.id.titleTV);
            descriptionTV = (TextView) view.findViewById(R.id.descriptionTV);
            corporatorNameTV = (TextView) view.findViewById(R.id.corporatorNameTV);
            imageView = (ImageView) view.findViewById(R.id.item_image);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
