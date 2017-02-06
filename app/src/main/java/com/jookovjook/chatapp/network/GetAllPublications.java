package com.jookovjook.chatapp.network;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.jookovjook.chatapp.feed_fragment.FeedCardProvider;
import com.jookovjook.chatapp.interfaces.NewGetPublicationsInterfase;
import com.jookovjook.chatapp.utils.AuthHelper;
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

public class GetAllPublications extends AsyncTask<String, Void, String> {

    private int type;
    private JSONObject jsonObject;
    private NewGetPublicationsInterfase getPsI;

    public GetAllPublications(Context context, int type, int param, NewGetPublicationsInterfase getPsI, int last_id){
        this.getPsI = getPsI;
        this.type = type;
        this.jsonObject = new JSONObject();
            try {
                if(type == 0) jsonObject.put("user_id", param);
                jsonObject.put("last_id", last_id);
                jsonObject.put("token", AuthHelper.getToken(context));
            } catch (Exception e) {
                Log.i("get publications", "error creating json");
            }
        Log.i("a","a");
    }

    @Override
    protected String doInBackground(String... params) {
        String s = "";
        try {
            URL url;
            if(type == 0) {
                url = new URL(Config.GET_USER_PUBLICATIONS_URL);
            }else{
                url = new URL(Config.GET_ALL_PUBLICATIONS_URL);
            }
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
            int last_id = -1;
            jsonArray = new JSONArray(jsonResult);
            Log.i("FeedCardAdapter", "jsonArrayLength = " + String.valueOf(jsonArray.length()));
            for(int i = 0; i<jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int publication_id = jsonObject.getInt("publication_id");
                last_id = publication_id;
                int user_id = jsonObject.getInt("user_id");
                String title = jsonObject.getString("title");
                int views = jsonObject.getInt("views");
                int stars = jsonObject.getInt("likes") + jsonObject.getInt("x2likes");
                int comments = jsonObject.getInt("comments");
                String username = jsonObject.getString("username");
                String img_link = jsonObject.getString("img_link");

//                String filenameArray[] = img_link.split("\\.");
//                String extension = filenameArray[filenameArray.length-1];
//                if(!extension.equals("png") & !extension.equals("jpg") & !extension.equals("jpeg") & !extension.equals("gif"))
//                    img_link = "grid.png";

                String text = jsonObject.getString("text");
                String datetime = jsonObject.getString("datetime");
                Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS").parse(datetime);
                String small_avatar = jsonObject.getString("small_avatar");

//                filenameArray = small_avatar.split("\\.");
//                extension = filenameArray[filenameArray.length-1];
//                if(!extension.equals("png") & !extension.equals("jpg") & !extension.equals("jpeg") & !extension.equals("gif"))
//                    small_avatar = "grid.png";

                int like = jsonObject.getInt("like");
                FeedCardProvider feedCardProvider = new FeedCardProvider
                        (publication_id, user_id, username, title, views, stars, comments, img_link,
                                text, date, small_avatar, like);
                getPsI.onGotPublication(feedCardProvider);
            }
            getPsI.onGotAll(last_id);
        }catch (Exception e){
            Log.i("FeedCardAdapter","error parsing json" + jsonResult);
        }
    }

}
