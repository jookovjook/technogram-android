package com.jookovjook.chatapp.network;

import android.util.Log;

import com.jookovjook.chatapp.utils.Config;

import org.json.JSONException;
import org.json.JSONObject;

public class NewGetUserInfo implements AbstractQuery.AbstractQueryCallback {

    private GetUserInfoCallback getUIC;
    JSONObject jsonObject = new JSONObject();
    private AbstractQuery query;
    int user_id;

    public class UserInfo{
        public int user_id;
        public String username;
        public String avatar;
        public String name;
        public String surname;
        public String about;
        public int views;
        public int likes;
        public int x2likes;
        public int subs;

        UserInfo(int user_id, String username, String avatar, String name, String surname, String about, int views, int likes, int x2likes, int subs){
            this.user_id = user_id;
            this.avatar = avatar;
            this.username = username;
            this.name = name;
            this.surname = surname;
            this.about = about;
            this.views = views;
            this.likes = likes;
            this.x2likes = x2likes;
            this.subs = subs;
        }

    }

    public interface GetUserInfoCallback{
        void onGotUserInfo(UserInfo userInfo);
        void onErrorGettingUserInfo();
    }

    public NewGetUserInfo(int user_id, GetUserInfoCallback getUIC){
        this.getUIC = getUIC;
        this.user_id = user_id;
        try{
            jsonObject.put("user_id", user_id);
            query = new AbstractQuery(this, jsonObject.toString(), Config.GET_USER_INFO_URL);
        } catch (JSONException e) {
            e.printStackTrace();
            getUIC.onErrorGettingUserInfo();
        }
    }

    public void execute(){
        if(query != null) {
            query.execute();
        }else{
            getUIC.onErrorGettingUserInfo();
        }
    }

    @Override
    public void onPostExecute(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            int error = jsonObject.getInt("error_code");
            if(error == 1){
                getUIC.onErrorGettingUserInfo();
            }else{
                UserInfo userInfo = new UserInfo(user_id,
                        jsonObject.getString("username"),
                        jsonObject.getString("img_link"),
                        jsonObject.getString("name"),
                        jsonObject.getString("surname"),
                        jsonObject.getString("about"),
                        jsonObject.getInt("views"),
                        jsonObject.getInt("likes"),
                        jsonObject.getInt("x2likes"),
                        jsonObject.getInt("subs"));
                Log.i("a", "a");
                getUIC.onGotUserInfo(userInfo);
            }
        }catch (Exception e){
            e.printStackTrace();
            Log.i("income json:", result);
            getUIC.onErrorGettingUserInfo();
        }
    }
}
