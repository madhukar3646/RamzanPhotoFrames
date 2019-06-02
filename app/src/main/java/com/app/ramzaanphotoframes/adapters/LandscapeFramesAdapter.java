package com.app.ramzaanphotoframes.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.ramzaanphotoframes.R;
import com.app.ramzaanphotoframes.classes.OnFrameClickListener;
import com.bumptech.glide.Glide;
import java.util.ArrayList;

public class LandscapeFramesAdapter extends RecyclerView.Adapter<LandscapeFramesAdapter.AdapterViewHolder> {
    private ArrayList<String> landscape_list;
    private Context context;
    private OnFrameClickListener onFrameClickListener;
    private boolean isAlbum;

    public LandscapeFramesAdapter(Context context, ArrayList<String> landscape_list) {
        this.landscape_list = landscape_list;
        this.context = context;
    }

    public void setOnFrameClickListener(OnFrameClickListener onFrameClickListener,boolean isAlbum)
    {
        this.onFrameClickListener=onFrameClickListener;
        this.isAlbum=isAlbum;
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

        return landscape_list.size();
    }

    @Override
    public AdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rowView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.adpter_frames_img_lpf, parent, false);

        return new AdapterViewHolder(rowView);
    }

    public void onBindViewHolder(AdapterViewHolder holder, final int position) {

        holder.img_frame.getLayoutParams().height = (int) ((context.getResources().getDisplayMetrics().heightPixels) /6);
        holder.img_frame.getLayoutParams().width = (int) ((context.getResources().getDisplayMetrics().widthPixels) / 2);
        Glide.with(context).load(landscape_list.get(position))
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
