package com.jookovjook.chatapp.about_fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jookovjook.chatapp.R;

import java.util.ArrayList;
import java.util.List;

public class AboutFragment extends Fragment {

    private AboutAdapter aboutAdapter;
    private List<AboutProvider> aboutProviderList = new ArrayList<>();
    private int started = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.about_fragment, container, false);
        bindFragment(rootView);
        if(started == 0) {
            AboutProvider aboutProvider = new AboutProvider(1, "jookovjook", "Jook", "Jookov");
            aboutProviderList.add(aboutProvider);
            aboutAdapter.notifyDataSetChanged();
            started ++;
        }
        return rootView;
    }

    private void bindFragment(View view){
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        aboutAdapter = new AboutAdapter(aboutProviderList, getActivity());
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);
        assert recyclerView != null;
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(aboutAdapter);
    }

    public static Fragment newInstance(String username) {
        AboutFragment aboutFragment = new AboutFragment();
        Bundle bundle = new Bundle();
        bundle.putString("username", username);
        aboutFragment.setArguments(bundle);
        return aboutFragment;
    }
}
