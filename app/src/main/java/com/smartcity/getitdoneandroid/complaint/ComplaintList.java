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
    public Corporator corporator_details;

    public ComplaintList() {
    }
}
