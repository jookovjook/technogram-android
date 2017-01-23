package com.jookovjook.chatapp.network;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.jookovjook.chatapp.utils.AndroidMultiPartEntity;
import com.jookovjook.chatapp.utils.AuthHelper;
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

public class UploadImage extends AsyncTask<Void, Integer, String> {

    private long totalSize;
    private int code;
    private String message;
    private String uri;
    private UploadImageCallback uploadImageCallback;
    private String token;
    private String filename;
    private int id;


    public interface UploadImageCallback{
        void onStartUploading();
        void onSuccess(String filename, int id);
        void onFailure(int code, String message);
        void onUpdateProgress(int progress);
    }

    public UploadImage(String token, String uri, UploadImageCallback uploadImageCallback){
        this.token = token;
        this.uri = uri;
        this.uploadImageCallback = uploadImageCallback;
    }

    public UploadImage(Context context, String uri, UploadImageCallback uploadImageCallback){
        this.token = AuthHelper.getToken(context);
        this.uri = uri;
        this.uploadImageCallback = uploadImageCallback;
    }

    @Override
    protected void onPreExecute() {
        uploadImageCallback.onStartUploading();
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        uploadImageCallback.onUpdateProgress(progress[0]);
    }

    @Override
    protected String doInBackground(Void... params) {
        return uploadFile();
    }

    @SuppressWarnings("deprecation")
    private String uploadFile() {
        String responseString;
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
            entity.addPart("token", new StringBody(token));
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
            uploadImageCallback.onFailure(code, message);
            responseString = e.toString();
        } catch (IOException e) {
            code = 210;
            message = "Client: IOlException.";
            Log.i("uploading file: ", message);
            uploadImageCallback.onFailure(code, message);
            responseString = e.toString();
        }
        return responseString;
    }

    @Override
    protected void onPostExecute(String jsonResult) {
        Log.i("TAG", "Response from server: " + jsonResult);
        super.onPostExecute(jsonResult);
        if(code == 209 || code == 210 ){
            uploadImageCallback.onFailure(code, "unknown error");
        }else {
            Boolean error;
            try {
                JSONObject jsonObject = new JSONObject(jsonResult);
                error = jsonObject.getBoolean("error");
                code = jsonObject.getInt("code");
                message = jsonObject.getString("message");
                filename = jsonObject.getString("filename");
                id = jsonObject.getInt("_id");
                Log.i("UploadImage", String.valueOf(id));
            } catch (Exception e) {
                error = true;
                code = 208;
                message = "Client: error parsing income JSON";
                uploadImageCallback.onFailure(code, message);
                Log.i("uploading file: ", message);
            }
            if (!error) {
                uploadImageCallback.onSuccess(filename, id);
                Log.i("uploading file: ", message);
            } else {
                uploadImageCallback.onFailure(code, message);
                Log.i("uploading file: ", message);
            }
        }

    }

}
