package com.jookovjook.chatapp.network;

import android.os.AsyncTask;
import android.util.Log;

import com.jookovjook.chatapp.interfaces.GetPublicationInterface;
import com.jookovjook.chatapp.utils.Config;
import com.jookovjook.chatapp.utils.StreamReader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetPublication extends AsyncTask<String, Void, String> {

    private JSONObject jsonObject;
    private GetPublicationInterface getPI;

    public GetPublication(int publication_id, GetPublicationInterface getPI){
        this.getPI = getPI;
        this.jsonObject = new JSONObject();
        try{
            jsonObject.put("publication_id", publication_id);
        }catch (Exception e){
            Log.i("get publication", "error creating json object");
        }
    }

    @Override
    protected String doInBackground(String... params) {
        String s = "";
        try{
            URL url = new URL(Config.GET_PUBLICATION_URL);
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
        }catch (Exception ignored){}
        return s;
    }

    @Override
    protected void onPostExecute(String jsonResult) {
        super.onPostExecute(jsonResult);
        try{
            JSONArray jsonArray = new JSONArray(jsonResult);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            String title = jsonObject.getString("title");
            String text = jsonObject.getString("text");
            int views = jsonObject.getInt("views");
            int stars = jsonObject.getInt("stars");
            int comments = jsonObject.getInt("comments");
            String username = jsonObject.getString("username");
            getPI.onGotPublication(title, text, views, stars, comments, username);
        } catch (Exception e){
            Log.i("get publication","error parsing income json");
        }
    }
}
