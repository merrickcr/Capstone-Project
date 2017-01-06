package com.example.atv684.positivityreminders;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by atv684 on 9/21/16.
 */
public class MainActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        MainFragment mainFragment = new MainFragment();

        mainFragment.setArguments(this.getIntent().getExtras());


        transaction.add(R.id.content, mainFragment);
        transaction.commit();
    }
}
