package com.jookovjook.chatapp.interfaces;

public interface RegisterInterface {
    void onSuccess(int user_id, String token);
    void onFailure(int error, String message);
}
