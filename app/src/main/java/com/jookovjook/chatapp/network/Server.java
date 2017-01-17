package com.jookovjook.chatapp.network;

import android.os.AsyncTask;
import android.os.Handler;

import com.jookovjook.chatapp.utils.Config;
import com.jookovjook.chatapp.utils.StreamReader;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Server extends AsyncTask<String, Void, String> {

    private ServerCallback callback;

    public interface ServerCallback{
        void onAvailable();
        void onNotAvailable();
    }

    public Server(ServerCallback callback){
        this.callback = callback;
    };

    @Override
    protected String doInBackground(String... params) {
        String s = "";
        try {
            URL url = new URL(Config.CHECK_URL);
            HttpURLConnection mUrlConnection = (HttpURLConnection) url.openConnection();
            mUrlConnection.setDoOutput(true);
            mUrlConnection.setDoInput(true);
            mUrlConnection.setRequestProperty("Content-Type", "application/json");
            mUrlConnection.connect();
            OutputStreamWriter out = new OutputStreamWriter(mUrlConnection.getOutputStream());
            out.close();
            InputStream inputStream = new BufferedInputStream(mUrlConnection.getInputStream());
            s = StreamReader.read(inputStream);
        }catch (Exception e){
            e.printStackTrace();
        }
        return s;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if(s.equals("1")){
            callback.onAvailable();
        }else{
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    callback.onNotAvailable();
                }
            }, 1000);
        }
    }
}
