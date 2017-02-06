package com.jookovjook.chatapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class AuthHelper {
    private final static String SHARED_PREF_NAME = "com.jookovjook.chatapp.auth.AUTH";
    private final static String TOKEN_KEY = "com.jookovjook.chatapp.auth.TOKEN_KEY";
    private final static String USERNAME = "com.jookovjook.chatapp.auth.USERNAME";
    private final static String USER_ID = "com.jookovjook.chatapp.auth.USER_ID";
    private final static String AVATAR = "com.jookovjook.chatapp.auth.AVATAR";

    public static String getToken(Context c) {
        SharedPreferences prefs = c.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(TOKEN_KEY, "");
    }

    public static String getUsername(Context c) {
        SharedPreferences prefs = c.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(USERNAME, "");
    }

    public static int getUserId(Context c){
        SharedPreferences prefs = c.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getInt(USER_ID, -1);
    }

    public static String getAvatar(Context c){
        SharedPreferences prefs = c.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(AVATAR, "");
    }

    public static void setToken(Context c, String token) {
        SharedPreferences prefs = c.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(TOKEN_KEY, token);
        editor.apply();
    }

    public static void setUsername(Context c, String username) {
        SharedPreferences prefs = c.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(USERNAME, username);
        editor.apply();
    }

    public static void setUserId(Context c, int user_id) {
        SharedPreferences prefs = c.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(USER_ID, user_id);
        editor.apply();
    }

    public static void setAvatar(Context c, String avatar){
        SharedPreferences prefs = c.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(AVATAR, avatar);
        editor.apply();
    }
}
