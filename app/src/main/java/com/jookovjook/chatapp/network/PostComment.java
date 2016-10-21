package com.jookovjook.chatapp.network;

import android.os.AsyncTask;
import android.util.Log;

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

    public PostComment(int publication_id, int user_id, String comment){
        this.jsonObject = new JSONObject();
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
}