package com.jookovjook.chatapp.user_profile;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jookovjook.chatapp.R;
import com.jookovjook.chatapp.about_fragment.AboutFragment;
import com.jookovjook.chatapp.feed_fragment.FeedFragment;
import com.jookovjook.chatapp.utils.ServerSettings;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class UserProfileActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener{

    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR  = 0.75f;
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
    private String username, name_surname;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Bundle bundle = getIntent().getExtras();
        user_id = -1;
        username = "";
        // or other values
        if (bundle != null) {
            user_id = bundle.getInt("user_id");
            Log.i("got user id", String.valueOf(user_id));
            username = bundle.getString("username");
        }

        bindActivity();

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
        viewPager.setAdapter(new TabsAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);

        GetUserInfo getUserInfo = new GetUserInfo(user_id);
        getUserInfo.execute();
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
        mToolbar        = (Toolbar) findViewById(R.id.main_toolbar);
        mToolbar.setPadding(0, getStatusBarHeight(), 0, 0);
        setStatusBarHeight();
        mTitle          = (TextView) findViewById(R.id.main_textview_title);
        mTitleContainer = (LinearLayout) findViewById(R.id.main_linearlayout_title);
        mAppBarLayout   = (AppBarLayout) findViewById(R.id.main_appbar);
        mUsername = (TextView) findViewById(R.id.username);
        mNameSurname = (TextView) findViewById(R.id.name_surname);
        mUsername.setText("\u0040" +  username);
    }

    //tabs layout
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
                case 0: return FeedFragment.newInstance(0, String.valueOf(user_id));
                case 1: return AboutFragment.newInstance("Tset");
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

    private String readStream(InputStream is) {
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            int i = is.read();
            while(i != -1) {
                bo.write(i);
                i = is.read();
            }
            return bo.toString();
        } catch (IOException e) {
            return "";
        }
    }

    private class GetUserInfo extends AsyncTask<String, Void, String>{

        int user_id_;

        GetUserInfo(int user_id_){
            this.user_id_ = user_id_;
        }

        @Override
        protected String doInBackground(String... params) {
            String s = "{&quoterror_code&quot:&quot001&quot}";
            try {
                Log.i("setting url = ", String.valueOf(user_id_));
                URL url = new URL("http://" + ServerSettings.serverURL + "/chatApp/get_user_info.php?user_id=" + String.valueOf(user_id_));
                HttpURLConnection mUrlConnection = (HttpURLConnection) url.openConnection();
                mUrlConnection.setDoInput(true);
                InputStream inputStream = new BufferedInputStream(mUrlConnection.getInputStream());
                s = readStream(inputStream);
            }catch (Exception e){
                Log.i("get_user_info","error");
            }
            return s;
        }

        @Override
        protected void onPostExecute(String jsonResult) {
            super.onPostExecute(jsonResult);
            // выводим целиком полученную json-строку
            JSONObject jsonObject = null;
            int error_code = 001;
            try {
                jsonObject = new JSONObject(jsonResult);
                error_code = jsonObject.getInt("error_code");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try{
                if(error_code == 0){
                    mNameSurname.setText(jsonObject.getString("name") + " " + jsonObject.getString("surname"));
                }
            } catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    /* Hiding toolbar animation */

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

    public void setStatusBarHeight(){
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) mToolbar.getLayoutParams();
        int actionBarHeight = 0;
        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
        {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
        }
        layoutParams.height = actionBarHeight + getStatusBarHeight();
        mToolbar.setLayoutParams(layoutParams);
    }

}
