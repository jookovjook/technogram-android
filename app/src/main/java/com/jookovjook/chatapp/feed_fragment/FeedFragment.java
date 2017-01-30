package com.jookovjook.chatapp.feed_fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jookovjook.chatapp.R;
import com.jookovjook.chatapp.interfaces.GetPubsInterfase;
import com.jookovjook.chatapp.network.Server;

import org.zakariya.stickyheaders.PagedLoadScrollListener;
import org.zakariya.stickyheaders.StickyHeaderLayoutManager;

public class FeedFragment extends Fragment implements GetPubsInterfase, Server.ServerCallback, FeedCardAdapter.FeedCardAdapterCallback {

    private int type;
    private int param;
    Boolean loading = true;
    Boolean loaded_all = false;
    private LinearLayout networkLayout;
    private ProgressBar loading_spinner;
    private Button retryButton;
    private TextView suka;
    private ImageView bigHeart;
    Server.ServerCallback callback = this;
    int tapCount = 0;
    private FeedCardAdapter feedCardAdapter;
    PagedLoadScrollListener.LoadCompleteNotifier loadCompleteNotifier;
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.feed_fragment, container, false);
        type = getArguments().getInt("type");
        param = getArguments().getInt("param");
        networkLayout = (LinearLayout) rootView.findViewById(R.id.networkLayout);
        loading_spinner = (ProgressBar) rootView.findViewById(R.id.loading_spinner);
        retryButton = (Button) rootView.findViewById(R.id.retryButton);
        suka = (TextView) rootView.findViewById(R.id.suka);
        new Server(callback).execute();
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tapCount ++;
                if(tapCount >= 4 & tapCount <= 7) suka.setVisibility(View.VISIBLE);
                networkLayout.setVisibility(View.INVISIBLE);
                loading_spinner.setVisibility(View.VISIBLE);
                new Server(callback).execute();
            }
        });
        bindFragment(rootView);
        return rootView;
    }

    private void bindFragment(View view){
        bigHeart = (ImageView) view.findViewById(R.id.bigHeart);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        //feedCardAdapter = new FeedCardAdapter(type, param, getActivity(), this);
        loading = true;
        //final LinearLayoutManager mLayoutManager  = new LinearLayoutManager(getActivity());
        assert recyclerView != null;
        feedCardAdapter = new FeedCardAdapter(type, param, getActivity(), this, this);
        //recyclerView.setLayoutManager(mLayoutManager);
        //recyclerView.setAdapter(feedCardAdapter);

        final StickyHeaderLayoutManager stickyHeaderLayoutManager = new StickyHeaderLayoutManager();
        recyclerView.setLayoutManager(stickyHeaderLayoutManager);

        stickyHeaderLayoutManager.setHeaderPositionChangedCallback(new StickyHeaderLayoutManager.HeaderPositionChangedCallback() {
            @Override
            public void onHeaderPositionChanged(int sectionIndex, View header, StickyHeaderLayoutManager.HeaderPosition oldPosition, StickyHeaderLayoutManager.HeaderPosition newPosition) {
                //Log.i(TAG, "onHeaderPositionChanged: section: " + sectionIndex + " -> old: " + oldPosition.name() + " new: " + newPosition.name());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    boolean elevated = newPosition == StickyHeaderLayoutManager.HeaderPosition.STICKY;
                    header.setElevation(elevated ? 8 : 0);
                }
            }
        });

        recyclerView.setAdapter(feedCardAdapter);

//        recyclerView.addOnScrollListener(new PagedLoadScrollListener(stickyHeaderLayoutManager) {
//            @Override
//            public void onLoadMore(int page, LoadCompleteNotifier loadComplete) {
//                FeedFragment.this.loadCompleteNotifier = loadCompleteNotifier;
//                feedCardAdapter.loadNextTen();
//            }
//        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int visibleItemCount;
            int totalItemCount;
            int firstVisibleItemIndex;
            int previousTotal;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = recyclerView.getChildCount();
                totalItemCount = stickyHeaderLayoutManager.getItemCount();
                firstVisibleItemIndex = stickyHeaderLayoutManager.getFirstVisibleItemViewHolder(true).getPosition();
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

    @Override
    public void onAvailable() {
        Log.i("feed","server is availible");
        feedCardAdapter.execute();
        loading_spinner.setVisibility(View.INVISIBLE);
        suka.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onNotAvailable() {
        Log.i("feed","server is not availible");
        networkLayout.setVisibility(View.VISIBLE);
        loading_spinner.setVisibility(View.INVISIBLE);
        if(tapCount <= 4 || tapCount >= 7) suka.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onDoubleLiked() {
        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.recycler_anim);
        Animation bigHeartAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.big_heart_anim);
        recyclerView.clearAnimation();
        recyclerView.setAnimation(animation);
        this.bigHeart.setVisibility(View.VISIBLE);
        bigHeartAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                bigHeart.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        bigHeart.clearAnimation();
        this.bigHeart.setAnimation(bigHeartAnim);
    }
}