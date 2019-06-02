package com.app.ramzaanphotoframes.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.app.ramzaanphotoframes.R;
import com.app.ramzaanphotoframes.classes.OnFrameClickListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

public class PortraitFrames_Adapter extends RecyclerView.Adapter<PortraitFrames_Adapter.AdapterViewHolder> {
   private ArrayList<String> portrait_list;
    private Context context;
    private OnFrameClickListener onFrameClickListener;
    private boolean isAlbum;

    public PortraitFrames_Adapter(Context context, ArrayList<String> portrait_list) {
        this.portrait_list = portrait_list;
        this.context = context;
    }

    public void setOnFrameClickListener(OnFrameClickListener onFrameClickListener,boolean isAlbum)
    {
        this.onFrameClickListener=onFrameClickListener;
        this.isAlbum=isAlbum;
    }

    class AdapterViewHolder extends RecyclerView.ViewHolder {

        ImageView img_frame;
        RelativeLayout layout_root;


        AdapterViewHolder(View itemView) {
            super(itemView);

            img_frame = (ImageView) itemView.findViewById(R.id.img_frame);
            layout_root=(RelativeLayout)itemView.findViewById(R.id.layout_root);
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

    public void onBindViewHolder(AdapterViewHolder holder, final int position) {

        holder.layout_root.getLayoutParams().height = (int) ((context.getResources().getDisplayMetrics().heightPixels) /3);
        holder.layout_root.getLayoutParams().width = (int) ((context.getResources().getDisplayMetrics().widthPixels) / 2);

        holder.img_frame.getLayoutParams().height = (int) ((context.getResources().getDisplayMetrics().heightPixels) /3);
        holder.img_frame.getLayoutParams().width = (int) ((context.getResources().getDisplayMetrics().widthPixels) / 2);
        Glide.with(context).load(portrait_list.get(position))
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.drawable.loading_icon).error(R.drawable.loading_icon)
                .into(holder.img_frame);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onFrameClickListener!=null)
                {
                    if(isAlbum)
                        onFrameClickListener.onAlbumClick(position);
                    else
                        onFrameClickListener.onFrameClick(position);
                }
            }
        });
    }
}
