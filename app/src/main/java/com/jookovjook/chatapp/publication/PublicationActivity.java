package com.jookovjook.chatapp.publication;

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
import com.jookovjook.chatapp.interfaces.CommentAdapterCallback;
import com.jookovjook.chatapp.interfaces.GetPublicationInterface;
import com.jookovjook.chatapp.network.GetPublication;
import com.jookovjook.chatapp.network.GetPublicationImages;
import com.jookovjook.chatapp.network.PostComment;
import com.jookovjook.chatapp.paralaxviewpager.ParallaxViewPager;
import com.jookovjook.chatapp.utils.Config;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class PublicationActivity extends AppCompatActivity implements CommentAdapterCallback, GetPublicationInterface{

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
            GetPublicationImages getPublicationImages = new GetPublicationImages(pub_id, this);
            getPublicationImages.execute();
            GetPublication getPublication = new GetPublication(pub_id, this);
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
        username_textView = (TextView) findViewById(R.id.username);
        image = (ImageView) findViewById(R.id.image);
        hidden_image = (ImageView) findViewById(R.id.hidden_image);
        editText = (EditText) findViewById(R.id.editText);
        button_send = (Button) findViewById(R.id.send_button);
        button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostComment postComment = new PostComment(pub_id, 0, editText.getText().toString(), null, PublicationActivity.this);
                postComment.execute();

            }
        });
        //image.requestFocus();
        mParallaxViewPager = (ParallaxViewPager) findViewById(R.id.viewpager);
        mFadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        mFadeOutAnimation.setFillAfter(true);
        //initViewPager();
    }

    @Override
    public void onGotPublication(String title, String text, int views, int stars, int comments, String username, String avatar, Date date) {
        title_textView.setText(title);
        text_textView.setText(text);
        views_textView.setText(String.valueOf(views));
        stars_textView.setText(String.valueOf(stars));
        comments_textView.setText(String.valueOf(comments));
        username_textView.setText("\u0040" + username);
    }

    @Override
    public void onGotSoftAdv(int license, int stage) {

    }

    @Override
    public void onGotPubImage(String url) {
        images.add(url);
    }

    @Override
    public void onFinishGettingImages() {
        initViewPager();
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
                            .load(Config.IMAGE_RESOURCES_URL + img_link)
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
