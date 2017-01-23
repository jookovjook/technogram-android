package com.jookovjook.chatapp.about_fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jookovjook.chatapp.R;
import com.jookovjook.chatapp.interfaces.GetAddUserInfo;
import com.jookovjook.chatapp.network.GetUserAddInfo;
import com.jookovjook.chatapp.network.UpdateProfile;
import com.jookovjook.chatapp.utils.AuthHelper;
import com.jookovjook.chatapp.utils.Config;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AboutAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements GetAddUserInfo{

    private List<AboutProvider> mList;
    private Context mContext;
    private int user_id;
    private Boolean own;


    @Override
    public void onGotAddUserInfo(String username, String name, String surname, String avatar, String about) {
        AboutProvider aboutProvider = new AboutProvider(user_id, username, name, surname, about, avatar);
        mList.add(aboutProvider);
        notifyDataSetChanged();
    }

    public class InfoVH extends RecyclerView.ViewHolder{
        public TextView username;
        public TextView name_surname;
        public TextView about;
        public ImageView avatar;
        public LinearLayout aboutLayout;
        public InfoVH(View itemView) {
            super(itemView);
            username = (TextView) itemView.findViewById(R.id.username);
            name_surname = (TextView) itemView.findViewById(R.id.name_surname);
            about = (TextView) itemView.findViewById(R.id.about);
            avatar = (ImageView) itemView.findViewById(R.id.avatar);
            aboutLayout = (LinearLayout) itemView.findViewById(R.id.aboutLayout);
        }
    }

    public class LogOutVH extends RecyclerView.ViewHolder{
        public Button logOutButton;
        public Button expireButton;
        public Button editButton;
        public LogOutVH(View itemView) {
            super(itemView);
            logOutButton = (Button) itemView.findViewById(R.id.logOutButton);
            expireButton = (Button) itemView.findViewById(R.id.expireButton);
            editButton = (Button) itemView.findViewById(R.id.editButton);
        }
    }

    public AboutAdapter(int user_id, Context mContext, Boolean own){
        this.own = own;
        this.mList = new ArrayList<>();
        this.mContext = mContext;
        this.user_id = user_id;
        GetUserAddInfo getUserAddInfo = new GetUserAddInfo(user_id, this);
        getUserAddInfo.execute();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case 0:
                View itemView0 = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.about_card, parent, false);
                return new AboutAdapter.InfoVH(itemView0);
            default:
                View itemView1 = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.logout_card, parent, false);
                return new AboutAdapter.LogOutVH(itemView1);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()){
            case 0:
                InfoVH holder0 = (InfoVH) holder;
                final AboutProvider aboutProvider = mList.get(position);
                holder0.username.setText(aboutProvider.getUsername());
                holder0.name_surname.setText(aboutProvider.getName() + " " + aboutProvider.getSurname());
                holder0.about.setText(aboutProvider.getAbout());
                Picasso.with(mContext)
                        .load(Config.IMAGE_RESOURCES_URL + aboutProvider.getAvatar())
                        .resize(256, 256).onlyScaleDown().centerCrop().into(holder0.avatar);
                return;
            default:
                LogOutVH holder1 = (LogOutVH) holder;
                holder1.expireButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(mContext, "Expire pressed",
                                Toast.LENGTH_SHORT).show();
                    }
                });
                holder1.logOutButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AuthHelper.setUserId(mContext, -1);
                        AuthHelper.setToken(mContext,"");
                        AuthHelper.setUsername(mContext,"");
                        own = false;
                        notifyDataSetChanged();
                        Toast.makeText(mContext, "You have logged out",
                                Toast.LENGTH_SHORT).show();
                    }
                });
                holder1.editButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mList.size() == 0) return;
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                        alertDialog.setTitle("Edit profile");
                        alertDialog.setMessage("Edit your description");
                        final EditText input = new EditText(mContext);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT);
                        input.setLayoutParams(lp);
                        final String initial_text = mList.get(0).getAbout();
                        input.setText(initial_text);
                        alertDialog.setView(input);
                        alertDialog.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int which) {
                                        // Write your code here to execute after dialog
                                        String edited_text = input.getText().toString();
                                        if(!initial_text.equals(edited_text)) {
                                            UpdateProfile updateProfile = new UpdateProfile(mContext);
                                            updateProfile.addAbout(edited_text);
                                            updateProfile.execute();
                                        }
                                    }
                                });
                        alertDialog.setNegativeButton("Cancle",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Write your code here to execute after dialog
                                        dialog.cancel();
                                    }
                                });
                        alertDialog.show();
                    }
                });
                return;
        }

    }

    @Override
    public int getItemViewType(int position) {
        int value;
        if(own){
            if(mList.size() > 0) {
                value = position;
            }else {
                value = 1;
            }
        }else {
            value = 0;
        }
        return value;
    }

    @Override
    public int getItemCount() {
        if (own) return (1 + mList.size());
        return mList.size();
    }
}
