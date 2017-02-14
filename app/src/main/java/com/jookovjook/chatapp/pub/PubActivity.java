package com.jookovjook.chatapp.pub;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jookovjook.chatapp.R;
import com.jookovjook.chatapp.interfaces.GetPublicationInterface;
import com.jookovjook.chatapp.interfaces.GetUserInfoInterface;
import com.jookovjook.chatapp.network.GetOwnInfo;
import com.jookovjook.chatapp.network.GetPublicationImages;
import com.jookovjook.chatapp.network.LikePub;
import com.jookovjook.chatapp.network.PostComment;
import com.jookovjook.chatapp.paralaxviewpager.ParallaxViewPager;
import com.jookovjook.chatapp.utils.AuthHelper;
import com.jookovjook.chatapp.utils.Config;
import com.jookovjook.chatapp.utils.DateTimeConverter;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class PubActivity extends AppCompatActivity implements GetPublicationInterface,
        PostComment.PostCommentI, GetUserInfoInterface, LikePub.LikePubCallback, ParallaxViewPager.VPDoubleClickListener{

    private CommentAdapter commentAdapter;
    private ArrayList<CommentProvider> mList = new ArrayList<>();
    private ParallaxViewPager mParallaxViewPager;
    private ArrayList<String> images = new ArrayList<>();
    private int pub_id, like, likes, firstlyTyped = 0;
    private String img_link;
    private RecyclerView recyclerView;
    private CircleImageView avatar, avatar_;
    private TextView title, username, description, datetime;
    private EditText editText;
    private Button send;
    private ImageView imageLike;
    private PostComment.PostCommentI postCommentI = this;
    private LinearLayout comment_layout;
    private TextView textView1, likesText;
    private Boolean loggedIn = false;
    private LikesChangedCallBack callBack;
    private NestedScrollView nestedScrollView;

    @Override
    public void onVPDoubleClicked() {
        Log.d("TEST", "onDoubleTap");
        onLikeClicked();
    }

    public interface LikesChangedCallBack extends Serializable {
        void onLikesChanged(int likes, int like);
    }

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
        comment_layout = (LinearLayout) findViewById(R.id.comment_layout);
        textView1 = (TextView) findViewById(R.id.textView1);
        imageLike = (ImageView) findViewById(R.id.imageLike);
        likesText = (TextView) findViewById(R.id.likesText);
        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);
    }

    private void proceedBunle(){
        Bundle bundle = getIntent().getExtras();
        pub_id = -1;
        if(bundle != null){
            img_link = bundle.getString("img_link");
            pub_id = bundle.getInt("publication_id");
            loggedIn = bundle.getBoolean("loggedIn");
            datetime.setText(bundle.getString("datetime"));
            title.setText(bundle.getString("title"));
            description.setText(bundle.getString("text"));
            username.setText("\u0040" + bundle.getString("username"));
            Picasso.with(this).load(Config.IMAGE_RESOURCES_URL + bundle.getString("avatar"))
                    .error(R.drawable.grid).resize(128, 128)
                    .onlyScaleDown().centerCrop().into(this.avatar);
            Log.i("pub activity ", String.valueOf(pub_id));
            like = bundle.getInt("like");
            likes = bundle.getInt("likes");
            //ActivityCompat.postponeEnterTransition(this);
            GetPublicationImages getPublicationImages = new GetPublicationImages(pub_id, this);
            getPublicationImages.execute();
            //GetPublication getPublication = new GetPublication(pub_id, this);
            //getPublication.execute();
            try {
                //callBack = (LikesChangedCallBack) bundle.getSerializable("callback");
            }catch (Exception e){
                e.printStackTrace();
            }
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
        if(loggedIn)
        {
            comment_layout.setVisibility(View.VISIBLE);
            Picasso.with(this).load(Config.IMAGE_RESOURCES_URL + AuthHelper.getAvatar(this))
                    .error(R.drawable.grid).resize(128, 128)
                    .onlyScaleDown().centerCrop().into(this.avatar_);
        }
        changeLikeIcon();
        incLikes(likesText, like, likes);
        imageLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLikeClicked();
                Log.i("PubActivity", "image clicked!");
            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(firstlyTyped == 0) nestedScrollView.fullScroll(View.FOCUS_DOWN);
                firstlyTyped ++;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setLike(final ImageView imageView, final int like){
        Animation anim = new ScaleAnimation(
                1f, 0.5f, // Start and end values for the X axis scaling
                1f, 0.5f, // Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
                Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setFillAfter(true); // Needed to keep the result of the animation
        anim.setDuration(75);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) { }

            @Override
            public void onAnimationEnd(Animation animation) {
                changeLikeIcon();
                Animation anim2 = new ScaleAnimation(
                        0.5f, 1f, // Start and end values for the X axis scaling
                        0.5f, 1f, // Start and end values for the Y axis scaling
                        Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
                        Animation.RELATIVE_TO_SELF, 0.5f);
                anim2.setFillAfter(true); // Needed to keep the result of the animation
                anim2.setDuration(75);
                imageLike.startAnimation(anim2);
            }

            @Override
            public void onAnimationRepeat(Animation animation) { }
        });
        imageLike.startAnimation(anim);
    }

    private void changeLikeIcon(){
        if(like == 1){
            imageLike.setImageDrawable(PubActivity.this.getResources().getDrawable(R.drawable.heart_full));
            imageLike.setColorFilter(ContextCompat.getColor(PubActivity.this, R.color.colorHeartUnliked));
            imageLike.clearAnimation();
        }else{
            if(like == 2){
                imageLike.clearAnimation();
                imageLike.setImageDrawable(PubActivity.this.getResources().getDrawable(R.drawable.heart_double));
                imageLike.setColorFilter(ContextCompat.getColor(PubActivity.this, R.color.colorHeart));
                Animation heartAnimation = AnimationUtils.loadAnimation(PubActivity.this, R.anim.heart_anim);
                imageLike.setAnimation(heartAnimation);
            }else{
                imageLike.setImageDrawable(PubActivity.this.getResources().getDrawable(R.drawable.heart_empty));
                imageLike.setColorFilter(ContextCompat.getColor(PubActivity.this, R.color.colorHeartUnliked));
                imageLike.clearAnimation();
            }
        }
    }

    private void incLikes(TextView textView, int i, int current){
        switch(i){
            case 1:
                textView.setText("(" + String.valueOf(current) + ")");
                textView.setTextColor(ContextCompat.getColor(this, R.color.colorHeartUnliked));
                break;
            case 2:
                textView.setText("(" + String.valueOf(current) + ")");
                textView.setTextColor(ContextCompat.getColor(this, R.color.colorHeart));
                break;
            default:
                textView.setText("(" + String.valueOf(current) + ")");
                textView.setTextColor(ContextCompat.getColor(this, R.color.colorHeartUnliked));
        }
    }

    private void onLikeClicked(){
        LikePub likePub = new LikePub(this, this, pub_id);
        likePub.execute();
        switch (like){
            case 0:
                like = 1;
                likes ++;
                setLike(imageLike, 1);
                incLikes(likesText, 1, likes);
                break;
            case 1:
                //feedCallback.onDoubleLiked();
                like = 2;
                likes ++;
                setLike(imageLike, 2);
                incLikes(likesText, 2, likes);
                break;
            default:
                like = 0;
                likes -= 2;
                setLike(imageLike, 0);
                incLikes(likesText, 0, likes);
                break;
        }
        //callBack.onLikesChanged(likes, like);
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
                            .error(R.drawable.grid)
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
                            .error(R.drawable.grid)
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
        mParallaxViewPager.setVpDoubleClickListener(this);
    }

    @Override
    public void onGotPublication(String title, String text, int views, int stars, int comments, String username, String avatar, Date date) {
        Picasso.with(this).load(Config.IMAGE_RESOURCES_URL + avatar).error(R.drawable.grid)
                .resize(128, 128).onlyScaleDown().centerCrop().into(this.avatar);
        this.username.setText("\u0040" + username);
        this.title.setText(title);
        this.description.setText(text);
        this.datetime.setText(DateTimeConverter.convert(date));
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
                .error(R.drawable.grid)
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
    public void onWrongToken() { }

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

    @Override
    public void onDisliked() {

    }

    @Override
    public void onLiked() {

    }

    @Override
    public void onDoubleLiked() {

    }

    @Override
    public void onError() {

    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }
}
