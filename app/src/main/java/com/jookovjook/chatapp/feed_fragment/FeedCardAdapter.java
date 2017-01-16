package com.jookovjook.chatapp.feed_fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jookovjook.chatapp.R;
import com.jookovjook.chatapp.interfaces.GetPublicationsInterfase;
import com.jookovjook.chatapp.interfaces.GetPubsInterfase;
import com.jookovjook.chatapp.network.GetPublications;
import com.jookovjook.chatapp.pub.PubActivity;
import com.jookovjook.chatapp.user_profile.UserProfileActivity;
import com.jookovjook.chatapp.utils.Config;
import com.jookovjook.chatapp.utils.DateTimeConverter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FeedCardAdapter extends RecyclerView.Adapter<FeedCardAdapter.MyViewHolder> implements GetPublicationsInterfase{

    private ArrayList<FeedCardProvider> mList;
    private Context mContext;
    private boolean down_layout_expanded;
    private int startHeight = 100;
    private int finalHeight = 400;
    private int lastPosition = -1;
    private int REQUEST_EXIT = 1;
    private int type;
    private int param;
    private GetPubsInterfase gPI;

    @Override
    public void onGotPublication(FeedCardProvider feedCardProvider) {
        mList.add(feedCardProvider);
        notifyDataSetChanged();
    }

    @Override
    public void onGotAll(int last_id) {
        gPI.onGotAll(last_id);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView username;
        public TextView title;
        public TextView views;
        public TextView stars;
        public TextView comments;
        public ImageButton card_more_button;
        public LinearLayout down_layout;
        public CircleImageView circleImageView;
        public CardView cardView;
        public ImageView main_image;
        public TextView text;
        public TextView datetime;
        public CircleImageView user_avatar;

        public MyViewHolder(View itemView) {
            super(itemView);
            username = (TextView) itemView.findViewById(R.id.feed_card_username);
            title = (TextView) itemView.findViewById(R.id.feed_card_title);
            views = (TextView) itemView.findViewById(R.id.views);
            stars = (TextView) itemView.findViewById(R.id.stars);
            comments = (TextView) itemView.findViewById(R.id.comments);
            card_more_button = (ImageButton) itemView.findViewById(R.id.card_more_button);
            down_layout = (LinearLayout) itemView.findViewById(R.id.down_layout);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
            main_image = (ImageView) itemView.findViewById(R.id.main_image);
            text = (TextView) itemView.findViewById(R.id.text);
            datetime = (TextView) itemView.findViewById(R.id.datetime);
            user_avatar = (CircleImageView) itemView.findViewById(R.id.user_avatar);
        }
    }

    public FeedCardAdapter(int type, int param, Context mContext, GetPubsInterfase gPI){
        this.mList = new ArrayList<>();
        this.mContext = mContext;
        this.gPI = gPI;
        down_layout_expanded = false;
        this.type = type;
        this.param = param;
        GetPublications getPublications = new GetPublications(type, param, this, -1);
        getPublications.execute();
    }

    @Override
    public FeedCardAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.feed_card, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final FeedCardAdapter.MyViewHolder holder, int position) {
        final FeedCardProvider feedCardProvider = mList.get(position);
        holder.title.setText(feedCardProvider.getTitle());
        holder.username.setText("\u0040" + feedCardProvider.getUsername());
        holder.views.setText(String.valueOf(feedCardProvider.getViews()));
        holder.stars.setText(String.valueOf(feedCardProvider.getStars()));
        holder.comments.setText(String.valueOf(feedCardProvider.getComments()));
        holder.text.setText(feedCardProvider.getText());
        holder.datetime.setText(DateTimeConverter.convert(feedCardProvider.getDate()));
        Picasso.with(mContext)
                .load(Config.IMAGE_RESOURCES_URL + feedCardProvider.getSmall_avatar())
                .resize(64, 64).onlyScaleDown().centerCrop().into(holder.user_avatar);
        Picasso.with(mContext)
                .load(Config.IMAGE_RESOURCES_URL + feedCardProvider.getImg_link())
                .resize(720,720).onlyScaleDown().centerCrop().into(holder.main_image);
        setAnimation(holder.cardView, position);

//        holder.card_more_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//               if(down_layout_expanded == true) {
//                    ResizeAnimation resizeAnimation1 = new ResizeAnimation(holder.down_layout, startHeight, finalHeight);
//                    resizeAnimation1.setDuration(200);
//                    holder.down_layout.startAnimation(resizeAnimation1);
//                    holder.username.setText("collapsed");
//                    down_layout_expanded = false;
//                }else{
//                    ResizeAnimation resizeAnimation2 = new ResizeAnimation(holder.down_layout, finalHeight, startHeight);
//                    resizeAnimation2.setDuration(200);
//                    holder.down_layout.startAnimation(resizeAnimation2);
//                    holder.username.setText("expanded");
//                    down_layout_expanded = true;
//                };
//                //ResizeAnimation resizeAnimation = new ResizeAnimation(holder.down_layout, -100, 200);
//        resizeAnimation.setDuration(1000);
//        holder.down_layout.startAnimation(resizeAnimation);
//            }
//        });

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(mContext, UserProfileActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("user_id", feedCardProvider.getUser_id());
                bundle.putString("username", feedCardProvider.getUsername());
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            };
        };
        holder.username.setOnClickListener(onClickListener);
        holder.user_avatar.setOnClickListener(onClickListener);
        holder.main_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(mContext, PubActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("publication_id",feedCardProvider.getPublication_id());
                bundle.putString("img_link",feedCardProvider.getImg_link());
                intent.putExtras(bundle);
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation((Activity) mContext, (View) holder.main_image, "main_image_transition");
                ((Activity) mContext).startActivityForResult(intent, REQUEST_EXIT, options.toBundle());
            }
        });
    };

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void onViewDetachedFromWindow(MyViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.going_up);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    public void loadNextTen(){
        int last_pub_id = mList.get(mList.size() - 1).getPublication_id();
        GetPublications getPublications = new GetPublications(type, param, this, last_pub_id);
        getPublications.execute();
    };

}