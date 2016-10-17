package com.jookovjook.chatapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.jookovjook.chatapp.new_publication.NewPublicationActivity;

public class NewPublicationFragment extends Fragment {

    public static final int REQUEST_CODE_GALLERY = 1;
    Button button;
    ImageView imageView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.new_publication_fragment, container, false);
        imageView = (ImageView) rootView.findViewById(R.id.imageView);
        button = (Button) rootView.findViewById(R.id.button);

        button.setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewPublicationActivity.class);
                startActivity(intent);
            }

        }
        );
        return rootView;
    }

    public static Fragment newInstance(){
        return new NewPublicationFragment();
    }
}
