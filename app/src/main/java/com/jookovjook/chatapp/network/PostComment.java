package com.jookovjook.chatapp.network;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.jookovjook.chatapp.pub.CommentProvider;
import com.jookovjook.chatapp.utils.AuthHelper;
import com.jookovjook.chatapp.utils.Config;
import com.jookovjook.chatapp.utils.StreamReader;

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
        void onFailure();
    }

    private PostCommentI postCommentI;

    public PostComment(int publication_id, int user_id, String comment, PostCommentI postCommentI, Context context){
        this.jsonObject = new JSONObject();
        this.postCommentI = postCommentI;
        try {
            jsonObject.put("publication_id", publication_id);
            jsonObject.put("user_id", user_id);
            jsonObject.put("comment", comment);
            jsonObject.put("token", AuthHelper.getToken(context));
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
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(jsonResult);
            int error = jsonObject.getInt("error");
            if(error == 1){
                postCommentI.onFailure();
            }else {
                String username = jsonObject.getString("username");
                int comment_id = jsonObject.getInt("comment_id");
                int user_id = jsonObject.getInt("user_id");
                String comment = jsonObject.getString("comment");
                int publication_id = jsonObject.getInt("publication_id");
                String avatar = jsonObject.getString("img_link");
                if (comment_id > 0) {
                    postCommentI.onSuccess(new CommentProvider(publication_id, comment_id, user_id, username, comment, avatar));
                }
            }
        }catch (Exception e){
            Log.i("Comment adapter","errorrrr");
        }
        Log.i("Comment adapter","dataSet changed");
    }
}