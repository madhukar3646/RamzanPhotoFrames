package com.app.ramzaanphotoframes.activities;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.app.ramzaanphotoframes.R;
import com.app.ramzaanphotoframes.adapters.AppsParkAdsAdapter;
import com.app.ramzaanphotoframes.classes.App;
import com.app.ramzaanphotoframes.classes.ConnectionDetector;
import com.app.ramzaanphotoframes.classes.PlaystoreappslistingResponse;
import com.app.ramzaanphotoframes.classes.RetrofitApis;
import com.app.ramzaanphotoframes.landscape_module.activities.Albums_Activity_lpf;
import com.app.ramzaanphotoframes.landscape_module.activities.Frames_Activity_lpf;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {

    private final int CAMERA_RESULT = 101, GALLERY_RESULT = 102;
    private int screen_width, screen_height;


    private ConnectionDetector cd;
    private boolean isInternetPresent;
    private AdView adView;
    private boolean isStartClicked=true;
    private Dialog dialog;
    private RelativeLayout more_apps_lay_out;
    private RecyclerView more_app_recycler_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        screen_width = this.getResources().getDisplayMetrics().widthPixels;
        screen_height = this.getResources().getDisplayMetrics().heightPixels;

        cd = new ConnectionDetector(this);
        isInternetPresent = cd.isConnectingToInternet();

        dialog = new Dialog(this,
                android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.servicecall_loading);
        dialog.setCancelable(false);

        more_apps_lay_out=(RelativeLayout)findViewById(R.id.more_apps_lay_out);
        more_apps_lay_out.setVisibility(View.GONE);
        more_app_recycler_view=(RecyclerView)findViewById(R.id.more_app_recycler_view);
        more_app_recycler_view.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        SpannableStringBuilder title = new SpannableStringBuilder(getResources().getString(R.string.app_name));
        getSupportActionBar().setTitle(title);

        findViewById(R.id.start_lay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStartClicked=true;
                displayFrameSelectionDialog();
            }
        });

        findViewById(R.id.viewfiles_lay).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                isStartClicked=false;
                if (ContextCompat.checkSelfPermission(MainActivity.this, READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(MainActivity.this, WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    displayFrameSelectionDialog();
                } else {
                    if (shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE)) {
                        //Toast.makeText(getApplicationContext(), "Permission Needed.", Toast.LENGTH_LONG).show();
                    }
                    requestPermissions(new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE}, GALLERY_RESULT);
                }
            }
        });

        findViewById(R.id.rate_us_lay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=" + getPackageName())));
                } catch (Exception e) {
                    viewInBrowser(MainActivity.this, "https://play.google.com/store/apps/details?id=" + getPackageName());
                }
            }
        });

        if (isInternetPresent) {
            displayAd();
            getPlaystoreApps();
        }
    }

    public void getPlaystoreApps(){
        Call<PlaystoreappslistingResponse> call= RetrofitApis.Factory.create(MainActivity.this).getAppsList();
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
                        List<App> appslist=playstoreappslistingResponse.getApps();
                        more_app_recycler_view.setAdapter(new AppsParkAdsAdapter(MainActivity.this,appslist));
                        more_apps_lay_out.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<PlaystoreappslistingResponse> call, Throwable t) {
                if(dialog!=null)
                    dialog.dismiss();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.like) {
            if (isInternetPresent) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=" + getPackageName())));
                } catch (Exception e) {
                    viewInBrowser(MainActivity.this, "https://play.google.com/store/apps/details?id=" + getPackageName());
                }
            } else
                No_Internet_Dialouge();

            return true;
        }



        return super.onOptionsItemSelected(item);
    }
    public void No_Internet_Dialouge()
    {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(
                MainActivity.this);
        mBuilder.setMessage("Sorry No Internet Connection please try again later");
        mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        mBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Toast.makeText(MainActivity.this,"Please connect internet....",Toast.LENGTH_SHORT).show();

                    }
                });
        mBuilder.create();
        mBuilder.show();
    }

    private void viewInBrowser(MainActivity mainActivity, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        if (null != intent.resolveActivity(mainActivity.getPackageManager())) {
            mainActivity.startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == GALLERY_RESULT) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                displayFrameSelectionDialog();

            } else {
                // Toast.makeText(getApplicationContext(), "Permission Needed.", Toast.LENGTH_LONG).show();
                permssiondialog();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void permssiondialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false);
        builder.setTitle("App requires Storage permissions to work perfectly..!");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", getPackageName(), null));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Exit",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.show();
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



    @Override
    public void onBackPressed() {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(
                MainActivity.this);
        mBuilder.setIcon(R.drawable.appicon);
        mBuilder.setTitle("Confirm");
        mBuilder.setMessage("Do you want exit?");
        mBuilder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.this.finish();
                dialog.dismiss();
            }
        });
        mBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        mBuilder.create();
        mBuilder.show();
    }


    private void displayFrameSelectionDialog()
    {
        final Dialog dialog=new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_pfandlf_selection);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);

        RelativeLayout layout_landscape=(RelativeLayout) dialog.findViewById(R.id.layout_landscape);
        RelativeLayout layout_portrait=(RelativeLayout)dialog.findViewById(R.id.layout_portrait);

        layout_landscape.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(isStartClicked)
                   startActivity(new Intent(MainActivity.this, Frames_Activity_lpf.class));
               else
                   startActivity(new Intent(MainActivity.this, Albums_Activity_lpf.class));
                dialog.dismiss();
            }
        });

        layout_portrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 if(isStartClicked)
                     startActivity(new Intent(MainActivity.this, Frames_Activity.class));
                 else
                     startActivity(new Intent(MainActivity.this, Albums_Activity.class));
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

}
