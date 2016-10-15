package com.jookovjook.chatapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

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
        /*
        button.setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new GalleryHelper().setMultiselection(true)
                        .setMaxSelectedItems(10)
                        .setShowVideos(false)
                        .getCallingIntent(getActivity()), REQUEST_CODE_GALLERY);
            }

        }
        );
        */
        return rootView;
    }

    /*
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK) {
            List<GalleryMedia> galleryMedias =
                    data.getParcelableArrayListExtra(GalleryActivity.RESULT_GALLERY_MEDIA_LIST);
            Log.i("photo 0: ", galleryMedias.get(0).mediaUri());
            Glide.with(getActivity())
                    .load(galleryMedias.get(0).mediaUri())
                    .fitCenter()
                    .into(imageView);
        }

    }
    */
    public static Fragment newInstance(){
        return new NewPublicationFragment();
    }
}
