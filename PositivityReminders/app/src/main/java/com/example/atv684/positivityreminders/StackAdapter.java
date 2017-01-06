package com.example.atv684.positivityreminders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class StackAdapter extends RecyclerView.Adapter<StackAdapter.ViewHolder> {

    ArrayList<QuoteObject> arrayList;
    LayoutInflater inflater;

    Context context;

    public StackAdapter(Context context, ArrayList arrayList) {
        this.arrayList = arrayList;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.stack_view_item_layout, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        QuoteObject object = arrayList.get(position);

        holder.author.setText("-"+object.getAuthor());
        holder.text.setText(object.getText());

        Picasso.with(context).load("https://source.unsplash.com/category/nature/800x600").into(holder.image);
    }


    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void setItems(ArrayList items) {
        this.arrayList = items;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView text;
        TextView author;

        public ViewHolder(View view) {
            super(view);

            image = (ImageView) view.findViewById(R.id.image);
            text = (TextView) view.findViewById(R.id.text);
            author = (TextView) view.findViewById(R.id.author);
        }
    }

}