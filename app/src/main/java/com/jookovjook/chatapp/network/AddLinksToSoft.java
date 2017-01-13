package com.jookovjook.chatapp.network;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.jookovjook.chatapp.new_pub.LinkProvider;
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

public class AddLinksToSoft extends AsyncTask<String, Void, String> {

    private JSONArray jsonArray;

    public AddLinksToSoft(int publication_id, ArrayList<LinkProvider> nList, Context context){
        this.jsonArray = new JSONArray();
        try{
            JSONObject jsonObject= new JSONObject();
            jsonObject.put("publication_id", publication_id);
            jsonObject.put("token", AuthHelper.getToken(context));
            jsonArray.put(jsonObject);
            for(int i = 0; i< nList.size(); i++){
                jsonObject = new JSONObject();
                jsonObject.put("link",nList.get(i).link);
                jsonObject.put("type", nList.get(i).getType());
                jsonArray.put(jsonObject);
            }
        }catch(Exception e){
            Log.i("add_links_to_soft","error creating json");
        }
    }

    void Test(){
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArray1 = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("hi",1);
            jsonObject.put("you",1);
            jsonArray.put(jsonObject);
            jsonArray1.put(jsonArray);
        }
        catch (Exception ignore){}

    }

    @Override
    protected String doInBackground(String... params) {
        String s = "";
        try{
            URL url = new URL(Config.ADD_LINKS_TO_SOFT_URL);
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
            Log.i("add_links_to_soft","error do in background");
        }
        return s;
    }

    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            Log.i("add_links_to_soft", s);
        }catch (Exception ignored) {
            Log.i("add_links_to_soft", "error parsing json");
        }
    }
}
