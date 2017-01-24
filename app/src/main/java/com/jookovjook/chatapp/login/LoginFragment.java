package com.jookovjook.chatapp.login;

import android.animation.Animator;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.jookovjook.chatapp.R;
import com.jookovjook.chatapp.network.LogIn;
import com.jookovjook.chatapp.utils.Metrics;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Arrays;
import java.util.Collections;

public class LoginFragment extends Fragment implements LogIn.LogInCallback{

    OnFragmentTouched listener;
    int revealTime = 1000;
    int unrevealTime = 500;
    Button cancelButton, loginButton;
    MaterialEditText username, password;
    LogIn.LogInCallback logInCallback = this;
    RelativeLayout redLayout;
    NewLoginFragmentCallback newLoginFragmentCallBack;
    int cx_, cy_, radius_, init_radius;

    public interface NewLoginFragmentCallback{
        void onSuccess();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.new_login_fragment, container, false);

        rootView.setBackgroundColor(getArguments().getInt("color"));
        redLayout = (RelativeLayout) rootView.findViewById(R.id.redLayout);

        cancelButton = (Button) rootView.findViewById(R.id.cancelButton);
        loginButton = (Button) rootView.findViewById(R.id.loginButton);
        username = (MaterialEditText) rootView.findViewById(R.id.username);
        password = (MaterialEditText) rootView.findViewById(R.id.password);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogIn login = new LogIn(username.getText().toString(), password.getText().toString(), getActivity(), logInCallback);
                login.execute();
                loginButton.setEnabled(false);
            }
        });

        rootView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop,
                                       int oldRight, int oldBottom) {
                v.removeOnLayoutChangeListener(this);
                final int cx = getArguments().getInt("cx");
                final int cy = getArguments().getInt("cy");

                final int radius = (int) Math.hypot(right, bottom);

                cx_ = cx;
                cy_ = cy;
                radius_ = radius;

                init_radius = (int) Math.hypot(loginButton.getHeight(), loginButton.getWidth());

                Animator reveal = ViewAnimationUtils.createCircularReveal(v, cx, cy, init_radius, radius);
                reveal.setInterpolator(new DecelerateInterpolator(2f));
                reveal.setDuration(revealTime);
                reveal.start();
            }
        });



        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                username.setHelperText("");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                password.setHelperText("");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onFragmentTouched(LoginFragment.this, Metrics.dpToPx(150), Metrics.dpToPx(150));
                }
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnFragmentTouched) {
            listener = (OnFragmentTouched) activity;
        }
        try{
            newLoginFragmentCallBack = (NewLoginFragmentCallback) activity;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Get the animator to unreveal the circle
     *
     * @param cx center x of the circle (or where the view was touched)
     * @param cy center y of the circle (or where the view was touched)
     * @return Animator object that will be used for the animation
     */
    public Animator prepareUnrevealAnimator(float cx, float cy) {
        int radius = getEnclosingCircleRadius(getView(), (int) cx, (int) cy);
        Animator anim = ViewAnimationUtils.createCircularReveal(getView(), (int) cx, (int) cy, radius, 0);
        anim.setInterpolator(new AccelerateInterpolator(2f));
        anim.setDuration(unrevealTime);
        return anim;
    }

    /**
     * To be really accurate we have to start the circle on the furthest corner of the view
     *
     * @param v  the view to unreveal
     * @param cx center x of the circle
     * @param cy center y of the circle
     * @return the maximum radius
     */
    private int getEnclosingCircleRadius(View v, int cx, int cy) {
        int realCenterX = cx + v.getLeft();
        int realCenterY = cy + v.getTop();
        int distanceTopLeft = (int) Math.hypot(realCenterX - v.getLeft(), realCenterY - v.getTop());
        int distanceTopRight = (int) Math.hypot(v.getRight() - realCenterX, realCenterY - v.getTop());
        int distanceBottomLeft = (int) Math.hypot(realCenterX - v.getLeft(), v.getBottom() - realCenterY);
        int distanceBottomRight = (int) Math.hypot(v.getRight() - realCenterX, v.getBottom() - realCenterY);

        Integer[] distances = new Integer[]{distanceTopLeft, distanceTopRight, distanceBottomLeft,
                distanceBottomRight};

        return Collections.max(Arrays.asList(distances));
    }

    public static LoginFragment newInstance(int centerX, int centerY, int color) {
        Bundle args = new Bundle();
        args.putInt("cx", centerX);
        args.putInt("cy", centerY);
        args.putInt("color", color);
        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void RevealCircle(final RelativeLayout relativeLayout, final int cx, final int cy, final int radius){
        Animator reveal = ViewAnimationUtils.createCircularReveal(relativeLayout, cx_, cy_, init_radius, radius_);
        reveal.setInterpolator(new AccelerateInterpolator(2f));
        reveal.setDuration(revealTime);
        //reveal.setStartDelay(revealTime);
        reveal.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                relativeLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                newLoginFragmentCallBack.onSuccess();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        reveal.start();
    }

    @Override
    public void onSuccess() {
        loginButton.setEnabled(true);
        RevealCircle(redLayout, cx_, cx_, radius_);
    }

    @Override
    public void onWrongUsername() {
        loginButton.setEnabled(true);
        username.setHelperTextAlwaysShown(true);
        username.setHelperText("Wrong username");
    }

    @Override
    public void onWrongPassword() {
        loginButton.setEnabled(true);
        password.setHelperText("Wrong password");
    }

    @Override
    public void onUnknownError() {
        loginButton.setEnabled(true);
    }
}
