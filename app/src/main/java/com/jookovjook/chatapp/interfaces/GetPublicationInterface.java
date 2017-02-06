package com.jookovjook.chatapp.interfaces;

import java.util.Date;

public interface GetPublicationInterface{
    void onGotPublication(String title, String text, int views, int likes, int comments, String username, String avatar, Date date);
    void onGotPubImage(String url);
    void onFinishGettingImages();
}
