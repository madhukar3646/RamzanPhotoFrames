package com.app.ramzaanphotoframes.adapters;

import java.util.ArrayList;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.app.ramzaanphotoframes.R;
import com.app.ramzaanphotoframes.classes.CircularTextView;

public class ColorArrayAdapter extends ArrayAdapter<Integer> 
{
	
	ArrayList<Integer> paramArrayList;
	Context context;
	LayoutInflater inflater;
	public ColorArrayAdapter(Context paramContext , ArrayList<Integer> paramArrayList)
	  {
	    super(paramContext, 0,paramArrayList);
	    this.context = paramContext;
	    this.paramArrayList = paramArrayList;
	    inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	  }
	  
	  public View getView(int position, View View, ViewGroup ViewGroup)
	  {
		  
		 //View view = inflater.inflate(R.layout.item_text, null);
	    //TextView localTextView = (TextView) view.findViewById(R.id.mark);
		/*TextView textView = new TextView(context);
	  
		 textView.setBackgroundColor(((Integer)this.paramArrayList.get(position)).intValue());
		 textView.setText("");
	     textView.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	     AbsListView.LayoutParams localLayoutParams = (AbsListView.LayoutParams)textView.getLayoutParams();
	     localLayoutParams.width = 60;
	     localLayoutParams.height = 60;
	     textView.setLayoutParams(localLayoutParams);
		 textView.requestLayout();*/
		 
		 Log.e("", ""+((Integer)this.paramArrayList.get(position)).intValue());
		
		 View view = inflater.inflate(R.layout.item_text_color_array_adapter, null);
		 CircularTextView circularTextView = (CircularTextView) view.findViewById(R.id.circularTextView);
		 //circularTextView.setStrokeWidth(1);
		 //circularTextView.setStrokeColor("#ffffff");
		 String hexColor = String.format("#%06X", (0xFFFFFF & ((Integer)this.paramArrayList.get(position)).intValue()));
		 circularTextView.setSolidColor(hexColor);
	     return view;
	  }

}
