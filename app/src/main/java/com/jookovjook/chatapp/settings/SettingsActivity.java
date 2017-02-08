package com.jookovjook.chatapp.settings;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.jookovjook.chatapp.R;
import com.jookovjook.chatapp.utils.AuthHelper;
import com.jookovjook.chatapp.utils.Config;
import com.jookovjook.chatapp.utils.Metrics;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    CircleImageView avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle("Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        avatar = (CircleImageView) findViewById(R.id.avatar);

        Picasso.with(this)
                .load(Config.IMAGE_RESOURCES_URL + AuthHelper.getAvatar(this))
                .error(R.drawable.grid)
                .resize(Metrics.dpToPx(100), Metrics.dpToPx(100)).onlyScaleDown().centerCrop().into(avatar);

        RecyclerView settingsRecycler = (RecyclerView) findViewById(R.id.settingsRecycler);
        settingsRecycler.setLayoutManager(new GridLayoutManager(this, 1));
        settingsRecycler.setAdapter(new InfoAdapter(this));
        settingsRecycler.setNestedScrollingEnabled(false);
        settingsRecycler.addItemDecoration(new SimpleDividerItemDecoration(this));

        RecyclerView securityRecycler = (RecyclerView) findViewById(R.id.securityRecycler);
        securityRecycler.setLayoutManager(new GridLayoutManager(this, 1));
        securityRecycler.setAdapter(new SecurityAdapter(this));
        securityRecycler.setNestedScrollingEnabled(false);
        securityRecycler.addItemDecoration(new SimpleDividerItemDecoration(this));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }
}
