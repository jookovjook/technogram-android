package com.jookovjook.chatapp.about_fragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jookovjook.chatapp.R;
import com.jookovjook.chatapp.interfaces.GetAddUserInfo;
import com.jookovjook.chatapp.network.GetUserAddInfo;
import com.jookovjook.chatapp.utils.Config;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AboutAdapter extends RecyclerView.Adapter<AboutAdapter.MyViewHolder> implements GetAddUserInfo{

    private List<AboutProvider> mList;
    private Context mContext;
    private int user_id;

    @Override
    public void onGotAddUserInfo(String username, String name, String surname, String avatar, String about) {
        AboutProvider aboutProvider = new AboutProvider(user_id, username, name, surname, about, avatar);
        mList.add(aboutProvider);
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView username;
        public TextView name_surname;
        public TextView about;
        public ImageView avatar;

        public MyViewHolder(View itemView) {
            super(itemView);
            username = (TextView) itemView.findViewById(R.id.username);
            name_surname = (TextView) itemView.findViewById(R.id.name_surname);
            about = (TextView) itemView.findViewById(R.id.about);
            avatar = (ImageView) itemView.findViewById(R.id.avatar);
        }
    }

    public AboutAdapter(int user_id, Context mContext ){
        this.mList = new ArrayList<>();
        this.mContext = mContext;
        this.user_id = user_id;
        GetUserAddInfo getUserAddInfo = new GetUserAddInfo(user_id, this);
        getUserAddInfo.execute();
    }

    @Override
    public AboutAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.about_card, parent, false);
        return new AboutAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AboutAdapter.MyViewHolder holder, int position) {
        final AboutProvider aboutProvider = mList.get(position);
        holder.username.setText(aboutProvider.getUsername());
        holder.name_surname.setText(aboutProvider.getName() + " " + aboutProvider.getSurname());
        holder.about.setText(aboutProvider.getAbout());
        Picasso.with(mContext)
                .load(Config.IMAGE_RESOURCES_URL + aboutProvider.getAvatar())
                .resize(256, 256).onlyScaleDown().centerCrop().into(holder.avatar);
    }

    @Override
    public int getItemCount() {
        //return 0;
        return mList.size();
    }
}
