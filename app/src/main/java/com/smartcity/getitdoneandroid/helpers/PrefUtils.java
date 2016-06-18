package com.smartcity.getitdoneandroid.helpers;


import android.content.Context;

/**
 * Created by xitij on 17-03-2015.
 */
public class PrefUtils {


    public static void setLogin(Context context, boolean b) {
        Prefs.with(context).save("login", b);
    }

    public static boolean isLogin(Context ctx) {
        return Prefs.with(ctx).getBoolean("login", false);

    }

}
