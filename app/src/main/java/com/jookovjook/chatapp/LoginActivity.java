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

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    EditText usernameEditText, passwordEditText;
    Button loginButton;
    TextView textView;
    final static String serverURL = "10.83.10.39";
    String username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        textView = (TextView) findViewById(R.id.textView);
        loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = usernameEditText.getText().toString();
                password = passwordEditText.getText().toString();
                LoginClass login = new LoginClass(username,password);
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

    private class LoginClass extends AsyncTask<String, Void, String> {

        private String usernname;
        private String password;

        LoginClass(String username, String password){
            this.usernname = username;
            this.password = password;
        }

        @Override
        protected String doInBackground(String[] params) {

            String s = "Connection error.";

            try{
                URL url = new URL("http://" + serverURL + "/chatApp/login_check.php?username=" + usernname + "&password=" + password);
                HttpURLConnection mUrlConnection = (HttpURLConnection) url.openConnection();
                mUrlConnection.setDoInput(true);
                InputStream inputStream = new BufferedInputStream(mUrlConnection.getInputStream());
                s = readStream(inputStream);
                Log.i("test","Done!");
            }catch (Exception e){
                Log.i("error",e.toString());
            }
            return s;
        }

        @Override
        protected void onPostExecute(String message) {
            super.onPostExecute(message);
            textView.setText(message);
        }
    }

    private String readStream(InputStream is) {
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            int i = is.read();
            while(i != -1) {
                bo.write(i);
                i = is.read();
            }
            return bo.toString();
        } catch (IOException e) {
            return "";
        }
    }

}
