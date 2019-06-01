package com.app.ramzaanphotoframes.landscape_module.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.ramzaanphotoframes.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;


public class Image_Adapter extends RecyclerView.Adapter<Image_Adapter.AdapterViewHolder> {
    private ArrayList<String> landscape_list;
    private Context context;


    public Image_Adapter(Context context, ArrayList<String> landscape_list) {
        this.landscape_list = landscape_list;
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

        return landscape_list.size();
    }

    @Override
    public AdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rowView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.bg_img_layout_lpf, parent, false);



        rowView.getLayoutParams().height = (int) ((context.getResources().getDisplayMetrics().widthPixels) / 4);
        rowView.getLayoutParams().width = (int) ((context.getResources().getDisplayMetrics().widthPixels) / 2);

        return new AdapterViewHolder(rowView);
    }

    public void onBindViewHolder(AdapterViewHolder holder, int position) {


            Glide.with(context).load(landscape_list.get(position))
                    .placeholder(R.drawable.load_icon).error(R.mipmap.ic_launcher)
                    .into(holder.img);
    }

}
