package com.example.atv684.positivityreminders;

import com.google.firebase.analytics.FirebaseAnalytics;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.atv684.positivityreminders.provider.QuoteDBHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class MainFragment extends BaseFragment implements QuoteDBHelper.DBHelperCallbackListener {

    static final String INTIAL_SETUP_PREFERENCE = "has_run_initial_setup";

    private static final int ADD_QUOTE_REQUEST_CODE = 1;

//    FloatingActionButton fab;

    QuoteDBHelper dbHelper;

    private RecyclerView recyclerView;

    private QuoteAdapter adapter;

    private ArrayList<QuoteObject> quotes = new ArrayList<QuoteObject>();

    private RelativeLayout loadingLayout;

    private TextView loadingText;

    private boolean shouldRefreshView = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(savedInstanceState == null) {

            dbHelper = QuoteDBHelper.get(getContext(), this);

            Bundle extras = getActivity().getIntent().getExtras();
            if (extras != null &&
                (extras.containsKey(Constants.VIEW_FAVORITES_BUNDLE) || extras.containsKey(Constants.VIEW_CUSTOM_BUNDLE))) {
                adapter = new QuoteAdapter(getContext(), quotes, false);
            } else {
                adapter = new QuoteAdapter(getContext(), quotes, true);
            }

        }

        if(dbHelper != null){
            dbHelper.setContext(getContext());
        }

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        loadingLayout = (RelativeLayout) view.findViewById(R.id.loading_layout);
        loadingText = (TextView) view.findViewById(R.id.loading_text);


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

        //fetch quotes on initial setup
        if (!PreferenceManager.getDefaultSharedPreferences(getContext()).contains(INTIAL_SETUP_PREFERENCE)) {
            loadingText.setText(getString(R.string.loading_first_time_data));
        } else if(adapter.getItemCount() <= 0){
            loadingText.setText(R.string.loading_data);
        }

        dbHelper.fetchQuotesFromOnline();
        dbHelper.fetchImagesFromOnline();

        String trackingTag = Constants.HOME_TAG;

        if (adapter.getItemCount() <= 0) {

            Bundle extras = getActivity().getIntent().getExtras();

            if (extras != null && extras.containsKey(Constants.VIEW_FAVORITES_BUNDLE)) {
                fetchFavoriteQuotes();
                trackingTag = Constants.FAVORITE_TAG;
            } else if (extras != null && extras.containsKey(Constants.VIEW_CUSTOM_BUNDLE)) {
                fetchCustomQuotes();
                trackingTag = Constants.CUSTOM_TAG;
            } else {
                if (!PreferenceManager.getDefaultSharedPreferences(getContext()).contains(INTIAL_SETUP_PREFERENCE)) {
                    shouldRefreshView = true;
                }

                //dbHelper.fetchQuotesFromOnline();
                //dbHelper.fetchImagesFromOnline();

                dbHelper.fetchQuotesFromDB();
            }
        }
        else{
            if(adapter.getItemCount() > 0){
                loadingLayout.setVisibility(View.GONE);
            }
        }

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, Constants.PAGE_VIEWED_ITEM_NAME);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, trackingTag);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "string");
        ((BaseActivity) getActivity()).getAnalytics().logEvent(Constants.PAGE_VIEWED_ITEM_NAME, bundle);



    }

    private void fetchCustomQuotes() {
        setCursorToQuoteList(dbHelper.fetchCustomQuotes());
    }

    private void fetchFavoriteQuotes() {
        setCursorToQuoteList(dbHelper.fetchFavoriteQuotes());
    }


    private void setCursorToQuoteList(Cursor c) {
        ArrayList list = new ArrayList();

        while(c.moveToNext()){
            list.add(new QuoteObject(c));
        }

        c.close();

        //randomize
        Collections.shuffle(list, new Random(System.currentTimeMillis()));

        if (quotes.isEmpty()) {
            loadingText.setText(R.string.nothing_here);
            loadingLayout.findViewById(R.id.progress_bar).setVisibility(View.GONE);
        } else {
            loadingLayout.setVisibility(View.GONE);
        }

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

        PreferenceManager.getDefaultSharedPreferences(getContext()).edit().putBoolean(INTIAL_SETUP_PREFERENCE, true).commit();

        if (adapter.getItemCount() <= 0) {
            dbHelper.fetchQuotesFromDB();
        } else {
            loadingLayout.setVisibility(View.GONE);
        }

        shouldRefreshView = false;


    }

    @Override
    public void onDataFinished(final ArrayList<QuoteObject> quotes) {

        if (quotes.size() < 20) {
            dbHelper.fetchQuotesFromOnline();
        } else {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Collections.shuffle(quotes, new Random(SystemClock.currentThreadTimeMillis()));

                    MainFragment.this.quotes.addAll(quotes);
                    adapter.setItems(MainFragment.this.quotes);
                    adapter.notifyDataSetChanged();

                    loadingLayout.setVisibility(View.GONE);


                }
            });
        }


    }
}
