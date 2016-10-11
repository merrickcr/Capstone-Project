package com.example.atv684.positivityreminders.Schedules;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by atv684 on 10/7/16.
 */
public class ScheduleUtil {

    public static SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");

    public static final String SCHEDULE_PREFERENCE = "schedules_pref";

    public static String getFrequencyLabel(int freq){
        if(freq < 33){
            return "LOW";
        } else if(freq < 66){
            return "MEDIUM";
        } else {
            return "HIGH";
        }
    }

    public static ArrayList<ScheduleObject> getSchedules(Context context) {

        ArrayList<ScheduleObject> schedules = new ArrayList<>();

        HashSet<String> stringSchedules = (HashSet<String>) PreferenceManager.getDefaultSharedPreferences(context).getStringSet
            (ScheduleUtil.SCHEDULE_PREFERENCE, new HashSet<String>());

        for (String s : stringSchedules) {
            schedules.add(new ScheduleObject(s));
        }

        return schedules;
    }

    public static void addSchedule(Context context, ScheduleObject schedule) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        HashSet<String> schedules = (HashSet)sharedPreferences.getStringSet(ScheduleUtil.SCHEDULE_PREFERENCE, new HashSet<String>());

        if(schedules != null){

            schedules.add(schedule.toJson());

            sharedPreferences.edit().putStringSet(ScheduleUtil.SCHEDULE_PREFERENCE, schedules).commit();
            Log.e("ADD", "Added schedule");

        }
    }

    public static void deleteSchedule(Context context, ScheduleObject object) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();

        HashSet<String> schedules = (HashSet) PreferenceManager.getDefaultSharedPreferences(context).getStringSet(ScheduleUtil.SCHEDULE_PREFERENCE, new
            HashSet<String>());

        for (String string : schedules) {
            ScheduleObject scheduleObject = new ScheduleObject(string);
            if (scheduleObject.id == object.id) {
                schedules.remove(string);
            }
        }

        editor.putStringSet(ScheduleUtil.SCHEDULE_PREFERENCE, schedules).commit();

        Log.e("deleted", "deleted schedule");
    }
}
