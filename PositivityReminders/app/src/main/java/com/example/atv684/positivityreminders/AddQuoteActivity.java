package com.example.atv684.positivityreminders;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by atv684 on 9/21/16.
 */
public class AddQuoteActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        AddQuoteFragment fragment = new AddQuoteFragment();

        transaction.add(R.id.content, fragment);
        transaction.commit();
    }
}
