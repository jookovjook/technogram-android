package com.jookovjook.chatapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jookovjook.chatapp.utils.AuthHelper;
import com.jookovjook.chatapp.utils.Config;
import com.jookovjook.chatapp.utils.StreamReader;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    EditText usernameEditText, passwordEditText;
    Button loginButton;
    TextView textView;
    String username, token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        username = AuthHelper.getUsername(this);
        token = AuthHelper.getToken(this);
        String s = "Username and token are not stored";
        if(username != ""){
            usernameEditText.setText(username);
            s = "Username: " + username;
        }
        if(token != ""){
            s += " Token: " + token;
        }
        textView = (TextView) findViewById(R.id.textView);
        textView.setText(s);
        loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthHelper.setUsername(LoginActivity.this, usernameEditText.getText().toString());
                PerformLogin login = new PerformLogin(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());

                login.execute();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class PerformLogin extends AsyncTask<String, Void, String>{

        private JSONObject jsonObject;
        String username, password;

        PerformLogin(String username, String password){
            this.username = username;
            this.password = password;
            this.jsonObject = new JSONObject();
            try{
                jsonObject.put("username", username);
                jsonObject.put("password", password);
            }catch (Exception e) {
                Log.i("perform login", "error creating json");
            }

        }

        @Override
        protected String doInBackground(String... params) {
            String s = "";
            try{
                URL url;
                url = new URL(Config.AUTH_URL);
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
                Log.i("perform login","network error");
            }
            return s;
        }

        @Override
        protected void onPostExecute(String jsonResult) {
            super.onPostExecute(jsonResult);
            JSONObject jsonObject;
            try{
                jsonObject = new JSONObject(jsonResult);
                int error = jsonObject.getInt("error");
                switch (error){
                    case 0:
                        String token = jsonObject.getString("token");
                        textView.setText("Success. Token: " + token);
                        AuthHelper.setUsername(LoginActivity.this, username);
                        AuthHelper.setToken(LoginActivity.this, token);
                        break;
                    case 1:
                        textView.setText("Wrong username");
                        AuthHelper.setUsername(LoginActivity.this, "");
                        AuthHelper.setToken(LoginActivity.this, "");
                        break;
                    case 2:
                        textView.setText("Wrong password");
                        AuthHelper.setUsername(LoginActivity.this,username);
                        AuthHelper.setToken(LoginActivity.this, "");
                        break;
                    default:
                        textView.setText("Unknown error");
                        break;
                }
            }catch(Exception e){}
        }
    }
}
