package com.jookovjook.chatapp.network;

import android.os.AsyncTask;
import android.util.Log;

import com.jookovjook.chatapp.pub.CommentProvider;
import com.jookovjook.chatapp.utils.Config;
import com.jookovjook.chatapp.utils.StreamReader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class PostComment extends AsyncTask<String, Void, String> {

    private JSONObject jsonObject;

    public interface PostCommentI{
        void onSuccess(com.jookovjook.chatapp.pub.CommentProvider commentProvider);
    }

    private PostCommentI postCommentI;

    public PostComment(int publication_id, int user_id, String comment, PostCommentI postCommentI){
        this.jsonObject = new JSONObject();
        this.postCommentI = postCommentI;
        try {
            jsonObject.put("publication_id", publication_id);
            jsonObject.put("user_id", user_id);
            jsonObject.put("comment", comment);
        }catch (Exception e){
            Log.i("comment","error creating json");
        }
    }

    @Override
    protected String doInBackground(String... params) {
        String s = "";
        try{
            URL url = new URL(Config.POST_COMMENT_URL);
            HttpURLConnection mUrlConnection = (HttpURLConnection) url.openConnection();
            mUrlConnection.setDoOutput(true);
            mUrlConnection.setDoInput(true);
            mUrlConnection.setRequestProperty("Content-Type","application/json");
            mUrlConnection.connect();
            OutputStreamWriter out = new OutputStreamWriter(mUrlConnection.getOutputStream());
            out.write(jsonObject.toString());
            out.close();
            InputStream inputStream = new BufferedInputStream(mUrlConnection.getInputStream());
            s = StreamReader.read(inputStream);
        }catch (Exception e){}
        return s;
    }

    @Override
    protected void onPostExecute(String jsonResult) {
        super.onPostExecute(jsonResult);
        Log.i("posted", jsonResult);
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
                postCommentI.onSuccess(new CommentProvider(publication_id, comment_id, user_id, username, comment, avatar));
            }
        }catch (Exception e){
            Log.i("Comment adapter","errorrrr");
        }
        Log.i("Comment adapter","dataSet changed");
    }
}