package com.example.atv684.positivityreminders;

import com.google.firebase.analytics.FirebaseAnalytics;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.atv684.positivityreminders.provider.QuoteDBHelper;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class MainFragment extends BaseFragment implements QuoteDBHelper.DBHelperCallbackListener {

    private static final int ADD_QUOTE_REQUEST_CODE = 1;

    private RecyclerView recyclerView;

//    FloatingActionButton fab;

    private QuoteAdapter adapter;

    private ArrayList<QuoteObject> quotes = new ArrayList<QuoteObject>();

    static final String INTIAL_SETUP_PREFERENCE = "has_run_initial_setup";

    QuoteDBHelper dbHelper = QuoteDBHelper.get(this);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = (RecyclerView) view.findViewById(R.id.stack_view);

        adapter = new QuoteAdapter(getActivity(), quotes);
        this.quotes.add(new QuoteObject("Blah"));

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

//        fab = (FloatingActionButton) view.findViewById(R.id.fab);
//
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), AddQuoteActivity.class);
//                startActivityForResult(intent, ADD_QUOTE_REQUEST_CODE);
//            }
//        });
    }

    @Override
    public void onResume() {
        super.onResume();

        String trackingTag = Constants.HOME_TAG;

        if (adapter.getItemCount() <= 0) {

            Bundle extras = getActivity().getIntent().getExtras();

            if (extras != null) {
                if (extras.containsKey(Constants.VIEW_FAVORITES_BUNDLE)) {
                    fetchFavoriteQuotes();
                    trackingTag = Constants.FAVORITE_TAG;
                } else if (extras.containsKey(Constants.VIEW_CUSTOM_BUNDLE)) {
                    fetchCustomQuotes();
                    trackingTag = Constants.CUSTOM_TAG;
                }
            } else {
                dbHelper.fetchQuotesFromDB();
            }

        }

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID,Constants.PAGE_VIEWED_ITEM_NAME);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, trackingTag);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "string");
        ((BaseActivity)getActivity()).getAnalytics().logEvent(Constants.PAGE_VIEWED_ITEM_NAME, bundle);

        //dbHelper.fetchQuotesFromOnline();

        //fetch quotes on initial setup
        if (!PreferenceManager.getDefaultSharedPreferences(getContext()).contains(INTIAL_SETUP_PREFERENCE)) {
//            dbHelper.fetchQuotesFromOnline();
//            dbHelper.fetchImagesFromOnline();
        }

    }

    private void fetchCustomQuotes() {
        setCursorToQuoteList(dbHelper.fetchCustomQuotes());
    }

    private void fetchFavoriteQuotes() {
        setCursorToQuoteList(dbHelper.fetchFavoriteQuotes());
    }


    private void setCursorToQuoteList(Cursor c) {
        ArrayList list = new ArrayList();

        while (c.moveToNext()) {
            list.add(new QuoteObject(c));
        }

        //randomize
        Collections.shuffle(list, new Random(System.currentTimeMillis()));

        this.quotes.addAll(quotes);
        adapter.setItems(list);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_QUOTE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Snackbar.make(getView(), R.string.snackbar_quote_has_been_added, Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLoadOnlineQuotes() {

        if (adapter.getItemCount() <= 0) {
            //dbHelper.fetchQuotesFromDB();
        }
    }

    @Override
    public void onDataFinished(ArrayList<QuoteObject> quotes) {

        Collections.shuffle(quotes, new Random(SystemClock.currentThreadTimeMillis()));

        this.quotes.addAll(quotes);
        adapter.setItems(this.quotes);
        adapter.notifyDataSetChanged();
    }
}
