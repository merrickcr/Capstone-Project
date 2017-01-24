package com.example.atv684.positivityreminders.Detail;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.atv684.positivityreminders.QuoteObject;
import com.example.atv684.positivityreminders.R;
import com.example.atv684.positivityreminders.provider.QuoteDBHelper;
import com.squareup.picasso.Picasso;

public class QuoteDetailFragment extends Fragment {


    private QuoteDBHelper dbHelper;

    private QuoteObject quote;

    private TextView text;

    private TextView author;

    private ImageView image;

    private LinearLayout listArea;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.quote_detail_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        text = (TextView)view.findViewById(R.id.text);
        author = (TextView) view.findViewById(R.id.author);
        image = (ImageView) view.findViewById(R.id.image);
        listArea = (LinearLayout)view.findViewById(R.id.list_area);


        int id = 0;
        if(getArguments() != null) {
            id = getArguments().getInt("id");
        }

        dbHelper = QuoteDBHelper.get(getContext());

        quote = dbHelper.fetchQuoteById(id);

        populateFields(quote);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void populateFields(QuoteObject quote){
        text.setText(quote.getText());
        author.setText(quote.getAuthor());
        Picasso.with(getActivity()).load("https://source.unsplash.com/category/nature/800x600").into(image);

        listArea.removeAllViews();
        listArea.addView(new DetailRow(getActivity(), "Number of Views:", String.valueOf(quote.getNumViews())));
        listArea.addView(new DetailRow(getActivity(), "Favorite:", quote.isFavorite() ? "yes" : "no"));
        listArea.addView(new DetailRow(getActivity(), "Custom:", quote.isCustom() ? "yes" : "no"));
    }

    public void setSelectedQuote(QuoteObject object) {
        this.quote = object;
        populateFields(quote);
    }
}
