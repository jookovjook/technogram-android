package com.jookovjook.chatapp.feed_fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jookovjook.chatapp.utils.DateTimeConverter;
import com.jookovjook.chatapp.R;
import com.jookovjook.chatapp.publication.PublicationActivity;
import com.jookovjook.chatapp.user_profile.UserProfileActivity;
import com.jookovjook.chatapp.utils.ServerSettings;
import com.jookovjook.chatapp.utils.StreamReader;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class FeedCardAdapter extends RecyclerView.Adapter<FeedCardAdapter.MyViewHolder> {

    private ArrayList<FeedCardProvider> mList;
    private Context mContext;
    public boolean down_layout_expanded;
    public int startHeight = 100;
    public int finalHeight = 400;
    private int lastPosition = -1;

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

        public MyViewHolder(View itemView) {
            super(itemView);
            username = (TextView) itemView.findViewById(R.id.feed_card_username);
            title = (TextView) itemView.findViewById(R.id.feed_card_title);
            views = (TextView) itemView.findViewById(R.id.views);
            stars = (TextView) itemView.findViewById(R.id.stars);
            comments = (TextView) itemView.findViewById(R.id.comments);
            card_more_button = (ImageButton) itemView.findViewById(R.id.card_more_button);
            down_layout = (LinearLayout) itemView.findViewById(R.id.down_layout);
            circleImageView = (CircleImageView) itemView.findViewById(R.id.circleImageView);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
            main_image = (ImageView) itemView.findViewById(R.id.main_image);
            text = (TextView) itemView.findViewById(R.id.text);
            datetime = (TextView) itemView.findViewById(R.id.datetime);
        }
    }

    public FeedCardAdapter(int type, String param, Context mContext){
        this.mList = new ArrayList<>();
        this.mContext = mContext;
        down_layout_expanded = false;
        GetPublications getPublications = new GetPublications(type, param);
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
        DateTimeConverter converter = new DateTimeConverter(feedCardProvider.getDate());
        holder.datetime.setText(converter.convert());
        Picasso.with(mContext)
                .load("http://" + ServerSettings.serverURL + "/chatApp/image_resources/" + feedCardProvider.getImg_link())
                .resize(720,720).onlyScaleDown().centerCrop().into(holder.main_image);
        setAnimation(holder.cardView, position);

        //holder.card_more_button.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View v) {
        //       if(down_layout_expanded == true) {
        //            ResizeAnimation resizeAnimation1 = new ResizeAnimation(holder.down_layout, startHeight, finalHeight);
        //            resizeAnimation1.setDuration(200);
        //            holder.down_layout.startAnimation(resizeAnimation1);
        //            holder.username.setText("collapsed");
        //            down_layout_expanded = false;
        //        }else{
        //            ResizeAnimation resizeAnimation2 = new ResizeAnimation(holder.down_layout, finalHeight, startHeight);
        //            resizeAnimation2.setDuration(200);
        //            holder.down_layout.startAnimation(resizeAnimation2);
        //            holder.username.setText("expanded");
        //            down_layout_expanded = true;
        //        };
        //        //ResizeAnimation resizeAnimation = new ResizeAnimation(holder.down_layout, -100, 200);
        //resizeAnimation.setDuration(1000);
        //holder.down_layout.startAnimation(resizeAnimation);
        //    }
        //});

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
        holder.circleImageView.setOnClickListener(onClickListener);
        holder.main_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(mContext, PublicationActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("publication_id",feedCardProvider.getPublication_id());
                bundle.putString("img_link",feedCardProvider.getImg_link());
                intent.putExtras(bundle);
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation((Activity) mContext, (View) holder.main_image, "main_image_transition");
                mContext.startActivity(intent, options.toBundle());
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

    private class GetPublications extends AsyncTask<String, Void, String> {

        int type;
        String param;
        String user_id;

        GetPublications(int type, String param){
            this.type = type;
            this.param = param;
            this.user_id = "0";
            if(type == 0) this.user_id = param;
        }

        @Override
        protected String doInBackground(String... params) {
            String s = "";
            try {
                URL url;
                if(type == 0) {
                    url = new URL("http://" + ServerSettings.serverURL + "/chatApp/get_user_publications.php?user_id=" + user_id);
                }else {
                    url = new URL("http://" + ServerSettings.serverURL + "/chatApp/get_all_publications");
                }
                HttpURLConnection mUrlConnection = (HttpURLConnection) url.openConnection();
                mUrlConnection.setDoInput(true);
                InputStream inputStream = new BufferedInputStream(mUrlConnection.getInputStream());
                s = StreamReader.read(inputStream);
            }catch (Exception e){
                Log.i("FeedCardAdapter","error getting json answer");
            }
            return s;
        }

        @Override
        protected void onPostExecute(String jsonResult) {
            super.onPostExecute(jsonResult);
            JSONArray jsonArray;
            try {
                jsonArray = new JSONArray(jsonResult);
                for(int i= 0; i<jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    int publication_id = jsonObject.getInt("publication_id");
                    int user_id = jsonObject.getInt("user_id");
                    String title = jsonObject.getString("title");
                    int views = jsonObject.getInt("views");
                    int stars = jsonObject.getInt("stars");
                    int comments = jsonObject.getInt("comments");
                    String username = jsonObject.getString("username");
                    String img_link = jsonObject.getString("img_link");
                    String text = jsonObject.getString("text");
                    String datetime = jsonObject.getString("datetime");
                    Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS").parse(datetime);
                    FeedCardProvider feedCardProvider = new FeedCardProvider
                            (publication_id, user_id, username, title, views, stars, comments, img_link, text, date);
                    mList.add(feedCardProvider);
                    notifyDataSetChanged();
                }
            }catch (Exception e){
                Log.i("FeedCardAdapter","error parsing json");
            }
        }

    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.going_up);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
}
