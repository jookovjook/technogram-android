package com.jookovjook.chatapp.feed_fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jookovjook.chatapp.R;

public class FeedFragment extends Fragment {

    private FeedCardAdapter feedCardAdapter;
    private int type;
    private String param;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.feed_fragment, container, false);

        type = getArguments().getInt("type");
        param = getArguments().getString("param");
        bindFragment(rootView);
        return rootView;
    }

    private void bindFragment(View view){
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        feedCardAdapter = new FeedCardAdapter(type, param, getActivity());
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);
        assert recyclerView != null;
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(feedCardAdapter);
    }

    public static Fragment newInstance(int type, String param) {
        FeedFragment feedFragment = new FeedFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        bundle.putString("param", param);
        feedFragment.setArguments(bundle);
        return feedFragment;
    }

}