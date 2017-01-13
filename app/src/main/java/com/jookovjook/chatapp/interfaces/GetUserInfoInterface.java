package com.jookovjook.chatapp.interfaces;

public interface GetUserInfoInterface {
    void onGotUserInfo(String username, String name, String surname, String avatar_link);
    void onWrongToken();
}
