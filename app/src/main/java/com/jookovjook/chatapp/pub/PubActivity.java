package com.jookovjook.chatapp.pub;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jookovjook.chatapp.R;
import com.jookovjook.chatapp.interfaces.GetPublicationInterface;
import com.jookovjook.chatapp.interfaces.GetUserInfoInterface;
import com.jookovjook.chatapp.network.GetOwnInfo;
import com.jookovjook.chatapp.network.GetPublication;
import com.jookovjook.chatapp.network.GetPublicationImages;
import com.jookovjook.chatapp.network.PostComment;
import com.jookovjook.chatapp.paralaxviewpager.ParallaxViewPager;
import com.jookovjook.chatapp.utils.AuthHelper;
import com.jookovjook.chatapp.utils.Config;
import com.jookovjook.chatapp.utils.DateTimeConverter;
import com.jookovjook.chatapp.utils.SoftAdd;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class PubActivity extends AppCompatActivity implements GetPublicationInterface, PostComment.PostCommentI, GetUserInfoInterface{

    private CommentAdapter commentAdapter;
    private ArrayList<CommentProvider> mList = new ArrayList<>();
    private ParallaxViewPager mParallaxViewPager;
    private ArrayList<String> images = new ArrayList<>();
    private int pub_id, stars_int;
    private String img_link;
    private RecyclerView recyclerView;
    private CircleImageView avatar, avatar_;
    private TextView title, username, description, datetime, stars;
    private EditText editText;
    private Button send;
    private ImageButton star_button;
    private PostComment.PostCommentI postCommentI = this;
    private LinearLayout comment_layout;
    private TextView textView1;

    private void findViews(){
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mParallaxViewPager = (ParallaxViewPager) findViewById(R.id.viewpager);
        avatar = (CircleImageView) findViewById(R.id.avatar);
        avatar_ = (CircleImageView) findViewById(R.id.avatar_);
        title = (TextView) findViewById(R.id.title);
        username = (TextView) findViewById(R.id.username);
        description = (TextView) findViewById(R.id.description);
        datetime = (TextView) findViewById(R.id.datetime);
        editText = (EditText) findViewById(R.id.editText);
        send = (Button) findViewById(R.id.send_button);
        stars = (TextView) findViewById(R.id.stars);
        comment_layout = (LinearLayout) findViewById(R.id.comment_layout);
        textView1 = (TextView) findViewById(R.id.textView1);
        //star_button = (ImageButton) findViewById(R.id.star_button);
    }

    private void proceedBunle(){
        stars_int = 0;
        Bundle bundle = getIntent().getExtras();
        pub_id = -1;
        if(bundle != null){
            img_link = bundle.getString("img_link");
            pub_id = bundle.getInt("publication_id");
            Log.i("pub activity ", String.valueOf(pub_id));
            //ActivityCompat.postponeEnterTransition(this);
            GetPublicationImages getPublicationImages = new GetPublicationImages(pub_id, this);
            getPublicationImages.execute();
            GetPublication getPublication = new GetPublication(pub_id, this);
            getPublication.execute();
        }
    }

    private void setupRV(){
        commentAdapter = new CommentAdapter(pub_id, this, mList);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        assert recyclerView != null;
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(commentAdapter);
        recyclerView.setNestedScrollingEnabled(false);
    }

    private void setupOther(){
        //        star_button.setOnClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View v) {
        //                stars_int ++;
        //                stars.setText(String.valueOf(stars_int));
        //            }
        //        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editText.getText().toString().equals("")) {
                    PostComment postComment = new PostComment(pub_id, 0, editText.getText().toString(), postCommentI, PubActivity.this);
                    postComment.execute();
                }
                if (v != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        });
        customTextView(textView1);
    }

    @SuppressWarnings("SpellCheckingInspection")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pub);
        findViews();
        proceedBunle();
        setupRV();
        setupOther();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void initViewPager() {
        PagerAdapter adapter = new PagerAdapter() {

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object obj) {
                container.removeView((View) obj);
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View view = View.inflate(container.getContext(), R.layout.pager_item, null);
                ImageView imageView = (ImageView) view.findViewById(R.id.item_img);
                if(position == 0) {
                    Picasso.with(PubActivity.this)
                            .load(Config.IMAGE_RESOURCES_URL + img_link)
                            .noFade().resize(720, 720).onlyScaleDown().noFade().centerCrop()
                            .into(imageView, new Callback() {
                                @Override
                                public void onSuccess() {
                                    ActivityCompat.startPostponedEnterTransition(PubActivity.this);
                                    loadOwnAvatar();
                                }

                                @Override
                                public void onError() {
                                    ActivityCompat.startPostponedEnterTransition(PubActivity.this);
                                    loadOwnAvatar();
                                }
                            });
                }else {
                    Picasso.with(PubActivity.this)
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

    @Override
    public void onGotPublication(String title, String text, int views, int stars, int comments, String username, String avatar, Date date) {
        Picasso.with(this).load(Config.IMAGE_RESOURCES_URL + avatar).resize(128, 128).onlyScaleDown().centerCrop().into(this.avatar);
        this.username.setText("\u0040" + username);
        this.title.setText(title);
        this.description.setText(text);
        this.datetime.setText(DateTimeConverter.convert(date));
        if(stars < 1) stars = 1;
        stars_int = stars - 1;
        this.stars.setText(String.valueOf(stars_int));
    }

    @Override
    public void onGotSoftAdv(int license, int stage) {
        String license_str = SoftAdd.getLicenseById(license);
        if(!license_str.equals("")) {
            //TextView license_text = (TextView) findViewById(R.id.license);
            //license_text.setText(" " + license_str + " ");
            //license_text.setVisibility(View.VISIBLE);
        }

        String stage_str = SoftAdd.getStageById(stage);
        if(!stage_str.equals("")){
            //TextView stage_text = (TextView) findViewById(R.id.stage);
            //stage_text.setText(" " + stage_str + " ");
            //stage_text.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onGotPubImage(String url) {
        images.add(url);
    }

    @Override
    public void onFinishGettingImages() {
        initViewPager();
    }

    @Override
    public void onSuccess(CommentProvider commentProvider) {
        mList.add(commentProvider);
        commentAdapter.notifyDataSetChanged();
        editText.clearFocus();
        editText.setText("");
        Log.i("PubActivity: ", "comment posted");
    }

    @Override
    public void onFailure() {
        logOut();
    }

    @Override
    public void onGotUserInfo(String username, String name, String surname, String avatar_link) {
        Picasso.with(this).load(Config.IMAGE_RESOURCES_URL + avatar_link).resize(128, 128)
                .onlyScaleDown().centerCrop().into(this.avatar_, new Callback() {
            @Override
            public void onSuccess() {
                comment_layout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError() {

            }
        });

    }

    @Override
    public void onWrongToken() {
        //logOut();
    }

    private void loadOwnAvatar(){
        GetOwnInfo getOwnInfo = new GetOwnInfo(AuthHelper.getToken(this), this);
        getOwnInfo.execute();
    }

    private void logOut(){

    }

    private void customTextView(TextView view) {
        // was 15
        // remained 8
        SpannableStringBuilder spanTxt = new SpannableStringBuilder(
                "Please, ");
        // was 16
        // remained 6
        spanTxt.append("Log in");
        spanTxt.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Toast.makeText(getApplicationContext(), "Log in Clicked",
                        Toast.LENGTH_SHORT).show();
            }
        }, spanTxt.length() - "Log in".length(), spanTxt.length(), 0);
        // was 4
        // remained 3
        spanTxt.append(" or");
        spanTxt.setSpan(new ForegroundColorSpan(Color.BLACK), 15, spanTxt.length(), 0);
        spanTxt.append(" Register");
        spanTxt.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Toast.makeText(getApplicationContext(), "Register Clicked",
                        Toast.LENGTH_SHORT).show();
            }
        }, spanTxt.length() - " Register".length(), spanTxt.length(), 0);
        spanTxt.append(" to leave a comment");
        view.setMovementMethod(LinkMovementMethod.getInstance());
        view.setText(spanTxt, TextView.BufferType.SPANNABLE);
    }
}
