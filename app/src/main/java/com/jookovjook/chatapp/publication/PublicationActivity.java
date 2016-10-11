package com.jookovjook.chatapp.publication;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jookovjook.chatapp.R;
import com.jookovjook.chatapp.paralaxviewpager.ParallaxViewPager;
import com.jookovjook.chatapp.utils.ServerSettings;
import com.jookovjook.chatapp.utils.StreamReader;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class PublicationActivity extends AppCompatActivity implements CommentAdapterCallback{

    TextView title_textView;
    TextView text_textView, username_textView;
    TextView views_textView, stars_textView, comments_textView;
    ImageView image;
    int pub_id;
    CommentAdapter cAdapter;
    EditText editText;
    Button button_send;
    String img_link;
    ArrayList<String> images = new ArrayList<>();
    private Animation mFadeOutAnimation;
    private ParallaxViewPager mParallaxViewPager;
    ImageView hidden_image;
    @SuppressWarnings("SpellCheckingInspection")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publication);
        pub_id = -1;
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            img_link = bundle.getString("img_link");
            pub_id = bundle.getInt("publication_id");
            ActivityCompat.postponeEnterTransition(this);
            GetPublicationImages getPublicationImages = new GetPublicationImages();
            getPublicationImages.execute();
            GetPublication getPublication = new GetPublication(pub_id);
            getPublication.execute();
        }

        bindActivity();

        this.cAdapter = new CommentAdapter(pub_id, this, this);
        ListView lvComments = (ListView) findViewById(R.id.lvComments);
        lvComments.setAdapter(cAdapter);
    }

    @Override
    public void onBackPressed() {
        //hidden_image.setAlpha((float) 1);
        //hidden_image.startAnimation(mFadeOutAnimation);
        super.onBackPressed();
    }

    @Override
    public void onDataInsertedCallback() {
        ListView lvComments = (ListView) findViewById(R.id.lvComments);
        setListViewHeightBasedOnChildren(lvComments);
        Log.i("callback; ", "done");
    }

    void bindActivity(){
        title_textView = (TextView) findViewById(R.id.publication_title);
        text_textView = (TextView) findViewById(R.id.text);
        views_textView = (TextView) findViewById(R.id.views);
        stars_textView = (TextView) findViewById(R.id.stars);
        comments_textView = (TextView) findViewById(R.id.comments);
        username_textView = (TextView) findViewById(R.id.feed_card_username);
        image = (ImageView) findViewById(R.id.image);
        hidden_image = (ImageView) findViewById(R.id.hidden_image);
        editText = (EditText) findViewById(R.id.editText);
        button_send = (Button) findViewById(R.id.send_button);
        button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostComment postComment = new PostComment(pub_id, 0, editText.getText().toString());
                postComment.execute();

            }
        });
        //image.requestFocus();
        mParallaxViewPager = (ParallaxViewPager) findViewById(R.id.viewpager);
        mFadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        mFadeOutAnimation.setFillAfter(true);
        //initViewPager();
    }

    //hui
    //one more hui

    private class GetPublication extends AsyncTask<String, Void, String>{

        int publication_id;

        GetPublication(int publication_id){
            this.publication_id = publication_id;
        }

        @Override
        protected String doInBackground(String... params) {
            String s = "";
            try{
                URL url = new URL("http://" + ServerSettings.serverURL + "/chatApp/get_publication.php?publication_id=" + String.valueOf(publication_id));
                HttpURLConnection mUrlConnection = (HttpURLConnection) url.openConnection();
                mUrlConnection.setDoInput(true);
                InputStream inputStream = new BufferedInputStream(mUrlConnection.getInputStream());
                s = StreamReader.read(inputStream);
            }catch (Exception e){}
            return s;
        }

        @Override
        protected void onPostExecute(String jsonResult) {
            super.onPostExecute(jsonResult);
            try{
                JSONArray jsonArray = new JSONArray(jsonResult);
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                int publication_id = jsonObject.getInt("publication_id");
                int user_id = jsonObject.getInt("user_id");
                title_textView.setText(jsonObject.getString("title"));
                text_textView.setText(jsonObject.getString("text"));
                views_textView.setText(String.valueOf(jsonObject.getInt("views")));
                stars_textView.setText(String.valueOf(jsonObject.getInt("stars")));
                comments_textView.setText(String.valueOf(jsonObject.getInt("comments")));
                username_textView.setText("\u0040" + jsonObject.getString("username"));
            } catch (Exception e){}
        }
    }

    private class PostComment extends AsyncTask<String, Void, String>{

        int publication_id;
        int user_id;
        String comment;
        JSONObject jsonObject;

        PostComment(int publication_id, int user_id, String comment){
            this.publication_id = publication_id;
            this.user_id = user_id;
            this.comment = comment;
            this.jsonObject = new JSONObject();
            try {
                jsonObject.put("publication_id", this.publication_id);
                jsonObject.put("user_id", this.user_id);
                jsonObject.put("comment", this.comment);
                Log.i("comment","object created");
                Log.i("comment",jsonObject.toString());
            }catch (Exception e){
                Log.i("comment","error creating json");
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String s = "";
            try{
                //http://localhost/chatApp/post_comment.php?publication_id=4&user_id=0&comment=allo
                /*
                URL url = new URL("http://" + ServerSettings.serverURL +
                        "/chatApp/post_comment.php?publication_id=" + String.valueOf(publication_id)
                        + "&user_id=" + String.valueOf(user_id) + "&comment=" + comment);
                        */
                URL url = new URL("http://" + ServerSettings.serverURL +
                        "/chatApp/post_comment.php");
                HttpURLConnection mUrlConnection = (HttpURLConnection) url.openConnection();
                mUrlConnection.setDoOutput(true);
                mUrlConnection.setDoInput(true);
                mUrlConnection.setRequestProperty("Content-Type","application/json");
                Log.i("comment","properties adjusted");
                mUrlConnection.connect();
                Log.i("comment","url connected");

                OutputStreamWriter out = new   OutputStreamWriter(mUrlConnection.getOutputStream());
                out.write(jsonObject.toString());
                Log.i("comment","out written");
                out.close();
                Log.i("comment","out closed");
                InputStream inputStream = new BufferedInputStream(mUrlConnection.getInputStream());
                Log.i("comment","input stream got");
                s = StreamReader.read(inputStream);
                Log.i("comment","input stream decoded");
            }catch (Exception e){}
            return s;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                Log.i("comment",jsonObject.getString("comment"));
            }catch (Exception e){}

        }
    }

    private class GetPublicationImages extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... params) {
            String s = "";
            try{
                URL url = new URL("http://" + ServerSettings.serverURL + "/chatApp/get_publication_images.php?publication_id=" + String.valueOf(pub_id));
                HttpURLConnection mUrlConnection = (HttpURLConnection) url.openConnection();
                mUrlConnection.setDoInput(true);
                InputStream inputStream = new BufferedInputStream(mUrlConnection.getInputStream());
                s = StreamReader.read(inputStream);
            }catch (Exception e){}
            return s;
        }

        @Override
        protected void onPostExecute(String jsonResult) {
            super.onPostExecute(jsonResult);
            JSONArray jsonArray;
            try {
                jsonArray = new JSONArray(jsonResult);
                for(int i= 0; i<jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String img_link_ = jsonObject.getString("img_link");
                    images.add("http://" + ServerSettings.serverURL + "/chatApp/image_resources/" + img_link_);
                }
            }catch (Exception e){}
            initViewPager();
        }

    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    //viewpager
    private void initViewPager() {
        PagerAdapter adapter = new PagerAdapter() {

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object obj) {
                container.removeView((View) obj);
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View view = View.inflate(container.getContext(), R.layout.pager_item, null);
                ImageView imageView = (ImageView) view.findViewById(R.id.item_img);
                if(position == 0) {
                    Picasso.with(PublicationActivity.this)
                            .load("http://" + ServerSettings.serverURL + "/chatApp/image_resources/" + img_link)
                            .noFade().resize(720, 720).onlyScaleDown().noFade().centerCrop()
                            .into(imageView, new Callback() {
                                @Override
                                public void onSuccess() {
                                    ActivityCompat.startPostponedEnterTransition(PublicationActivity.this);
                                }

                                @Override
                                public void onError() {
                                    ActivityCompat.startPostponedEnterTransition(PublicationActivity.this);
                                }
                            });
                }else {
                    Picasso.with(PublicationActivity.this)
                            .load(images.get(position - 1))
                            .noFade().resize(720, 720).onlyScaleDown().noFade().centerCrop()
                            .into(imageView);
                }
                container.addView(view, ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                return view;
            }

            @Override
            public int getCount() {
                return images.size() + 1;
            }
        };
        mParallaxViewPager.setAdapter(adapter);
    }
}
