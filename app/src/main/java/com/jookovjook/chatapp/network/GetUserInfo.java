package com.jookovjook.chatapp.network;

import android.os.AsyncTask;
import android.util.Log;

import com.jookovjook.chatapp.interfaces.GetUserInfoInterface;
import com.jookovjook.chatapp.utils.Config;
import com.jookovjook.chatapp.utils.StreamReader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetUserInfo extends AsyncTask<String, Void, String> {

    private int user_id = -1;
    private JSONObject jsonObject;
    private GetUserInfoInterface getUII;

    public GetUserInfo(int user_id, GetUserInfoInterface getUII){
        this.getUII = getUII;
        this.user_id = user_id;
        this.jsonObject = new JSONObject();
        try{
            jsonObject.put("user_id", user_id);
        }catch (Exception e){
            Log.i("user profile","error creating json");
        }
    }


    @Override
    protected String doInBackground(String... params) {
        String s = "";
        try {
            Log.i("setting url = ", String.valueOf(user_id));
            URL url = new URL(Config.GET_USER_INFO_URL);
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
            Log.i("get_user_info","error");
        }
        return s;
    }

    @Override
    protected void onPostExecute(String jsonResult) {
        super.onPostExecute(jsonResult);
        int error_code;
        try {
            JSONObject jsonObject = new JSONObject(jsonResult);
            error_code = jsonObject.getInt("error_code");
            if(error_code == 0){
                getUII.onGotUserInfo(jsonObject.getString("username")
                        ,jsonObject.getString("name")
                        ,jsonObject.getString("surname")
                        ,jsonObject.getString("img_link"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
