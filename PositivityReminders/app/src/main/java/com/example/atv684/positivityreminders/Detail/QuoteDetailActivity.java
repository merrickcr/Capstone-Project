package com.example.atv684.positivityreminders.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.example.atv684.positivityreminders.BaseActivity;
import com.example.atv684.positivityreminders.R;

/**
 * Created Chris on 1/9/17.
 */

public class QuoteDetailActivity extends BaseActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        QuoteDetailFragment quoteDetailFragment = new QuoteDetailFragment();

        quoteDetailFragment.setArguments(this.getIntent().getExtras());

        transaction.add(R.id.content, quoteDetailFragment);
        transaction.commit();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

}
