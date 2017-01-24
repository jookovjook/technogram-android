package com.jookovjook.chatapp.new_feed_fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jookovjook.chatapp.R;
import com.jookovjook.chatapp.interfaces.GetPubsInterfase;
import com.jookovjook.chatapp.interfaces.NewGetPublicationsInterfase;
import com.jookovjook.chatapp.network.GetPublications;
import com.jookovjook.chatapp.pub.PubActivity;
import com.jookovjook.chatapp.user_profile.UserProfileActivity;
import com.jookovjook.chatapp.utils.AuthHelper;
import com.jookovjook.chatapp.utils.Config;
import com.jookovjook.chatapp.utils.DateTimeConverter;
import com.jookovjook.chatapp.utils.Metrics;
import com.squareup.picasso.Picasso;

import org.zakariya.stickyheaders.SectioningAdapter;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FeedCardAdapter extends SectioningAdapter implements NewGetPublicationsInterfase {

    static final String TAG = FeedCardAdapter.class.getSimpleName();
    static final boolean USE_DEBUG_APPEARANCE = false;
    private int screenWidth;
    private int screenHeight;

    private ArrayList<FeedCardProvider> mList;
    private Context context;
    private int lastPosition = -1;
    private int type;
    private int param;
    private GetPubsInterfase gPI;

    private int REQUEST_EXIT = 1;

    @Override
    public void onGotPublication(FeedCardProvider feedCardProvider) {
        mList.add(feedCardProvider);
        notifyAllSectionsDataSetChanged();
        //Log.i("muther", String.valueOf(mList.size()));
    }

    @Override
    public void onGotAll(int last_id) {
        gPI.onGotAll(last_id);
    }

    public class ItemViewHolder extends SectioningAdapter.ItemViewHolder {

        RelativeLayout relativeLayout;

        public TextView views;
        public TextView title;
        public TextView text;
        public TextView comments;
        public TextView likes;
        public ImageView main_image;

        public ItemViewHolder(View itemView) {
            super(itemView);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relativeLayout);
            title = (TextView) itemView.findViewById(R.id.feed_card_title);
            views = (TextView) itemView.findViewById(R.id.views);
            comments = (TextView) itemView.findViewById(R.id.comments);
            likes = (TextView) itemView.findViewById(R.id.likes);
            main_image = (ImageView) itemView.findViewById(R.id.main_image);
            text = (TextView) itemView.findViewById(R.id.text);
        }

    }

    public class HeaderViewHolder extends SectioningAdapter.HeaderViewHolder {

        LinearLayout headerLayout;
        public TextView username;
        public CircleImageView user_avatar;
        public TextView datetime;
        public ImageButton card_more_button;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            headerLayout = (LinearLayout) itemView.findViewById(R.id.headerLayout);
            user_avatar = (CircleImageView) itemView.findViewById(R.id.user_avatar);
            datetime = (TextView) itemView.findViewById(R.id.datetime);
            card_more_button = (ImageButton) itemView.findViewById(R.id.card_more_button);
            username = (TextView) itemView.findViewById(R.id.feed_card_username);

        }
    }


    public FeedCardAdapter(int type, int param, Context context, GetPubsInterfase gPI) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displaymetrics);
        this.screenHeight = displaymetrics.heightPixels;
        this.screenWidth = displaymetrics.widthPixels;
        this.mList = new ArrayList<>();
        this.context = context;
        this.gPI = gPI;
        this.type = type;
        this.param = param;
        //execute();
    }

    public void execute(){
        GetPublications getPublications = new GetPublications(type, param, this, -1);
        getPublications.execute();
    }

    @Override
    public int getNumberOfSections() {
        return mList.size();
    }

    @Override
    public int getNumberOfItemsInSection(int sectionIndex) {
        return 1;
    }

    @Override
    public boolean doesSectionHaveHeader(int sectionIndex) {
        return true;
    }

    @Override
    public boolean doesSectionHaveFooter(int sectionIndex) {
        return false;
    }

    @Override
    public ItemViewHolder onCreateItemViewHolder(ViewGroup parent, int itemType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.new_pub_item, parent, false);
        return new ItemViewHolder(v);
    }

    @Override
    public HeaderViewHolder onCreateHeaderViewHolder(ViewGroup parent, int headerType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.new_pub_header, parent, false);
        return new HeaderViewHolder(v);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindItemViewHolder(SectioningAdapter.ItemViewHolder viewHolder, int sectionIndex, int itemIndex, int itemType) {
        final ItemViewHolder ivh = (ItemViewHolder) viewHolder;
        ViewGroup.LayoutParams layoutParams =  ivh.relativeLayout.getLayoutParams();
        layoutParams.height = screenWidth;
        layoutParams.width = screenWidth;
        ivh.relativeLayout.setLayoutParams(layoutParams);
        final FeedCardProvider feedCardProvider = mList.get(sectionIndex);
        ivh.title.setText(feedCardProvider.title);
        ivh.text.setText(feedCardProvider.text);
        ivh.views.setText(String.valueOf(feedCardProvider.views) + " views");
        ivh.comments.setText("Add a comment (" + String.valueOf(feedCardProvider.views) + ")...");
        ivh.likes.setText("(" + String.valueOf(feedCardProvider.likes) + ")");
        Picasso.with(context)
                .load(Config.IMAGE_RESOURCES_URL + feedCardProvider.img_link)
                .resize(720,720).onlyScaleDown().centerCrop().into(ivh.main_image);
        ivh.main_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(context, PubActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("publication_id",feedCardProvider.pub_id);
                bundle.putString("img_link",feedCardProvider.img_link);
                intent.putExtras(bundle);
                //ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, (View) ivh.main_image, "main_image_transition");
                //((Activity) context).startActivityForResult(intent, REQUEST_EXIT, options.toBundle());
                context.startActivity(intent);
            }
        });
    };

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindHeaderViewHolder(SectioningAdapter.HeaderViewHolder viewHolder, int sectionIndex, int headerType) {
        HeaderViewHolder hvh = (HeaderViewHolder) viewHolder;
        ViewGroup.LayoutParams layoutParams =  hvh.headerLayout.getLayoutParams();
        layoutParams.width = screenWidth - Metrics.dpToPx(16);
        hvh.headerLayout.setLayoutParams(layoutParams);
        final FeedCardProvider feedCardProvider = mList.get(sectionIndex);
        hvh.username.setText("\u0040" + feedCardProvider.username);
        hvh.datetime.setText(DateTimeConverter.convert(feedCardProvider.date));
        Picasso.with(context)
                .load(Config.IMAGE_RESOURCES_URL + feedCardProvider.small_avatar)
                .resize(64, 64).onlyScaleDown().centerCrop().into(hvh.user_avatar);
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(context, UserProfileActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("user_id", feedCardProvider.user_id);
                bundle.putString("username", feedCardProvider.username);
                bundle.putBoolean("own", (AuthHelper.getUserId(context) == feedCardProvider.user_id));
                intent.putExtras(bundle);
                context.startActivity(intent);
            };
        };
        hvh.username.setOnClickListener(onClickListener);
        hvh.user_avatar.setOnClickListener(onClickListener);
    }

    @Override
    public void onBindGhostHeaderViewHolder(SectioningAdapter.GhostHeaderViewHolder viewHolder, int sectionIndex) {
        if (USE_DEBUG_APPEARANCE) {
            viewHolder.itemView.setBackgroundColor(0xFF9999FF);
        }
    }

    public void loadNextTen(){
        int last_pub_id = mList.get(mList.size() - 1).pub_id;
        Log.i("New Feed Fragmnet", "loadNextTexn, pub_id = " + String.valueOf(last_pub_id));
        GetPublications getPublications = new GetPublications(type, param, this, last_pub_id);
        getPublications.execute();
    };

}
