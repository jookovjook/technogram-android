package com.jookovjook.chatapp.new_publication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.jookovjook.chatapp.R;

import java.util.ArrayList;
import java.util.List;

import es.guiguegon.gallerymodule.GalleryActivity;
import es.guiguegon.gallerymodule.GalleryHelper;
import es.guiguegon.gallerymodule.model.GalleryMedia;

public class NewPublicationActivity extends AppCompatActivity {

    private ArrayList<ImageProvider> mList = new ArrayList<>();

    public static final int REQUEST_CODE_GALLERY = 1;

    ImageAdapter imageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_publication);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.image_recycler);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        imageAdapter = new ImageAdapter(mList, getApplicationContext());
        recyclerView.setAdapter(imageAdapter);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new GalleryHelper().setMultiselection(true)
                        .setMaxSelectedItems(10)
                        .setShowVideos(false)
                        .getCallingIntent(NewPublicationActivity.this), REQUEST_CODE_GALLERY);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK) {
            List<GalleryMedia> galleryMedias =
                    data.getParcelableArrayListExtra(GalleryActivity.RESULT_GALLERY_MEDIA_LIST);
            for (int i = 0; i < galleryMedias.size(); i++) {
                mList.add(new ImageProvider(galleryMedias.get(i).mediaUri()));
            }
            imageAdapter.notifyDataSetChanged();
        }
    }
}
