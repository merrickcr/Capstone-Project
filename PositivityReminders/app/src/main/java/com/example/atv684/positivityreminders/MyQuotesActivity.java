package com.example.atv684.positivityreminders;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class MyQuotesActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        MyQuotesFragment fragment = new MyQuotesFragment();

        transaction.add(R.id.content, fragment);
        transaction.commit();
    }

}
