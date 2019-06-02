package com.app.ramzaanphotoframes.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.ramzaanphotoframes.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;


public class Image_Adapter extends RecyclerView.Adapter<Image_Adapter.AdapterViewHolder> {
    private ArrayList<String> portrait_list;
    private Context context;


    public Image_Adapter(Context context, ArrayList<String> portrait_list) {
        this.portrait_list = portrait_list;
        this.context = context;

    }



    class AdapterViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        AdapterViewHolder(View itemView) {
            super(itemView);

            img = (ImageView) itemView.findViewById(R.id.img);

        }
    }

    @Override
    public int getItemCount() {

        return portrait_list.size();
    }

    @Override
    public AdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rowView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.bg_img_layout, parent, false);

        rowView.getLayoutParams().height = (int) ((context.getResources().getDisplayMetrics().widthPixels) / 2);
        rowView.getLayoutParams().width = (int) ((context.getResources().getDisplayMetrics().widthPixels) / 3);

        return new AdapterViewHolder(rowView);
    }

    public void onBindViewHolder(AdapterViewHolder holder, int position) {
            Glide.with(context).load(portrait_list.get(position))
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .placeholder(R.drawable.loading_icon).error(R.drawable.loading_icon)
                    .into(holder.img);
    }
}
