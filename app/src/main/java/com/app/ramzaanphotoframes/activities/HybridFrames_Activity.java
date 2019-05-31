package com.app.ramzaanphotoframes.activities;

import android.app.Dialog;
import android.os.Parcelable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.RelativeLayout;

import com.app.ramzaanphotoframes.R;
import com.app.ramzaanphotoframes.adapters.AppsParkAdsAdapter;
import com.app.ramzaanphotoframes.adapters.ViewPagerAdapter;
import com.app.ramzaanphotoframes.classes.App;
import com.app.ramzaanphotoframes.classes.ConnectionDetector;
import com.app.ramzaanphotoframes.classes.FramesModel;
import com.app.ramzaanphotoframes.classes.PlaystoreappslistingResponse;
import com.app.ramzaanphotoframes.classes.RetrofitApis;
import com.app.ramzaanphotoframes.fragments.Landscape_Frames;
import com.app.ramzaanphotoframes.fragments.Morefree_apps;
import com.app.ramzaanphotoframes.fragments.Portrait_Frames;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HybridFrames_Activity extends AppCompatActivity {

    private ConnectionDetector cd;
    private boolean isInternetPresent;
    private AdView adView;
    TabLayout tabs;
    ViewPager viewpager;
    ViewPagerAdapter adapter;
    ArrayList<String> landscape_list,portrait_list;
    ArrayList<App> apps_list;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hybrid_frames);
        init();
    }

    private void init()
    {
        cd = new ConnectionDetector(this);
        isInternetPresent = cd.isConnectingToInternet();

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        SpannableStringBuilder title = new SpannableStringBuilder(getResources().getString(R.string.app_name));
        getSupportActionBar().setTitle(title);

        landscape_list=new ArrayList<>();
        portrait_list=new ArrayList<>();
        apps_list=new ArrayList<>();

        dialog = new Dialog(this,
                android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.servicecall_loading);
        dialog.setCancelable(false);

        tabs=(TabLayout)findViewById(R.id.tabs);
        viewpager=(ViewPager)findViewById(R.id.viewpager);

        if (isInternetPresent) {
            displayAd();
            getPhotoframes();
        }
    }

    private void setupTabsAndViewpager()
    {
        setupViewPager(viewpager);
        tabs.setupWithViewPager(viewpager);
        tabs.setupWithViewPager(viewpager,true);
    }

    public void getPlaystoreApps(){
        Call<PlaystoreappslistingResponse> call= RetrofitApis.Factory.create(HybridFrames_Activity.this).getAppsList();
        dialog.show();
        call.enqueue(new Callback<PlaystoreappslistingResponse>() {
            @Override
            public void onResponse(Call<PlaystoreappslistingResponse> call, Response<PlaystoreappslistingResponse> response) {
                if(dialog!=null)
                    dialog.dismiss();
                if(response.isSuccessful()){
                    PlaystoreappslistingResponse playstoreappslistingResponse=response.body();
                    if(playstoreappslistingResponse!=null)
                    {
                        apps_list.addAll(playstoreappslistingResponse.getApps());
                    }
                    setupTabsAndViewpager();
                }
            }

            @Override
            public void onFailure(Call<PlaystoreappslistingResponse> call, Throwable t) {
                if(dialog!=null)
                    dialog.dismiss();
            }
        });
    }

    public void getPhotoframes(){
        Call<FramesModel> call= RetrofitApis.Factory.createRamzaanFrames(HybridFrames_Activity.this).getFramesList();
        dialog.show();
        call.enqueue(new Callback<FramesModel>() {
            @Override
            public void onResponse(Call<FramesModel> call, Response<FramesModel> response) {
                if(dialog!=null)
                    dialog.dismiss();
                if(response.isSuccessful()){
                    FramesModel framesModel=response.body();
                    if(framesModel!=null)
                    {
                        if(framesModel.getLandscape()!=null && framesModel.getLandscape().size()>0)
                          landscape_list.addAll(framesModel.getLandscape());
                        if(framesModel.getPortrait()!=null && framesModel.getPortrait().size()>0)
                            portrait_list.addAll(framesModel.getPortrait());
                    }
                    getPlaystoreApps();
                }
            }

            @Override
            public void onFailure(Call<FramesModel> call, Throwable t) {
                if(dialog!=null)
                    dialog.dismiss();
            }
        });
    }

    private void displayAd() {
        try {
            RelativeLayout banner_ads_layout = (RelativeLayout) findViewById(R.id.banner_ads_layout);
            banner_ads_layout.setVisibility(View.VISIBLE);
            adView = findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
        } catch (Exception e) {

        }
    }

    private void setupViewPager(final ViewPager viewPager)
    {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        Landscape_Frames landscape_frames=new Landscape_Frames();
        Bundle landscapebundle=new Bundle();
        landscapebundle.putStringArrayList("Landscape",landscape_list);
        landscape_frames.setArguments(landscapebundle);
        adapter.addFragment(landscape_frames, "Landscape");

        Portrait_Frames portrait_frames=new Portrait_Frames();
        Bundle portrait_bundle=new Bundle();
        portrait_bundle.putStringArrayList("Portrait",portrait_list);
        portrait_frames.setArguments(portrait_bundle);
        adapter.addFragment(portrait_frames, "Portrait");

        Morefree_apps morefree_apps=new Morefree_apps();
        Bundle appsbundle=new Bundle();
        appsbundle.putParcelableArrayList("appslist",apps_list);
        morefree_apps.setArguments(appsbundle);
        adapter.addFragment(morefree_apps, "More Free Apps[AD]");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (isInternetPresent) {
            if (adView != null) {
                adView.resume();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (isInternetPresent) {
            if (adView != null) {
                adView.pause();
            }
        }
    }


    @Override
    public void onDestroy() {
        if (isInternetPresent) {
            if (adView != null) {
                adView.destroy();
            }
        }

        super.onDestroy();
    }
}
