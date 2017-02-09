package com.jookovjook.chatapp.settings;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jookovjook.chatapp.R;
import com.jookovjook.chatapp.utils.AuthHelper;

import java.util.ArrayList;

class SecurityAdapter extends RecyclerView.Adapter<SecurityAdapter.ViewHolder>{

    static final String EMAIL = "E-mail";
    static final String PASSWORD = "Password";

    private class SettingsProvider {

        String type;
        String value;

        SettingsProvider(String type, String value){
            this.type = type;
            this.value = value;
        }


    }

    private ArrayList<SettingsProvider> mList;
    private Context context;

    public SecurityAdapter(Context context){
        this.context = context;
        update();
    }

    public void update() {
        mList = new ArrayList<>();
        mList.add(new SettingsProvider(EMAIL, AuthHelper.getEMAIL(context)));
        mList.add(new SettingsProvider(PASSWORD , "\u2022 \u2022 \u2022 \u2022 \u2022 \u2022 \u2022 \u2022"));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.settings_item, parent, false);
        return new SecurityAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final SettingsProvider sProvider = mList.get(position);
        holder.name.setText(sProvider.type);
        holder.value.setText(sProvider.value);
        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if(sProvider.type.equals(PASSWORD)) {
                    intent = new Intent(context, PassSettActivity.class);
                }else{
                    intent = new Intent(context, InfoSettActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("type", sProvider.type);
                    bundle.putString("value", sProvider.value);
                    intent.putExtras(bundle);
                }
                ((Activity) context).startActivityForResult(intent, SettingsActivity.REQUEST_CODE_SETTING);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView name, value;
        LinearLayout itemLayout;

        ViewHolder (View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            value = (TextView) itemView.findViewById(R.id.value);
            itemLayout = (LinearLayout) itemView.findViewById(R.id.itemLayout);

        }
    }

}
