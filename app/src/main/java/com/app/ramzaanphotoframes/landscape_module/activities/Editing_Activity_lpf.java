package com.app.ramzaanphotoframes.landscape_module.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.ramzaanphotoframes.R;
import com.app.ramzaanphotoframes.activities.AddTextActivity;
import com.app.ramzaanphotoframes.activities.FilterActivity;
import com.app.ramzaanphotoframes.classes.ClipArt;
import com.app.ramzaanphotoframes.classes.ConnectionDetector;
import com.app.ramzaanphotoframes.colorPicker.ColorPickerDialog;
import com.app.ramzaanphotoframes.landscape_module.adapters.Image_Adapter;
import com.app.ramzaanphotoframes.recycler_click_listener.ClickListener;
import com.app.ramzaanphotoframes.recycler_click_listener.RecyclerTouchListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Editing_Activity_lpf extends AppCompatActivity implements View.OnClickListener {

    private int screenWidth, screenHeight;
    private RelativeLayout root_main_layout;
    private int frame_items[] = {R.drawable.lf1, R.drawable.lf2,
            R.drawable.lf3, R.drawable.lf4, R.drawable.lf5,
            R.drawable.lf6, R.drawable.lf7, R.drawable.lf8,
            R.drawable.lf9, R.drawable.lf10, R.drawable.lf11,
            R.drawable.lf12, R.drawable.lf13, R.drawable.lf14,
            R.drawable.lf15, R.drawable.lf16, R.drawable.lf17,
            R.drawable.lf18,R.drawable.lf19,R.drawable.lf20};

    private RelativeLayout options_layout, frame_img_view_layout;
    private RecyclerView frames_recycler_view;
    private ImageView user_img_view;

    private Uri myUri;
    private int selectedFramePosition;
    private Bitmap bitmap;


    ///////////////  Text Layout Variable
    private AlertDialog.Builder textdialog;
    private AlertDialog textDialogAlert;
    private EditText editText;
    private Button theButton;
    public  static  Bitmap textBitmap;
    private RelativeLayout colorLayout, fontLayout, editTextLayout;
    private LinearLayout textStyleLayout;
    private String[] font_styles = {"font/hellofont6.otf", "font/font1.ttf", "font/f1.ttf",
            "font/hellofont3.TTF", "font/f2.ttf", "font/f3.ttf", "font/f4.ttf", "font/f5.ttf",
            "font/hellofont4.otf", "font/hellofont5.ttf", "font/hellofont1.ttf", "font/hellofont7.TTF",
            "font/hellofont8.ttf", "font/hellofont9.ttf", "font/hellofont10.ttf", "font/hellofont11.otf",
            "font/hellofont12.ttf", "font/hellofont13.ttf", "font/hellofont14.otf", "font/hellofont15.ttf",
            "font/hellofont17.ttf", "font/hellofont18.otf", "font/hellofont19.ttf", "font/hellofont20.ttf"};


    private String colorcodes[] = {"#293375", "#4f70b0", "#489ebf", "#87ccde",
            "#a4cde3", "#ef5a24", "#f5911e", "#000000", "#333333", "#65b200"};

    private ArrayList<Typeface> font_styles_List = new ArrayList<>();
    //    private String[] colorCodes;
    private ImageView imgColorPicker;
    //    TextView txtCancel, txtDone;
    private RecyclerView fontRecyclerView, colorRecyclerView;
    private RelativeLayout colorPicker;
    private int colorr = Color.BLACK;
    private Typeface savedTypeface;
    private int savedColor, selectedfontstyle = Typeface.NORMAL, preTypeFaceSelect = 8;
    private BitmapFactory.Options opts;
    private Button btnBold, btnNormal, btnItalic;


    //Gallery Code
    private int camera_ReqCode = 121, gallery_ReqCode = 212;

    //save code
    private String root;
    private File MyDir, distLocation;
    private String image_Name;
    private Context context;

    private ConnectionDetector cd;
    private boolean isInternetPresent;
    private AdView adView;
    private InterstitialAd mInterstitialAd;

    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_editing_lpf);

        cd = new ConnectionDetector(this);
        isInternetPresent = cd.isConnectingToInternet();

        if (isInternetPresent) {

            mInterstitialAd_setup();
        }


        context = this;
        root = Environment.getExternalStorageDirectory().toString();
        root += File.separator + getString(R.string.app_name)+"landscape";
        MyDir = new File(root);
        if (!MyDir.isDirectory())
            MyDir.mkdirs();


        screenWidth = getResources().getDisplayMetrics().widthPixels;
        screenHeight = getResources().getDisplayMetrics().heightPixels;

        root_main_layout = (RelativeLayout) findViewById(R.id.root_main_layout);

        options_layout = (RelativeLayout) findViewById(R.id.options_layout);
        options_layout.getLayoutParams().width = (screenWidth * 10) / 100;
        options_layout.getBackground().setAlpha(180);
        options_layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });


        if (getIntent().getExtras().getString("imageUri") != null) {
            this.myUri = Uri.parse(getIntent().getExtras().getString("imageUri"));
        }
        this.selectedFramePosition = getIntent().getExtras().getInt("selectedFramePosition");

        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), myUri);
        } catch (IOException e) {
            e.printStackTrace();
        }


        initialization_layout();
        initialization_img_vews();
        initialization_recycler_view();
        textLayoutIniti();
        frame_img_view_layout = (RelativeLayout) findViewById(R.id.frame_img_view_layout);
        frame_img_view_layout.setBackgroundResource(frame_items[selectedFramePosition]);

        user_img_view = findViewById(R.id.user_img_view);
        Touch touch = new Touch();
        user_img_view.setImageMatrix(touch.getMatrix());
        user_img_view.setOnTouchListener(touch);
        user_img_view.setImageBitmap(bitmap);

    }

    private void initialization_layout() {
        RelativeLayout gallery_layout = (RelativeLayout) findViewById(R.id.gallery_layout);
        gallery_layout.getLayoutParams().height = (screenHeight * 20) / 100;
        gallery_layout.getLayoutParams().width = (screenWidth / 5);
        gallery_layout.setOnClickListener(this);

        RelativeLayout frames_layout = (RelativeLayout) findViewById(R.id.frames_layout);
        frames_layout.getLayoutParams().height = (screenHeight * 20) / 100;
        frames_layout.getLayoutParams().width = (screenWidth / 5);
        frames_layout.setOnClickListener(this);

        RelativeLayout effect_layout = (RelativeLayout) findViewById(R.id.effect_layout);
        effect_layout.getLayoutParams().height = (screenHeight * 20) / 100;
        effect_layout.getLayoutParams().width = (screenWidth / 5);
        effect_layout.setOnClickListener(this);

        RelativeLayout text_layout = (RelativeLayout) findViewById(R.id.text_layout);
        text_layout.getLayoutParams().height = (screenHeight * 20) / 100;
        text_layout.getLayoutParams().width = (screenWidth / 5);
        text_layout.setOnClickListener(this);


        RelativeLayout save_layout = (RelativeLayout) findViewById(R.id.save_layout);
        save_layout.getLayoutParams().height = (screenHeight * 20) / 100;
        save_layout.getLayoutParams().width = (screenWidth / 5);
        save_layout.setOnClickListener(this);
    }

    private void initialization_img_vews() {
        ImageView gallery_button = (ImageView) findViewById(R.id.gallery_button);
        gallery_button.getLayoutParams().height = (screenWidth / 15);
        gallery_button.getLayoutParams().width = (screenWidth / 15);

        ImageView frames_button = (ImageView) findViewById(R.id.frames_button);
        frames_button.getLayoutParams().height = (screenWidth / 15);
        frames_button.getLayoutParams().width = (screenWidth / 15);

        ImageView effect_button = (ImageView) findViewById(R.id.effect_button);
        effect_button.getLayoutParams().height = (screenWidth / 15);
        effect_button.getLayoutParams().width = (screenWidth / 15);

        ImageView text_button = (ImageView) findViewById(R.id.text_button);
        text_button.getLayoutParams().height = (screenWidth / 15);
        text_button.getLayoutParams().width = (screenWidth / 15);

        ImageView save_button = (ImageView) findViewById(R.id.save_button);
        save_button.getLayoutParams().height = (screenWidth / 15);
        save_button.getLayoutParams().width = (screenWidth / 15);
    }

    private void initialization_recycler_view() {

        frames_recycler_view = (RecyclerView) findViewById(R.id.frames_recycler_view);
        frames_recycler_view.getBackground().setAlpha(180);
        frames_recycler_view.setHasFixedSize(true);
        frames_recycler_view.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));

        Image_Adapter image_adapter = new Image_Adapter(Editing_Activity_lpf.this, frame_items);
        frames_recycler_view.setAdapter(image_adapter);

        frames_recycler_view.addOnItemTouchListener(new RecyclerTouchListener(Editing_Activity_lpf.this,
                frames_recycler_view, new ClickListener() {
            @Override
            public void onClick(View view, int position) {

                frame_img_view_layout.setBackgroundResource(frame_items[position]);
                frames_recycler_view.setVisibility(View.GONE);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    private void textLayoutIniti() {
        textdialog = new AlertDialog.Builder(Editing_Activity_lpf.this);
        LayoutInflater lf = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View vv = lf.inflate(R.layout.text_dialoge, null);
        textdialog.setView(vv);
        textdialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        textdialog.setPositiveButton("Done", null);

        editTextLayout = (RelativeLayout) vv.findViewById(R.id.editTextLayout);
        textStyleLayout = (LinearLayout) vv.findViewById(R.id.textStyleLayout);
        fontLayout = (RelativeLayout) vv.findViewById(R.id.fontLayout);
        colorLayout = (RelativeLayout) vv.findViewById(R.id.colorLayout);

        editTextLayout.getLayoutParams().width = (int) (screenWidth - screenWidth / 2.5);
        editTextLayout.getLayoutParams().height = screenHeight / 8;
        textStyleLayout.getLayoutParams().height = screenHeight / 8;
        fontLayout.getLayoutParams().height = screenHeight / 8;
        colorLayout.getLayoutParams().height = screenHeight / 8;

        fontRecyclerView = (RecyclerView) vv.findViewById(R.id.fontRecyclerView);
        fontRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,
                StaggeredGridLayoutManager.HORIZONTAL));
        fontRecyclerView.setItemViewCacheSize(font_styles.length);

        for (int i = 0; i < font_styles.length; i++) {
            Typeface tf1 = Typeface.createFromAsset(getAssets(), font_styles[i]);
            font_styles_List.add(tf1);

        }
        TextStyleRecyclerAdapter textStyleRecyclerAdapter = new TextStyleRecyclerAdapter();
        fontRecyclerView.setAdapter(textStyleRecyclerAdapter);

        editText = (EditText) vv.findViewById(R.id.editText);

        colorRecyclerView = (RecyclerView) vv.findViewById(R.id.colorRecyclerView);
        colorRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,
                StaggeredGridLayoutManager.HORIZONTAL));
        colorRecyclerView.setItemViewCacheSize(colorcodes.length);

        TextColorRecyclerAdapter textColorRecyclerAdapter = new TextColorRecyclerAdapter();
        colorRecyclerView.setAdapter(textColorRecyclerAdapter);

        imgColorPicker = (ImageView) vv.findViewById(R.id.imgColorPicker);
        imgColorPicker.getLayoutParams().height = screenWidth / 12;
        imgColorPicker.getLayoutParams().width = screenWidth / 14;
        imgColorPicker.setOnClickListener(this);


        btnBold = (Button) vv.findViewById(R.id.btnBold);
        btnNormal = (Button) vv.findViewById(R.id.btnNormal);
        btnItalic = (Button) vv.findViewById(R.id.btnItalic);
        btnBold.setOnClickListener(this);
        btnNormal.setOnClickListener(this);
        btnItalic.setOnClickListener(this);
        textDialogAlert = textdialog.create();

        textDialogAlert.setCanceledOnTouchOutside(false);
    }

    @Override
    public void onClick(View v) {
        disableall();
        switch (v.getId()) {
            case R.id.gallery_layout:

                frames_recycler_view.setVisibility(View.GONE);
                try {
                    startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), gallery_ReqCode);
                } catch (Exception e) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), gallery_ReqCode);
                }


                break;

            case R.id.frames_layout:

                if (frames_recycler_view.getVisibility() == View.GONE) {
                    frames_recycler_view.setVisibility(View.VISIBLE);


                } else {

                    frames_recycler_view.setVisibility(View.GONE);
                }

                break;

            case R.id.effect_layout:

                Intent intent = new Intent(this, FilterActivity.class);
                intent.putExtra("image", myUri.toString());
                startActivityForResult(intent, 3);

                break;


            case R.id.save_layout:
                frames_recycler_view.setVisibility(View.GONE);
                save();

                break;

            case R.id.text_layout:
                frames_recycler_view.setVisibility(View.GONE);
                textClickAction();
                break;
            case R.id.imgColorPicker:
                colorpicker();
                break;
            case R.id.btnBold:
                if (editText.getText().toString().length() > 0) {
                    btnBold.setBackgroundColor(getResources().getColor(R.color.white));
                    btnNormal.setBackgroundColor(getResources().getColor(R.color.black));
                    btnItalic.setBackgroundColor(getResources().getColor(R.color.black));

                    btnBold.setTextColor(getResources().getColor(R.color.black));
                    btnNormal.setTextColor(getResources().getColor(R.color.white));
                    btnItalic.setTextColor(getResources().getColor(R.color.white));
                    selectedfontstyle = Typeface.BOLD;

                    editText.setTypeface(Typeface.createFromAsset(getAssets(),
                            font_styles[preTypeFaceSelect]), selectedfontstyle);
                } else
                    Toast.makeText(Editing_Activity_lpf.this, "Enter text...", Toast.LENGTH_SHORT).show();
                break;

            case R.id.btnNormal:
                if (editText.getText().toString().length() > 0) {
                    btnBold.setBackgroundColor(getResources().getColor(R.color.black));
                    btnNormal.setBackgroundColor(getResources().getColor(R.color.white));
                    btnItalic.setBackgroundColor(getResources().getColor(R.color.black));

                    btnBold.setTextColor(getResources().getColor(R.color.white));
                    btnNormal.setTextColor(getResources().getColor(R.color.black));
                    btnItalic.setTextColor(getResources().getColor(R.color.white));
                    selectedfontstyle = Typeface.NORMAL;

                    editText.setTypeface(Typeface.createFromAsset(getAssets(),
                            font_styles[preTypeFaceSelect]), selectedfontstyle);
                } else
                    Toast.makeText(Editing_Activity_lpf.this, "Enter text...", Toast.LENGTH_SHORT).show();
                break;

            case R.id.btnItalic:
                if (editText.getText().toString().length() > 0) {
                    btnBold.setBackgroundColor(getResources().getColor(R.color.black));
                    btnNormal.setBackgroundColor(getResources().getColor(R.color.black));
                    btnItalic.setBackgroundColor(getResources().getColor(R.color.white));

                    btnBold.setTextColor(getResources().getColor(R.color.white));
                    btnNormal.setTextColor(getResources().getColor(R.color.white));
                    btnItalic.setTextColor(getResources().getColor(R.color.black));
                    selectedfontstyle = Typeface.ITALIC;

                    editText.setTypeface(Typeface.createFromAsset(getAssets(),
                            font_styles[preTypeFaceSelect]), selectedfontstyle);
                } else
                    Toast.makeText(Editing_Activity_lpf.this, "Enter text...", Toast.LENGTH_SHORT).show();
                break;
        }

    }

    private void textClickAction() {
        /*textDialogAlert.show();
        theButton = textDialogAlert.getButton(DialogInterface.BUTTON_POSITIVE);
        theButton.setOnClickListener(new PositiveButtonClick(textDialogAlert));*/
        Intent intent = new Intent(Editing_Activity_lpf.this, AddTextActivity.class);
        startActivityForResult(intent,100);
    }


    public class PositiveButtonClick implements View.OnClickListener {
        Dialog dialog;
        public PositiveButtonClick(Dialog dialog) {
            this.dialog = dialog;
        }
        @Override
        public void onClick(View v) {
            if (editText.getText().toString().length() > 0) {
                addTextToLayout();
                dialog.dismiss();
            } else {
                Toast.makeText(Editing_Activity_lpf.this, "Enter text...", Toast.LENGTH_SHORT).show();
            }

        }
    }
    //color picker
    private void colorpicker() {
        ColorPickerDialog dialog = new ColorPickerDialog(this, colorr,
                new ColorPickerDialog.OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int color) {
                        colorr = color;
                        savedColor = color;
                        editText.setTextColor(colorr);
                    }
                    @Override
                    public void onColorSelected(int color, Boolean selected) {

                    }
                });
        dialog.show();
    }

    public void addTextToLayout() {
        Bitmap tempbitmap;
        String userInputString = editText.getText().toString();
        if (userInputString.length() == 0)
            Toast.makeText(Editing_Activity_lpf.this, "Enter text...", Toast.LENGTH_SHORT).show();
        else {
            tempbitmap = textAsBitmap(userInputString, colorr, editText.getTypeface());
            ClipArt art = new ClipArt(Editing_Activity_lpf.this, tempbitmap, screenWidth, screenHeight);
            disableall();
            art.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    disableall();
                }
            });
            root_main_layout.addView(art);
        }

    }

    public Bitmap textAsBitmap(String text, int textColor, Typeface typeface1) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(20);
        paint.setColor(textColor);
        paint.setTypeface(typeface1);
        paint.setTextAlign(Paint.Align.LEFT);
        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(text) + 0.5f); // round
        int height = (int) (baseline + paint.descent() + 0.5f);
        Bitmap image = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 0, baseline, paint);
        return image;
    }

    public  void disableall() {
        for (int i = 0; i < root_main_layout.getChildCount(); i++) {
            if (root_main_layout.getChildAt(i) instanceof ClipArt) {
                ((ClipArt) root_main_layout.getChildAt(i)).disableAll();
            }
        }
    }


    private void save() {
        root_main_layout.setDrawingCacheEnabled(true);
        Bitmap saveBitmap = root_main_layout.getDrawingCache();
        image_Name = "Image-" + new SimpleDateFormat("ddMMyy_HHmmss").format(Calendar
                .getInstance().getTime()) + ".jpg";


        distLocation = new File(MyDir, image_Name);
        if (distLocation.exists()) {
            distLocation.delete();
            Log.e("msg", "if statement");
        } else {

            Log.e("msg", "else statement");
            try {
                // file.createNewFile();
                Log.e("msg", "try statement");
                FileOutputStream out = new FileOutputStream(distLocation);

                saveBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                Toast.makeText(Editing_Activity_lpf.this, "Image saved successfully", Toast.LENGTH_SHORT).show();
                out.flush();
                root_main_layout.setDrawingCacheEnabled(false);
                addImageToGallery(distLocation.getAbsolutePath(), context);

                Intent intent = new Intent(context, Share_Activity_lpf.class);
                intent.putExtra("final_image_path", distLocation.getAbsolutePath());
                startActivity(intent);

                if (isInternetPresent) {
                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                    } else {
                        Log.d("TAG", "The interstitial wasn't loaded yet.");
                    }
                }

                Log.e("msg", "try intent statement");
                out.close();

            } catch (Exception e) {

                e.printStackTrace();
            }
        }
    }


    private void addImageToGallery(String absolutePath, Context context) {

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.MediaColumns.DATA, absolutePath);
        context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                values);
    }

    public class TextColorRecyclerAdapter extends RecyclerView.Adapter<TextColorRecyclerAdapter.MainRViewHolder> {
        ImageView tmpImageView = null;
        public TextColorRecyclerAdapter() {

        }
        @Override
        public MainRViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View rowView = null;
            try {
                rowView = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.font_color_adapter, parent, false);
            } catch (Exception e) {
                Log.e("Info", "Info ==>" + e.getMessage());
            }
            return new TextColorRecyclerAdapter.MainRViewHolder(rowView);
        }
        @Override
        public void onBindViewHolder(final MainRViewHolder holder, final int position) {
            holder.rectangleImg.getLayoutParams().height = screenWidth / 12;
            holder.rectangleImg.getLayoutParams().width = screenWidth / 14;
            holder.rectangleImg.getBackground().setColorFilter(Color.parseColor(colorcodes[position]), PorterDuff.Mode.MULTIPLY);
            holder.rectangleImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String str = colorcodes[position];
                    colorr = Color.parseColor(str);

                    editText.setTextColor(colorr);
                }
            });
        }
        @Override
        public int getItemCount() {
            return colorcodes.length;
        }

        public class MainRViewHolder extends RecyclerView.ViewHolder {

            Button rectangleImg;
            RelativeLayout colorAdapterLayout;

            public MainRViewHolder(View itemView) {
                super(itemView);
                rectangleImg = (Button) itemView.findViewById(R.id.rectangleImg);
                colorAdapterLayout = (RelativeLayout) itemView.findViewById(R.id.colorAdapterLayout);
                colorAdapterLayout.getLayoutParams().height = screenHeight / 7;


            }
        }
    }

    public class TextStyleRecyclerAdapter extends RecyclerView.Adapter<TextStyleRecyclerAdapter.MainRViewHolder> {

        ImageView tmpImageView = null;

        public TextStyleRecyclerAdapter() {

        }

        @Override
        public MainRViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View rowView = null;
            try {
                rowView = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.font_style_adapter, parent, false);
            } catch (Exception e) {
                Log.e("Info", "Info ==>" + e.getMessage());
            }
            return new TextStyleRecyclerAdapter.MainRViewHolder(rowView);
        }

        @Override
        public void onBindViewHolder(final MainRViewHolder holder, final int position) {

            holder.fontStyleTextView.setTypeface(font_styles_List.get(position));

            holder.fontStyleTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    preTypeFaceSelect = position;
                    editText.setTypeface(font_styles_List.get(position), selectedfontstyle);
                }
            });
        }


        @Override
        public int getItemCount() {

            return font_styles_List.size();
        }

        public class MainRViewHolder extends RecyclerView.ViewHolder {

            TextView fontStyleTextView;
            RelativeLayout textAdapterLayout;

            public MainRViewHolder(View itemView) {
                super(itemView);
                fontStyleTextView = (TextView) itemView.findViewById(R.id.fontStyleTextView);
                textAdapterLayout = (RelativeLayout) itemView.findViewById(R.id.textAdapterLayout);
                textAdapterLayout.getLayoutParams().height = screenHeight / 7;


            }
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == gallery_ReqCode) {
            cropImage(data.getData());
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == -1) {
                myUri = result.getUri();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), myUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                user_img_view.setImageBitmap(bitmap);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                result.getError();
            }
        }
        if(resultCode == RESULT_OK && requestCode == 100){
            ClipArt art = new ClipArt(Editing_Activity_lpf.this, textBitmap, screenWidth, screenHeight);
            disableall();
            art.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    disableall();
                }
            });
            root_main_layout.addView(art);
        }
        if (resultCode == 3) {
            Uri parse;
            parse = Uri.parse(data.getStringExtra("ImageUri"));
            Log.e("msg", "re" + parse);
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), parse);
            } catch (IOException e) {
                e.printStackTrace();
            }
            user_img_view.setImageBitmap(bitmap);
        }


    }


    private void cropImage(Uri capturedFileUri) {
        CropImage.activity(capturedFileUri).setGuidelines(CropImageView.Guidelines.ON).start(this);
    }



    public void exit_alert() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(
                Editing_Activity_lpf.this);
        mBuilder.setIcon(R.drawable.appicon);
        mBuilder.setTitle("Confirm");
        mBuilder.setMessage("Do you want exit?");
        mBuilder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Editing_Activity_lpf.this.finish();
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

    @Override
    public void onBackPressed() {
        exit_alert();
    }

    public class Touch implements View.OnTouchListener {
        // We can be in one of these 3 states
        final int NONE = 0;
        final int DRAG = 1;
        final int ZOOM = 2;
        // These matrices will be used to move and zoom image
        Matrix matrix = new Matrix();
        Matrix savedMatrix = new Matrix();
        int mode = NONE;
        // Remember some things for zooming
        PointF start = new PointF();
        PointF mid = new PointF();
        float oldDist = 1f;
        float[] lastEvent = null;
        float d = 0f;
        float newRot = 0f;

        public Matrix getMatrix() {
            matrix.reset();
            return matrix;
        }

        public boolean onTouch(View v, MotionEvent event) {

            ImageView view = (ImageView) v;
            view.setScaleType(ImageView.ScaleType.MATRIX);
            // Dump touch event to log
            dumpEvent(event);
            disableall();

            // Handle touch events here...
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    savedMatrix.set(matrix);
                    start.set(event.getX(), event.getY());

                    mode = DRAG;
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    oldDist = spacing(event);
                    if (oldDist > 10f) {
                        savedMatrix.set(matrix);
                        midPoint(mid, event);
                        mode = ZOOM;

                    }


                    lastEvent = new float[4];
                    lastEvent[0] = event.getX(0);
                    lastEvent[1] = event.getX(1);
                    lastEvent[2] = event.getY(0);
                    lastEvent[3] = event.getY(1);
                    d = rotation(event);

                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP:
                    mode = NONE;

                    break;
                case MotionEvent.ACTION_MOVE:
                    if (mode == DRAG) {
                        // ...
                        matrix.set(savedMatrix);
                        matrix.postTranslate(event.getX() - start.x,
                                event.getY() - start.y);
                    } else if (mode == ZOOM) {
                        float newDist = spacing(event);

                        if (newDist > 10f) {
                            matrix.set(savedMatrix);
                            float scale = newDist / oldDist;
                            matrix.postScale(scale, scale, mid.x, mid.y);
                        }

                        if (lastEvent != null) {
                            newRot = rotation(event);
                            float r1 = newRot - d;
                            matrix.postRotate(r1, view.getMeasuredWidth() / 2, view.getMeasuredHeight() / 2);
                        }
                    }


                    break;
            }

            view.setImageMatrix(matrix);
            return true; // indicate event was handled
        }

        private float rotation(MotionEvent event) {
            double delta_x = (event.getX(0) - event.getX(1));
            double delta_y = (event.getY(0) - event.getY(1));
            double radians = Math.atan2(delta_y, delta_x);
            return (float) Math.toDegrees(radians);
        }

        /**
         * Show an event in the LogCat view, for debugging
         */
        @SuppressWarnings("deprecation")
        private void dumpEvent(MotionEvent event) {
            String names[] = {"DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE",
                    "POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?"};
            StringBuilder sb = new StringBuilder();
            int action = event.getAction();
            int actionCode = action & MotionEvent.ACTION_MASK;
            sb.append("event ACTION_").append(names[actionCode]);
            if (actionCode == MotionEvent.ACTION_POINTER_DOWN
                    || actionCode == MotionEvent.ACTION_POINTER_UP) {
                sb.append("(pid ").append(
                        action >> MotionEvent.ACTION_POINTER_ID_SHIFT);
                sb.append(")");
            }
            sb.append("[");
            for (int i = 0; i < event.getPointerCount(); i++) {
                sb.append("#").append(i);
                sb.append("(pid ").append(event.getPointerId(i));
                sb.append(")=").append((int) event.getX(i));
                sb.append(",").append((int) event.getY(i));
                if (i + 1 < event.getPointerCount())
                    sb.append(";");
            }
            sb.append("]");

        }

        /**
         * Determine the space between the first two fingers
         */
        @SuppressLint("FloatMath")
        private float spacing(MotionEvent event) {
            float x = event.getX(0) - event.getX(1);
            float y = event.getY(0) - event.getY(1);
            return (float) Math.sqrt(x * x + y * y);
        }

        /**
         * Calculate the mid point of the first two fingers
         */
        private void midPoint(PointF point, MotionEvent event) {
            float x = event.getX(0) + event.getX(1);
            float y = event.getY(0) + event.getY(1);
            point.set(x / 2, y / 2);
        }
    }


    private void mInterstitialAd_setup() {
        mInterstitialAd = new InterstitialAd(Editing_Activity_lpf.this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.interstitial_Ad_id_save));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
    }
}
