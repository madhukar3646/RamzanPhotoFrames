package com.app.ramzaanphotoframes.activities;

import android.app.Dialog;
import android.os.Environment;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import com.app.ramzaanphotoframes.R;
import com.app.ramzaanphotoframes.adapters.ViewPagerAdapter;
import com.app.ramzaanphotoframes.classes.App;
import com.app.ramzaanphotoframes.classes.ConnectionDetector;
import com.app.ramzaanphotoframes.classes.FramesModel;
import com.app.ramzaanphotoframes.classes.OnDisplayAddListener;
import com.app.ramzaanphotoframes.classes.PlaystoreappslistingResponse;
import com.app.ramzaanphotoframes.classes.RetrofitApis;
import com.app.ramzaanphotoframes.classes.Utils;
import com.app.ramzaanphotoframes.fragments.Landscape_Frames;
import com.app.ramzaanphotoframes.fragments.Morefree_apps;
import com.app.ramzaanphotoframes.fragments.Portrait_Frames;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.io.File;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HybridFrames_Activity extends AppCompatActivity implements OnDisplayAddListener {

    private ConnectionDetector cd;
    private boolean isInternetPresent;
    private AdView adView;
    private InterstitialAd mInterstitialAd;
    TabLayout tabs;
    ViewPager viewpager;
    ViewPagerAdapter adapter;
    ArrayList<String> landscape_list,portrait_list;
    ArrayList<App> apps_list;
    private Dialog dialog;
    private boolean isAlbums=false;

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
        isAlbums=getIntent().getBooleanExtra("isAlbums",false);

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
            mInterstitialAd_setup();
            if(isAlbums)
            {
               loadLandscapeFiles();
               loadPortraitFiles();
               getPlaystoreApps();
            }
            else {
                getPhotoframes();
            }
        }
        else {
            Utils.showInternetToast(HybridFrames_Activity.this);
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
                    apps_list.clear();
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

                        for(int i=0;i<landscape_list.size();i++)
                        {
                            Glide.with(HybridFrames_Activity.this)
                                    .load(landscape_list.get(i))
                                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                    .preload();
                        }

                        for(int i=0;i<portrait_list.size();i++)
                        {
                            Glide.with(HybridFrames_Activity.this)
                                    .load(portrait_list.get(i))
                                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                    .preload();
                        }
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
        landscapebundle.putBoolean("isAlbum",isAlbums);
        landscape_frames.setArguments(landscapebundle);
        landscape_frames.setOnDisplayAddListener(this);
        adapter.addFragment(landscape_frames, "Landscape");

        Portrait_Frames portrait_frames=new Portrait_Frames();
        Bundle portrait_bundle=new Bundle();
        portrait_bundle.putStringArrayList("Portrait",portrait_list);
        portrait_bundle.putBoolean("isAlbum",isAlbums);
        portrait_frames.setArguments(portrait_bundle);
        portrait_frames.setOnDisplayAddListener(this);
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
        if(isAlbums)
        {
            loadLandscapeFiles();
            loadPortraitFiles();
            getPlaystoreApps();
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

    public void loadPortraitFiles() {
        portrait_list.clear();
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + getString(R.string.app_name));

        if (file.isDirectory()) {
            String fileNames[] = file.list();
            for (int i = 0; i < fileNames.length; i++) {
                if (fileNames[i].endsWith(".jpg"))
                    portrait_list.add(file.toString() + "/" + fileNames[i]);
            }
        }
    }

    public void loadLandscapeFiles() {
        landscape_list.clear();
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + getString(R.string.app_name)+"landscape");
        if (file.isDirectory()) {
           String fileNames[] = file.list();
            for (int i = 0; i < fileNames.length; i++) {
                if (fileNames[i].endsWith(".jpg"))
                    landscape_list.add(file.toString() + "/" + fileNames[i]);
            }
        }
    }

    @Override
    public void OnDisplayInterstitialAdd() {
        if (isInternetPresent) {
            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            } else {
                Log.d("TAG", "The interstitial wasn't loaded yet.");
            }
        }
    }

    private void mInterstitialAd_setup() {
        mInterstitialAd = new InterstitialAd(HybridFrames_Activity.this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.interstitial_Ad_id_save));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
    }
}
