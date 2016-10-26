package com.jookovjook.chatapp.interfaces;

import java.util.Date;

public interface GetPublicationInterface{
    void onGotPublication(String title, String text, int views, int stars, int comments, String username, String avatar, Date date);
    void onGotSoftAdv(int license, int stage);
    void onGotPubImage(String url);
    void onFinishGettingImages();
}
