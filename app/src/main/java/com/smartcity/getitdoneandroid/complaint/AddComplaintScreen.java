package com.smartcity.getitdoneandroid.complaint;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.smartcity.getitdoneandroid.R;

public class AddComplaintScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.submit_complaint_screen);
        getSupportActionBar().setTitle("Add Complaint");
        getSupportActionBar().setElevation(0);
    }
}
