package com.app.ramzaanphotoframes.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.app.ramzaanphotoframes.R;
import com.app.ramzaanphotoframes.adapters.FilterImageAdapter;
import com.app.ramzaanphotoframes.twoway.TwoWayAdapterView;
import com.app.ramzaanphotoframes.twoway.TwoWayGridView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageToneCurveFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageView;

public class FilterActivity extends AppCompatActivity {

    private GPUImageView img;
    private ImageView img_back_filter;

    private RelativeLayout ZoomPan_Layout;

    private TwoWayGridView mImageGrid;
    private TwoWayGridView mPackGrid;

    private String[] mFilterPack1 = {"pack1_1.acv", "pack1_2.acv", "pack1_3.acv", "pack1_4.acv", "pack1_5.acv", "pack1_6.acv", "pack1_7.acv", "pack1_8.acv", "pack1_9.acv", "pack1_10.acv"};
    private String[] mFilterPack2 = {"pack2_1.acv", "pack2_2.acv", "pack2_3.acv", "pack2_4.acv", "pack2_5.acv", "pack2_6.acv", "pack2_7.acv", "pack2_8.acv", "pack2_9.acv", "pack2_10.acv"};
    private String[] mFilterPack3 = {"pack3_1.acv", "pack3_2.acv", "pack3_3.acv", "pack3_4.acv", "pack3_5.acv", "pack3_6.acv", "pack3_7.acv", "pack3_8.acv", "pack3_9.acv", "pack3_10.acv"};
    private String[] mFilterPack4 = {"pack4_1.acv", "pack4_2.acv", "pack4_3.acv", "pack4_4.acv", "pack4_5.acv", "pack4_6.acv", "pack4_7.acv", "pack4_8.acv", "pack4_9.acv", "pack4_10.acv"};
    private String[] mFilterPack5 = {"pack5_1.acv", "pack5_2.acv", "pack5_3.acv", "pack5_4.acv", "pack5_5.acv", "pack5_6.acv", "pack5_7.acv", "pack5_8.acv", "pack5_9.acv", "pack5_10.acv"};
    private String[] mFilterPack6 = {"pack6_1.acv", "pack6_2.acv", "pack6_3.acv", "pack6_4.acv", "pack6_5.acv", "pack6_6.acv", "pack6_7.acv", "pack6_8.acv", "pack6_9.acv", "pack6_10.acv"};
    private String[] mFilterPack7 = {"pack7_1.acv", "pack7_2.acv", "pack7_3.acv", "pack7_4.acv", "pack7_5.acv", "pack7_6.acv", "pack7_7.acv", "pack7_8.acv", "pack7_9.acv", "pack7_10.acv"};
    private String[] mFilterPack8 = {"pack8_1.acv", "pack8_2.acv", "pack8_3.acv", "pack8_4.acv", "pack8_5.acv", "pack8_6.acv", "pack8_7.acv", "pack8_8.acv", "pack8_9.acv", "pack8_10.acv", "pack8_11.acv", "pack8_12.acv"};

    private int[] mPackIds = {R.drawable.thumb_pack_1_1, R.drawable.thumb_pack_2_1, R.drawable.thumb_pack_3_1, R.drawable.thumb_pack_4_1, R.drawable.thumb_pack_5_1, R.drawable.thumb_pack_6_1, R.drawable.thumb_pack_7_1, R.drawable.thumb_pack_8_1};
    private String[] mSelectedPack;
    private int[] mThumbIdsPack1 = {R.drawable.thumb_pack_1_1, R.drawable.thumb_pack_1_2, R.drawable.thumb_pack_1_3, R.drawable.thumb_pack_1_4, R.drawable.thumb_pack_1_5, R.drawable.thumb_pack_1_6, R.drawable.thumb_pack_1_7, R.drawable.thumb_pack_1_8, R.drawable.thumb_pack_1_9, R.drawable.thumb_pack_1_10};
    private int[] mThumbIdsPack2 = {R.drawable.thumb_pack_2_1, R.drawable.thumb_pack_2_2, R.drawable.thumb_pack_2_3, R.drawable.thumb_pack_2_4, R.drawable.thumb_pack_2_5, R.drawable.thumb_pack_2_6, R.drawable.thumb_pack_2_7, R.drawable.thumb_pack_2_8, R.drawable.thumb_pack_2_9, R.drawable.thumb_pack_2_10};
    public int[] mThumbIdsPack3 = {R.drawable.thumb_pack_3_1, R.drawable.thumb_pack_3_2, R.drawable.thumb_pack_3_3, R.drawable.thumb_pack_3_4, R.drawable.thumb_pack_3_5, R.drawable.thumb_pack_3_6, R.drawable.thumb_pack_3_7, R.drawable.thumb_pack_3_8, R.drawable.thumb_pack_3_9, R.drawable.thumb_pack_3_10};
    public int[] mThumbIdsPack4 = {R.drawable.thumb_pack_4_1, R.drawable.thumb_pack_4_2, R.drawable.thumb_pack_4_3, R.drawable.thumb_pack_4_4, R.drawable.thumb_pack_4_5, R.drawable.thumb_pack_4_6, R.drawable.thumb_pack_4_7, R.drawable.thumb_pack_4_8, R.drawable.thumb_pack_4_9, R.drawable.thumb_pack_4_10};
    public int[] mThumbIdsPack5 = {R.drawable.thumb_pack_5_1, R.drawable.thumb_pack_5_2, R.drawable.thumb_pack_5_3, R.drawable.thumb_pack_5_4, R.drawable.thumb_pack_5_5, R.drawable.thumb_pack_5_6, R.drawable.thumb_pack_5_7, R.drawable.thumb_pack_5_8, R.drawable.thumb_pack_5_9, R.drawable.thumb_pack_5_10};
    public int[] mThumbIdsPack6 = {R.drawable.thumb_pack_6_1, R.drawable.thumb_pack_6_2, R.drawable.thumb_pack_6_3, R.drawable.thumb_pack_6_4, R.drawable.thumb_pack_6_5, R.drawable.thumb_pack_6_6, R.drawable.thumb_pack_6_7, R.drawable.thumb_pack_6_8, R.drawable.thumb_pack_6_9, R.drawable.thumb_pack_6_10};
    public int[] mThumbIdsPack7 = {R.drawable.thumb_pack_7_1, R.drawable.thumb_pack_7_2, R.drawable.thumb_pack_7_3, R.drawable.thumb_pack_7_4, R.drawable.thumb_pack_7_5, R.drawable.thumb_pack_7_6, R.drawable.thumb_pack_7_7, R.drawable.thumb_pack_7_8, R.drawable.thumb_pack_7_9, R.drawable.thumb_pack_7_10};
    public int[] mThumbIdsPack8 = {R.drawable.thumb_pack_8_1, R.drawable.thumb_pack_8_2, R.drawable.thumb_pack_8_3, R.drawable.thumb_pack_8_4, R.drawable.thumb_pack_8_5, R.drawable.thumb_pack_8_6, R.drawable.thumb_pack_8_7, R.drawable.thumb_pack_8_8, R.drawable.thumb_pack_8_9, R.drawable.thumb_pack_8_10, R.drawable.thumb_pack_8_11, R.drawable.thumb_pack_8_12};

    private InputStream is = null;
    private GPUImageToneCurveFilter filter;

    Uri myUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        SpannableStringBuilder title = new SpannableStringBuilder(getResources().getString(R.string.filter));
        getSupportActionBar().setTitle(title);

        ZoomPan_Layout = (RelativeLayout) findViewById(R.id.ZoomPan_Layout);
        //ZoomPan_Layout.setOnTouchListener(new MultiTouchListener());
        if (getIntent().getExtras().getString("image") != null) {
            this.myUri = Uri.parse(getIntent().getExtras().getString("image"));
        }

        img = (GPUImageView) findViewById(R.id.img);
        img.setScaleType(GPUImage.ScaleType.CENTER_INSIDE);
        img.setImage(this.myUri);
        //img.setOnTouchListener(new MultiTouchListener());

        img_back_filter = (ImageView) findViewById(R.id.ic_back_filter);
        mImageGrid = (TwoWayGridView) findViewById(R.id.gridview);
        mPackGrid = (TwoWayGridView) findViewById(R.id.gridview1);
        initGrid();

        img_back_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TwoWayGridView twoWayGridView = mPackGrid;
                twoWayGridView.setVisibility(View.VISIBLE);
                twoWayGridView = mImageGrid;
                twoWayGridView.setVisibility(View.GONE);
                ImageView imageView = img_back_filter;
                imageView.setVisibility(View.VISIBLE);
            }
        });
    }

    private void initGrid() {
        mPackGrid.setAdapter(new FilterImageAdapter(this, mPackIds));

        mPackGrid.setOnItemClickListener(new TwoWayAdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(TwoWayAdapterView<?> parent, View view, int i, long id) {

                if (i == 0) {
                    mSelectedPack = mFilterPack1;
                    mImageGrid.setAdapter(new FilterImageAdapter(FilterActivity.this, mThumbIdsPack1));
                } else if (i == 1) {
                    mSelectedPack = mFilterPack2;
                    mImageGrid.setAdapter(new FilterImageAdapter(FilterActivity.this, mThumbIdsPack2));
                } else if (i == 2) {
                    mSelectedPack = mFilterPack3;
                    mImageGrid.setAdapter(new FilterImageAdapter(FilterActivity.this, mThumbIdsPack3));
                } else if (i == 3) {
                    mSelectedPack = mFilterPack4;
                    mImageGrid.setAdapter(new FilterImageAdapter(FilterActivity.this, mThumbIdsPack4));
                } else if (i == 4) {
                    mSelectedPack = mFilterPack5;
                    mImageGrid.setAdapter(new FilterImageAdapter(FilterActivity.this, mThumbIdsPack5));
                } else if (i == 5) {
                    mSelectedPack = mFilterPack6;
                    mImageGrid.setAdapter(new FilterImageAdapter(FilterActivity.this, mThumbIdsPack6));
                } else if (i == 6) {
                    mSelectedPack = mFilterPack7;
                    mImageGrid.setAdapter(new FilterImageAdapter(FilterActivity.this, mThumbIdsPack7));
                } else if (i == 7) {
                    mSelectedPack = mFilterPack8;
                    mImageGrid.setAdapter(new FilterImageAdapter(FilterActivity.this, mThumbIdsPack8));
                }
                TwoWayGridView twoWayGridView = mPackGrid;
                twoWayGridView.setVisibility(View.GONE);
                twoWayGridView = mImageGrid;
                twoWayGridView.setVisibility(View.VISIBLE);
                ImageView imageView = img_back_filter;
                imageView.setVisibility(View.VISIBLE);
            }
        });

        mImageGrid.setOnItemClickListener(new TwoWayAdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(TwoWayAdapterView<?> parent, View view, int position, long id) {
                filter = new GPUImageToneCurveFilter();
                try {
                    is = getApplicationContext().getAssets().open(mSelectedPack[position]);
                    filter.setFromCurveFileInputStream(is);

                    is.close();
                } catch (IOException e) {
                }
                img.setFilter(filter);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.filter_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Bitmap c;
        OutputStream outputStream = null;
        if (id == R.id.action_done) {
            Bitmap c2 = this.img.getGPUImage().getBitmapWithFilterApplied();
            Matrix matrix = new Matrix();
            try {
                int attributeInt = new ExifInterface(String.valueOf(myUri)).getAttributeInt("Orientation", 1);
                if (attributeInt == 6) {
                    matrix.postRotate(90.0f);
                } else if (attributeInt == 8) {
                    matrix.postRotate(270.0f);
                } else if (attributeInt == 3) {
                    matrix.postRotate(180.0f);
                }
                c = Bitmap.createBitmap(c2, 0, 0, c2.getWidth(), c2.getHeight(), matrix, true);
            } catch (Exception e3) {
                e3.printStackTrace();
                c = null;
            }
            if (c != null) {
                c2 = c;
            }
            String str2 = Environment.getExternalStorageDirectory() + File.separator + ".tempedit";
            File file2 = new File(str2);
            if (!file2.exists()) {
                file2.mkdir();
            }
            file2 = new File(str2 + File.separator + "temp.jpg");
            try {
                outputStream = new FileOutputStream(file2);
            } catch (FileNotFoundException e4) {
                e4.printStackTrace();
            }
            c2.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            try {
                outputStream.close();
            } catch (IOException e22) {
                e22.printStackTrace();
            }
            Log.e("msg", "" + file2.getAbsolutePath());

            Intent localIntent1 = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
            localIntent1.setData(Uri.fromFile(file2));
            sendBroadcast(localIntent1);

            Uri fromFile = Uri.fromFile(file2);
            Intent intent = new Intent();
            intent.putExtra("ImageUri", fromFile.toString());
            setResult(3, intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
