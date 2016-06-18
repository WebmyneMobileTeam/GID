package com.smartcity.getitdoneandroid;

import android.app.Application;

import com.facebook.FacebookSdk;

/**
 * Created by xitij on 18/6/16.
 */
public class MyApplication extends Application{


    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
    }
}
