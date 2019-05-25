package com.app.ramzaanphotoframes.adapters;

import java.util.ArrayList;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.app.ramzaanphotoframes.R;

public class FontAdapter extends BaseAdapter {

	Context context;
	ArrayList<String> fontList;
	LayoutInflater inflater;
	public FontAdapter(Context context,ArrayList<String> fontList) 
	{
		this.context = context;
		this.fontList = fontList;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return fontList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return fontList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		ViewHolder holder;
		
		if(convertView==null)
		{
			convertView = inflater.inflate(R.layout.item_font_style, null);
			holder = new ViewHolder();
			holder.fontText = (TextView) convertView.findViewById(R.id.img_grid_item);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.typeface = Typeface.createFromAsset(this.context.getAssets(), (String)this.fontList.get(position));
		holder.fontText.setTypeface(holder.typeface);
		return convertView;
	}

	class ViewHolder
	{
		TextView fontText;
		Typeface typeface;
	}
}
