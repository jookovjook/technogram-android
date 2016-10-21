package com.jookovjook.chatapp.network;

import android.os.AsyncTask;
import android.util.Log;

import com.jookovjook.chatapp.interfaces.GetUserAvatar;
import com.jookovjook.chatapp.utils.Config;
import com.jookovjook.chatapp.utils.StreamReader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetUserSmallAvatar extends AsyncTask<String, Void, String> {

    private JSONObject jsonObject;
    private GetUserAvatar getUA;

    public GetUserSmallAvatar(int user_id, GetUserAvatar getUA){
        this.getUA = getUA;
        this.jsonObject = new JSONObject();
        try{
            jsonObject.put("user_id", user_id);
        }catch (JSONException e){

        }
    }

    @Override
    protected String doInBackground(String... params) {
        String s = "";
        try {
            URL url = new URL(Config.GET_USER_SMALL_AVATAR_URL);
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
            Log.i("get_user_avatar","error");
        }
        return s;
    }

    @Override
    protected void onPostExecute(String jsonResult) {
        super.onPostExecute(jsonResult);
        Log.i("get_user_avatar",jsonResult);
        try {
            JSONObject jsonObject = new JSONObject(jsonResult);
            getUA.onGotUserAvatar(Config.IMAGE_RESOURCES_URL + jsonObject.getString("img_link"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
