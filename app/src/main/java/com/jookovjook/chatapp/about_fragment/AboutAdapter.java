package com.jookovjook.chatapp.about_fragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jookovjook.chatapp.R;

import java.util.List;

public class AboutAdapter extends RecyclerView.Adapter<AboutAdapter.MyViewHolder>{

    private List<AboutProvider> mList;
    private Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView username;
        public TextView name_surname;

        public MyViewHolder(View itemView) {
            super(itemView);
            username = (TextView) itemView.findViewById(R.id.username);
            name_surname = (TextView) itemView.findViewById(R.id.name_surname);
        }
    }

    public AboutAdapter(List<AboutProvider> mList, Context mContext ){
        this.mList = mList;
        this.mContext = mContext;
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
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
