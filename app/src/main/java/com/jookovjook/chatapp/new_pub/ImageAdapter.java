package com.jookovjook.chatapp.new_pub;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.jookovjook.chatapp.R;
import com.jookovjook.chatapp.interfaces.ImagesLoaderInterface;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_ITEM = 1;
    private ImagesLoaderInterface imagesLoaderInterface;
    private ArrayList<ImageProvider> mList;
    private Context mContext;
    private int lastPosition = -1;

    public ImageAdapter(ArrayList<ImageProvider> mList, Context mContext, ImagesLoaderInterface imagesLoaderInterface){
        this.mList = mList;
        this.mContext = mContext;
        this.imagesLoaderInterface = imagesLoaderInterface;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_HEADER) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_header, parent, false);
            return new VHHeader(itemView);
        }else if (viewType == VIEW_TYPE_ITEM) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item, parent, false);
                return new VHItem(itemView);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof VHHeader){
            VHHeader vhHeader = (VHHeader) holder;
            vhHeader.addPhotosButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    imagesLoaderInterface.onAddImagesClicked();
                }
            });
        } else if (holder instanceof VHItem){
            final ImageProvider imageProvider = mList.get(position - 1);
            final VHItem vhItem = (VHItem) holder;
            imageProvider.setVhItem(vhItem);
            setAnimation(vhItem.imageItemLayout, position);
            Glide.with(mContext).load(imageProvider.getUri()).fitCenter().into(vhItem.image);
            if (imageProvider.getUploaded()){
                vhItem.progressBar.setVisibility(View.INVISIBLE);
                vhItem.refreshImageButton.setVisibility(View.INVISIBLE);
                vhItem.haze.setAlpha(0f);
            }else if(imageProvider.getUploading_status() == 100){
                vhItem.refreshImageButton.setVisibility(View.VISIBLE);
            }
            vhItem.refreshImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    imageProvider.reload();
                }
            });
            vhItem.deleteImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setGoneAnimation(vhItem.imageItemLayout, position);
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(isPositionHeader(position))
            return VIEW_TYPE_HEADER;
        return VIEW_TYPE_ITEM;
    }

    private boolean isPositionHeader(int position)
    {
        return position == 0;
    }

    @Override
    public int getItemCount() {
        return mList.size() + 1;
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if(holder instanceof VHItem){
            VHItem vhItem = (VHItem) holder;
            vhItem.imageItemLayout.clearAnimation();
        }
    }

    public class VHItem extends RecyclerView.ViewHolder {
        public ImageView image;
        public ProgressBar progressBar;
        public ImageButton deleteImageButton;
        public RelativeLayout imageItemLayout;
        public View haze;
        public ImageButton refreshImageButton;

        public VHItem(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
            deleteImageButton = (ImageButton) itemView.findViewById(R.id.delete_image_button);
            imageItemLayout = (RelativeLayout) itemView.findViewById(R.id.image_item_layout);
            haze = itemView.findViewById(R.id.haze);
            refreshImageButton = (ImageButton) itemView.findViewById(R.id.refresh_image_button);
        }
    }

    public class VHHeader extends  RecyclerView.ViewHolder{

        public ImageButton addPhotosButton;

        public VHHeader(View itemView){
            super(itemView);
            addPhotosButton = (ImageButton) itemView.findViewById(R.id.add_photos_button);
        }
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.image_appear);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    private void setGoneAnimation(View viewToAnimate, final int position){
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.image_disappear);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mList.remove(position - 1);
                notifyDataSetChanged();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        viewToAnimate.startAnimation(animation);
        lastPosition = position - 1;
    }
}