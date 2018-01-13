package com.jookovjook.chatapp.about_fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jookovjook.chatapp.R;
import com.jookovjook.chatapp.network.NewGetUserInfo;

import java.util.Random;

public class AboutFragment extends Fragment{

    TextView viewsStats, likesStats, x2LikesStats, subsStats;

    View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_about, container, false);

        ViewGroup inclusionGroup = (ViewGroup) rootView.findViewById(R.id.statsContainer);
        View statsView = LayoutInflater.from(getActivity()).inflate(R.layout.stats_layout, null);
        inclusionGroup.addView(statsView);
        viewsStats = (TextView) rootView.findViewById(R.id.viewsStats);
        likesStats = (TextView) rootView.findViewById(R.id.likesStats);
        x2LikesStats = (TextView) rootView.findViewById(R.id.x2LikesStats);
        subsStats = (TextView) rootView.findViewById(R.id.subsStats);
        this.rootView = rootView;
        return rootView;
    }

    public void updateInfo(NewGetUserInfo.UserInfo userInfo){
        Random random = new Random();
        int min = 0;
        int max = 10;
        int rnd = random.nextInt(max - min + 1) + min;
//        viewsStats.setText(String.valueOf(userInfo.views));
//        likesStats.setText(String.valueOf(userInfo.likes));
//        x2LikesStats.setText(String.valueOf(userInfo.x2likes));
//        subsStats.setText(String.valueOf(userInfo.subs));

        viewsStats.setText(String.valueOf(rnd));
        rnd = random.nextInt(max - min + 1) + min;
        likesStats.setText(String.valueOf(rnd));
        rnd = random.nextInt(max - min + 1) + min;
        x2LikesStats.setText(String.valueOf(rnd));
        rnd = random.nextInt(max - min + 1) + min;
        subsStats.setText(String.valueOf(rnd));

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        InfoFragment infoFragment = InfoFragment.newInstance(false, userInfo);
        transaction.replace(R.id.fragmentContainer, infoFragment);
        //transaction.addToBackStack(null);
        transaction.commit();
    }

    public static AboutFragment newInstance() {
//        AboutFragment aboutFragment = new AboutFragment();
//        Bundle args = new Bundle();
//        if(userInfo!= null) {
//            args.putInt("views", userInfo.views);
//            args.putInt("likes", userInfo.likes);
//            args.putInt("x2likes", userInfo.x2likes);
//            args.putInt("subs", userInfo.subs);
//        }
//        aboutFragment.setArguments(args);
        return new AboutFragment();
    }

}
