package com.example.atv684.positivityreminders;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.StackView;

import com.example.atv684.positivityreminders.provider.QuoteDBHelper;

import java.util.ArrayList;
import java.util.zip.InflaterInputStream;

/**
 * Created by atv684 on 9/16/16.
 */
public class MainFragment extends Fragment implements QuoteDBHelper.DBHelperCallbackListener{

    private static final int ADD_QUOTE_REQUEST_CODE = 1;

    private RecyclerView stackView;

    FloatingActionButton fab;

    private StackAdapter adapter;

    static final String INTIAL_SETUP_PREFERENCE = "has_run_initial_setup";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }
    
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        stackView = (RecyclerView) view.findViewById(R.id.stack_view);


        adapter = new StackAdapter(getActivity(), new ArrayList<String>());
        stackView.setAdapter(adapter);
        stackView.setLayoutManager(new LinearLayoutManager(getActivity()));

        fab = (FloatingActionButton)view.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddQuoteActivity.class);
                startActivityForResult(intent, ADD_QUOTE_REQUEST_CODE);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        QuoteDBHelper dbHelper = new QuoteDBHelper(getContext(), this);
        //fetchQuotesFromDB();

        //fetch quotes on initial setup

        if(!PreferenceManager.getDefaultSharedPreferences(getContext()).contains(INTIAL_SETUP_PREFERENCE)){
            dbHelper.fetchQuotesFromOnline();
        }
    }

    private void fetchQuotesFromDB() {
        ArrayList list = new ArrayList();
        QuoteDBHelper dbHelper = new QuoteDBHelper(getContext(), this);

        //dbHelper.addQuote("Quote 1", "me", "");
        for(int i = 0; i < 10; i++) {
            QuoteObject object = dbHelper.readQuote();

            if (object != null) {
                list.add(object);
                object.incrementViews();
                dbHelper.updateQuote(object);
            }
        }

        adapter.setItems(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ADD_QUOTE_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            Snackbar.make(getView(), R.string.snackbar_quote_has_been_added, Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLoadOnlineQuotes() {
        fetchQuotesFromDB();
    }
}
