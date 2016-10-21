package com.jookovjook.chatapp.interfaces;

public interface GetPublicationInterface{
    void onGotPublication(String title, String text, int views, int stars, int comments, String username);
    void onGotPubImage(String url);
    void onFinishGettingImages();
}
