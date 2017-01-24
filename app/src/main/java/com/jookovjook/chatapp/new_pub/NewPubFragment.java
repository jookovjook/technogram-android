package com.jookovjook.chatapp.new_pub;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Layout;
import android.text.Selection;
import android.text.TextWatcher;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

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
        HashTagsAdapter.HashTagCallback, MakePost.MakePostCalllback {

    //UI
    EditText editText;
    RecyclerView recyclerView, hashTagRecycler; //, linksRecycler;
    Button hashButton, doneButton;
    NestedScrollView scrollView;

    //backend
    List<String> tagsList;
    String firstLine;
    ImageAdapter imageAdapter;
    ArrayList<ImageProvider> imageList = new ArrayList<>();
    HashTagsAdapter hashTagsAdapter;
    ArrayList<HashTagsProvider> hashTagList = new ArrayList<>();
    MakePost.MakePostCalllback makePostCalllback;

    //constants
    private static final int MAX_IMAGES = 20;
    public static final int REQUEST_CODE_GALLERY = 1;
    private static final int RESULT_OK = -1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_pub, container, false);
        bindFragment(rootView);
        makePostCalllback = this;
        return rootView;
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
        doneButton = (Button) rootView.findViewById(R.id.doneButton);
    }

    private void setupEditText(){
        editText.setNestedScrollingEnabled(true);
        final Linkify.TransformFilter filter = new Linkify.TransformFilter() {
            public final String transformUrl(final Matcher match, String url) {
                return match.group();
            }
        };
        final Pattern hashtagPattern = Pattern.compile("#([ء-يA-Za-zа-яА-Я0-9_-]+)");
        final String hashtagScheme = "content://com.hashtag.jojo/";
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //GET #HASHTAGS
                Linkify.addLinks(s, hashtagPattern, hashtagScheme, null, filter);
                Matcher m = hashtagPattern.matcher(s);
                tagsList = new ArrayList<String>();
                while (m.find()) {
                    String hashtag = m.group(1);
                    tagsList.add(hashtag);
                    Log.i("hashtags", String.valueOf(tagsList));
                }

                //GET 1ST LINE
                String multiLines = String.valueOf(s);
                String delimiter = "\n";
                String[] lines;
                lines = multiLines.split(delimiter);
                if(lines.length == 0) firstLine = "";
                else firstLine = lines[0];

                //Scroll down
                int all_lines = editText.getLineCount();
                int current_line = getCurrentCursorLine(editText);
                Log.i("lines", String.valueOf(all_lines) + " " + String.valueOf(current_line));
                if((all_lines - current_line) <= 1)
                    scrollView.fullScroll(View.FOCUS_DOWN);
            }

        });
    }

    public int getCurrentCursorLine(EditText editText)
    {
        int selectionStart = Selection.getSelectionStart(editText.getText());
        Layout layout = editText.getLayout();

        if (!(selectionStart == -1)) {
            return layout.getLineForOffset(selectionStart);
        }

        return -1;
    }

    private void setupRecyclerView(View rootView){
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        final LinearLayoutManager rvLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(rvLayoutManager);
        imageAdapter = new ImageAdapter(imageList, getActivity(), this);
        recyclerView.setAdapter(imageAdapter);
    }

    private void setupHashTagRecycler(View rootView){
        hashTagRecycler = (RecyclerView) rootView.findViewById(R.id.hashTagRecycler);
        final LinearLayoutManager rvLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        hashTagRecycler.setLayoutManager(rvLayoutManager);
        setupTags(0, 0);
        hashTagsAdapter = new HashTagsAdapter(hashTagList, getActivity(), this);
        hashTagRecycler.setAdapter(hashTagsAdapter);
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
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MakePost makePost = new MakePost(firstLine, editText.getText().toString(),
                        imageList, makePostCalllback, getActivity());
                makePost.execute();
            }
        });
    }

    private void insertHashTag(String hashtag){
        Log.i("addingHash", hashtag);
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

                if (!text.substring(selectionStart - 1).equals(" ")) {
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
                imageList.add(new ImageProvider(galleryMedias.get(i).mediaUri()));
            }
            imageAdapter.notifyDataSetChanged();
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
        Log.i("Make post", "error");
    }

    @Override
    public void onAddImagesError() {
        Log.i("Make post", "add images error");
    }

    @Override
    public void onPostCreated() {
        Log.i("Make post", "success");
    }
}
