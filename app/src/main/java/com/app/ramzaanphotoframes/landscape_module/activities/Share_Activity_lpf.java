package com.app.ramzaanphotoframes.landscape_module.activities;

import android.app.Dialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.ramzaanphotoframes.BuildConfig;
import com.app.ramzaanphotoframes.R;
import com.app.ramzaanphotoframes.activities.MainActivity;
import com.app.ramzaanphotoframes.adapters.AppsParkAdsAdapter;
import com.app.ramzaanphotoframes.classes.App;
import com.app.ramzaanphotoframes.classes.ConnectionDetector;
import com.app.ramzaanphotoframes.classes.PlaystoreappslistingResponse;
import com.app.ramzaanphotoframes.classes.RetrofitApis;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Share_Activity_lpf extends AppCompatActivity {

    private int screenWidth, screenHeight;

    private RelativeLayout mainlayout;
    private String filepath;
    private Bitmap shareImage;
    private ImageView share_image;
    private RecyclerView share_grid;
    private ArrayList<AppBean> appBeanList = new ArrayList<AppBean>();
    private List<String> appNames = new ArrayList<String>();
    private List<Drawable> appIcons = new ArrayList<Drawable>();
    private List<String> infoNamesList;


    private ConnectionDetector cd;
    private boolean isInternetPresent;
    private AdView adView;
    private RelativeLayout more_apps_lay_out;
    private RecyclerView more_app_recycler_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_lpf);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;

        more_apps_lay_out=(RelativeLayout)findViewById(R.id.more_apps_lay_out);
        more_apps_lay_out.setVisibility(View.GONE);
        more_app_recycler_view=(RecyclerView)findViewById(R.id.more_app_recycler_view);
        more_app_recycler_view.setNestedScrollingEnabled(false);
        more_app_recycler_view.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        Toolbar toolbar = (Toolbar) findViewById(R.id.share_toolbar);
        setSupportActionBar(toolbar);
        SpannableStringBuilder title = new SpannableStringBuilder(getResources().getString(R.string.share));
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Share_Activity_lpf.this.finish();

            }
        });


        filepath = getIntent().getExtras().getString("final_image_path");
        shareImage = BitmapFactory.decodeFile(filepath);


        mainlayout = (RelativeLayout) findViewById(R.id.image_layout);
        share_image = (ImageView) findViewById(R.id.share_image);
      //  share_image.getLayoutParams().height = (int) ((this.getResources().getDisplayMetrics().heightPixels) / 2.5);
       // share_image.getLayoutParams().width = (int) ((this.getResources().getDisplayMetrics().widthPixels) / 2.1);
        if (shareImage != null)
            share_image.setImageBitmap(shareImage);


        this.appNames.add("WhatsApp");
        this.appNames.add("facebook");
        this.appNames.add("instagram");
        this.appNames.add("Gmail");
        this.appNames.add("LinkedIn");
        this.appNames.add("Twitter");


        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);

        Uri uri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/drawable/nimage");

        sendIntent.setType("image/*");
        sendIntent.putExtra(Intent.EXTRA_STREAM, uri);
        List<ResolveInfo> resolveInfoList = getPackageManager().queryIntentActivities(sendIntent, 0);

        appBeanList.clear();
        for (int i = 0; i < this.appNames.size(); i++) {
            for (ResolveInfo resolveInfo : resolveInfoList) {
                if (resolveInfo.activityInfo.loadLabel(getPackageManager()).toString().toLowerCase().startsWith(((String) this.appNames.get(i)).toLowerCase())) {
                    AppBean bean = new AppBean();
                    bean.setAppName("" + resolveInfo.activityInfo.loadLabel(getPackageManager()).toString());
                    bean.setPackName("" + resolveInfo.activityInfo.packageName.toString());
                    bean.setIcon(resolveInfo.activityInfo.loadIcon(this.getPackageManager()));
                    if (this.appBeanList.size() - 1 >= 6) {
                        break;
                    }
                    this.appBeanList.add(bean);
                }
            }
        }
        AppBean bean2 = new AppBean();
        bean2.setAppName("More");
        Drawable allIcon = getResources().getDrawable(R.drawable.more);
        bean2.setIcon(allIcon);
        appBeanList.add(bean2);

        share_grid = (RecyclerView) findViewById(R.id.share_grid);
        LinearLayoutManager mLManager_efct1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        share_grid.setLayoutManager(mLManager_efct1);
        share_grid.setHasFixedSize(true);
        Share_Adapter adapter = new Share_Adapter(getApplicationContext());
        share_grid.setAdapter(adapter);

        cd = new ConnectionDetector(this);
        isInternetPresent = cd.isConnectingToInternet();

        if (isInternetPresent) {
            displayAd();
            getPlaystoreApps();
        }
    }


    public void getPlaystoreApps(){
        Call<PlaystoreappslistingResponse> call= RetrofitApis.Factory.create(Share_Activity_lpf.this).getAppsList();
        call.enqueue(new Callback<PlaystoreappslistingResponse>() {
            @Override
            public void onResponse(Call<PlaystoreappslistingResponse> call, Response<PlaystoreappslistingResponse> response) {
                if(response.isSuccessful()){
                    PlaystoreappslistingResponse playstoreappslistingResponse=response.body();
                    if(playstoreappslistingResponse!=null)
                    {
                        List<App> appslist=playstoreappslistingResponse.getApps();
                        more_app_recycler_view.setAdapter(new AppsParkAdsAdapter(Share_Activity_lpf.this,appslist));
                        more_apps_lay_out.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<PlaystoreappslistingResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.btn_wall) {

            AlertDialog alertDialog = new AlertDialog.Builder(Share_Activity_lpf.this)
                    .setMessage("Do you want to set as a wallpaper?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                DisplayMetrics displayMetrics = new DisplayMetrics();
                                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                                int i = displayMetrics.heightPixels;
                                int i2 = displayMetrics.widthPixels;
                                WallpaperManager instance = WallpaperManager.getInstance(getApplicationContext());
                                instance.setBitmap(shareImage);
                                instance.suggestDesiredDimensions(i2, i);
                                Toast.makeText(getApplicationContext(), "Wallpaper successfully changed", Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            dialog.dismiss();
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create();

            alertDialog.show();

        } else if (id == R.id.btn_delete) {
            AlertDialog alertDialog = new AlertDialog.Builder(Share_Activity_lpf.this)
                    .setMessage("Do you want Delete?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            File f = new File(filepath);
                            f.delete();
                            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(f)));
                            Share_Activity_lpf.this.finish();
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create();

            alertDialog.show();

        }

        return super.onOptionsItemSelected(item);
    }


    private class Share_Adapter extends RecyclerView.Adapter<ViewHolder> {
        private Context context;

        public Share_Adapter(Context applicationContext) {

            this.context = applicationContext;
        }

        @Override
        public int getItemCount() {
            return appBeanList.size();
        }

        @Override
        public void onBindViewHolder(ViewHolder arg0, final int position) {
            View view;
            ViewHolder holder = new ViewHolder(arg0.itemView);

            holder.appIcon.getLayoutParams().height = screenWidth / 7;
            holder.appIcon.getLayoutParams().width = screenWidth / 7;

            holder.appIcon.setImageDrawable(appBeanList.get(position).getIcon());
            holder.appName.setText(appBeanList.get(position).getAppName());

            if (appBeanList.get(position).getAppName()
                    .equalsIgnoreCase("Twitter")) {
                holder.appName.setTextColor(Color.parseColor("#e72638"));

            } else if (appBeanList.get(position).getAppName()
                    .equalsIgnoreCase("facebook")) {
                holder.appName.setTextColor(Color.parseColor("#5d84c2"));
            } else if (appBeanList.get(position).getAppName()
                    .equalsIgnoreCase("LinkedIn")) {
                holder.appName.setTextColor(Color.parseColor("#47c3ee"));

            } else if (appBeanList.get(position).getAppName()
                    .equalsIgnoreCase("WhatsApp")) {
                holder.appName.setTextColor(Color.parseColor("#20b384"));
            } else if (appBeanList.get(position).getAppName()
                    .equalsIgnoreCase("instagram")) {
                holder.appName.setTextColor(Color.parseColor("#7b482c"));
            } else if (appBeanList.get(position).getAppName()
                    .equalsIgnoreCase("Gmail")) {
                holder.appName.setTextColor(Color.parseColor("#e84c5f"));
            } else {
                holder.appName.setTextColor(Color.parseColor("#4d3b53"));
            }

            holder.appIcon.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("image/*");
                    Uri uri = FileProvider.getUriForFile(Share_Activity_lpf.this, BuildConfig.APPLICATION_ID + ".provider", new File(filepath));
                    intent.putExtra(Intent.EXTRA_STREAM, uri);
                    intent.putExtra(Intent.EXTRA_TEXT, "Try this app :  https://play.google.com/store/apps/details?id=" + getPackageName());

                    if (position != appBeanList.size() - 1) {
                        intent.setPackage(appBeanList.get(position).getPackName());


                    }
                    if (intent != null)
                        startActivity(Intent.createChooser(intent, "share image using " + appBeanList.get(position).getAppName()));

                }
            });

        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
            View view = View.inflate(getApplicationContext(),
                    R.layout.share_item_layout, null);
            return new ViewHolder(view);
        }

    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView appName;
        ImageView appIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            appIcon = (ImageView) itemView
                    .findViewById(R.id.shareicon);
            appName = (TextView) itemView
                    .findViewById(R.id.appnametv);

        }
    }

    public class AppBean {
        String appName, packName;
        Drawable icon;

        public String getAppName() {
            return appName;
        }

        public void setAppName(String appName) {
            this.appName = appName;
        }

        public String getPackName() {
            return packName;
        }

        public void setPackName(String packName) {
            this.packName = packName;
        }

        public Drawable getIcon() {
            return icon;
        }

        public void setIcon(Drawable icon) {
            this.icon = icon;
        }
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

    /**
     * Called when returning to the activity
     */
    @Override
    public void onResume() {
        super.onResume();
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
