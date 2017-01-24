package com.jookovjook.chatapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.jookovjook.chatapp.about_fragment.AboutFragment;
import com.jookovjook.chatapp.feed_fragment.FeedFragment;
import com.jookovjook.chatapp.new_login.NewLoginActivity;
import com.jookovjook.chatapp.new_new_pub.NewPubFragment;
import com.jookovjook.chatapp.user_profile.UserProfileActivity;
import com.jookovjook.chatapp.utils.AuthHelper;

public class MainActivity extends AppCompatActivity {

    private int REQUEST_EXIT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindActivity();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int ht = displaymetrics.heightPixels;
        int wt = displaymetrics.widthPixels;
        Log.i("height", String.valueOf(ht));
        Log.i("width", String.valueOf(wt));
    }

    void bindActivity(){
        TabLayout tabLayout = (TabLayout) findViewById(R.id.materialup_tabs);
        ViewPager viewPager  = (ViewPager) findViewById(R.id.materialup_viewpager);
        viewPager.setAdapter(new MainActivity.TabsAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
        Button newPub = (Button) findViewById(R.id.newPub);
        newPub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();

                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);

                NewPubFragment newCustomFragment = (NewPubFragment) NewPubFragment.newInstance();
                transaction.replace(R.id.fragment_container, newCustomFragment );
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.login_activity:
                Intent intent = new Intent(MainActivity.this, NewLoginActivity.class);
                startActivity(intent);
                return true;
            case R.id.user_profile_activity:
                int user_id = AuthHelper.getUserId(MainActivity.this);
                if(user_id >= 0){
                    Intent intent2 = new Intent(MainActivity.this, UserProfileActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("own", true);
                    bundle.putInt("user_id", user_id);
                    bundle.putString("username", AuthHelper.getUsername(MainActivity.this));
                    intent2.putExtras(bundle);
                    startActivity(intent2);
                }else{
                    Toast.makeText(getApplicationContext(), "You are not Logged in",
                            Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.publication_activity:
                Intent intent3 = new Intent(MainActivity.this, TestActivity.class);
                startActivity(intent3);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        if (requestCode == REQUEST_EXIT) {
//            if (resultCode == RESULT_OK) {
//                this.finish();
//
//            }
//        }
//    }

    class TabsAdapter extends FragmentPagerAdapter {

        public TabsAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Fragment getItem(int i) {
            switch(i) {
                //case 0: return SoftwareFragment.newInstance();
                case 0: return FeedFragment.newInstance(1, -1);
                case 1: return FeedFragment.newInstance(0, 0);
                case 2: return AboutFragment.newInstance("Tset", 1, false);
            }
            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch(position) {
                //case 0: return "New Pub";
                case 0: return "Recommended";
                case 1: return "Subscription";
                case 2: return "My Profile";
            }
            return "";
        }
    }
}
