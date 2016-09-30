package com.example.atv684.positivityreminders;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.StackView;

import com.example.atv684.positivityreminders.provider.QuoteDBHelper;

import java.util.ArrayList;

/**
 * Created by atv684 on 9/16/16.
 */
public class MainFragment extends Fragment {

    private static final int ADD_QUOTE_REQUEST_CODE = 1;

    private StackView stackView;

    FloatingActionButton fab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }
    
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        stackView = (StackView) view.findViewById(R.id.stack_view);
        ArrayList list = new ArrayList();


        QuoteDBHelper dbHelper = new QuoteDBHelper(getContext());


        //dbHelper.addQuote("Quote 1", "me", "");
        QuoteObject object = dbHelper.readQuote();

        list.add(object);

        list.add(dbHelper.readQuote());

        list.add(dbHelper.readQuote());

        StackAdapter adapter = new StackAdapter(getActivity(), list);
        stackView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        object.incrementViews();

        dbHelper.updateQuote(object);

        object = dbHelper.readQuote();

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ADD_QUOTE_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            Snackbar.make(getView(), R.string.snackbar_quote_has_been_added, Snackbar.LENGTH_LONG).show();
        }
    }
}
