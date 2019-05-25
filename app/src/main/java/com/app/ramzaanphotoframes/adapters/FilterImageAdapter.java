package com.app.ramzaanphotoframes.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.app.ramzaanphotoframes.R;
import com.bumptech.glide.Glide;


public class FilterImageAdapter extends BaseAdapter {
    private Context context;
    private final int[] mThumbIds;
    ViewHolderItem viewHolder;

    static class ViewHolderItem {
        ImageView imageView;

        ViewHolderItem() {
        }
    }

    public FilterImageAdapter(Context context, int[] numArr) {
        this.context = context;
        this.mThumbIds = numArr;
    }

    public int getCount() {
        return this.mThumbIds.length;
    }

    public Object getItem(int i) {
        return null;
    }

    public long getItemId(int i) {
        return 0;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view2 = new View(this.context);
        this.viewHolder = new ViewHolderItem();
        view2 = layoutInflater.inflate(R.layout.edit_art_grid_item, null);
        this.viewHolder.imageView = (ImageView) view2.findViewById(R.id.img_theme);
        view2.setTag(this.viewHolder);

        Glide.with(context).load(this.mThumbIds[i])
                .placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher)
                .into(viewHolder.imageView);

        return view2;
    }
}
