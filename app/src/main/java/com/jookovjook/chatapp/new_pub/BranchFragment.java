package com.jookovjook.chatapp.new_pub;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jookovjook.chatapp.R;

public class BranchFragment extends Fragment {

    public interface BranchSelector{
        void onSoftwareSelected();
    }

    private BranchSelector branchSelector;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.branchSelector = (BranchSelector) activity;
        }catch (Exception e){}
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_branch, container, false);
        Button button = (Button) rootView.findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                branchSelector.onSoftwareSelected();
            }
        });
        return rootView;

    }

    public static Fragment newInstance() {
        return new BranchFragment();
    }
}
