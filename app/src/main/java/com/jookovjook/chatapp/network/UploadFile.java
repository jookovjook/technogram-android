package com.jookovjook.chatapp.network;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.jookovjook.chatapp.new_publication.ImageAdapter;
import com.jookovjook.chatapp.new_publication.ImageProvider;
import com.jookovjook.chatapp.utils.AndroidMultiPartEntity;
import com.jookovjook.chatapp.utils.Config;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

public class UploadFile extends AsyncTask<Void, Integer, String> {

    private ProgressBar progressBar;
    private ImageButton refreshImageButton;
    private View haze;
    private long totalSize;
    private String uri;
    private ImageProvider imageProvider;
    private ImageAdapter.VHItem vhItem;
    private int code;
    private String message;
    private Boolean error;

    public UploadFile(ImageProvider imageProvider, ImageAdapter.VHItem vhItem){
        //this.holder = holder;
        this.code = 207;
        this.message = "Client: error connecting to sever.";
        this.error = true;
        this.imageProvider = imageProvider;
        this.vhItem = vhItem;
        this.uri = imageProvider.getUri();
        this.progressBar = null;
        this.refreshImageButton = null;
        this.haze = null;
        if(vhItem != null ) {
            this.progressBar = vhItem.progressBar;
            this.haze = vhItem.haze;
            this.refreshImageButton = vhItem.refreshImageButton;
        }
        totalSize = 0;
    }

    public void setVhItem(ImageAdapter.VHItem vhItem) {
        this.vhItem = vhItem;
        if(vhItem != null) {
            this.progressBar = vhItem.progressBar;
            this.haze = vhItem.haze;
            this.refreshImageButton = vhItem.refreshImageButton;
        }
    }

    @Override
    protected void onPreExecute() {
        // setting progress bar to zero
        if(progressBar != null) progressBar.setProgress(0);
        if(refreshImageButton != null) refreshImageButton.setVisibility(View.INVISIBLE);
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        // Making progress bar visible
        imageProvider.setUploading_status((float) progress[0]);
        // updating progress bar value
        if(progressBar != null) {
            if(progress[0] >= 100 && imageProvider.getUploaded()) {
                progressBar.setVisibility(View.INVISIBLE);
            }
            progressBar.setProgress(progress[0]);
        }
        if(progress[0] >= 100 && haze!= null && imageProvider.getUploaded()){
            haze.setAlpha(0f);
        }
        if(progress[0] >= 100 && refreshImageButton!=null && !imageProvider.getUploaded()){
            refreshImageButton.setVisibility(View.INVISIBLE);
        }
        // updating percentage value
        //txtPercentage.setText(String.valueOf(progress[0]) + "%");
    }

    @Override
    protected String doInBackground(Void... params) {
        return uploadFile();
    }

    @SuppressWarnings("deprecation")
    private String uploadFile() {
        String responseString = null;
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(Config.FILE_UPLOAD_URL);
        try {
            AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                    new AndroidMultiPartEntity.ProgressListener() {
                        @Override
                        public void transferred(long num) {
                            publishProgress((int) ((num / (float) totalSize) * 100));
                        }
                    });
            File sourceFile = new File(uri);
            // Adding file data to http body
            entity.addPart("image", new FileBody(sourceFile));
            // Extra parameters if you want to pass to server
            entity.addPart("token", new StringBody(Config.TOKEN));
            totalSize = entity.getContentLength();
            httppost.setEntity(entity);
            // Making server call
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity r_entity = response.getEntity();
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                // Server response
                responseString = EntityUtils.toString(r_entity);
            } else {
                responseString = "Error occurred! Http Status Code: "
                        + statusCode;
            }
        } catch (ClientProtocolException e) {
            code = 209;
            message = "Client: ClientProtocolException.";
            Log.i("uploading file: ", message);
            progressBar.setProgress(0);
            imageProvider.setUploading_status(100f);
            responseString = e.toString();
        } catch (IOException e) {
            code = 210;
            message = "Client: IOlException.";
            Log.i("uploading file: ", message);
            progressBar.setProgress(0);
            imageProvider.setUploading_status(100f);
            responseString = e.toString();
        }
        return responseString;
    }

    @Override
    protected void onPostExecute(String jsonResult) {
        Log.i("TAG", "Response from server: " + jsonResult);
        super.onPostExecute(jsonResult);
        if(code == 209 || code == 210 ){
            if(refreshImageButton!=null)refreshImageButton.setVisibility(View.VISIBLE);
            if(progressBar!=null)progressBar.setProgress(0);
        }else {
            try {
                JSONObject jsonObject = new JSONObject(jsonResult);
                error = jsonObject.getBoolean("error");
                code = jsonObject.getInt("code");
                message = jsonObject.getString("message");
                imageProvider.setFilename(jsonObject.getString("filename"));
                imageProvider.set_id(jsonObject.getInt("_id"));
            } catch (Exception e) {
                error = true;
                code = 208;
                message = "Client: error parsing income JSON";
                if (refreshImageButton != null) refreshImageButton.setVisibility(View.VISIBLE);
                if (progressBar != null) progressBar.setProgress(0);
                Log.i("uploading file: ", message);
            }
            imageProvider.setUploaded(!error);
            if (error == false) {
                if (progressBar != null) progressBar.setVisibility(View.INVISIBLE);
                if (haze != null) haze.setAlpha(0f);
                if (refreshImageButton != null) refreshImageButton.setVisibility(View.INVISIBLE);
                imageProvider.setUploading_status(0f);
                imageProvider.setUploaded(true);
                Log.i("uploading file: ", message);
            } else {
                if (progressBar != null) {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(0);
                }
                if (haze != null) {
                    haze.setAlpha(0.5f);
                }
                if (refreshImageButton != null) {
                    refreshImageButton.setVisibility(View.VISIBLE);
                }
                imageProvider.setUploaded(false);
                imageProvider.setUploading_status(100f);
                Log.i("uploading file: ", message);
            }
        }

    }

}
