package com.jookovjook.chatapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.jookovjook.chatapp.about_fragment.AboutFragment;
import com.jookovjook.chatapp.feed_fragment.FeedFragment;
import com.jookovjook.chatapp.new_publication.NewPublicationFragment;
import com.jookovjook.chatapp.publication.PublicationActivity;
import com.jookovjook.chatapp.user_profile.UserProfileActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindActivity();
    }

    void bindActivity(){
        TabLayout tabLayout = (TabLayout) findViewById(R.id.materialup_tabs);
        ViewPager viewPager  = (ViewPager) findViewById(R.id.materialup_viewpager);
        viewPager.setAdapter(new MainActivity.TabsAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
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
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                return true;
            case R.id.user_profile_activity:
                Intent intent2 = new Intent(MainActivity.this, UserProfileActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("user_id", 0);
                bundle.putString("uername", "jookovjook");
                bundle.putString("name_surname","Jook Jookov");
                intent2.putExtras(bundle);
                startActivity(intent2);
                return true;
            case R.id.publication_activity:
                Intent intent3 = new Intent(MainActivity.this, PublicationActivity.class);
                startActivity(intent3);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //tabs layout
    class TabsAdapter extends FragmentPagerAdapter {

        public TabsAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public Fragment getItem(int i) {
            switch(i) {
                case 0: return FeedFragment.newInstance(1," ");
                case 1: return FeedFragment.newInstance(0,"0");
                case 2: return NewPublicationFragment.newInstance();
                case 3: return AboutFragment.newInstance("Tset");
            }
            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch(position) {
                case 0: return "Interesting";
                case 1: return "Subscriptions";
                case 2: return "Recomendet";
                case 3: return "Settings";
            }
            return "";
        }
    }
}
