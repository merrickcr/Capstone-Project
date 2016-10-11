package com.example.atv684.positivityreminders.Schedules;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.atv684.positivityreminders.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by atv684 on 10/7/16.
 */
public class ScheduleListAdapter extends BaseAdapter {

    ArrayList<ScheduleObject> arrayList;

    LayoutInflater inflater;

    ViewHolder holder = null;

    Context context = null;

    public ScheduleListAdapter(Context context, ArrayList arrayList) {
        this.arrayList = arrayList;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public ScheduleObject getItem(int pos) {
        return arrayList.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(int pos, View view, ViewGroup parent) {

        final int finalPos = pos;
        if (view == null) {
            view = inflater.inflate(R.layout.view_schedule_item, parent, false);
            holder = new ViewHolder();
            holder.startTime = (TextView) view.findViewById(R.id.start_time_textview);
            holder.frequency = (TextView) view.findViewById(R.id.frequency_textview);
            holder.deleteButton = (ImageButton) view.findViewById(R.id.delete_button);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.startTime.setText(ScheduleUtil.dateFormat.format(getItem(pos).getStartTime()) + " to " + ScheduleUtil.dateFormat.format
            (getItem(pos).getEndTime()));
        holder.frequency.setText(ScheduleUtil.getFrequencyLabel(getItem(pos).getFrequency()));

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteSchedule(getItem(finalPos));
            }
        });

        return view;
    }


    public class ViewHolder {

        TextView startTime;

        TextView frequency;

        ImageButton deleteButton;
    }


    void deleteSchedule(ScheduleObject object) {
        arrayList.remove(object);

        ScheduleUtil.deleteSchedule(context, object);

        notifyDataSetChanged();

    }

}