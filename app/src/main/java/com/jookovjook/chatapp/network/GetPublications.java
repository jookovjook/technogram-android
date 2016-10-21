package com.jookovjook.chatapp.network;

import android.os.AsyncTask;
import android.util.Log;

import com.jookovjook.chatapp.feed_fragment.FeedCardProvider;
import com.jookovjook.chatapp.interfaces.GetPublicationsInterfase;
import com.jookovjook.chatapp.utils.Config;
import com.jookovjook.chatapp.utils.StreamReader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GetPublications extends AsyncTask<String, Void, String> {

    private int type;
    private JSONObject jsonObject;
    private GetPublicationsInterfase getPsI;

    public GetPublications(int type, int param, GetPublicationsInterfase getPsI){
        this.getPsI = getPsI;
        this.type = type;
        if(type == 0) {
            this.jsonObject = new JSONObject();
            try {
                jsonObject.put("user_id", param);
            } catch (Exception e) {
                Log.i("get publications", "error creating json");
            }
        }
    }

    @Override
    protected String doInBackground(String... params) {
        String s = "";
        try {
            URL url;
            if(type == 0) {
                url = new URL(Config.GET_USER_PUBLICATIONS_URL);
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
            }else {
                url = new URL(Config.GET_ALL_PUBLICATIONS_URL);
                HttpURLConnection mUrlConnection = (HttpURLConnection) url.openConnection();
                mUrlConnection.setDoInput(true);
                InputStream inputStream = new BufferedInputStream(mUrlConnection.getInputStream());
                s = StreamReader.read(inputStream);
            }
        }catch (Exception e){
            Log.i("FeedCardAdapter","error getting json answer");
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
                int publication_id = jsonObject.getInt("publication_id");
                int user_id = jsonObject.getInt("user_id");
                String title = jsonObject.getString("title");
                int views = jsonObject.getInt("views");
                int stars = jsonObject.getInt("stars");
                int comments = jsonObject.getInt("comments");
                String username = jsonObject.getString("username");
                String img_link = jsonObject.getString("img_link");
                String text = jsonObject.getString("text");
                String datetime = jsonObject.getString("datetime");
                Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS").parse(datetime);
                String small_avatar = jsonObject.getString("small_avatar");
                FeedCardProvider feedCardProvider = new FeedCardProvider
                        (publication_id, user_id, username, title, views, stars, comments, img_link, text, date, small_avatar);
                getPsI.onGotPublication(feedCardProvider);
            }
        }catch (Exception e){
            Log.i("FeedCardAdapter","error parsing json");
        }
    }

}
