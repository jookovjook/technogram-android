package com.jookovjook.chatapp.network;

import android.os.AsyncTask;
import android.util.Log;

import com.jookovjook.chatapp.interfaces.RegisterInterface;
import com.jookovjook.chatapp.utils.Config;
import com.jookovjook.chatapp.utils.StreamReader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Register extends AsyncTask<String, Void, String> {

    private JSONObject jsonObject;
    private RegisterInterface regI;

    public Register(String username, String password, String email, RegisterInterface regI){
        this.regI = regI;
        this.jsonObject = new JSONObject();
        try{
            jsonObject.put("username", username);
            jsonObject.put("password", password);
            jsonObject.put("email", email);
        }catch (Exception e){
            Log.i("user profile","error creating json");
        }
    }

    @Override
    protected String doInBackground(String... params) {
        String s = "";
        try {
            URL url = new URL(Config.REGISTER_URL);
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
        int error = 5;
        try {
            JSONObject jsonObject = new JSONObject(jsonResult);
            error = jsonObject.getInt("error");
            if(error == 0){
                regI.onSuccess(jsonObject.getInt("user_id"), jsonObject.getString("token"));
            }else{
                regI.onFailure(jsonObject.getInt("error"), jsonObject.getString("message"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            regI.onFailure(error, "unknown 5");
            Log.i("Reg", jsonResult);
        }
    }
}
