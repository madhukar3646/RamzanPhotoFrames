package com.app.ramzaanphotoframes.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.ramzaanphotoframes.R;
import com.bumptech.glide.Glide;

public class Frames_Adapter extends RecyclerView.Adapter<Frames_Adapter.AdapterViewHolder> {
    int[] listsize;
    private Context context;


    public Frames_Adapter(Context context, int[] filepaths) {
        this.listsize = filepaths;
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

        return listsize.length;
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
        Glide.with(context).load(listsize[position])
                .placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher)
                .into(holder.img_frame);


    }

}
