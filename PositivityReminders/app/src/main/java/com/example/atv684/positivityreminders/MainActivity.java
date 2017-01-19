package com.example.atv684.positivityreminders;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.RelativeLayout;

import com.example.atv684.positivityreminders.Detail.QuoteDetailFragment;

/**
 * Created by atv684 on 9/21/16.
 */
public class MainActivity extends BaseActivity {

    MainFragment mainFragment;

    QuoteDetailFragment detailFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        mainFragment = new MainFragment();

        mainFragment.setArguments(this.getIntent().getExtras());

        transaction.add(R.id.content, mainFragment);

        if(hasDualContent()){

            detailFragment = new QuoteDetailFragment();
            detailFragment.setArguments(this.getIntent().getExtras());

            transaction.add(R.id.dual_content, detailFragment);

        }

        transaction.commit();
    }

    public MainFragment getMainFragment() {
        return mainFragment;
    }

    public QuoteDetailFragment getDetailFragment() {
        return detailFragment;
    }
}
