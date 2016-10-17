package com.jookovjook.chatapp.new_publication;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jookovjook.chatapp.R;

import java.util.ArrayList;

/**
 * Created by jookovjook on 17/10/16.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.MyViewHolder> {

    private ArrayList<ImageProvider> mList;
    private Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView image;

        public MyViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
        }
    }

    public ImageAdapter(ArrayList<ImageProvider> mList, Context mContext){
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_item, parent, false);
        return new ImageAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final ImageProvider imageProvider = mList.get(position);
        Glide.with(mContext)
                .load(imageProvider.getMediaUri())
                .fitCenter()
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

}
