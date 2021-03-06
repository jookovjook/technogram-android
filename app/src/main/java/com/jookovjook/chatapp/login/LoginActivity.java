package com.jookovjook.chatapp.login;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jookovjook.chatapp.MainActivity;
import com.jookovjook.chatapp.R;
import com.jookovjook.chatapp.utils.Metrics;

import io.codetail.animation.arcanimator.ArcAnimator;
import io.codetail.animation.arcanimator.Side;

public class LoginActivity extends AppCompatActivity implements OnFragmentTouched, LoginFragment.NewLoginFragmentCallback{

    Button loginButton, signupButton, loginButtonHidden, signupButtonHidden;
    Button centerButton, bottomButton;
    FrameLayout fragmentContainer;
    int absHeight, absWidth;
    int rectLogin[] = new int[2];
    int butFlyTime = 350;
    TextView skipTextView;
    Fragment newLoginFragment;
    ObjectAnimator elevUpAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        adjustStatusbar();
        findViews();
        calculateMetrics();
        adjustViews();
        super.onCreate(savedInstanceState);
    }

    private void attachFragment(){
        int randomColor = getResources().getColor(R.color.colorAccent);
        newLoginFragment = LoginFragment.newInstance(Metrics.dpToPx((int)300.0/2), Metrics.dpToPx((int)300.0/2), randomColor);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, newLoginFragment).commit();
    }

    private void calculateMetrics() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        absWidth = size.x;
        absHeight = size.y;
    }

    private void adjustStatusbar(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decor = getWindow().getDecorView();
            if (true) {
                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                decor.setSystemUiVisibility(0);
            }
        }
    }

    private void findViews(){
        loginButton = (Button) findViewById(R.id.loginButton);
        signupButton = (Button) findViewById(R.id.signupButton);
        loginButtonHidden = (Button) findViewById(R.id.loginButtonHidden);
        signupButtonHidden = (Button) findViewById(R.id.signupButtonHidden);
        centerButton = (Button) findViewById(R.id.centerButton);
        bottomButton = (Button) findViewById(R.id.bottomButton);
        skipTextView = (TextView) findViewById(R.id.skipTextView);
        fragmentContainer = (FrameLayout) findViewById(R.id.fragment_container);
    }

    private void adjustViews(){
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lp.setMargins((int)(absWidth/2.0) - 250, 0 , 0 , 100);
        loginButton.setLayoutParams(lp);
        loginButtonHidden.setLayoutParams(lp);

        lp = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        lp.setMargins(0, 0, (int)(absWidth/2.0) - 250, 100);
        signupButton.setLayoutParams(lp);
        signupButtonHidden.setLayoutParams(lp);
        signupButton.setEnabled(false);
        signupButton.setAlpha(.5f);

        lp = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
        lp.setMargins(0, absHeight + (int)(absHeight/2.0), 0, 0);
        bottomButton.setLayoutParams(lp);


        //loginButton.setLayoutParams(params);
        loginButton.setLeft((int)(absWidth/4.0));
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.getLocationOnScreen(rectLogin);
                ArcAnimator arcAnimator = ArcAnimator.createArcAnimator(loginButton, centerButton, 90, Side.RIGHT).setDuration(butFlyTime);
                arcAnimator.addListener(new com.nineoldandroids.animation.Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(com.nineoldandroids.animation.Animator animation) { }

                    @Override
                    public void onAnimationEnd(com.nineoldandroids.animation.Animator animation) {
                        onLoginPressed();
                    }

                    @Override
                    public void onAnimationCancel(com.nineoldandroids.animation.Animator animation) { }

                    @Override
                    public void onAnimationRepeat(com.nineoldandroids.animation.Animator animation) { }
                });
                arcAnimator.setInterpolator(new AccelerateInterpolator());
                arcAnimator.start();

                ArcAnimator arcAnimator2 = ArcAnimator.createArcAnimator(signupButton, bottomButton, 90, Side.LEFT).setDuration(butFlyTime);
                arcAnimator2.setInterpolator(new AccelerateInterpolator());
                arcAnimator2.start();
            }
        });

        skipTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                finish();
            }
        });
    }

    private void onLoginPressed(){
        attachFragment();
    }

    @Override
    public void onFragmentTouched(Fragment fragment, float x, float y) {
        elevUpAnim.cancel();
        fragmentContainer.clearAnimation();
        fragmentContainer.setBackgroundColor(ContextCompat.getColor(this, R.color.colorEmpty));
        ObjectAnimator.ofFloat(fragmentContainer, "z", 0).setDuration(0).start();
        if (fragment instanceof LoginFragment) {
            final LoginFragment theFragment = (LoginFragment) fragment;
            Animator unreveal = theFragment.prepareUnrevealAnimator(x, y);
            unreveal.addListener(new Animator.AnimatorListener() {

                @Override
                public void onAnimationStart(Animator animation) { }

                @Override
                public void onAnimationEnd(Animator animation) {
                    (findViewById(R.id.fragment_container)).setVisibility(View.INVISIBLE);
                    // remove the fragment only when the animation finishes
                    getSupportFragmentManager().beginTransaction().remove(theFragment).commit();
                    //to prevent flashing the fragment before removing it, execute pending transactions inmediately
                    getFragmentManager().executePendingTransactions();
                    ArcAnimator arcAnimator = ArcAnimator.createArcAnimator(loginButton, loginButtonHidden, 90, Side.RIGHT) .setDuration(butFlyTime);
                    arcAnimator.addListener(new com.nineoldandroids.animation.Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(com.nineoldandroids.animation.Animator animation) { }

                        @Override
                        public void onAnimationEnd(com.nineoldandroids.animation.Animator animation) {
                            (findViewById(R.id.fragment_container)).setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationCancel(com.nineoldandroids.animation.Animator animation) { }

                        @Override
                        public void onAnimationRepeat(com.nineoldandroids.animation.Animator animation) { }
                    });
                    arcAnimator.setInterpolator(new DecelerateInterpolator());
                    arcAnimator.start();

                    ArcAnimator arcAnimator2 = ArcAnimator.createArcAnimator(signupButton, signupButtonHidden, 90, Side.LEFT).setDuration(butFlyTime);
                    arcAnimator2.setInterpolator(new DecelerateInterpolator());
                    arcAnimator2.start();
                }

                @Override
                public void onAnimationCancel(Animator animation) { }

                @Override
                public void onAnimationRepeat(Animator animation) { }
            });
            unreveal.start();
        }
    }

    @Override
    public void onFragmentRevealed() {
        elevUpAnim = ObjectAnimator.ofFloat(fragmentContainer, "z", Metrics.dpToPx(4));
        elevUpAnim.setStartDelay(300);
        elevUpAnim.setDuration(300);
        elevUpAnim.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
                fragmentContainer.setBackgroundColor(ContextCompat.getColor(LoginActivity.this, R.color.colorAccent));
            }

            @Override
            public void onAnimationEnd(Animator animation) { }

            @Override
            public void onAnimationCancel(Animator animation) { }

            @Override
            public void onAnimationRepeat(Animator animation) { }
        });
        elevUpAnim.start();
    }

    @Override
    public void onSuccess() {
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        finish();
    }
}