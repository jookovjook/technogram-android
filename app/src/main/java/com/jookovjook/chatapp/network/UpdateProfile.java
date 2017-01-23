package com.jookovjook.chatapp.network;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.jookovjook.chatapp.utils.AuthHelper;
import com.jookovjook.chatapp.utils.Config;
import com.jookovjook.chatapp.utils.StreamReader;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateProfile extends AsyncTask<String, Void, String> {

    private JSONObject jsonObject;
    private UpdateProfileCallback uPC = null;
    private Boolean name = false, surname = false, about = false, username = false, password = false, email = false;

    public interface UpdateProfileCallback{
        void onNameSuc();
        void onNameFailure();
        void onSurnameSuc();
        void onSurnameFailure();
        void onAboutSuc();
        void onAboutFailure();
        void onUsernameSuc();
        void onUsernameFailure();
        void onPasswordSuc();
        void onPasswordFailure();
        void onEmailSuc();
        void onEmailFailure();
    }

    public UpdateProfile(Context context){
        this.jsonObject = new JSONObject();
        try {
            jsonObject.put("token", AuthHelper.getToken(context));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public UpdateProfile(Context context, UpdateProfileCallback uPC){
        this.jsonObject = new JSONObject();
        try {
            jsonObject.put("token", AuthHelper.getToken(context));
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.uPC = uPC;
    }

    public void addName(String name){
        addBool("up_name", true);
        addCustom("name", name);
        this.name = true;
    }

    public void addSurname(String surname){
        addBool("up_surname", true);
        addCustom("surname", surname);
        this.surname = true;
    }

    public void addAbout(String about){
        addBool("up_about", true);
        addCustom("about", about);
        this.about = true;
    }

    public void addUsername(String username) {
        addCustom("username", username);
        this.username = true;
    }

    public void addPassword(String curr_pass, String password) {
        addCustom("password", password);
        addCustom("curr_pass", curr_pass);
        this.password = true;
    }

    public void addEmail(String email){
        addCustom("email", email);
        this.email = true;
    }

    private void addCustom(String param, String value){
        try {
            jsonObject.put(param, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addBool(String param, Boolean value){
        try {
            jsonObject.put(param, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String doInBackground(String... params) {
        String s = "";
        try {
            URL url = new URL(Config.UPDATE_PROFILE_URL);
            HttpURLConnection mUrlConnection = (HttpURLConnection) url.openConnection();
            mUrlConnection.setDoOutput(true);
            mUrlConnection.setDoInput(true);
            mUrlConnection.setRequestProperty("Content-Type","application/json");
            mUrlConnection.connect();
            OutputStreamWriter out = new OutputStreamWriter(mUrlConnection.getOutputStream());
            out.write(jsonObject.toString());
            out.close();
            InputStream inputStream = new BufferedInputStream(mUrlConnection.getInputStream());
            s = StreamReader.read(inputStream);
        }catch (Exception e){
            Log.i("FeedCardAdapter","error getting json answer");
        }
        return s;
    }

    @Override
    protected void onPostExecute(String jsonResult) {
        super.onPostExecute(jsonResult);
        Log.i("update profile", jsonResult);
        try {
            JSONObject jsonObject = new JSONObject(jsonResult);
            if(this.name){
                int name = jsonObject.getInt("name");
                switch(name){
                    case 0: Log.i("UpdateProfile", "Name updated successfully"); break;
                    default: Log.i("UpdateProfile", "Error. Name was not updated"); break;
                }
            }
            if(this.surname){
                int surname = jsonObject.getInt("surname");
                switch(surname){
                    case 0: Log.i("UpdateProfile", "Surname updated successfully"); break;
                    default: Log.i("UpdateProfile", "Error. Surname was not updated"); break;
                }
            }
            if(this.about){
                int about = jsonObject.getInt("about");
                switch(about){
                    case 0: Log.i("UpdateProfile", "About updated successfully"); break;
                    default: Log.i("UpdateProfile", "Error. About was not updated"); break;
                }
            }
            if(this.username){
                int username = jsonObject.getInt("username");
                switch(username){
                    case 0: Log.i("UpdateProfile", "Username updated successfully"); break;
                    case 2: Log.i("UpdateProfile", "Error. Inserted username is too short"); break;
                    case 3: Log.i("UpdateProfile", "Error. User with such username already exists"); break;
                    default: Log.i("UpdateProfile", "Error. Username was not updated"); break;
                }
            }
            if(this.password){
                int password = jsonObject.getInt("password");
                switch(password){
                    case 0: Log.i("UpdateProfile", "Password updated successfully"); break;
                    case 2: Log.i("UpdateProfile", "Error. Current password is wrong"); break;
                    case 3: Log.i("UpdateProfile", "Error. New password is too short"); break;
                    case 4: Log.i("UpdateProfile", "Error. New password is unsafe"); break;
                    case 5: Log.i("UpdateProfile", "Error. Wrong email by updating password without token"); break;
                    default: Log.i("UpdateProfile", "Error. Password was not updated"); break;
                }
            }
            if(this.email){
                int email = jsonObject.getInt("email");
                switch(email){
                    case 0: Log.i("UpdateProfile", "Email updated successfully"); break;
                    case 2: Log.i("UpdateProfile", "Error. Inserted email is fake"); break;
                    case 3: Log.i("UpdateProfile", "Error. User with such email already exists"); break;
                    default: Log.i("UpdateProfile", "Error. Email was not updated"); break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
