package com.jookovjook.chatapp.network;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.jookovjook.chatapp.utils.AuthHelper;
import com.jookovjook.chatapp.utils.Config;
import com.jookovjook.chatapp.utils.StreamReader;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class LogIn extends AsyncTask<String, Void, String> {

    private JSONObject jsonObject;
    private LogInCallback logInCallback;
    private Context context;
    private String username;

    public interface  LogInCallback{
        void onSuccess();
        void onWrongUsername();
        void onWrongPassword();
        void onUnknownError();
    }

    public LogIn(String username, String password, Context context, LogInCallback logInCallback){
        this.context = context;
        this.logInCallback = logInCallback;
        this.jsonObject = new JSONObject();
        this.username = username;
        AuthHelper.setUsername(context, username);
        try{
            jsonObject.put("username", username);
            jsonObject.put("password", password);
        }catch (Exception e){
            Log.i("user profile","error creating json");
        }
    }

    @Override
    protected String doInBackground(String... params) {
        String s = "";
        try{
            URL url;
            url = new URL(Config.AUTH_URL);
            HttpURLConnection mUrlConnection = (HttpURLConnection) url.openConnection();
            mUrlConnection.setDoOutput(true);
            mUrlConnection.setDoInput(true);
            mUrlConnection.setRequestProperty("Content-Type","application/json");
            mUrlConnection.connect();
            OutputStreamWriter out = new OutputStreamWriter(mUrlConnection.getOutputStream());
            out.write(jsonObject.toString());
            Log.i("Log in", jsonObject.toString());
            out.close();
            InputStream inputStream = new BufferedInputStream(mUrlConnection.getInputStream());
            s = StreamReader.read(inputStream);
        }catch (Exception e){
            Log.i("perform login","network error");
        }
        return s;
    }

    @Override
    protected void onPostExecute(String jsonResult) {
        super.onPostExecute(jsonResult);
        JSONObject jsonObject;
        Log.i("LogIn", jsonResult);
        try{
            jsonObject = new JSONObject(jsonResult);
            int error = jsonObject.getInt("error");
            Log.i("Login error", String.valueOf(error));
            switch (error){
                case 0:
                    String token = jsonObject.getString("token");
                    int user_id = jsonObject.getInt("user_id");
                    logInCallback.onSuccess();
                    AuthHelper.setUsername(context, username);
                    AuthHelper.setToken(context, token);
                    AuthHelper.setUserId(context, user_id);
                    break;
                case 1:
                    logInCallback.onWrongUsername();
                    AuthHelper.setUsername(context, "");
                    AuthHelper.setToken(context, "");
                    AuthHelper.setUserId(context, -1);
                    break;
                case 2:
                    logInCallback.onWrongPassword();
                    AuthHelper.setUsername(context, username);
                    AuthHelper.setToken(context, "");
                    AuthHelper.setUserId(context, -1);
                    break;
                default:
                    Log.i("LogIn", jsonResult);
                    logInCallback.onUnknownError();
                    AuthHelper.setUsername(context, "");
                    AuthHelper.setToken(context, "");
                    AuthHelper.setUserId(context, -1);
                    break;
            }
        }catch(Exception e){
            Log.i("LogIn", jsonResult);
            e.printStackTrace();
            logInCallback.onUnknownError();
            AuthHelper.setUsername(context, "");
            AuthHelper.setToken(context, "");
            AuthHelper.setUserId(context, -1);
        }
    }
}