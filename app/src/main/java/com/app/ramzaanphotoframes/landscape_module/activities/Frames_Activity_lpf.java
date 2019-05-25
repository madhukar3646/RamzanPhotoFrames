package com.app.ramzaanphotoframes.landscape_module.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.app.ramzaanphotoframes.R;
import com.app.ramzaanphotoframes.classes.ConnectionDetector;
import com.app.ramzaanphotoframes.classes.ImagePath_MarshMallow;
import com.app.ramzaanphotoframes.landscape_module.adapters.Frames_Adapter;
import com.app.ramzaanphotoframes.recycler_click_listener.ClickListener;
import com.app.ramzaanphotoframes.recycler_click_listener.RecyclerTouchListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class Frames_Activity_lpf extends AppCompatActivity {

    private int frame_items[] = {R.drawable.lf1, R.drawable.lf2,
            R.drawable.lf3, R.drawable.lf4, R.drawable.lf5,
            R.drawable.lf6, R.drawable.lf7, R.drawable.lf8,
            R.drawable.lf9, R.drawable.lf10, R.drawable.lf11,
            R.drawable.lf12, R.drawable.lf13, R.drawable.lf14,
            R.drawable.lf15, R.drawable.lf16, R.drawable.lf17,
            R.drawable.lf18,R.drawable.lf19,R.drawable.lf20};
    private Dialog alertDialog;

    private int camera_ReqCode = 121, gallery_ReqCode = 212;
    private boolean permission = false;
    private final int CAMERA_RESULT = 101, GALLERY_RESULT = 102;
    private Uri capturedFileUri;
    private String getImageUrl = "";
    private int selectedFramePosition = 0;

    private ConnectionDetector cd;
    private boolean isInternetPresent;
    private AdView adView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frames);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        SpannableStringBuilder title = new SpannableStringBuilder(getResources().getString(R.string.frames));
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Frames_Activity_lpf.this.finish();
            }
        });

        RecyclerView frames_recycle_view = (RecyclerView) findViewById(R.id.frames_recycle_view);
        frames_recycle_view.setHasFixedSize(true);
        frames_recycle_view.setLayoutManager(new GridLayoutManager(Frames_Activity_lpf.this, 2));
        Frames_Adapter custom_adapter = new Frames_Adapter(Frames_Activity_lpf.this, frame_items);
        frames_recycle_view.setAdapter(custom_adapter);

        frames_recycle_view.addOnItemTouchListener(new RecyclerTouchListener(Frames_Activity_lpf.this,
                frames_recycle_view, new ClickListener() {
            @Override
            public void onClick(View view, final int position) {

                selectedFramePosition = position;

                alertDialog = new Dialog(Frames_Activity_lpf.this);
                alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                alertDialog.setContentView(R.layout.imageoption);
                alertDialog.setCancelable(false);

                ImageView imageView_close = (ImageView) alertDialog.findViewById(R.id.imageView_close);
                imageView_close.setColorFilter(Color.parseColor("#fad375"));
                imageView_close.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                ImageView gallry = (ImageView) alertDialog.findViewById(R.id.imageView_gallery);
                // gallry.setColorFilter(Color.parseColor("#4d3b53"));

                ImageView camera = (ImageView) alertDialog.findViewById(R.id.imageView_camera);
                //camera.setColorFilter(Color.parseColor("#4d3b53"));


                gallry.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(View view) {
                        if (ContextCompat.checkSelfPermission(Frames_Activity_lpf.this, READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                                ContextCompat.checkSelfPermission(Frames_Activity_lpf.this, WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                            GalleryPictureIntent();

                        } else {
                            if (shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE)) {
                                //Toast.makeText(getApplicationContext(), "Permission Needed.", Toast.LENGTH_LONG).show();
                            }
                            requestPermissions(new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE}, GALLERY_RESULT);
                        }
                        alertDialog.dismiss();
                    }
                });

                camera.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(View view) {
                        if (ContextCompat.checkSelfPermission(Frames_Activity_lpf.this, Manifest.permission.CAMERA) ==
                                PackageManager.PERMISSION_GRANTED) {
                            dispatchTakenPictureIntent();

                        } else {
                            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                                //Toast.makeText(getApplicationContext(), "Permission Needed.", Toast.LENGTH_LONG).show();
                            }
                            requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_RESULT);
                        }
                        alertDialog.dismiss();
                    }
                });


                alertDialog.show();
                Display display1 = ((WindowManager) getSystemService(Frames_Activity_lpf.WINDOW_SERVICE)).getDefaultDisplay();
                int width1 = display1.getWidth();
                int height1 = display1.getHeight();
                alertDialog.getWindow().setLayout((width1 - width1 / 5), (int) (height1 * 30) / 100);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


        cd = new ConnectionDetector(this);
        isInternetPresent = cd.isConnectingToInternet();

        if (isInternetPresent) {
            displayAd();
        }

    }

    private void dispatchTakenPictureIntent() {
        if (Build.VERSION.SDK_INT >= 24) {
            try {
                StrictMode.class.getMethod("disableDeathOnFileUriExposure", new Class[0]).invoke(null, new Object[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        capturedFileUri = getOutputMediaFileUri(Frames_Activity_lpf.this);
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra("output", capturedFileUri);
        startActivityForResult(intent, camera_ReqCode);
    }

    private void GalleryPictureIntent() {
        try {
            startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), gallery_ReqCode);
        } catch (Exception e) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), gallery_ReqCode);
        }
    }


    private Uri getOutputMediaFileUri(Context context) {
        File mediaStorageDir = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "Camera");
        if (!mediaStorageDir.exists() && mediaStorageDir.mkdir()) {
            Log.e("Create Directory", "Main Directory Created : " + mediaStorageDir);
        }
        return Uri.fromFile(new File(mediaStorageDir.getPath() + File.separator + "IMG_" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date()) + ".jpg"));
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_RESULT) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakenPictureIntent();
            } else {
                //Toast.makeText(getApplicationContext(), "Permission Needed.", Toast.LENGTH_LONG).show();
                permssiondialog();
            }
        }
        if (requestCode == GALLERY_RESULT) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                GalleryPictureIntent();
            } else {
                // Toast.makeText(getApplicationContext(), "Permission Needed.", Toast.LENGTH_LONG).show();
                permssiondialog();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void permssiondialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Frames_Activity_lpf.this);
        builder.setCancelable(false);
        builder.setTitle("App requires Storage permissions to work perfectly..!");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                permission = true;
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == gallery_ReqCode) {
            cropImage(data.getData());
        } else if (requestCode == camera_ReqCode && resultCode == RESULT_OK) {
            if (Build.VERSION.SDK_INT > 22) {
                this.getImageUrl = ImagePath_MarshMallow.getPath(this, this.capturedFileUri);
            } else {
                this.getImageUrl = this.capturedFileUri.getPath();
            }
            cropImage(this.capturedFileUri);
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == -1) {
                Uri resultUri = result.getUri();
                try {
                    Intent intent = new Intent(Frames_Activity_lpf.this, Editing_Activity_lpf.class);
                    intent.putExtra("imageUri", resultUri.toString());
                    intent.putExtra("selectedFramePosition", this.selectedFramePosition);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                result.getError();
            }
        }
    }

    private void cropImage(Uri capturedFileUri) {
        CropImage.activity(capturedFileUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
       /* CropImage.activity(capturedFileUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1,1)
                .start(this);*/

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
