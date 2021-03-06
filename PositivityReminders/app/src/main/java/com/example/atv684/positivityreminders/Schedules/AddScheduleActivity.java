package com.example.atv684.positivityreminders.schedules;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.view.View;

import com.example.atv684.positivityreminders.BaseActivity;
import com.example.atv684.positivityreminders.R;

/**
 * Created Chris on 10/1/16.
 */
public class AddScheduleActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        AddScheduleFragment fragment = new AddScheduleFragment();

        transaction.add(R.id.content, fragment);
        transaction.commit();
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }
}
