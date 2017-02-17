package com.example.atv684.positivityreminders.schedules;

import android.database.Cursor;
import android.util.Log;

import com.example.atv684.positivityreminders.provider.QuotesContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created Chris on 9/30/16.
 */
public class ScheduleObject {

    Date startTime;

    ArrayList<String> days;

    long id;

    public ScheduleObject() {

    }

    ScheduleObject(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);

            startTime = new Date(jsonObject.getString("startTime"));
            id = jsonObject.optInt("id");

            JSONArray daysArray = jsonObject.getJSONArray("days");

            for (int i = 0; i < daysArray.length(); i++) {
                addDay(daysArray.getString(i));
            }

        } catch (JSONException e) {
            Log.e("SCHEDULE OBJECT", e.getMessage());
        }

    }

    public ScheduleObject(Cursor c) {

        if (c.getCount() <= 0) {
            return;
        }

        id = c.getInt(c.getColumnIndex(QuotesContract.ScheduleEntry._ID));

        startTime = new Date(c.getString(c.getColumnIndex(QuotesContract.ScheduleEntry.COLUMN_TIME)));

        try {
            setDays(new JSONArray(c.getString(c.getColumnIndex(QuotesContract.ScheduleEntry.COLUMN_DAYS))));
        } catch (JSONException e) {
            Log.e("qbhelper", e.getMessage());
        }

        c.close();

    }

    public String toJson() {

        JSONObject object = new JSONObject();

        try {
            if (startTime != null) {
                object.put("startTime", startTime.toString());
                object.put("id", id);

                JSONArray daysArray = new JSONArray();

                for (String s : days) {
                    daysArray.put(s);
                }

                object.put("days", daysArray);
            }

        } catch (JSONException e) {
            return null;
        }

        return object.toString();

    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public void addDay(String day) {

        if (days == null) {
            days = new ArrayList<String>();
        }
        if (!days.contains(day)) {
            days.add(day);
        }
    }

    public void removeDay(String day) {
        days.remove(day);
    }

    public JSONArray getDaysJSONArray() {
        JSONArray daysArray = new JSONArray();

        for (String s : days) {
            daysArray.put(s);
        }

        return daysArray;
    }

    public void setDays(JSONArray array) {

        days = new ArrayList<String>();

        for (int i = 0; i < array.length(); i++) {
            days.add(array.optString(i));
        }

    }

    public ArrayList<String> getDays() {
        return days;
    }

    public void setDays(ArrayList<String> days) {
        this.days = days;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}

