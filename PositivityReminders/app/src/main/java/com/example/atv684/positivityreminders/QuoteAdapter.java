package com.example.atv684.positivityreminders;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.atv684.positivityreminders.Detail.QuoteDetailActivity;
import com.example.atv684.positivityreminders.provider.QuoteDBHelper;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

public class QuoteAdapter extends RecyclerView.Adapter<QuoteAdapter.ViewHolder> {

    ArrayList<QuoteObject> arrayList;

    BaseActivity context;

    Bitmap image;

    public QuoteAdapter(Context context, ArrayList<QuoteObject> arrayList) {
        this.arrayList = arrayList;

        if (context instanceof BaseActivity) {
            this.context = (BaseActivity) context;
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.stack_view_item_layout, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onViewAttachedToWindow(ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        Picasso.with(context).load("https://source.unsplash.com/category/nature/800x600").into(holder.image);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final QuoteDBHelper dbHelper = new QuoteDBHelper(context, null);

        final QuoteObject object = arrayList.get(position);

        holder.author.setText("-" + object.getAuthor());
        holder.text.setText(object.getText());

        setupFabColor(arrayList.get(position), holder.favoriteFab);

        dbHelper.updateQuote(object);

        holder.favoriteFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                object.setFavorite(!object.isFavorite());

                FloatingActionButton fab = (FloatingActionButton) v;

                setupFabColor(object, fab);

                dbHelper.updateQuote(object);
            }
        });

        holder.deleteFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dbHelper.deleteQuote(object);

                arrayList.remove(object);

                notifyDataSetChanged();
            }
        });

        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, QuoteDetailActivity.class);
                intent.putExtra("id", object.getId());

                if (context.hasDualContent()) {
                    if (context instanceof MainActivity) {
                        ((MainActivity) context).getDetailFragment().setSelectedQuote(object);
                    }
                } else {
                    context.startActivity(intent);
                }

            }
        });
    }

    public void setupFabColor(QuoteObject object, FloatingActionButton fab) {

        if (object.isFavorite()) {
            fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorAccent)));
        } else {
            fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorPrimary)));
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size() - 1;
    }

    public void setItems(ArrayList items) {
        this.arrayList = items;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        FloatingActionButton favoriteFab;

        FloatingActionButton deleteFab;

        ImageView image;

        TextView text;

        TextView author;

        View root;

        public ViewHolder(View view) {
            super(view);

            root = view;

            image = (ImageView) view.findViewById(R.id.image);
            text = (TextView) view.findViewById(R.id.text);
            author = (TextView) view.findViewById(R.id.author);

            deleteFab = (FloatingActionButton) view.findViewById(R.id.delete_fab);
            favoriteFab = (FloatingActionButton) view.findViewById(R.id.favorite_fab);
        }
    }

}