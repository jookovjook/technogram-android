package com.jookovjook.chatapp.new_pub;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.jookovjook.chatapp.R;
import com.jookovjook.chatapp.interfaces.ImagesLoaderInterface;
import com.jookovjook.chatapp.network.MakePost;
import com.jookovjook.chatapp.new_publication.ImageAdapter;
import com.jookovjook.chatapp.new_publication.ImageProvider;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.List;

import es.guiguegon.gallerymodule.GalleryActivity;
import es.guiguegon.gallerymodule.GalleryHelper;
import es.guiguegon.gallerymodule.model.GalleryMedia;

public class SoftwareFragment extends Fragment implements ImagesLoaderInterface, MakePost.MakePostCalllback{

    private ArrayList<ImageProvider> mList = new ArrayList<ImageProvider>();
    public static final int REQUEST_CODE_GALLERY = 1;
    private static final int MAX_IMAGES = 20;
    private static final int RESULT_OK = -1;
    ImageAdapter imageAdapter;
    LinkAdapter linkAdapter;
    RecyclerView recyclerView, links_recycler;
    EditText title, description;
    Button done, adv_opt;
    MakePost.MakePostCalllback calllback = this;
    int license, stage;
    ExpandableLayout expandableLayout;

    private ArrayList<LinkProvider> nList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_pub_soft, container, false);
        license = 0;
        stage = 0;
        title = (EditText) rootView.findViewById(R.id.title);
        description = (EditText) rootView.findViewById(R.id.description);
        done = (Button) rootView.findViewById(R.id.done);
        expandableLayout = (ExpandableLayout) rootView.findViewById(R.id.expandable_layout);
        adv_opt = (Button) rootView.findViewById(R.id.adv_opt);
        adv_opt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(expandableLayout.isExpanded()) {
                    expandableLayout.collapse();
                }else{
                    expandableLayout.expand();
                }
            }
        });
        //
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        final LinearLayoutManager rvLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(rvLayoutManager);
        imageAdapter = new ImageAdapter(mList, getActivity(), this);
        recyclerView.setAdapter(imageAdapter);
        //
        links_recycler = (RecyclerView) rootView.findViewById(R.id.links_recycler);
        final LinearLayoutManager linksLM = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        links_recycler.setLayoutManager(linksLM);
        linkAdapter = new LinkAdapter(nList, getActivity());
        links_recycler.setAdapter(linkAdapter);
        links_recycler.setNestedScrollingEnabled(false);
        //
        setupLicenseSpinner(rootView);
        setupStageSpinner(rootView);
        //
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int check = checkImages();
                if(check != 0) return;
                String title_ = title.getText().toString();
                String description_ = description.getText().toString();
                MakePost makePost = new MakePost(title_, description_, mList, 1, calllback, license, stage, nList);
                makePost.execute();
            }
        });
        Button addLink = (Button) rootView.findViewById(R.id.addLink);
        linkAdapter.addLink();
        linkAdapter.notifyDataSetChanged();
        addLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nList.add(new LinkProvider());
                linkAdapter.notifyDataSetChanged();
            }
        });
        return rootView;
    }

    void setupLicenseSpinner(View rootView){
        String[] data = {"", "Free Source", "Free API", "API", "MIT License", "Apache License", "BSD License", "Proprietary"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner = (Spinner) rootView.findViewById(R.id.spinnerLicense);
        spinner.setAdapter(adapter);
        // заголовок
        spinner.setPrompt("Title");
        spinner.setSelection(0);
        // устанавливаем обработчик нажатия
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                license = (int) id;
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    void setupStageSpinner(View rootView){
        String[] data = {"", "Concept", "Prototype", "Product", "Alpha Version", "Beta Version",
                "Public Beta", "Developing", "Release", "Frozen", "Closed"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner = (Spinner) rootView.findViewById(R.id.spinnerStage);
        spinner.setAdapter(adapter);
        // заголовок
        spinner.setPrompt("Title");
        spinner.setSelection(0);
        // устанавливаем обработчик нажатия
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                stage = (int) id;
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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

    public static Fragment newInstance() {
        return new SoftwareFragment();
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
                    .getCallingIntent(getActivity()), REQUEST_CODE_GALLERY);
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

}

