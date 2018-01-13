package com.jookovjook.chatapp.about_fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jookovjook.chatapp.R;
import com.jookovjook.chatapp.feed_fragment.FeedFragment;
import com.jookovjook.chatapp.network.NewGetUserInfo;
import com.jookovjook.chatapp.settings.SettingsActivity;
import com.jookovjook.chatapp.utils.AuthHelper;
import com.jookovjook.chatapp.utils.Config;
import com.squareup.picasso.Picasso;

import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class AboutOwnFragment extends Fragment implements NewGetUserInfo.GetUserInfoCallback{

    TextView viewsStats, likesStats, x2LikesStats, subsStats;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_about_own, container, false);
        rootView.findViewById(R.id.settings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
            }
        });
        setupHeader(rootView);
        return rootView;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupTabs(view);
    }

    private void setupTabs(View rootView){
        final TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tabLayout);
        final ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.viewPager);

        //setupViewpager(tabLayout, viewPager);

        viewPager.setAdapter(new SectionPagerAdapter(getActivity().getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            int iconId = -1;
            switch (i) {
                case 0:
                    iconId = R.drawable.ic_info_outline_black;
                    break;
                case 1:
                    iconId = R.drawable.ic_pubs_black;
                    break;
                case 2:
                    iconId = R.drawable.heart_double;
                    break;
            }
            tabLayout.getTabAt(i).setIcon(iconId);
        }

        // Needed since support libraries version 23.0.0
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                tabLayout.getTabAt(position).select();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }

        });

        tabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);

//                tabLayout.setSelectedTabIndicatorColor(colorId); // For the line
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    for (int i = 0; i < tabLayout.getTabCount(); i++) {
//                        tabLayout.getTabAt(i).getIcon().setTint(getResources().getColor(R.color.gray));
//                    }
//                    tabLayout.getTabAt(selectedTabPosition).getIcon().setTint(colorId);
//                }
            }
        });


    }

    private void setupHeader(View rootView){
        ViewGroup inclusionGroup = (ViewGroup) rootView.findViewById(R.id.statsContainer);
        View statsView = LayoutInflater.from(getActivity()).inflate(R.layout.stats_layout, null);
        inclusionGroup.addView(statsView);
        viewsStats = (TextView) rootView.findViewById(R.id.viewsStats);
        likesStats = (TextView) rootView.findViewById(R.id.likesStats);
        x2LikesStats = (TextView) rootView.findViewById(R.id.x2LikesStats);
        subsStats = (TextView) rootView.findViewById(R.id.subsStats);
        NewGetUserInfo getUserInfo = new NewGetUserInfo(AuthHelper.getUserId(getActivity()), this);
        getUserInfo.execute();
        Picasso.with(getActivity())
                .load(Config.IMAGE_RESOURCES_URL + AuthHelper.getAvatar(getActivity()))
                .resize(256, 256).onlyScaleDown().centerCrop().into((CircleImageView) rootView.findViewById(R.id.avatar));
        ((TextView) rootView.findViewById(R.id.username)).setText("\u0040" + AuthHelper.getUsername(getActivity()));
    }

    @Override
    public void onGotUserInfo(NewGetUserInfo.UserInfo userInfo) {
        Random random = new Random();
        int min = 0;
        int max = 10;
        int rnd = random.nextInt(max - min + 1) + min;
//        viewsStats.setText(String.valueOf(userInfo.views));
//        likesStats.setText(String.valueOf(userInfo.likes));
//        x2LikesStats.setText(String.valueOf(userInfo.x2likes));
//        subsStats.setText(String.valueOf(userInfo.subs));
        viewsStats.setText(String.valueOf(rnd));
        rnd = random.nextInt(max - min + 1) + min;
        likesStats.setText(String.valueOf(rnd));
        rnd = random.nextInt(max - min + 1) + min;
        x2LikesStats.setText(String.valueOf(rnd));
        rnd = random.nextInt(max - min + 1) + min;
        subsStats.setText(String.valueOf(rnd));
    }

    @Override
    public void onErrorGettingUserInfo() {
    }

    public class SectionPagerAdapter extends FragmentPagerAdapter {

        public SectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return InfoFragment.newInstance(true, null);
                case 1:
                    return FeedFragment.newInstance(0, AuthHelper.getUserId(getActivity()));
                case 2:
                    return FeedFragment.newInstance(0, 0);
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "";
        }
    }

    public static Fragment newInstance(String username, int user_id, Boolean own) {
        AboutOwnFragment aboutOwnFragment = new AboutOwnFragment();
        Bundle bundle = new Bundle();
        bundle.putString("username", username);
        bundle.putInt("user_id", user_id);
        bundle.putBoolean("own", own);
        aboutOwnFragment.setArguments(bundle);
        return aboutOwnFragment;
    }
}
