package com.jookovjook.chatapp;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.jookovjook.chatapp.network.UpdateProfile;
import com.jookovjook.chatapp.network.UpdateProfileImage;
import com.jookovjook.chatapp.network.UploadImage;

public class TestActivity extends AppCompatActivity implements UploadImage.UploadImageCallback{

    private EditText name, surname, about, username, password, currpass, email;
    private Button nameButton, surnameButton, aboutButton, usernameButton, passwordButton, emailButton, imageButton;
    private ImageView image;
    private UploadImage.UploadImageCallback uploadImageCallback = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        findViews();

        nameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateProfile updateProfile = new UpdateProfile(TestActivity.this);
                updateProfile.addName(name.getText().toString());
                updateProfile.execute();
            }
        });

        surnameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateProfile updateProfile = new UpdateProfile(TestActivity.this);
                updateProfile.addSurname(surname.getText().toString());
                updateProfile.execute();
            }
        });

        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateProfile updateProfile = new UpdateProfile(TestActivity.this);
                updateProfile.addAbout(about.getText().toString());
                updateProfile.execute();
            }
        });

        usernameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateProfile updateProfile = new UpdateProfile(TestActivity.this);
                updateProfile.addUsername(username.getText().toString());
                updateProfile.execute();
            }
        });

        passwordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateProfile updateProfile = new UpdateProfile(TestActivity.this);
                updateProfile.addPassword(currpass.getText().toString(), password.getText().toString());
                updateProfile.execute();
            }
        });

        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateProfile updateProfile = new UpdateProfile(TestActivity.this);
                updateProfile.addEmail(email.getText().toString());
                updateProfile.execute();
            }
        });

        final String uri = "/storage/emulated/0/DCIM/Camera/IMG_20170118_131637.jpg";

        image.setImageURI(Uri.parse(uri));

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadImage uploadImage = new UploadImage(TestActivity.this, uri, uploadImageCallback);
                uploadImage.execute();
            }
        });
    }

    private void findViews(){
        name = (EditText) findViewById(R.id.name);
        surname = (EditText) findViewById(R.id.surname);
        about = (EditText) findViewById(R.id.about);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        currpass = (EditText) findViewById(R.id.currpass);
        email = (EditText) findViewById(R.id.email);

        nameButton = (Button) findViewById(R.id.nameButton);
        surnameButton = (Button) findViewById(R.id.surnameButton);
        aboutButton = (Button) findViewById(R.id.aboutButton);
        usernameButton = (Button) findViewById(R.id.usernameButton);
        passwordButton = (Button) findViewById(R.id.passwordButton);
        emailButton = (Button) findViewById(R.id.emailButton);
        imageButton = (Button) findViewById(R.id.imageButton);

        image = (ImageView) findViewById(R.id.image);
    }

    @Override
    public void onStartUploading() {
        Log.i("TestActivity", "Start uploading");
    }

    @Override
    public void onSuccess(String filename, int id) {
        Log.i("TestActivity", filename);
        UpdateProfileImage updateProfileImage = new UpdateProfileImage(TestActivity.this, filename, id, null);
        updateProfileImage.execute();
    }

    @Override
    public void onFailure(int code, String message) {
        Log.i("TestActivity", message);
    }

    @Override
    public void onUpdateProgress(int progress) {
        Log.i("TestActivity", String.valueOf(progress));
    }
}
