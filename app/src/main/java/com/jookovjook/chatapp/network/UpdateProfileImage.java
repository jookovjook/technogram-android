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

public class UpdateProfileImage extends AsyncTask<String, Void, String> {

    private JSONObject jsonObject;
    private UpdateProfileImageCallback uPC = null;

    public interface UpdateProfileImageCallback{
        void onSuccess(String img_link);
        void onFailure();
    }

    public UpdateProfileImage(Context context, String img_link, int id, UpdateProfileImageCallback uPC){
        this.jsonObject = new JSONObject();
        this.uPC = uPC;
        try {
            jsonObject.put("token", AuthHelper.getToken(context));
            jsonObject.put("img_link", img_link);
            jsonObject.put("id", id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String doInBackground(String... params) {
        String s = "";
        try {
            URL url = new URL(Config.UPDATE_PROFILE_IMAGE_URL);
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
        Log.i("update profile image", jsonResult);
        try {
            JSONObject jsonObject = new JSONObject(jsonResult);
            int img_link = jsonObject.getInt("img_link");
            if(img_link == 0){
                Log.i("UpdateProfileImage", "success");
                uPC.onSuccess(this.jsonObject.getString("img_link"));
            }else{
                Log.i("IpdateProfileImage", "failure");
                uPC.onFailure();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
