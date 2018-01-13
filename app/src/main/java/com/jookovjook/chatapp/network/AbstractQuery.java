package com.jookovjook.chatapp.network;

import android.os.AsyncTask;
import android.util.Log;

import com.jookovjook.chatapp.utils.StreamReader;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

class AbstractQuery extends AsyncTask<String, Void, String> {

    private AbstractQueryCallback callback;
    private String query;
    private String url;

    interface AbstractQueryCallback{
        void onPostExecute(String result);
    }

    public AbstractQuery(AbstractQueryCallback callback, String query, String url){
        this.callback = callback;
        this.query = query;
        this.url = url;
    }

    @Override
    protected String doInBackground(String... params) {
        String s = "";
        Log.i("Abstract query", this.url);
        try {
            URL url = new URL(this.url);
            HttpURLConnection mUrlConnection = (HttpURLConnection) url.openConnection();
            mUrlConnection.setDoOutput(true);
            mUrlConnection.setDoInput(true);
            mUrlConnection.setRequestProperty("Content-Type","application/json");
            mUrlConnection.connect();
            OutputStreamWriter out = new OutputStreamWriter(mUrlConnection.getOutputStream());
            out.write(query);
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
        Log.i("AbstractQuery", s);
        callback.onPostExecute(s);
    }
}
