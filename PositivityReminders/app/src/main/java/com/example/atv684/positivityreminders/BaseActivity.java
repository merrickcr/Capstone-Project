package com.example.atv684.positivityreminders;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.atv684.positivityreminders.Menu.MenuListAdapter;
import com.example.atv684.positivityreminders.Menu.SideNavMenuItem;
import com.example.atv684.positivityreminders.Schedules.AddScheduleActivity;
import com.example.atv684.positivityreminders.Schedules.ViewScheduleActivity;

import java.util.ArrayList;

public class BaseActivity extends AppCompatActivity {

    ArrayList<Object> menuObjects = new ArrayList<>();

    ListView drawerList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_base);

        drawerList = (ListView)findViewById(R.id.left_drawer);

        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                handleMenuClick(position);
            }
        });

        setupMenuOptions();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));

    }

    private void handleMenuClick(int position) {
        //TODO

        Intent intent = null;
        if(position == 0){
           intent = new Intent(this, ViewScheduleActivity.class);

        }
        else if(position == 1){
            intent = new Intent(this, MyQuotesActivity.class);
        }

        if(intent == null) return;

        startActivity(intent);
    }


    private void setupMenuOptions() {

        menuObjects.add(new SideNavMenuItem(R.drawable.ic_event_white_24dp, "Schedules"));
        menuObjects.add(new SideNavMenuItem(R.drawable.ic_star_white_24dp, "Favorites"));
        menuObjects.add(new SideNavMenuItem(R.drawable.ic_chat_white_24dp, "My Quotes"));

        MenuListAdapter adapter = new MenuListAdapter(menuObjects, this);

        drawerList.setAdapter(adapter);

    }
}
