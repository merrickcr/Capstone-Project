package com.example.atv684.positivityreminders.schedules;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.atv684.positivityreminders.R;

import java.util.ArrayList;

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
            holder.deleteButton = (ImageButton) view.findViewById(R.id.delete_button);
            holder.days = (TextView) view.findViewById(R.id.days_of_week);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        ScheduleObject object = getItem(pos);

        holder.startTime.setText(ScheduleUtil.dateFormat.format(object.getStartTime()));

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteSchedule(getItem(finalPos));
            }
        });

        String daysString = "";

        for (int i = 0; i < object.getDaysJSONArray().length(); i++) {
            daysString += object.getDaysJSONArray().optString(i) + " ";
        }
        holder.days.setText(daysString);

        return view;
    }

    void deleteSchedule(ScheduleObject object) {
        arrayList.remove(object);

        ScheduleUtil.deleteSchedule(context, object);

        notifyDataSetChanged();

    }

    public class ViewHolder {

        public TextView days;

        TextView startTime;

        ImageButton deleteButton;
    }

}