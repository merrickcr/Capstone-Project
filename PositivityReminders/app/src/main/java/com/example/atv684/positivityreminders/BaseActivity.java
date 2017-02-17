package com.example.atv684.positivityreminders;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.analytics.FirebaseAnalytics;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.atv684.positivityreminders.menu.MenuListAdapter;
import com.example.atv684.positivityreminders.menu.SideNavMenuItem;
import com.example.atv684.positivityreminders.schedules.ViewScheduleActivity;

import java.util.ArrayList;

public class BaseActivity extends AppCompatActivity {

    ArrayList<Object> menuObjects = new ArrayList<>();

    ListView drawerList;

    DrawerLayout drawerLayout;

    AdView mAdView;

    private FirebaseAnalytics mFirebaseAnalytics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_base);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        drawerList = (ListView) findViewById(R.id.left_drawer);

        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                handleMenuClick(position);
            }
        });

        String android_id = "FE4FE93518F6AB21F1CDEAA5AE531872";
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
            .addTestDevice(android_id)
            .build();
        mAdView.loadAd(adRequest);

        setupMenuOptions();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START, true);
            }
        });

        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));

    }

    private void handleMenuClick(int position) {

        Intent intent = null;
        if (position == Constants.VIEW_SCHEDULE_LIST_INDEX) {
            intent = new Intent(this, ViewScheduleActivity.class);

        } else if (position == Constants.VIEW_FAVORITES_LIST_INDEX) {
            intent = new Intent(this, MainActivity.class);
            intent.putExtra(Constants.VIEW_FAVORITES_BUNDLE, true);
        } else if (position == Constants.VIEW_CUSTOM_LIST_INDEX) {
            intent = new Intent(this, MainActivity.class);
            intent.putExtra(Constants.VIEW_CUSTOM_BUNDLE, true);
        } else if (position == Constants.HOME_LIST_INDEX) {
            intent = new Intent(this, MainActivity.class);
        } else if (position == Constants.ADD_QUOTE_INDEX) {
            intent = new Intent(this, AddQuoteActivity.class);
        }

        if (intent == null) {
            return;
        }

        drawerLayout.closeDrawer(Gravity.LEFT);
        startActivity(intent);
    }


    private void setupMenuOptions() {

        menuObjects.add(new SideNavMenuItem(R.drawable.ic_event_white_24dp, getString(R.string.schedules_li)));
        menuObjects.add(new SideNavMenuItem(R.drawable.ic_star_white_24dp, getString(R.string.favorites_li)));
        menuObjects.add(new SideNavMenuItem(R.drawable.ic_chat_white_24dp, getString(R.string.my_quotes_li)));
        menuObjects.add(new SideNavMenuItem(R.drawable.ic_grade_white_24dp, getString(R.string.home_li)));
        menuObjects.add(new SideNavMenuItem(R.drawable.ic_add_white_24dp, getString(R.string.add_quote_li)));

        MenuListAdapter adapter = new MenuListAdapter(menuObjects, this);

        drawerList.setAdapter(adapter);

    }

    public FirebaseAnalytics getAnalytics() {
        return mFirebaseAnalytics;
    }

    public boolean hasDualContent() {
        RelativeLayout dual_content = (RelativeLayout) findViewById(R.id.dual_content);

        return dual_content != null;
    }

}
