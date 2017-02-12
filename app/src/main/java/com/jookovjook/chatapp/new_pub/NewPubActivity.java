package com.jookovjook.chatapp.new_pub;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.jookovjook.chatapp.R;

public class NewPubActivity extends AppCompatActivity implements NewPubFragment.FragmentPublishCallback{

    Toolbar mToolbar;
    ProgressBar loadingSpinner;
    ImageView doneButton;
    NewPubFragment newPubFragment;

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

        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) relativeLayout.getLayoutParams();
        lp.height = getActionBarHeight();
        lp.setMargins(0, getStatusBarHeight(), 0, 0);
        relativeLayout.setLayoutParams(lp);

        LinearLayout mainContent = (LinearLayout) findViewById(R.id.mainContent);
        CoordinatorLayout.LayoutParams lp2 = (CoordinatorLayout.LayoutParams) mainContent.getLayoutParams();
        lp2.setMargins(0, getStatusBarHeight() + getActionBarHeight(), 0, 0);
        mainContent.setLayoutParams(lp2);

        loadingSpinner = (ProgressBar) findViewById(R.id.loadingSpinner);

        doneButton = (ImageView) findViewById(R.id.doneButton);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadingSpinner.setVisibility(View.VISIBLE);
                doneButton.setClickable(false);
                newPubFragment.publish();
            }
        });

        newPubFragment = (NewPubFragment) NewPubFragment.newInstance();
        newPubFragment.setFragmentPublishCallback(this);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, newPubFragment).commit();

    }

    @Override
    public void onPublishSuccessful() {
        loadingSpinner.setVisibility(View.GONE);
        doneButton.setClickable(true);
    }

    @Override
    public void onPublishError() {
        loadingSpinner.setVisibility(View.GONE);
        doneButton.setClickable(true);
    }
}
