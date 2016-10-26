package com.jookovjook.chatapp.network;

import android.os.AsyncTask;
import android.util.Log;

import com.jookovjook.chatapp.new_pub.LinkProvider;
import com.jookovjook.chatapp.new_publication.ImageProvider;
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

public class MakePost extends AsyncTask<String, Void, String> {

    private JSONArray jsonArray;
    private ArrayList<ImageProvider> mList;
    private ArrayList<LinkProvider> nList;
    private int license;
    private int stage;

    public interface MakePostCalllback{
        void onMakePostError();
        void onAddImagesError();
        void onAddAdvError();
        void onAddLinksError();
    }

    private MakePostCalllback makePostCalllback;

    public MakePost(String title, String description, ArrayList<ImageProvider> mList, int branch
                        , MakePostCalllback makePostCalllback, int license, int stage, ArrayList<LinkProvider> nList){
        this.jsonArray = new JSONArray();
        this.makePostCalllback = makePostCalllback;
        this.mList = mList;
        this.nList = nList;
        this.license = license;
        this.stage = stage;
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("title", title);
            jsonObject.put("description", description);
            jsonObject.put("user_id", 0);
            jsonObject.put("token", Config.TOKEN);
            jsonObject.put("branch", branch);
            jsonArray.put(jsonObject);
            for(int i = 0; i < 1; i++){
                jsonObject = new JSONObject();
                jsonObject.put("_id",mList.get(i).get_id());
                jsonObject.put("filename",mList.get(i).getFilename());
                jsonArray.put(jsonObject);
            }
        }catch (Exception e){

        }
    }

    @Override
    protected String doInBackground(String... strings) {
        String s = "";
        try{
            URL url = new URL(Config.MAKE_POST_URL);
            HttpURLConnection mUrlConnection = (HttpURLConnection) url.openConnection();
            mUrlConnection.setDoOutput(true);
            mUrlConnection.setDoInput(true);
            mUrlConnection.setRequestProperty("Content-Type","application/json");
            mUrlConnection.connect();
            OutputStreamWriter out = new OutputStreamWriter(mUrlConnection.getOutputStream());
            out.write(jsonArray.toString());
            out.close();
            Log.i("comment", "getting input stream");
            InputStream inputStream = new BufferedInputStream(mUrlConnection.getInputStream());
            s = StreamReader.read(inputStream);
        }catch (Exception e){
            Log.i("comment", "doInBackground exception");
        }
        return s;
    }

    @Override
    protected void onPostExecute(String s) {
        Log.i("comment", "onPostExecute running");
        Log.i("answer: ", s);
        super.onPostExecute(s);
        boolean error = true;
        int publication_id = -1;
        try {
            JSONObject jsonObject = new JSONObject(s);
            publication_id = jsonObject.getInt("publication_id");
            error = jsonObject.getBoolean("error");
            Log.i("comment", "got");
            Log.i("comment", s);
        }catch (Exception e){
            makePostCalllback.onMakePostError();
        }
        if(error){
            makePostCalllback.onMakePostError();
        }else{
            if(mList.size()>1){
                AddImagesToPost addImagesToPost = new AddImagesToPost(publication_id, mList);
                addImagesToPost.execute();
            }
            AddAdvToSoft addAdvToSoft = new AddAdvToSoft(publication_id, license, stage);
            addAdvToSoft.execute();
            if (nList.size() > 0) {
                AddLinksToSoft addLinksToSoft = new AddLinksToSoft(publication_id, nList);
                addLinksToSoft.execute();
            }
        }
    }
}
