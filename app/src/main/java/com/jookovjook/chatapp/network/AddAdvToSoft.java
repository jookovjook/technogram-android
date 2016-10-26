package com.jookovjook.chatapp.network;

import android.os.AsyncTask;
import android.util.Log;

import com.jookovjook.chatapp.utils.Config;
import com.jookovjook.chatapp.utils.StreamReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class AddAdvToSoft extends AsyncTask<String, Void, String> {

    private JSONArray jsonArray;

    public AddAdvToSoft(int publication_id, int license, int stage){
        jsonArray = new JSONArray();
        try{
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("publication_id", publication_id);
            jsonObject.put("token", Config.TOKEN);
            jsonObject.put("license", license);
            jsonObject.put("stage", stage);
            jsonArray.put(jsonObject);
        }catch (JSONException e){
            Log.i("add_adv_to_soft","error creating json");
        }
    }

    @Override
    protected String doInBackground(String... params) {
        String s = "";
        try{
            URL url = new URL(Config.ADD_ADV_TO_SOFT_URL);
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
            Log.i("add_adv_to_soft","error do in background");
        }
        return s;
    }

    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            Log.i("comment_add_adv_to_soft", s);
        }catch (Exception ignored) {
            Log.i("add_adv_to_soft","error parsing json");
        }
    }
}
