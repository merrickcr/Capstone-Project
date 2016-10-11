package com.example.atv684.positivityreminders;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.atv684.positivityreminders.provider.QuoteDBHelper;

public class AddQuoteFragment extends Fragment implements QuoteDBHelper.DBHelperCallbackListener {

    Button addQuoteButton;
    EditText text;
    EditText author;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_quote_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addQuoteButton = (Button) view.findViewById(R.id.add_quote_button);
        text = (EditText) view.findViewById(R.id.text);
        author = (EditText) view.findViewById(R.id.author);


        addQuoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addQuote();
                getActivity().setResult(Activity.RESULT_OK);
                getActivity().finish();
            }
        });
    }

    private void addQuote() {

        QuoteDBHelper helper = new QuoteDBHelper(getContext(), this);

        helper.addQuote(text.getText().toString(), author.getText().toString(), null);


    }

    @Override
    public void onLoadOnlineQuotes() {
        //not needed
    }
}
