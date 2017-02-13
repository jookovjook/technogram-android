package com.jookovjook.chatapp.new_pub;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.jookovjook.chatapp.R;
import com.jookovjook.chatapp.network.UploadFile;

public class ImageProvider implements CalculateColor.CalculateColorCallback{

    private String uri;
    private Boolean uploaded;
    private float uploading_status;
    private String filename;
    private int _id;
    private ImageAdapter.VHItem vhItem;
    private UploadFile uploadFile;
    private Context context;
    public int color = 0;

    public ImageProvider(String uri, Context context){
        Log.i("uri", uri);
        this.context = context;
        this._id = -1;
        this.filename = null;
        this.uri = uri;
        this.uploaded = false;
        this.uploading_status = 0f;
        this.filename = null;
        this.vhItem = null;
        uploadFile = new UploadFile(this, vhItem, context);
        uploadFile.execute();
        this.color = 0;
        CalculateColor calculateColor = new CalculateColor(uri, context, this);
        Thread thread = new Thread(calculateColor);
        thread.start();
    }

    public void reload(){
        uploadFile = new UploadFile(this, vhItem, context);
        uploadFile.execute();
    }

    public void setUploaded(Boolean uploaded) {
        this.uploaded = uploaded;
    }

    public void setUploading_status(float uploading_status) {
        this.uploading_status = uploading_status;
    }

    public void setVhItem(ImageAdapter.VHItem vhItem) {
        this.vhItem = vhItem;
        uploadFile.setVhItem(vhItem);
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getUri() {
        return uri;
    }

    public Boolean getUploaded() {
        return uploaded;
    }

    public float getUploading_status() {
        return uploading_status;
    }

    public int get_id() {
        return _id;
    }

    public String getFilename() {
        return filename;
    }

    @Override
    public void onColorCalculated(int color) {
        this.color = color;

        if(vhItem != null){
            if(color == 1){
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        vhItem.deleteImageButton.setImageResource(R.drawable.ic_cancel_white);
                    }
                });

            }
        }
    }


}
