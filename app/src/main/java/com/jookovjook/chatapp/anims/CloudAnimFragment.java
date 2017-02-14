package com.jookovjook.chatapp.anims;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.jookovjook.chatapp.R;

public class CloudAnimFragment extends Fragment {

    ImageView imageView1, imageView2, cloud, arrow;
    ImageView signUp, signDown;
    ProgressBar loadingSpinner;
    Boolean loading;
    View rootView;
    CloudAnimCallback callback;

    public interface CloudAnimCallback{
        void onStartPressed();
        void onSuccessEnd();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            callback = (CloudAnimCallback) activity;
        } catch (ClassCastException e) {
            e.printStackTrace();
            //throw new ClassCastException(activity.toString() + " must implement OnHeadlineSelectedListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cloud_anim, container, false);
        this.rootView = rootView;
        try
        {
            if(getArguments().getBoolean("dark")){

            }
        }catch (Exception e){
            e.printStackTrace();
        }
        loading = false;
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!loading) {
                    upload();
                    loading = true;
                }
            }
        });
        imageView1 = (ImageView) rootView.findViewById(R.id.imageView1);
        imageView2 = (ImageView) rootView.findViewById(R.id.imageView2);
        loadingSpinner = (ProgressBar) rootView.findViewById(R.id.loadingSpinner);
        cloud = (ImageView) rootView.findViewById(R.id.cloud);
        arrow = (ImageView) rootView.findViewById(R.id.arrow);
        signUp = (ImageView) rootView.findViewById(R.id.signUp);
        signDown = (ImageView) rootView.findViewById(R.id.signDown);

        loadingSpinner.setVisibility(View.GONE);
        imageView1.setVisibility(View.GONE);
        imageView2.setVisibility(View.GONE);
        signUp.setVisibility(View.GONE);
        signDown.setVisibility(View.GONE);
        arrow.setVisibility(View.VISIBLE);
        setClickeable(true);
        return rootView;
    }

    private void setClickeable(Boolean b){
        rootView.setClickable(b);
        rootView.setFocusable(b);
    }

    public void success(){
        setClickeable(false);
        Animation animation1 = new ScaleAnimation(0, 1, 1, 1, 1f, 1f);
        animation1.setInterpolator(new AccelerateInterpolator());
        int firstDuration = 200;
        int secondDuration = 300;
        animation1.setDuration(firstDuration);
        animation1.setFillAfter(true);

        Animation animation2 = new ScaleAnimation(1, 1, 0, 1, Animation.ABSOLUTE, 0,
                Animation.RELATIVE_TO_SELF , 1);
        animation2.setInterpolator(new DecelerateInterpolator());
        animation2.setDuration(secondDuration);
        animation2.setFillAfter(true);
        animation2.setFillBefore(true);
        animation2.setStartOffset(firstDuration);

        imageView1.setVisibility(View.VISIBLE);
        imageView2.setVisibility(View.VISIBLE);

        Animation fadeSpinner = new AlphaAnimation(1, 0);
        fadeSpinner.setDuration(200);
        fadeSpinner.setFillAfter(true);
        fadeSpinner.setInterpolator(new AccelerateInterpolator());
        fadeSpinner.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                loadingSpinner.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                loadingSpinner.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) { }
        });
        loadingSpinner.clearAnimation();
        if(loading) loadingSpinner.startAnimation(fadeSpinner);
        loading = false;

        signUp.setVisibility(View.GONE);
        signUp.clearAnimation();
        signDown.setVisibility(View.GONE);
        signDown.clearAnimation();
        arrow.setVisibility(View.GONE);
        arrow.clearAnimation();

        imageView1.clearAnimation();
        imageView1.setAnimation(animation1);
        imageView2.clearAnimation();
        imageView2.setAnimation(animation2);
    }

    public void upload(){
        setClickeable(false);
        loading = true;
        loadingSpinner.setVisibility(View.GONE);
        loadingSpinner.clearAnimation();
        imageView1.setVisibility(View.GONE);
        imageView1.clearAnimation();
        imageView2.setVisibility(View.GONE);
        imageView2.clearAnimation();
        signUp.setVisibility(View.GONE);
        signUp.clearAnimation();
        signDown.setVisibility(View.GONE);
        signDown.clearAnimation();
        final Animation arrowAnim = new TranslateAnimation(0, 0, 0, - arrow.getHeight());
        arrowAnim.setInterpolator(new AccelerateInterpolator());
        arrowAnim.setDuration(501);
        arrowAnim.setFillAfter(true);
        arrowAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                arrow.setVisibility(View.VISIBLE);
                Animation fadeSpinner = new AlphaAnimation(0, 1);
                fadeSpinner.setDuration(200);
                fadeSpinner.setStartOffset(300);
                fadeSpinner.setFillAfter(true);
                fadeSpinner.setInterpolator(new AccelerateInterpolator());

                loadingSpinner.setVisibility(View.VISIBLE);
                loadingSpinner.clearAnimation();
                loadingSpinner.startAnimation(fadeSpinner);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                callback.onStartPressed();
            }

            @Override
            public void onAnimationRepeat(Animation animation) { }
        });
        arrow.clearAnimation();
        arrow.startAnimation(arrowAnim);
    }

    public void error(){
        Animation animation1 = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0f);
        animation1.setInterpolator(new AccelerateDecelerateInterpolator());
        int firstDuration = 300;
        int secondDuration = (int) (firstDuration/2.0);
        animation1.setDuration(firstDuration);
        animation1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                signUp.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) { }

            @Override
            public void onAnimationRepeat(Animation animation) { }
        });
        animation1.setFillAfter(true);

        Animation animation2 = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0f);
        animation2.setDuration(secondDuration);
        animation2.setStartOffset(firstDuration);
        animation2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                signDown.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                final Animation arrowAnim = new TranslateAnimation(0, 0, arrow.getHeight(), 0);
                arrowAnim.setInterpolator(new AccelerateInterpolator());
                arrowAnim.setDuration(500);
                arrowAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) { }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        setClickeable(true);
                        callback.onSuccessEnd();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) { }
                });
                arrowAnim.setStartOffset(1000);
                arrowAnim.setFillAfter(true);

                Animation fadeOut = new AlphaAnimation(1, 0);
                fadeOut.setStartOffset(1200);
                fadeOut.setDuration(100);
                fadeOut.setFillAfter(true);
                signUp.startAnimation(fadeOut);
                signDown.startAnimation(fadeOut);

                arrow.setVisibility(View.VISIBLE);
                arrow.clearAnimation();
                arrow.startAnimation(arrowAnim);
            }

            @Override
            public void onAnimationRepeat(Animation animation) { }
        });
        animation2.setFillAfter(true);

        Animation fadeSpinner = new AlphaAnimation(1, 0);
        fadeSpinner.setDuration(200);
        fadeSpinner.setFillAfter(true);
        fadeSpinner.setInterpolator(new AccelerateInterpolator());
        fadeSpinner.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                loadingSpinner.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                loadingSpinner.setVisibility(View.GONE);
                loading = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) { }
        });

        loadingSpinner.clearAnimation();
        if(loading) loadingSpinner.startAnimation(fadeSpinner);

        imageView1.setVisibility(View.GONE);
        imageView1.clearAnimation();
        imageView2.setVisibility(View.GONE);
        imageView2.clearAnimation();

        arrow.setVisibility(View.GONE);
        arrow.clearAnimation();


        signUp.clearAnimation();
        signDown.clearAnimation();
        signUp.startAnimation(animation1);
        signDown.startAnimation(animation2);
        //loadingSpinner.setVisibility(View.GONE);
    }

    public static CloudAnimFragment newInstance(Boolean dark) {

        Bundle args = new Bundle();
        args.putBoolean("dark", dark);

        CloudAnimFragment fragment = new CloudAnimFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
