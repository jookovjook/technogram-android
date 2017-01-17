package com.jookovjook.chatapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jookovjook.chatapp.interfaces.RegisterInterface;
import com.jookovjook.chatapp.network.Register;
import com.jookovjook.chatapp.utils.AuthHelper;
import com.jookovjook.chatapp.utils.Config;
import com.jookovjook.chatapp.utils.StreamReader;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity implements RegisterInterface{

    EditText usernameEditText, passwordEditText, usernameReg, passwordReg, emailReg;
    Button loginButton, logreg, buttonReg;
    TextView textView;
    String username, token;
    CardView regcard;
    RegisterInterface regI = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        usernameReg = (EditText) findViewById(R.id.usernameReg);
        passwordReg = (EditText) findViewById(R.id.passwordReg);
        emailReg = (EditText) findViewById(R.id.emailReg);
        logreg = (Button) findViewById(R.id.logreg);
        regcard = (CardView) findViewById(R.id.regcard);
        logreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(regcard.getVisibility() == View.VISIBLE){
                    regcard.setVisibility(View.INVISIBLE);
                }else{
                    regcard.setVisibility(View.VISIBLE);
                }
            }
        });
        buttonReg = (Button) findViewById(R.id.buttonReg);
        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Register register = new Register(usernameReg.getText().toString(),
                        passwordReg.getText().toString(), emailReg.getText().toString(), regI);
                register.execute();
            }
        });
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

    @Override
    public void onSuccess(int user_id, String token) {
        Toast.makeText(getApplicationContext(), "Reg success. user id = " + String.valueOf(user_id),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailure(int error, String message) {
        Toast.makeText(getApplicationContext(), "Reg error " + String.valueOf(error) + ": " + message,
                Toast.LENGTH_SHORT).show();
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
            Log.i("a","a");
            try{
                jsonObject = new JSONObject(jsonResult);
                int error = jsonObject.getInt("error");
                switch (error){
                    case 0:
                        String token = jsonObject.getString("token");
                        int user_id = jsonObject.getInt("user_id");
                        textView.setText("Success. Token: " + token + ". User_id = " + String.valueOf(user_id));
                        AuthHelper.setUsername(LoginActivity.this, username);
                        AuthHelper.setToken(LoginActivity.this, token);
                        AuthHelper.setUserId(LoginActivity.this, user_id);
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case 1:
                        textView.setText("Wrong username");
                        AuthHelper.setUsername(LoginActivity.this, "");
                        AuthHelper.setToken(LoginActivity.this, "");
                        AuthHelper.setUserId(LoginActivity.this, -1);
                        break;
                    case 2:
                        textView.setText("Wrong password");
                        AuthHelper.setUsername(LoginActivity.this,username);
                        AuthHelper.setToken(LoginActivity.this, "");
                        AuthHelper.setUserId(LoginActivity.this, -1);
                        break;
                    default:
                        textView.setText("Unknown error");
                        AuthHelper.setUsername(LoginActivity.this, "");
                        AuthHelper.setToken(LoginActivity.this, "");
                        AuthHelper.setUserId(LoginActivity.this, -1);
                        break;
                }
            }catch(Exception e){
                e.printStackTrace();
                textView.setText("Unknown error");
                AuthHelper.setUsername(LoginActivity.this, "");
                AuthHelper.setToken(LoginActivity.this, "");
                AuthHelper.setUserId(LoginActivity.this, -1);
            }
        }
    }
}