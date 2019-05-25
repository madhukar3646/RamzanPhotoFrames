package com.app.ramzaanphotoframes.landscape_module.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.app.ramzaanphotoframes.R;
import com.app.ramzaanphotoframes.classes.ConnectionDetector;
import com.app.ramzaanphotoframes.landscape_module.adapters.Albums_Adapter;
import com.app.ramzaanphotoframes.recycler_click_listener.ClickListener;
import com.app.ramzaanphotoframes.recycler_click_listener.RecyclerTouchListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.io.File;
import java.util.ArrayList;

public class Albums_Activity_lpf extends AppCompatActivity {
    private ArrayList<String> filepaths = new ArrayList<String>();
    private File file;
    private ImageView noimagetext;
    private RecyclerView fileimagesgrid;

    private Albums_Adapter mAdapter;
    private int width, height;
    private File fileSendToFinal;
    private String filePath;
    private String[] fileNames;
    private int pos;


    private ConnectionDetector cd;
    private boolean isInternetPresent;
    private AdView adView;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_albums_);


        DisplayMetrics metrics = this.getResources().getDisplayMetrics();
        width = metrics.widthPixels;
        height = metrics.heightPixels;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_album);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });


        fileimagesgrid = (RecyclerView) findViewById(R.id.card_recycler);

        noimagetext = (ImageView) findViewById(R.id.image);
        noimagetext.getLayoutParams().width = width / 2;
        noimagetext.getLayoutParams().height = width / 2;


        loadFiles();
        if (filepaths.size() >= 1) {
            fileimagesgrid.setVisibility(View.VISIBLE);
            noimagetext.setVisibility(View.GONE);
        } else {
            fileimagesgrid.setVisibility(View.GONE);
            noimagetext.setVisibility(View.VISIBLE);
        }


        fileimagesgrid.setHasFixedSize(true);
        fileimagesgrid.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mAdapter = new Albums_Adapter(this, filepaths);
        fileimagesgrid.setAdapter(mAdapter);

        fileimagesgrid.addOnItemTouchListener(new RecyclerTouchListener(this,
                fileimagesgrid, new ClickListener() {
            @Override
            public void onClick(View view, final int position) {

                fileSendToFinal = new File(filepaths.get(position));
                filePath = filepaths.get(position);
                Intent intent = new Intent(Albums_Activity_lpf.this, Share_Activity_lpf.class);
                pos = position;
                intent.putExtra("final_image_path", filePath);
                startActivity(intent);

                if (isInternetPresent) {
                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                    } else {
                        Log.d("TAG", "The interstitial wasn't loaded yet.");
                    }
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


        cd = new ConnectionDetector(this);
        isInternetPresent = cd.isConnectingToInternet();

        if (isInternetPresent) {
            displayAd();
            mInterstitialAd_setup();
        }

    }


    public void loadFiles() {
        filepaths.clear();
        file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + getString(R.string.app_name)+"landscape");

        if (file.isDirectory()) {
            fileNames = file.list();
            for (int i = 0; i < fileNames.length; i++) {
                if (fileNames[i].endsWith(".jpg"))
                    filepaths.add(file.toString() + "/" + fileNames[i]);


            }
        }

    }

    private void mInterstitialAd_setup() {
        mInterstitialAd = new InterstitialAd(Albums_Activity_lpf.this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.interstitial_Ad_id_save));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
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

    /**
     * Called when leaving the activity
     */
    @Override
    public void onPause() {
        if (isInternetPresent) {
            if (adView != null) {
                adView.pause();
            }
        }

        super.onPause();
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadFiles();
        try {
            mAdapter.notifyDataSetChanged();
            if (filepaths.size() >= 1) {
                fileimagesgrid.setVisibility(View.VISIBLE);

                noimagetext.setVisibility(View.GONE);
            } else {
                fileimagesgrid.setVisibility(View.GONE);
                noimagetext.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {

        }

        if (isInternetPresent) {
            if (adView != null) {
                adView.resume();
            }
        }
    }


    /**
     * Called before the activity is destroyed
     */
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
