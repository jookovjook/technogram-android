package com.jookovjook.chatapp.settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.jookovjook.chatapp.R;
import com.jookovjook.chatapp.network.UpdateProfileImage;
import com.jookovjook.chatapp.network.UploadImage;
import com.jookovjook.chatapp.utils.AuthHelper;
import com.jookovjook.chatapp.utils.Config;
import com.jookovjook.chatapp.utils.Metrics;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity implements UploadImage.UploadImageCallback, UpdateProfileImage.UpdateProfileImageCallback{

    CircleImageView avatar;
    final static int REQUEST_CODE_PICKER = 101;
    final static int REQUEST_CODE_SETTING = 102;
    TextView statusText;
    InfoAdapter infoAdapter;
    SecurityAdapter securityAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle("Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        avatar = (CircleImageView) findViewById(R.id.avatar);

        Picasso.with(this)
                .load(Config.IMAGE_RESOURCES_URL + AuthHelper.getAvatar(this))
                .error(R.drawable.grid).placeholder(R.drawable.grid)
                .resize(Metrics.dpToPx(100), Metrics.dpToPx(100)).onlyScaleDown().centerCrop().into(avatar);

        (findViewById(R.id.imageButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.create(SettingsActivity.this).returnAfterFirst(false).single()
                        .showCamera(true).start(REQUEST_CODE_PICKER);
            }
        });

        statusText = (TextView) findViewById(R.id.statusText);

        RecyclerView settingsRecycler = (RecyclerView) findViewById(R.id.settingsRecycler);
        settingsRecycler.setLayoutManager(new GridLayoutManager(this, 1));
        infoAdapter = new InfoAdapter(this);
        settingsRecycler.setAdapter(infoAdapter);
        settingsRecycler.setNestedScrollingEnabled(false);
        settingsRecycler.addItemDecoration(new SimpleDividerItemDecoration(this));

        RecyclerView securityRecycler = (RecyclerView) findViewById(R.id.securityRecycler);
        securityRecycler.setLayoutManager(new GridLayoutManager(this, 1));
        securityAdapter = new SecurityAdapter(this);
        securityRecycler.setAdapter(securityAdapter);
        securityRecycler.setNestedScrollingEnabled(false);
        securityRecycler.addItemDecoration(new SimpleDividerItemDecoration(this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PICKER && resultCode == RESULT_OK && data != null) {
            ArrayList<Image> images = (ArrayList<Image>) ImagePicker.getImages(data);
            String uri = images.get(0).getPath();
            Glide.with(this).load(uri).fitCenter().into(avatar);
            UploadImage uploadImage = new UploadImage(this, images.get(0).getPath(), this);
            uploadImage.execute();
        }
        Log.i("requestCode", String.valueOf(requestCode));
        if (requestCode == REQUEST_CODE_SETTING) {
            if (resultCode == RESULT_OK) {
                //Use Data to get string
                String string = data.getStringExtra("RESULT_STRING");
                Log.i("got result", string);
                infoAdapter.update();
                securityAdapter.update();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    @Override
    public void onStartUploading() {
        statusText.setText("Uploading: 0%");
    }

    @Override
    public void onSuccess(String filename, int id) {
        statusText.setText("Uploading: 100%");
        UpdateProfileImage updateProfileImage= new UpdateProfileImage(this, filename, id, this);
        updateProfileImage.execute();
    }

    @Override
    public void onFailure(int code, String message) {
        statusText.setText("Error. " + message);
    }

    @Override
    public void onUpdateProgress(int progress) {
        statusText.setText("Uploading: " + String.valueOf(progress) + "%");
    }

    @Override
    public void onSuccess(String img_link) {
        statusText.setText("Profile image successfully updated.");
        AuthHelper.setAvatar(this, img_link);
    }

    @Override
    public void onFailure() {
        statusText.setText("Error updating profile image");
    }
}
