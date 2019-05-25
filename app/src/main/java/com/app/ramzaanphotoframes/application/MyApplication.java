package com.app.ramzaanphotoframes.application;

import android.app.Application;

import com.app.ramzaanphotoframes.R;
import com.google.android.gms.ads.MobileAds;


public class MyApplication extends Application
{
    @Override
    public void onCreate() {
        super.onCreate();
        try
        {
            Class.forName("android.os.AsyncTask");
        }
        catch (Throwable throwable)
        {

        }

        MobileAds.initialize(getApplicationContext(),getResources().getString(R.string.app_id));
    }
}
