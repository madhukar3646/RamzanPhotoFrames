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

public class Albums_Adapter extends RecyclerView.Adapter<Albums_Adapter.CardViewHolder> {
    private ArrayList<String> filePaths;
    private Context context;

    public Albums_Adapter(Context context, ArrayList<String> filepaths) {
        this.filePaths = filepaths;
        this.context = context;

    }

    class CardViewHolder extends RecyclerView.ViewHolder {


        ImageView ivPic;


        CardViewHolder(View itemView) {
            super(itemView);

            ivPic = (ImageView) itemView.findViewById(R.id.img_frame);
        }
    }

    @Override
    public int getItemCount() {

        return filePaths.size();
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rowView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.albums_img, parent, false);

        return new CardViewHolder(rowView);
    }

    public void onBindViewHolder(CardViewHolder holder, int position) {

        holder.ivPic.getLayoutParams().height = (int) ((context.getResources()
                .getDisplayMetrics().heightPixels) / 3);

        holder.ivPic.getLayoutParams().width = (int) ((context.getResources()
                .getDisplayMetrics().widthPixels) / 2);


        Glide.with(context).load(filePaths.get(position))
                .placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher)
                .into(holder.ivPic);
    }

}
