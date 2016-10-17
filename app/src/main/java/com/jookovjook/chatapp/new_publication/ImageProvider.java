package com.jookovjook.chatapp.new_publication;

/**
 * Created by jookovjook on 17/10/16.
 */

public class ImageProvider {

    private String mediaUri;

    ImageProvider(String mediaUri){
        this.mediaUri = mediaUri;
    }

    public void setMediaUri(String mediaUri) {
        this.mediaUri = mediaUri;
    }

    public String getMediaUri() {
        return mediaUri;
    }
}
