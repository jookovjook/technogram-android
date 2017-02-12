package com.jookovjook.chatapp.about_fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jookovjook.chatapp.MainActivity;
import com.jookovjook.chatapp.R;
import com.jookovjook.chatapp.network.NewGetUserInfo;
import com.jookovjook.chatapp.user_profile.UserProfileActivity;
import com.jookovjook.chatapp.utils.AuthHelper;
import com.luseen.autolinklibrary.AutoLinkMode;
import com.luseen.autolinklibrary.AutoLinkOnClickListener;
import com.luseen.autolinklibrary.AutoLinkTextView;

public class InfoFragment extends Fragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_info, container, false);

        AutoLinkTextView about = (AutoLinkTextView) rootView.findViewById(R.id.about);
        about.setAutoLinkOnClickListener(new AutoLinkOnClickListener() {
            @Override
            public void onAutoLinkTextClick(AutoLinkMode autoLinkMode, String matchedText) {
                switch (autoLinkMode){
                    default:
                        try {
                            ((MainActivity) getActivity()).openLink(matchedText);
                        }catch (Exception e){
                            e.printStackTrace();
                            try {
                                ((UserProfileActivity) getActivity()).openLink(matchedText);
                            }catch (Exception e2){
                                e2.printStackTrace();
                            }
                        }
                        break;
                }
            }
        });

        about.addAutoLinkMode(AutoLinkMode.MODE_URL);
        about.setUrlModeColor(ContextCompat.getColor(getActivity(), R.color.colorBlue));

        if(getArguments().getBoolean("own")) {
            about.setAutoLinkText(AuthHelper.getABOUT(getActivity()));
            ((TextView) rootView.findViewById(R.id.nameSurname))
                    .setText(AuthHelper.getNAME(getActivity()) + " " + AuthHelper.getSURNAME(getActivity()));
        }else{
            about.setAutoLinkText(getArguments().getString("about"));
            ((TextView) rootView.findViewById(R.id.nameSurname))
                    .setText(getArguments().getString("name") + " " + getArguments().getString("surname"));
        }

        return rootView;
    }


    public static InfoFragment newInstance(Boolean own, NewGetUserInfo.UserInfo userInfo) {
        InfoFragment infoFragment = new InfoFragment();
        Bundle args = new Bundle();
        args.putBoolean("own", own);
        if(!own){
            args.putString("about", userInfo.about);
            args.putString("name", userInfo.name);
            args.putString("surname", userInfo.surname);
        }
        infoFragment.setArguments(args);
        return infoFragment;
    }
}
