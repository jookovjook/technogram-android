package com.jookovjook.chatapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jookovjook.chatapp.interfaces.CheckTokenInterface;
import com.jookovjook.chatapp.network.CheckToken;
import com.jookovjook.chatapp.login.LoginActivity;
import com.jookovjook.chatapp.utils.AuthHelper;

public class SplashActivity extends AppCompatActivity implements CheckTokenInterface{

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        CheckToken checkToken = new CheckToken(AuthHelper.getToken(this), this);
        checkToken.execute();

    }

    @Override
    public void onTokenChecked(int user_id) {
        if(user_id > -1){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }else{
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}