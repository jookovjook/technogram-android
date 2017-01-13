package com.jookovjook.chatapp.network;

import android.os.AsyncTask;
import android.util.Log;

import com.jookovjook.chatapp.interfaces.CheckTokenInterface;
import com.jookovjook.chatapp.utils.Config;
import com.jookovjook.chatapp.utils.StreamReader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class CheckToken extends AsyncTask<String, Void, String> {

    private String token;
    private int user_id;
    private JSONObject jsonObject;
    private CheckTokenInterface cTI;

    public CheckToken(String token, CheckTokenInterface cTI){
        this.token = token;
        this.cTI = cTI;
        this.user_id = -1;
        jsonObject = new JSONObject();
        try{
            jsonObject.put("token", token);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected String doInBackground(String... params) {
        String s = "";
        try {
            URL url = new URL(Config.CHECK_TOKEN_URL);
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
           e.printStackTrace();
        }
        return s;
    }

    @Override
    protected void onPostExecute(String jsonResult) {
        super.onPostExecute(jsonResult);
        Log.i("jsonResult", jsonResult);
        try {
            JSONObject jsonObject = new JSONObject(jsonResult);
            user_id = jsonObject.getInt("user_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        cTI.onTokenChecked(user_id);
    }
}
