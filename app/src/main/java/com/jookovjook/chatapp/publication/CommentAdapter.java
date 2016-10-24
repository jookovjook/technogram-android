package com.jookovjook.chatapp.publication;

import android.content.Context;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jookovjook.chatapp.R;
import com.jookovjook.chatapp.interfaces.CommentAdapterCallback;
import com.jookovjook.chatapp.utils.Config;
import com.jookovjook.chatapp.utils.StreamReader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class CommentAdapter extends BaseAdapter {

    Context mContext;
    LayoutInflater mLayoutInflater;
    ArrayList<CommentProvider> mList;
    int publication_id;

    private CommentAdapterCallback commentAdapterCallback;

    CommentAdapter(int publication_id, Context mContext, CommentAdapterCallback callback) {
        this.commentAdapterCallback = callback;
        this.mContext = mContext;
        this.publication_id = publication_id;
        this.mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mList = new ArrayList<CommentProvider>();
        GetComments getComments = new GetComments(this.publication_id);
        getComments.execute();
    }



    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = mLayoutInflater.inflate(R.layout.comment_item, parent, false);
        }
        CommentProvider cProvider = mList.get(position);
        String comment_text = "<b>" + "\u0040" + cProvider.getUsername() + ": " + "</b>" + cProvider.getComment();
        ((TextView) view.findViewById(R.id.comment_text)).setText(Html.fromHtml(comment_text));
        return view;
    }

    private class GetComments extends AsyncTask<String, Void, String>{

        private int publication_id;

        GetComments(int publication_id){
            this.publication_id = publication_id;
        }

        @Override
        protected String doInBackground(String... params) {
            String s = "";
            try{
                URL url = new URL(Config.SERVER_URL + "get_comments.php?publication_id=" + String.valueOf(publication_id));
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
                    mList.add(new CommentProvider(publication_id, comment_id, user_id, username, comment));
                    notifyDataSetChanged();
                    commentAdapterCallback.onDataInsertedCallback();
                }
            }catch (Exception e){
                Log.i("Comment adapter","errorrrr");
            }
            notifyDataSetChanged();
            Log.i("Comment adapter","dataSet changed");
        }
    }

}
