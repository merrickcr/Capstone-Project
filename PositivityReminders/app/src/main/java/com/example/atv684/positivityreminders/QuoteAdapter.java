package com.example.atv684.positivityreminders;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.atv684.positivityreminders.detail.QuoteDetailActivity;
import com.example.atv684.positivityreminders.provider.QuoteDBHelper;
import com.example.atv684.positivityreminders.provider.QuoteProvider;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class QuoteAdapter extends RecyclerView.Adapter<QuoteAdapter.ViewHolder> implements QuoteDBHelper.DBHelperCallbackListener {

    boolean loadOnScroll;

    ArrayList<QuoteObject> arrayList;

    BaseActivity context;

    Bitmap image;


    private QuoteDBHelper dbHelper;

    public QuoteAdapter(Context context, ArrayList<QuoteObject> arrayList, boolean loadOnScroll) {
        this.arrayList = arrayList;
        this.loadOnScroll = loadOnScroll;

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

    private void loadImageFromDatabase(final ViewHolder holder, final QuoteObject object) {

        GetImageFromDBAsyncTask task = new GetImageFromDBAsyncTask(context) {
            @Override
            protected void onPostExecute(HashMap<String, Bitmap> values) {
                super.onPostExecute(values);


                Iterator<Map.Entry<String, Bitmap>> it = values.entrySet().iterator();

                Map.Entry e = it.next();

                String name = (String)e.getKey();
                Bitmap bitmap = (Bitmap)e.getValue();


                holder.image.setImageBitmap(bitmap);
                object.setImage(bitmap);
                object.setImageURI(name);

                Cursor c = context.getContentResolver().query(Uri.parse(QuoteProvider.IMAGE_URI.toString() + "/" + object.getImageURI())
                    , null, null, null, null);

            }
        };

        holder.setTask(task);

        task.execute("test");
    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);

        if (holder.task != null) {
            holder.task.cancel(true);
        }

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final QuoteObject object = arrayList.get(position);

        holder.author.setText("-" + object.getAuthor());
        holder.text.setText(object.getText());

        setupFabColor(arrayList.get(position), holder.favoriteFab);

        dbHelper = QuoteDBHelper.get(context);
        dbHelper.setListener(this);

        object.incrementViews();
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
                intent.putExtra("bitmap", object.getImage());

                if (context.hasDualContent()) {
                    if (context instanceof MainActivity) {
                        ((MainActivity) context).getDetailFragment().setSelectedQuote(object);
                    }
                } else {
                    context.startActivity(intent);
                }

            }
        });

        holder.shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String textToSend = object.getText();

                if(object.getAuthor() != null && !object.getAuthor().isEmpty()){
                    textToSend += " -" + object.getAuthor();
                }


                textToSend += " (QuoteMe)";

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, textToSend);
                //sendIntent.putExtra(Intent.EXTRA_STREAM, QuoteProvider.IMAGE_URI + "/" + object.getImageURI());
                //sendIntent.putExtra(Intent.EXTRA_STREAM, object.getImage());
                //sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                //sendIntent.setType("image/*");

                context.startActivity(Intent.createChooser(sendIntent, "Share a quote"));
            }
        });

        if (object.getImage() == null) {
            loadImageFromDatabase(holder, object);
        }
        else{
            holder.image.setImageBitmap(object.getImage());
        }

        if (loadOnScroll && position >= getItemCount() - 1) {
            dbHelper.fetchImagesFromOnline();
            dbHelper.fetchQuotesFromOnline();
        }


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

    @Override
    public void onLoadOnlineQuotes() {

        if(this.getItemCount() <= 0) {
            dbHelper.fetchQuotesFromDB();
        }

        //notifyDataSetChanged();
    }

    @Override
    public void onDataFinished(ArrayList<QuoteObject> quotes) {
        setItems(quotes);
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        FloatingActionButton favoriteFab;

        FloatingActionButton deleteFab;

        ImageView image;

        TextView text;

        TextView author;

        ImageButton shareButton;

        View root;

        private GetImageFromDBAsyncTask task;

        public ViewHolder(View view) {
            super(view);

            root = view;

            image = (ImageView) view.findViewById(R.id.image);
            text = (TextView) view.findViewById(R.id.text);
            author = (TextView) view.findViewById(R.id.author);

            deleteFab = (FloatingActionButton) view.findViewById(R.id.delete_fab);
            favoriteFab = (FloatingActionButton) view.findViewById(R.id.favorite_fab);

            shareButton = (ImageButton) view.findViewById(R.id.share_button);
        }

        public void setTask(GetImageFromDBAsyncTask task) {
            this.task = task;
        }
    }




}