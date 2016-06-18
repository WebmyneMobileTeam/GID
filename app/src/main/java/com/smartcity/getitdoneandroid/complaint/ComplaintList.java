package com.smartcity.getitdoneandroid.complaint;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by xitij on 18/6/16.
 */
public class ComplaintList implements Serializable{

    @SerializedName("id")
    public int complaintID;
    public String title;
    public String description;
    public String file_link;
    public String created_datetime;
    public String status;
    public boolean upvoted;
    public boolean flagged;
    public int upvote_count = 0;
    public int flag_count = 0;

    public Corporator corporator_details;

    public ComplaintList() {
    }
}
