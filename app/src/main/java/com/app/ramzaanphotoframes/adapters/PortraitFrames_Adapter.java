package com.app.ramzaanphotoframes.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.ramzaanphotoframes.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class PortraitFrames_Adapter extends RecyclerView.Adapter<PortraitFrames_Adapter.AdapterViewHolder> {
   private ArrayList<String> portrait_list;
    private Context context;


    public PortraitFrames_Adapter(Context context, ArrayList<String> portrait_list) {
        this.portrait_list = portrait_list;
        this.context = context;
    }

    class AdapterViewHolder extends RecyclerView.ViewHolder {

        ImageView img_frame;


        AdapterViewHolder(View itemView) {
            super(itemView);

            img_frame = (ImageView) itemView.findViewById(R.id.img_frame);
        }
    }

    @Override
    public int getItemCount() {

        return portrait_list.size();
    }

    @Override
    public AdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rowView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.adpter_frames_img, parent, false);

        return new AdapterViewHolder(rowView);
    }

    public void onBindViewHolder(AdapterViewHolder holder, int position) {

        holder.img_frame.getLayoutParams().height = (int) ((context.getResources().getDisplayMetrics().heightPixels) /3);
        holder.img_frame.getLayoutParams().width = (int) ((context.getResources().getDisplayMetrics().widthPixels) / 2);
        Glide.with(context).load(portrait_list.get(position))
                .placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher)
                .into(holder.img_frame);
    }
}
