package com.smartcity.getitdoneandroid.complaint;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.smartcity.getitdoneandroid.AppUser;
import com.smartcity.getitdoneandroid.MyApplication;
import com.smartcity.getitdoneandroid.R;
import com.smartcity.getitdoneandroid.helpers.CallWebService;
import com.smartcity.getitdoneandroid.helpers.ComplexPreferences;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    private final List<ComplaintList> mValues;
    private Context mContext;
    private AppUser appUser;
    private boolean isVotedClicked = false;
    private boolean isFlagClicked = false;

    public MyItemRecyclerViewAdapter(Context context, List<ComplaintList> items) {
        mValues = items;
        mContext = context;
        ComplexPreferences preferences = ComplexPreferences.getComplexPreferences(context,"userpref",context.MODE_PRIVATE);
        appUser = preferences.getObject("appuser",AppUser.class);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.wall_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.mContentView.setText(holder.mItem.title);
        holder.descriptionTV.setText(holder.mItem.description);
        holder.corporatorNameTV.setText(holder.mItem.corporator_details != null ? "#"+holder.mItem.corporator_details.first_name: "" );
        holder.likeCount.setText(""+holder.mItem.upvote_count);
        holder.flagCount.setText(""+holder.mItem.flag_count);
        Glide.with(mContext)
                .load(holder.mItem.file_link!=null? holder.mItem.file_link:"")
                .into(holder.imageView);
        if(holder.mItem.flagged) {
            holder.flagIV.setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
        }else {
            holder.flagIV.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
        }

        if(holder.mItem.upvoted) {
            holder.likeIV.setColorFilter(Color.parseColor("#3F51B5"), PorterDuff.Mode.SRC_ATOP);
        }else {
            holder.likeIV.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
        }


        holder.likeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              int count =  holder.mItem.upvote_count;

                if(holder.mItem.isVote == true){
                    //voteRelatedCall(holder.mItem.complaintID);
                    holder.mItem.upvote_count = count - 1;
                    holder.likeIV.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);

                    holder.mItem.isVote = false;

                }else {

                    //voteRelatedCall(holder.mItem.complaintID);
                    holder.mItem.upvote_count = count + 1;
                    holder.likeIV.setColorFilter(Color.parseColor("#3F51B5"), PorterDuff.Mode.SRC_ATOP);
                    holder.mItem.isVote = true;
                }

                holder.likeCount.setText(""+holder.mItem.upvote_count);
              // notifyItemChanged(position);
            }
        });

        holder.flagLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int count =  holder.mItem.flag_count;

                if(holder.mItem.isflag == true){
                    //fragRelatedCall(holder.mItem.complaintID);
                    holder.mItem.flag_count = count - 1;
                    holder.flagIV.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);

                    holder.mItem.isflag = false;
                }else {
                    //fragRelatedCall(holder.mItem.complaintID);
                    holder.mItem.flag_count = count + 1;
                    holder.flagIV.setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);

                    holder.mItem.isflag = true;
                }
                holder.flagCount.setText(""+holder.mItem.flag_count);
              //  notifyItemChanged(position);
            }
        });
    }

    private void voteRelatedCall(int postId) {

        new CallWebService("http://getitdonee.azurewebsites.net/Post/"+postId+"/upvote?facebook_user_id="+appUser.getFbid(), CallWebService.TYPE_JSONOBJECT) {
            @Override
            public void response(String response) {
                Log.e("Response from net",response);
            }

            @Override
            public void error(VolleyError error) {

            }
        }.start();


    }

    private void fragRelatedCall(int postId) {
        new CallWebService("http://getitdonee.azurewebsites.net/Post/"+postId+"/flag?facebook_user_id="+appUser.getFbid(), CallWebService.TYPE_JSONOBJECT) {
            @Override
            public void response(String response) {
                Log.e("Response from net",response);
            }

            @Override
            public void error(VolleyError error) {

            }
        }.start();

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
        public final LinearLayout likeLayout, flagLayout;
        public final ImageView likeIV, flagIV;
        public final TextView likeCount, flagCount;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            mContentView = (TextView) view.findViewById(R.id.titleTV);
            descriptionTV = (TextView) view.findViewById(R.id.descriptionTV);
            corporatorNameTV = (TextView) view.findViewById(R.id.corporatorNameTV);
            imageView = (ImageView) view.findViewById(R.id.item_image);
            likeLayout = (LinearLayout) view.findViewById(R.id.likeLayout);
            flagLayout = (LinearLayout) view.findViewById(R.id.commentLayout);
            likeIV = (ImageView) view.findViewById(R.id.likeIV);
            flagIV = (ImageView) view.findViewById(R.id.flagIV);
            likeCount = (TextView) view.findViewById(R.id.likeNumber);
            flagCount = (TextView) view.findViewById(R.id.commentCount);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
