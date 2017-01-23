package com.jookovjook.chatapp.new_new_pub;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jookovjook.chatapp.R;
import com.jookovjook.chatapp.new_pub.SoftwareFragment;

public class NewPubFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_pub, container, false);
        return rootView;
    }

    public static Fragment newInstance() {
        return new NewPubFragment();
    }

}
