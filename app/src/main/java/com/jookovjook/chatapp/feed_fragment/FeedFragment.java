package com.jookovjook.chatapp.feed_fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jookovjook.chatapp.R;
import com.jookovjook.chatapp.interfaces.GetPubsInterfase;

public class FeedFragment extends Fragment implements GetPubsInterfase{

    private FeedCardAdapter feedCardAdapter;
    private int type;
    private int param;
    Boolean loading = true;
    Boolean loaded_all = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.feed_fragment, container, false);
        type = getArguments().getInt("type");
        param = getArguments().getInt("param");
        bindFragment(rootView);
        return rootView;
    }

    private void bindFragment(View view){
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        feedCardAdapter = new FeedCardAdapter(type, param, getActivity(), this);
        loading = true;
        final LinearLayoutManager mLayoutManager  = new LinearLayoutManager(getActivity());
        assert recyclerView != null;
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(feedCardAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int visibleItemCount;
            int totalItemCount;
            int firstVisibleItemIndex;
            int previousTotal;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = recyclerView.getChildCount();
                totalItemCount = mLayoutManager.getItemCount();
                firstVisibleItemIndex = mLayoutManager.findFirstVisibleItemPosition();

//                if (loading) {
//                    if (totalItemCount > previousTotal) {
//                        loading = false;
//                        previousTotal = totalItemCount;
//                    }
//                }
                if(!loaded_all)
                if(!loading)
                    if ((totalItemCount - visibleItemCount) <= firstVisibleItemIndex) {
                        Log.i("RV", "end of list reached");
                        feedCardAdapter.loadNextTen();
                        loading = true;
                        //you should start loading bottom elements
                        Log.i("RV", "start loading");

                    }
//                    else if (firstVisibleItemIndex == 0){
//                        Log.i("RV", "top of list reached");
//                        //loading = true;
//                        //you should start loading top elements
//                    }
            }
        });
    }

    public static Fragment newInstance(int type, int param) {
        FeedFragment feedFragment = new FeedFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        bundle.putInt("param", param);
        feedFragment.setArguments(bundle);
        return feedFragment;
    }

    @Override
    public void onGotAll(int last_id) {
        loading = false;
        Log.i("RV", "done loading. last_id = " + String.valueOf(last_id));
        if(last_id < 0) loaded_all = true;
    }
}