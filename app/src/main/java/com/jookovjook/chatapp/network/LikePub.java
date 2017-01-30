package com.jookovjook.chatapp.network;

import android.content.Context;
import android.util.Log;

import com.jookovjook.chatapp.utils.AuthHelper;
import com.jookovjook.chatapp.utils.Config;

import org.json.JSONException;
import org.json.JSONObject;

public class LikePub implements AbstractQuery.AbstractQueryCallback{

    JSONObject jsonObject = new JSONObject();
    private LikePubCallback callback;
    private AbstractQuery query;

    public interface LikePubCallback{
        void onDisliked();
        void onLiked();
        void onDoubleLiked();
        void onError();
    }

    public LikePub(LikePubCallback callback, Context context, int pub_id){
        this.callback = callback;
        try{
            jsonObject.put("token", AuthHelper.getToken(context));
            jsonObject.put("pub_id", pub_id);
            query = new AbstractQuery(this, jsonObject.toString(), Config.LIKE_PUB_URL);
        } catch (JSONException e) {
            e.printStackTrace();
            callback.onError();
        }
    }

    public void execute(){
        if(query != null) {
            query.execute();
        }else{
            callback.onError();
        }
    }

    @Override
    public void onPostExecute(String result) {
        Log.i("LikePub", result);
        try {
            JSONObject jsonObject = new JSONObject(result);
            int error = jsonObject.getInt("error");
            if(error > 0){
                callback.onError();
            }else{
                switch (jsonObject.getInt("like")){
                    case 0:
                        callback.onDisliked();
                        break;
                    case 1:
                        callback.onLiked();
                        break;
                    case 2:
                        callback.onDoubleLiked();
                        break;
                    default:
                        callback.onError();
                        break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            callback.onError();
        }
    }
}
