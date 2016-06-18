package com.smartcity.getitdoneandroid.complaint;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.smartcity.getitdoneandroid.R;

import java.util.List;

public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    private final List<ComplaintList> mValues;

    public MyItemRecyclerViewAdapter(List<ComplaintList> items) {
        mValues = items;

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
        holder.corporatorNameTV.setText(holder.mItem.corporator_details.first_name);

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

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mContentView = (TextView) view.findViewById(R.id.titleTV);
            descriptionTV = (TextView) view.findViewById(R.id.descriptionTV);
            corporatorNameTV = (TextView) view.findViewById(R.id.corporatorNameTV);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
