package com.jookovjook.chatapp.new_publication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jookovjook.chatapp.R;
import com.jookovjook.chatapp.interfaces.ImagesLoaderInterface;
import com.jookovjook.chatapp.network.MakePost;
import com.jookovjook.chatapp.new_new_pub.ImageAdapter;
import com.jookovjook.chatapp.new_new_pub.ImageProvider;
import com.jookovjook.chatapp.new_pub.LinkProvider;

import java.util.ArrayList;
import java.util.List;

import es.guiguegon.gallerymodule.GalleryActivity;
import es.guiguegon.gallerymodule.GalleryHelper;
import es.guiguegon.gallerymodule.model.GalleryMedia;

public class NewPublicationActivity extends AppCompatActivity implements ImagesLoaderInterface, MakePost.MakePostCalllback{

    private ArrayList<ImageProvider> mList = new ArrayList<ImageProvider>();
    public static final int REQUEST_CODE_GALLERY = 1;
    private static final int MAX_IMAGES = 20;
    ImageAdapter imageAdapter;
    TextView status;
    RecyclerView recyclerView;
    EditText title, description;
    Button button, done;
    MakePost.MakePostCalllback calllback = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_publication);
        findViews();
        final LinearLayoutManager rvLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(rvLayoutManager);
        imageAdapter = new ImageAdapter(mList, this, this);
        recyclerView.setAdapter(imageAdapter);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mList.size() < MAX_IMAGES)
                startActivityForResult(new GalleryHelper().setMultiselection(true)
                        .setMaxSelectedItems(MAX_IMAGES - mList.size())
                        .setShowVideos(false)
                        .getCallingIntent(NewPublicationActivity.this), REQUEST_CODE_GALLERY);
            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int check = checkImages();
                if(check != 0) return;
                String title_ = title.getText().toString();
                String description_ = description.getText().toString();
                MakePost makePost = new MakePost(title_, description_, mList, 1, calllback, 0, 0, new ArrayList<LinkProvider>(), NewPublicationActivity.this);
                makePost.execute();
            }
        });
    }

    private void findViews(){
        status = (TextView) findViewById(R.id.status);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        button = (Button) findViewById(R.id.button);
        title = (EditText) findViewById(R.id.title);
        description = (EditText) findViewById(R.id.description);
        done = (Button) findViewById(R.id.done);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("sukaaaa", "sukaaaa");
        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK) {
            Log.i("result code: ", String.valueOf(RESULT_OK));
            List<GalleryMedia> galleryMedias =
                    data.getParcelableArrayListExtra(GalleryActivity.RESULT_GALLERY_MEDIA_LIST);
            for (int i = 0; i < galleryMedias.size(); i++) {
                mList.add(new ImageProvider(galleryMedias.get(i).mediaUri()));
            }
            imageAdapter.notifyDataSetChanged();
        }
    }

    private int checkImages(){
        if(mList.size() == 0) return 1;
        int unuploaded = 0;
        for(int i = 0; i < mList.size(); i++){
            if(!mList.get(i).getUploaded()) unuploaded++;
        }
        if(unuploaded > 0) return 2;
        return 0;
    }

    @Override
    public void onAddImagesClicked() {
        if(mList.size() < MAX_IMAGES)
            startActivityForResult(new GalleryHelper().setMultiselection(true)
                    .setMaxSelectedItems(MAX_IMAGES - mList.size())
                    .setShowVideos(false)
                    .getCallingIntent(NewPublicationActivity.this), REQUEST_CODE_GALLERY);
    }

    @Override
    public void onMakePostError() {

    }

    @Override
    public void onAddImagesError() {

    }

    @Override
    public void onAddAdvError() {

    }

    @Override
    public void onAddLinksError() {

    }

    @Override
    public void onPostCreated() {

    }
}