package com.app.ramzaanphotoframes.activities;

import java.util.ArrayList;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.app.ramzaanphotoframes.R;
import com.app.ramzaanphotoframes.adapters.ColorArrayAdapter;
import com.app.ramzaanphotoframes.adapters.FontAdapter;
import com.app.ramzaanphotoframes.landscape_module.activities.Editing_Activity_lpf;


public class AddTextActivity extends Activity {

	InputMethodManager inManager;
	Dialog dialog;
	ArrayList<String> fontList = new ArrayList<String>();
	LinearLayout fontLayout,colorLayout;
	
	GridView fontGrid,colorGrid;
	ImageView keyboard,fonts,colors,alignment,done,cancel;
	EditText editText;
	int height,width;
	ArrayList<Integer> colorsIntList;
	private String editTextString;
	private int color = Color.BLACK;
	private Typeface typeface;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_text);
		
		Display mDisplay = getWindowManager().getDefaultDisplay();
		this.width  = mDisplay.getWidth();
		this.height = mDisplay.getHeight();
		
		// getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		inManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		inManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
		
		addFonts();
		
		editText = (EditText) findViewById(R.id.edittext);
		editText.requestFocus();
		
		fontLayout = (LinearLayout) findViewById(R.id.lyfontlist);
		fontLayout.setVisibility(View.GONE);
		fontGrid = (GridView) findViewById(R.id.gvfontlist);
		fontGrid.setAdapter(new FontAdapter(AddTextActivity.this, fontList));
		
		colorLayout = (LinearLayout) findViewById(R.id.lycolorlist);
		colorLayout.setVisibility(View.GONE);
		colorGrid = (GridView) findViewById(R.id.gvcolorlist);
		
		colorsIntList = generateColorCode();
	    ColorArrayAdapter localb = new ColorArrayAdapter(AddTextActivity.this, colorsIntList);
		colorGrid.setAdapter(localb);
		//colorGrid.setVisibility(View.GONE);
		//fontGrid.setVisibility(View.GONE);
		keyboard = (ImageView) findViewById(R.id.iv_keyboard);
		keyboard.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				inManager.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
				colorLayout.setVisibility(View.GONE);
				fontLayout.setVisibility(View.GONE);
			}
		});
		fonts = (ImageView) findViewById(R.id.iv_fontstyle);
		fonts.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				//Toast.makeText(getApplicationContext(), "Font clicked", Toast.LENGTH_SHORT).show();
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
				fontLayout.setVisibility(View.VISIBLE);
				colorLayout.setVisibility(View.GONE);				
			}
		});
		
		colors = (ImageView) findViewById(R.id.iv_color);
		colors.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
				//Toast.makeText(getApplicationContext(), "color clicked", Toast.LENGTH_SHORT).show();
				//inManager.hideSoftInputFromInputMethod(editText.getWindowToken(), 0);
				fontLayout.setVisibility(View.GONE);
				colorLayout.setVisibility(View.VISIBLE);
			}
		});
		//alignment = (ImageView) findViewById(R.id.iv_gravity);
	
		done = (ImageView) findViewById(R.id.iv_done);
		
		colorGrid.setOnItemClickListener(new OnItemClickListener() 
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) 
			{
				color = colorsIntList.get(arg2).intValue();
				editText.setTextColor(color);
				
			}
		});
		
		fontGrid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) 
			{
				
				editText.setTypeface(Typeface.createFromAsset(getAssets(), fontList.get(position)));
				
			}
		});
		
		done.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v)
			{
				editTextString = editText.getText().toString();
				
				if(editTextString == null || editTextString.length() == 0)
				{
					Toast.makeText(AddTextActivity.this, "Enter text...", Toast.LENGTH_SHORT).show();
				}
				
				EditingPortraitActivity.textBitmap = textAsBitmap(editTextString, color, editText.getTypeface());
				Editing_Activity_lpf.textBitmap = textAsBitmap(editTextString, color, editText.getTypeface());
				//EditingActivity.textTypeface = editText.getTypeface();
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
				
				Intent intent = new Intent();
				intent.putExtra("editTextString", editTextString);
				intent.putExtra("color",color);
				//intent.putExtra("TextTypeFace", editText.getTypeface());

				setResult(RESULT_OK,intent);
				finish();
			}
		});
		
		cancel = (ImageView) findViewById(R.id.iv_cancel);
		cancel.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
				finish();
			}
		});
		//dialogShow();
	}

	public Bitmap textAsBitmap(String text, int textColor, Typeface typeface1) {
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setTextSize(150);
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
	
	@Override
	public void onBackPressed() 
	{
		super.onBackPressed();
		/*if(dialog!=null)
		dialog.dismiss();*/
	}
	
	 private void addFonts()
	  {
	    this.fontList.clear();
	    
	    this.fontList.add("font/hellofont6.otf");
	    this.fontList.add("font/font1.ttf");
	    this.fontList.add("font/f1.ttf");
	    this.fontList.add("font/hellofont3.TTF");
	    this.fontList.add("font/f2.ttf");
	    this.fontList.add("font/f3.ttf");
	    this.fontList.add("font/f4.ttf");
	    this.fontList.add("font/f5.ttf");
	    this.fontList.add("font/hellofont8.ttf");
	    this.fontList.add("font/hellofont9.ttf");
	    this.fontList.add("font/hellofont10.ttf");
	    this.fontList.add("font/hellofont11.otf");
	    this.fontList.add("font/hellofont12.ttf");
	    this.fontList.add("font/hellofont13.ttf");
	    this.fontList.add("font/hellofont14.otf");
	    this.fontList.add("font/hellofont15.ttf");
	    this.fontList.add("font/hellofont17.ttf");
	    this.fontList.add("font/hellofont18.otf");
	    this.fontList.add("font/hellofont19.ttf");
	    this.fontList.add("font/hellofont20.ttf");
	    /*this.fontList.add("fonts/PrimadonaVintage.ttf");
	    this.fontList.add("fonts/the_greatest_high.ttf");
	    this.fontList.add("fonts/Young italic.ttf");*/
	    /*this.fontList.add("fonts/font36.TTF");
	    this.fontList.add("fonts/font37.OTF");
	    this.fontList.add("fonts/font38.ttf");
	    this.fontList.add("fonts/font39.otf");*/
	  }
	 
	 public static ArrayList<Integer> generateColorCode()
	  {
	    ArrayList<Integer> localArrayList = new ArrayList<Integer>();
	    for (int i1 = 0; i1 <= 360; i1 += 20) 
	    {
	      localArrayList.add(Integer.valueOf(HSVtoColor(i1, 1.0F, 1.0F)));
	    }
	    
	    int i3;
	    for (int i2 = 0;; i2 += 20)
	    {
	      i3 = 0;
	      if (i2 > 360) 
	      {
	        break;
	      }
	      localArrayList.add(Integer.valueOf(HSVtoColor(i2, 0.25F, 1.0F)));
	      localArrayList.add(Integer.valueOf(HSVtoColor(i2, 0.5F, 1.0F)));
	      localArrayList.add(Integer.valueOf(HSVtoColor(i2, 0.75F, 1.0F)));
	    }
	    while (i3 <= 360)
	    {
	      localArrayList.add(Integer.valueOf(HSVtoColor(i3, 1.0F, 0.5F)));
	      localArrayList.add(Integer.valueOf(HSVtoColor(i3, 1.0F, 0.75F)));
	      i3 += 20;
	    }
	    for (float f1 = 0.0F; f1 <= 1.0F; f1 += 0.1F) 
	    {
	      localArrayList.add(Integer.valueOf(HSVtoColor(0.0F, 0.0F, f1)));
	    }
	    return localArrayList;	  
	    
	}
	  
	 public static int HSVtoColor(float paramFloat1, float paramFloat2, float paramFloat3)
	  {
	    return Color.HSVToColor(255, new float[] { paramFloat1, paramFloat2, paramFloat3 });
	  }
}
