package com.jookovjook.chatapp.new_pub;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jookovjook.chatapp.R;

import java.util.ArrayList;

public class HashTagsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public interface HashTagCallback{
        void onTagAdded(String hashtag, int level, int type);
    }

    HashTagCallback hashTagCallback;
    ArrayList<HashTagsProvider> mList;
    Context context;

    public HashTagsAdapter(ArrayList<HashTagsProvider> mList, Context context, HashTagCallback hashTagCallback){
        this.mList = mList;
        this.context = context;
        this.hashTagCallback = hashTagCallback;
    }

    public class VHItem extends RecyclerView.ViewHolder {

        public Button hashTagTextView;

        public VHItem(View itemView) {
            super(itemView);
            hashTagTextView = (Button) itemView.findViewById(R.id.textViewR);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.hashtag_item, parent, false);
        return new VHItem(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final HashTagsProvider hashTagsProvider = mList.get(position);
        final String tag = hashTagsProvider.getHashTag();
        final String text = "\u0023" + tag;
        final VHItem vhItem = (VHItem) holder;
        vhItem.hashTagTextView.setText(text);
        vhItem.hashTagTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hashTagCallback.onTagAdded(tag, hashTagsProvider.getLevel(), hashTagsProvider.getType());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

}
