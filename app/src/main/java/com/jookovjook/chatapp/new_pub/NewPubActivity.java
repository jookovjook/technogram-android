package com.jookovjook.chatapp.new_pub;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;

import com.jookovjook.chatapp.R;
import com.jookovjook.chatapp.new_new_pub.NewPubFragment;

public class NewPubActivity extends AppCompatActivity implements BranchFragment.BranchSelector{

    NonSwipeableViewPager pager;
    PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_pub);

        pager = (NonSwipeableViewPager) findViewById(R.id.view_pager);
        pagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
    }

    @Override
    public void onBackPressed() {
        if( pager.getCurrentItem() == 0) {
            super.onBackPressed();
        }else{
            pager.setCurrentItem(pager.getCurrentItem() - 1);
        }
    }

    @Override
    public void onSoftwareSelected() {
        pager.setCurrentItem(1);
    }

    private class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        private Branch branch;

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void setBranch(Branch branch){
            this.branch = branch;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0: return BranchFragment.newInstance();
                case 1: return NewPubFragment.newInstance();
            }

            return BranchFragment.newInstance();
        }

        @Override
        public int getCount() {
            return 2;
        }

    }

}
