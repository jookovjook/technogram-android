package com.jookovjook.chatapp.new_pub;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.TextAppearanceSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jookovjook.chatapp.R;
import com.jookovjook.chatapp.interfaces.ImagesLoaderInterface;
import com.jookovjook.chatapp.network.MakePost;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.guiguegon.gallerymodule.GalleryActivity;
import es.guiguegon.gallerymodule.GalleryHelper;
import es.guiguegon.gallerymodule.model.GalleryMedia;

public class NewPubFragment extends Fragment implements ImagesLoaderInterface,
        HashTagsAdapter.HashTagCallback, MakePost.MakePostCallback {

    //UI
    EditText editText;
    TextView responseText, textView;
    RecyclerView recyclerView, hashTagRecycler; //, linksRecycler;
    Button hashButton;
    NestedScrollView scrollView;
    ImageView addPhotosButton,addPhotosImage;
    RelativeLayout layoutBelowEditText;

    //backend
    List<String> tagsList, linksList;
    List<Integer> tagsStart, tagsEnd, mentsStart, mentsEnd, linksStart, linksEnd;
    String firstLine;
    ImageAdapter imageAdapter;
    ArrayList<ImageProvider> imageList = new ArrayList<>();
    HashTagsAdapter hashTagsAdapter;
    ArrayList<HashTagsProvider> hashTagList = new ArrayList<>();
    MakePost.MakePostCallback makePostCallback = this;
    ImagesLoaderInterface imagesLoaderInterface = this;
    TextWatcher txtwt;

    //constants
    private static final int MAX_IMAGES = 10;
    public static final int REQUEST_CODE_GALLERY = 1;
    private static final int RESULT_OK = -1;

    FragmentPublishCallback callback;

    public interface FragmentPublishCallback{
        void onPublishSuccessful();
        void onPublishError();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_pub, container, false);
        bindFragment(rootView);
        return rootView;
    }

    public void publish(){
        if(imageList.size() > 0) {
            MakePost makePost = new MakePost(firstLine, editText.getText().toString(),
                    imageList, makePostCallback, getActivity());
            makePost.execute();
            responseText.setText("Creating post...");
        }else{
            responseText.setText("You have to attach at least 1 image");
            callback.onPublishError();
        }
    }

    public void setFragmentPublishCallback(FragmentPublishCallback callback){
        this.callback = callback;
    }

    private void bindFragment(View rootView){
        findViews(rootView);
        setupEditText();
        setupRecyclerView(rootView);
        setupHashTagRecycler(rootView);
        setupOthers();
    }

    private void findViews(View rootView){
        editText = (EditText) rootView.findViewById(R.id.editText);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        hashButton = (Button) rootView.findViewById(R.id.hashButton);
        hashTagRecycler = (RecyclerView) rootView.findViewById(R.id.hashTagRecycler);
        scrollView = (NestedScrollView) rootView.findViewById(R.id.scrollView);
        responseText = (TextView) rootView.findViewById(R.id.responseText);
        layoutBelowEditText = (RelativeLayout) rootView.findViewById(R.id.layoutBelowEditText);
        addPhotosButton = (ImageView) rootView.findViewById(R.id.addPhotosButton);
        addPhotosImage = (ImageView)rootView.findViewById(R.id.addPhotosImage);
        textView = (TextView) rootView.findViewById(R.id.textView);
    }

    private void setupEditText(){
        editText.setNestedScrollingEnabled(true);

        final Pattern hashtagPattern = Pattern.compile("#([A-Za-zа-яА-Я0-9_-]+)");
        final Pattern mentionsPattern = Pattern.compile("@([A-Za-z0-9_-]+)");
        final Pattern linksPattern = Pattern.compile("\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");

        txtwt = new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {

                //GET #HASHTAGS, @MENTIONS AND HTTP://HYPER.LINKS
                Matcher m = hashtagPattern.matcher(s);
                Matcher m1 = mentionsPattern.matcher(s);
                Matcher linksMatcher = linksPattern.matcher(s);
                tagsList = new ArrayList<>();
                tagsStart = new ArrayList<>();
                tagsEnd = new ArrayList<>();
                while (m.find()) {
                    tagsStart.add(m.start(1));
                    tagsEnd.add(m.end(1));
                    tagsList.add(m.group(1));
                }
                //mentsList = new ArrayList<>();
                mentsStart = new ArrayList<>();
                mentsEnd = new ArrayList<>();
                while (m1.find()){
                    //mentsList.add(m1.group(1));
                    mentsStart.add(m1.start(1));
                    mentsEnd.add(m1.end(1));
                }
                linksStart = new ArrayList<>();
                linksEnd = new ArrayList<>();
                while(linksMatcher.find()){
                    //linksList.add(linksMatcher.group(0));
                    linksStart.add(linksMatcher.start());
                    linksEnd.add(linksMatcher.end());
                }
                //Log.i("linksList", String.valueOf(linksList));


                //GET 1ST LINE
                String multiLines = String.valueOf(s);
                String delimiter = "\n";
                String[] lines;
                lines = multiLines.split(delimiter);
                if(lines.length == 0) firstLine = "";
                else firstLine = lines[0];


                //SET SPANNEABLE

                Spannable spanRange = new SpannableString(multiLines);
                TextAppearanceSpan tas = new TextAppearanceSpan(getActivity(), android.R.style.TextAppearance_Holo_Large);
                spanRange.setSpan(tas, 0, lines[0].length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                for(int i = 0; i < tagsList.size(); i++){
                    spanRange.setSpan(new ForegroundColorSpan(
                            ResourcesCompat.getColor(getResources(), R.color.colorBlue, null)),
                            tagsStart.get(i)-1, tagsEnd.get(i),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                for(int i = 0; i < mentsStart.size(); i++){
                    spanRange.setSpan(new ForegroundColorSpan(
                                    ResourcesCompat.getColor(getResources(), R.color.colorAccentDark, null)),
                            mentsStart.get(i)-1, mentsEnd.get(i),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                for(int i = 0; i < linksStart.size(); i++){
                    spanRange.setSpan(
                            new URLSpan(""),
                            linksStart.get(i), linksEnd.get(i),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                editText.removeTextChangedListener(txtwt);
                int selectionStart = editText.getSelectionStart();
                int selectionEnd = editText.getSelectionEnd();
                editText.setText(spanRange);
                editText.setSelection(selectionStart);
                //Log.i("_editext length", String.valueOf(editText.getText().toString().length()));
                //Log.i("textview length", String.valueOf(textView.getText().toString().length()));
                editText.addTextChangedListener(txtwt);

                textView.setText(spanRange);

            }

        };

        editText.addTextChangedListener(txtwt);


        layoutBelowEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.requestFocus();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
                editText.setSelection(editText.length());
            }
        });
    }

    private void setupRecyclerView(View rootView){
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        final LinearLayoutManager rvLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(rvLayoutManager);
        imageAdapter = new ImageAdapter(imageList, getActivity(), imagesLoaderInterface);
        recyclerView.setAdapter(imageAdapter);


        final RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) addPhotosButton.getLayoutParams();
        layoutParams.setMargins(100, 0, 0, 0);
        addPhotosButton.setLayoutParams(layoutParams);
        addPhotosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagesLoaderInterface.onAddImagesClicked();
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int totaldx = 0;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                totaldx = recyclerView.computeHorizontalScrollOffset();
                layoutParams.setMargins((int) (100-totaldx/2.50), 0, 0, 0);
                addPhotosButton.setLayoutParams(layoutParams);
                float point = (float) (totaldx/300.0);
                float size = 1 - point*point;
                float alph = (float) (totaldx/200.0);
                float alpha = 1 - alph*alph;
                alpha *= 0.75;
                if(alpha <= 0.01) addPhotosButton.setClickable(false);
                else addPhotosButton.setClickable(true);
                addPhotosButton.setAlpha(alpha);
                addPhotosButton.setScaleX(size);
                addPhotosButton.setScaleY(size);

                super.onScrolled(recyclerView, dx, dy);

            }
        });
    }

    private void setupHashTagRecycler(final View rootView){
        hashTagRecycler = (RecyclerView) rootView.findViewById(R.id.hashTagRecycler);
        final LinearLayoutManager rvLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        hashTagRecycler.setLayoutManager(rvLayoutManager);
        setupTags(0, 0);
        hashTagsAdapter = new HashTagsAdapter(hashTagList, getActivity(), this);
        hashTagRecycler.setAdapter(hashTagsAdapter);
        hashTagRecycler.addOnScrollListener(new RecyclerView.OnScrollListener(){

            ImageView leftShadow = (ImageView) rootView.findViewById(R.id.leftShadow);
            ImageView rightShadow = (ImageView) rootView.findViewById(R.id.rightShadow);
            int totaldx = 0;
            int previousVsible = 0;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                totaldx = hashTagRecycler.computeHorizontalScrollOffset();
                float alph = (float) (totaldx * 2.0/hashTagRecycler.getHeight());
                //float alpha = 1 - alph;
                leftShadow.setAlpha(alph);
                int nowVisible = rvLayoutManager.findLastCompletelyVisibleItemPosition();
                if(nowVisible > previousVsible & nowVisible == hashTagList.size() - 1){
                    Animation fadeOut = new AlphaAnimation(1, 0);
                    fadeOut.setDuration(200);
                    fadeOut.setFillAfter(true);
                    rightShadow.clearAnimation();
                    rightShadow.setAnimation(fadeOut);
                }
                if(nowVisible < previousVsible & previousVsible == hashTagList.size() - 1){
                    Animation fadeIn = new AlphaAnimation(0, 1);
                    fadeIn.setDuration(200);
                    fadeIn.setFillAfter(true);
                    rightShadow.clearAnimation();
                    rightShadow.setAnimation(fadeIn);
                }
                previousVsible = nowVisible;
                super.onScrolled(recyclerView, dx, dy);
            }

        });
    }

    private void setupTags(int level, int type){
        hashTagList.clear();
        switch (level){
            default:
                hashTagList.add(new HashTagsProvider("soft", 0, 1));
                hashTagList.add(new HashTagsProvider("hard", 0, 2));
                hashTagList.add(new HashTagsProvider("design", 0, 3));
                hashTagList.add(new HashTagsProvider("math", 0, 4));
                hashTagList.add(new HashTagsProvider("physics", 0, 5));
                hashTagList.add(new HashTagsProvider("technoGram", 0, 6));
                break;
            case 1:
                switch (type){
                    case 1:
                        hashTagList.add(new HashTagsProvider("dataScience", 1, 101));
                        hashTagList.add(new HashTagsProvider("iOS", 1, 102));
                        hashTagList.add(new HashTagsProvider("macOS", 1, 103));
                        hashTagList.add(new HashTagsProvider("technoGram", 1, 104));
                        break;
                    case 2:
                        hashTagList.add(new HashTagsProvider("diy", 1, 201));
                        hashTagList.add(new HashTagsProvider("pld", 1, 202));
                        hashTagList.add(new HashTagsProvider("PC", 1, 203));
                        break;
                    case 3:
                        hashTagList.add(new HashTagsProvider("web", 1, 301));
                        hashTagList.add(new HashTagsProvider("mobile", 1, 302));
                        hashTagList.add(new HashTagsProvider("art", 1, 303));
                        hashTagList.add(new HashTagsProvider("hipster", 1, 304));
                        break;
                    case 4:
                        hashTagList.add(new HashTagsProvider("sucks", 1, 401));
                        hashTagList.add(new HashTagsProvider("boring", 1, 402));
                        hashTagList.add(new HashTagsProvider("nonsense", 1, 403));
                        hashTagList.add(new HashTagsProvider("hate", 1, 404));
                        break;
                    case 5:
                        hashTagList.add(new HashTagsProvider("sucks", 1, 501));
                        hashTagList.add(new HashTagsProvider("boring", 1, 502));
                        hashTagList.add(new HashTagsProvider("nonsense", 1, 503));
                        hashTagList.add(new HashTagsProvider("hate", 1, 504));
                        break;
                    default:
                        hashTagList.add(new HashTagsProvider("is", 1, 601));
                        hashTagList.add(new HashTagsProvider("the", 1, 602));
                        hashTagList.add(new HashTagsProvider("best", 1, 603));
                        hashTagList.add(new HashTagsProvider("service", 1, 604));
                        break;
                }
                break;
            case 2:
                hashTagList.add(new HashTagsProvider("technoGram", 2, 1));
                hashTagList.add(new HashTagsProvider("technoGram", 2, 2));
                hashTagList.add(new HashTagsProvider("technoGram", 2, 3));
                hashTagList.add(new HashTagsProvider("technoGram", 2, 4));
                hashTagList.add(new HashTagsProvider("technoGram", 2, 5));
                hashTagList.add(new HashTagsProvider("technoGram", 2, 6));
                break;
            case 3:
                hashTagList.add(new HashTagsProvider("is", 3, 1));
                hashTagList.add(new HashTagsProvider("is", 3, 2));
                hashTagList.add(new HashTagsProvider("is", 3, 3));
                hashTagList.add(new HashTagsProvider("is", 3, 4));
                hashTagList.add(new HashTagsProvider("is", 3, 5));
                hashTagList.add(new HashTagsProvider("is", 3, 6));
                break;
            case 4:
                hashTagList.add(new HashTagsProvider("the", 4, 1));
                hashTagList.add(new HashTagsProvider("the", 4, 2));
                hashTagList.add(new HashTagsProvider("the", 4, 3));
                hashTagList.add(new HashTagsProvider("the", 4, 4));
                hashTagList.add(new HashTagsProvider("the", 4, 5));
                hashTagList.add(new HashTagsProvider("the", 4, 6));
                break;
            case 5:
                hashTagList.add(new HashTagsProvider("best", 5, 1));
                hashTagList.add(new HashTagsProvider("best", 5, 2));
                hashTagList.add(new HashTagsProvider("best", 5, 3));
                hashTagList.add(new HashTagsProvider("best", 5, 4));
                hashTagList.add(new HashTagsProvider("best", 5, 5));
                hashTagList.add(new HashTagsProvider("best", 5, 6));
                break;
            case 6:
                hashTagList.add(new HashTagsProvider("mamkoTraher", 6, 1));
                hashTagList.add(new HashTagsProvider("mamkoTraher", 6, 2));
                hashTagList.add(new HashTagsProvider("mamkoTraher", 6, 3));
                hashTagList.add(new HashTagsProvider("mamkoTraher", 6, 4));
                hashTagList.add(new HashTagsProvider("mamkoTraher", 6, 5));
                hashTagList.add(new HashTagsProvider("mamkoTraher", 6, 6));
                break;
        }
    }

    private void setupOthers(){
        hashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertHashTag("");
            }
        });

//                MakePost makePost = new MakePost(firstLine, editText.getText().toString(),
//                        imageList, makePostCallback, getActivity());
//                makePost.execute();

    }

    private void insertHashTag(String hashtag){
        //Log.i("addingHash", hashtag);
        String text = editText.getText().toString();
        int insertedChars = hashtag.length() + 1;
        int selectionStart = editText.getSelectionStart();
        int selectionEnd = editText.getSelectionEnd();
        if(text.length() > 0) {

            if(selectionStart == 0){
                text = hashtag + text;
                editText.setText(text);
                editText.setSelection(text.length());
            }else {
                String textStarts = text.substring(0, selectionStart);
                String textEnds = text.substring(selectionEnd, text.length());

                if (!text.substring(selectionStart - 1).equals(" ") & !text.substring(selectionStart - 1).equals("\n")) {
                    text = textStarts + " ";
                    insertedChars++;
                }
                text = text + "\u0023" + hashtag + textEnds;
                editText.setText(text);
                editText.setSelection(selectionStart + insertedChars);
            }
        }else{
            text = "\u0023" + hashtag;
            editText.setText(text);
            editText.setSelection(hashtag.length() + 1);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("NewPub", "onActivityResult");
        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK) {
            List<GalleryMedia> galleryMedias =
                    data.getParcelableArrayListExtra(GalleryActivity.RESULT_GALLERY_MEDIA_LIST);
            for (int i = 0; i < galleryMedias.size(); i++) {
                imageList.add(new ImageProvider(galleryMedias.get(i).mediaUri(), getActivity()));
            }
            imageAdapter.notifyDataSetChanged();
            addPhotosImage.setVisibility(View.GONE);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onAddImagesClicked() {
        if(imageList.size() < MAX_IMAGES)
            startActivityForResult(new GalleryHelper().setMultiselection(true)
                    .setMaxSelectedItems(MAX_IMAGES - imageList.size())
                    .setShowVideos(false)
                    .getCallingIntent(getActivity()), REQUEST_CODE_GALLERY);
    }

    public static Fragment newInstance() {
        return new NewPubFragment();
    }

    @Override
    public void onTagAdded(String hashtag, int level, int type) {
        insertHashTag(hashtag + " ");
        setupTags(level + 1, type);
        hashTagsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onMakePostError() {
        callback.onPublishError();
        responseText.setText("Error creating pub");
        Log.i("Make post", "error");
    }

    @Override
    public void onAddImagesError() {
        Log.i("Make post", "add images error");
        callback.onPublishError();
        responseText.setText("Error adding images to pub");
    }

    @Override
    public void onPostCreated() {
        Log.i("Make post", "success");
        responseText.setText("Successful!");
        callback.onPublishSuccessful();
    }

}
