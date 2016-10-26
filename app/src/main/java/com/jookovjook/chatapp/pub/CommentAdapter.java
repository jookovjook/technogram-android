package com.jookovjook.chatapp.pub;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jookovjook.chatapp.R;
import com.jookovjook.chatapp.utils.Config;
import com.jookovjook.chatapp.utils.StreamReader;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder>{


    ArrayList<CommentProvider> mList;
    Context mContext;
    int pub_id;

    public CommentAdapter(int pub_id, Context mContext, ArrayList<CommentProvider> mList){
        this.mContext = mContext;
        this.mList = mList;
        this.pub_id = pub_id;
        GetComments getComments = new GetComments(this.pub_id);
        getComments.execute();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
        return new CommentAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CommentProvider cProvider = mList.get(position);
        String comment_text = "<b>" + "\u0040" + cProvider.getUsername() + ": " + "</b>" + cProvider.getComment();
        holder.comment.setText(Html.fromHtml(comment_text));
        Picasso.with(mContext)
                .load(Config.IMAGE_RESOURCES_URL + cProvider.getAvatar())
                .resize(64, 64).onlyScaleDown().centerCrop().into(holder.avatar);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public CircleImageView avatar;
        public TextView comment;

        public ViewHolder(View itemView) {
            super(itemView);
            avatar = (CircleImageView) itemView.findViewById(R.id.avatar);
            comment = (TextView) itemView.findViewById(R.id.comment_text);

        }
    }

    private class GetComments extends AsyncTask<String, Void, String> {

        private int pub_id;

        GetComments(int pub_id){
            this.pub_id = pub_id;
        }

        @Override
        protected String doInBackground(String... params) {
            String s = "";
            try{
                Log.i("comment adapter 2:", "got pub_id" + String.valueOf(pub_id));
                URL url = new URL(Config.SERVER_URL + "get_comments.php?publication_id=" + String.valueOf(pub_id));
                HttpURLConnection mUrlConnection = (HttpURLConnection) url.openConnection();
                mUrlConnection.setDoInput(true);
                InputStream inputStream = new BufferedInputStream(mUrlConnection.getInputStream());
                s = StreamReader.read(inputStream);
                Log.i("Comment adapter","got stream");
            }catch (Exception e){

            }
            return s;
        }

        @Override
        protected void onPostExecute(String jsonResult) {
            super.onPostExecute(jsonResult);
            //Log.i("comment adapter", jsonResult);
            JSONArray jsonArray;
            try {
                jsonArray = new JSONArray(jsonResult);
                for(int i= 0; i<jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String username = jsonObject.getString("username");
                    int comment_id = jsonObject.getInt("comment_id");
                    int user_id = jsonObject.getInt("user_id");
                    String comment = jsonObject.getString("comment");
                    int publication_id = jsonObject.getInt("publication_id");
                    String avatar = jsonObject.getString("img_link");
                    mList.add(new com.jookovjook.chatapp.pub.CommentProvider(publication_id, comment_id, user_id, username, comment, avatar));
                    notifyDataSetChanged();
                    //commentAdapterCallback.onDataInsertedCallback();
                }
            }catch (Exception e){
                Log.i("Comment adapter","errorrrr");
            }
            notifyDataSetChanged();
            Log.i("Comment adapter","dataSet changed");
        }
    }
}
