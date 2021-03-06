package com.jookovjook.chatapp.user_profile;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jookovjook.chatapp.R;
import com.jookovjook.chatapp.about_fragment.AboutFragment;
import com.jookovjook.chatapp.feed_fragment.FeedFragment;
import com.jookovjook.chatapp.network.NewGetUserInfo;
import com.jookovjook.chatapp.utils.Config;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.picasso.transformations.BlurTransformation;

public class UserProfileActivity extends AppCompatActivity implements
        AppBarLayout.OnOffsetChangedListener, NewGetUserInfo.GetUserInfoCallback {

    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR  = 1f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS     = 0.2f;
    private static final int ALPHA_ANIMATIONS_DURATION              = 200;
    private boolean mIsTheTitleVisible          = false;
    private boolean mIsTheTitleContainerVisible = true;
    private LinearLayout mTitleContainer;
    private TextView mTitle;
    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;
    private TextView mUsername, mNameSurname;
    int user_id;
    private String username, avatarLink;
    private CircleImageView avatar;
    private View background;
    private Boolean own = false;
    private Boolean avatarPreload = false;
    private AboutFragment aboutFragment;
    private NewGetUserInfo.UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        findViews();
        proceedBundle();
        bindActivity();

    }

    private void proceedBundle(){
        Bundle bundle = getIntent().getExtras();
        user_id = -1;
        username = "";
        if (bundle != null) {
            try {
                user_id = bundle.getInt("user_id");
                own = bundle.getBoolean("own");
                username = bundle.getString( "username");
                mUsername.setText("\u0040" + username);
                avatarLink = bundle.getString("avatar");
                Picasso.with(this)
                        .load(Config.IMAGE_RESOURCES_URL + avatarLink)
                        .resize(256, 256).onlyScaleDown().centerCrop().into(avatar, new Callback() {
                    @Override
                    public void onSuccess() {
                        setAvatarFadeInAnimation(avatar);
                    }

                    @Override
                    public void onError() {
                        setAvatarFadeInAnimation(avatar);
                    }
                });
                avatarPreload = true;
                Picasso.with(this)
                        .load(Config.IMAGE_RESOURCES_URL + avatarLink)
                        .resize(180, 180)
                        .centerCrop()
                        .transform(new BlurTransformation(this, 5))
                        .into((ImageView) background);

            }catch (Exception e){

                e.printStackTrace();
            }
        }
    }

    void findViews(){
        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        mTitle          = (TextView) findViewById(R.id.main_textview_title);
        mTitleContainer = (LinearLayout) findViewById(R.id.main_linearlayout_title);
        mAppBarLayout   = (AppBarLayout) findViewById(R.id.main_appbar);
        mUsername = (TextView) findViewById(R.id.username);
        mNameSurname = (TextView) findViewById(R.id.name_surname);
        mUsername.setText("\u0040" +  username);
        avatar = (CircleImageView) findViewById(R.id.avatar);
        background = findViewById(R.id.background);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void bindActivity() {
        mToolbar.setPadding(0, getStatusBarHeight(), 0, 0);
        setStatusBarHeight();
        CollapsingToolbarLayout main_collapsing = (CollapsingToolbarLayout) findViewById(R.id.main_collapsing);
        main_collapsing.setMinimumHeight(getStatusBarHeight() + getActionBarHeight());
        mAppBarLayout.addOnOffsetChangedListener(this);
        //toolbar animation
        mToolbar.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
        //toolbar animation
        mToolbar.inflateMenu(R.menu.menu_user_profile);
        startAlphaAnimation(mTitle, 0, View.INVISIBLE);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });
        //tabs layout
        TabLayout tabLayout = (TabLayout) findViewById(R.id.materialup_tabs);
        ViewPager viewPager  = (ViewPager) findViewById(R.id.materialup_viewpager);
        aboutFragment = AboutFragment.newInstance();
        viewPager.setAdapter(new TabsAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
        NewGetUserInfo getUserInfo = new NewGetUserInfo(user_id, this);
        getUserInfo.execute();
    }

    @Override
    public void onGotUserInfo(NewGetUserInfo.UserInfo userInfo) {
        Log.i("UserProfileActivity", "gotUserInfo");
        mNameSurname.setText(userInfo.name + " " + userInfo.surname);
//        Picasso.with(this)
//                .load(Config.IMAGE_RESOURCES_URL + userInfo.avatar)
//                .resize(256, 256).onlyScaleDown().centerCrop().into(avatar, new Callback() {
//            @Override
//            public void onSuccess() {
//                if(!avatarPreload) setAvatarFadeInAnimation(avatar);
//            }
//
//            @Override
//            public void onError() {
//                if(!avatarPreload) setAvatarFadeInAnimation(avatar);
//            }
//        });
//        Picasso.with(this)
//                .load(Config.IMAGE_RESOURCES_URL + userInfo.avatar)
//                .resize(180, 180)
//                .centerCrop()
//                .transform(new BlurTransformation(this, 5))
//                .into((ImageView) background);

        aboutFragment.updateInfo(userInfo);
    }

    @Override
    public void onErrorGettingUserInfo() {
        Log.i("UserProfileActivity", "error gotUserInfo");
    }

    class TabsAdapter extends FragmentPagerAdapter {

        public TabsAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Fragment getItem(int i) {
            switch(i) {
                case 0: return FeedFragment.newInstance(0, user_id);
                case 1: return aboutFragment;
            }
            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch(position) {
                case 0: return "Feed";
                case 1: return "About";
            }
            return "";
        }
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;

        handleAlphaOnTitle(percentage);
        handleToolbarTitleVisibility(percentage);
    }

    public static void startAlphaAnimation (View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }

    public static void startColorAnimation(Context context, View v, int duration, int visibility){
        //ColorDrawable[] color1 = {new ColorDrawable(context.getResources().getColor(R.color.colorTransparent)), new ColorDrawable(context.getResources().getColor(R.color.colorPrimary))};
        //ColorDrawable[] color2 = {new ColorDrawable(context.getResources().getColor(R.color.colorPrimary)), new ColorDrawable(context.getResources().getColor(R.color.colorTransparent))};
        //TransitionDrawable trans1, trans2;
        if(visibility == View.VISIBLE) {
            //Log.i("test","Forward");
            //trans1 = new TransitionDrawable(color1);
            //v.setBackground(trans1);
            //trans1.startTransition(duration);
            v.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
        }else{
            //Log.i("test","Backward");
            //trans2 = new TransitionDrawable(color2);
            //v.setBackground(trans2);
            //trans2.startTransition(duration);
            v.setBackgroundColor(context.getResources().getColor(R.color.colorTransparent));
        }
        //TransitionDrawable transitionDrawable = (visibility == View.VISIBLE)
        //        ? new TransitionDrawable(color1)
        //        : new TransitionDrawable(color2);
        //v.setBackground(transitionDrawable);
        //transitionDrawable.startTransition(duration);
    }

    private void handleAlphaOnTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if(mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                //startColorAnimation(UserProfileActivity.this, mToolbar, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleContainerVisible = false;

            }

        } else {

            if (!mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                //startColorAnimation(UserProfileActivity.this, mToolbar, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleContainerVisible = true;
            }
        }
    }

    private void handleToolbarTitleVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

            if(!mIsTheTitleVisible) {
                startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                startColorAnimation(UserProfileActivity.this, mToolbar, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleVisible = true;
            }

        } else {

            if (mIsTheTitleVisible) {
                startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                startColorAnimation(UserProfileActivity.this, mToolbar, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleVisible = false;
            }
        }
    }

    // A method to find height of the status bar
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public int getActionBarHeight(){
        int actionBarHeight = 0;
        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
        {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }
        return actionBarHeight;
    }

    public void setStatusBarHeight(){
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) mToolbar.getLayoutParams();
        layoutParams.height = getActionBarHeight() + getStatusBarHeight();
        mToolbar.setLayoutParams(layoutParams);
    }

    private void setAvatarFadeInAnimation(final View viewToAnimate) {
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
            animation.setFillAfter(true);
            //animation.setDuration(100);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    viewToAnimate.setAlpha(1);
                }

                @Override
                public void onAnimationEnd(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        //viewToAnimate.clearAnimation();
        viewToAnimate.startAnimation(animation);
    }

    public void openLink(String s){
        s = s.replaceAll("[^a-zA-Z/:._0-9-]","");
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(s.replace(" ","")));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }else{
            Log.e("Error opening link", s);
        }
    }

}
