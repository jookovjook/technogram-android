package com.jookovjook.chatapp.network;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.jookovjook.chatapp.new_new_pub.ImageProvider;
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
import java.util.ArrayList;

public class AddImagesToPost extends AsyncTask<String, Void, String> {

    private JSONArray jsonArray;

    public AddImagesToPost(int publication_id, ArrayList<ImageProvider> mList, Context context){
        this.jsonArray = new JSONArray();
        try{
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token", AuthHelper.getToken(context));
            jsonObject.put("publication_id", publication_id);
            jsonArray.put(jsonObject);
            for (int i = 1; i < mList.size(); i++){
                jsonObject = new JSONObject();
                jsonObject.put("_id", mList.get(i).get_id());
                jsonObject.put("filename", mList.get(i).getFilename());
                jsonArray.put(jsonObject);
            }
        }catch (Exception e){
            Log.i("addImages","Error creating addImages object");
        }
    }

    @Override
    protected String doInBackground(String... strings) {
        String s = "";
        try{
            URL url = new URL(Config.ADD_IMAGES_TO_PUBLICATION_URL);
            HttpURLConnection mUrlConnection = (HttpURLConnection) url.openConnection();
            mUrlConnection.setDoOutput(true);
            mUrlConnection.setDoInput(true);
            mUrlConnection.setRequestProperty("Content-Type","application/json");
            mUrlConnection.connect();
            OutputStreamWriter out = new OutputStreamWriter(mUrlConnection.getOutputStream());
            out.write(jsonArray.toString());
            out.close();
            InputStream inputStream = new BufferedInputStream(mUrlConnection.getInputStream());
            s = StreamReader.read(inputStream);
        }catch (Exception e){
            Log.i("addIMages", "doInBackground exception");
        }
        return s;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            Log.i("comment", s);
        }catch (Exception ignored) {}
    }
}
