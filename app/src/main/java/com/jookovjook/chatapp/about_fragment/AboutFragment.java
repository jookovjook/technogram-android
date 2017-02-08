package com.jookovjook.chatapp.about_fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jookovjook.chatapp.R;
import com.jookovjook.chatapp.settings.SettingsActivity;

public class AboutFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.about_fragment, container, false);
        rootView.findViewById(R.id.settings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
            }
        });
        return rootView;
    }

    public static Fragment newInstance(String username, int user_id, Boolean own) {
        AboutFragment aboutFragment = new AboutFragment();
        Bundle bundle = new Bundle();
        bundle.putString("username", username);
        bundle.putInt("user_id", user_id);
        bundle.putBoolean("own", own);
        aboutFragment.setArguments(bundle);
        return aboutFragment;
    }
}
