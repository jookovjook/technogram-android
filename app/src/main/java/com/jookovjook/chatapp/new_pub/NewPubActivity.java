package com.jookovjook.chatapp.new_pub;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.jookovjook.chatapp.R;
import com.jookovjook.chatapp.anims.CloudAnimFragment;

public class NewPubActivity extends AppCompatActivity implements NewPubFragment.FragmentPublishCallback, CloudAnimFragment.CloudAnimCallback{

    Toolbar mToolbar;
//    ProgressBar loadingSpinner;
//    ImageView doneButton;
    NewPubFragment newPubFragment;
    CloudAnimFragment cloudFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_pub);
        setupLayouts();
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        result = 0;
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

    public void setupLayouts(){
        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        mToolbar.setPadding(0, getStatusBarHeight(), 0, 0);
        mToolbar.setTitle("New Pub");
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) mToolbar.getLayoutParams();
        layoutParams.height = getStatusBarHeight() + getActionBarHeight();
        mToolbar.setLayoutParams(layoutParams);
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        mToolbar.setNavigationIcon(upArrow);

        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) relativeLayout.getLayoutParams();
        lp.height = getActionBarHeight();
        lp.setMargins(0, getStatusBarHeight(), 0, 0);
        relativeLayout.setLayoutParams(lp);

        LinearLayout mainContent = (LinearLayout) findViewById(R.id.mainContent);
        CoordinatorLayout.LayoutParams lp2 = (CoordinatorLayout.LayoutParams) mainContent.getLayoutParams();
        lp2.setMargins(0, getStatusBarHeight() + getActionBarHeight(), 0, 0);
        mainContent.setLayoutParams(lp2);

        newPubFragment = (NewPubFragment) NewPubFragment.newInstance();
        newPubFragment.setFragmentPublishCallback(this);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, newPubFragment).commit();

        cloudFragment = CloudAnimFragment.newInstance(false);
        getSupportFragmentManager().beginTransaction().add(R.id.cloudContainer, cloudFragment).commit();

    }

    @Override
    public void onPublishSuccessful() {
        cloudFragment.success();
    }

    @Override
    public void onPublishError() {
        cloudFragment.error();
    }

    @Override
    public void onStartPressed() {
        newPubFragment.publish();
    }

    @Override
    public void onSuccessEnd() {

    }
}
