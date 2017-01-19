package com.example.atv684.positivityreminders.Schedules;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.atv684.positivityreminders.MainActivity;
import com.example.atv684.positivityreminders.NotificationBroadcastReceiver;
import com.example.atv684.positivityreminders.R;
import com.example.atv684.positivityreminders.provider.QuoteDBHelper;
import com.example.atv684.positivityreminders.provider.QuotesContract;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

/**
 * Created by atv684 on 10/1/16.
 */
public class AddScheduleFragment extends Fragment {

    private Button startTimeButton;

    private Date startTime;


    private Button addScheduleButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_schedule_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        startTimeButton = (Button) view.findViewById(R.id.start_time_button);

        addScheduleButton = (Button) view.findViewById(R.id.add_schedule_button);

        startTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        startTime = new Date();
                        startTime.setHours(selectedHour);
                        startTime.setMinutes(selectedMinute);

                        startTimeButton.setText(getResources().getString(R.string.start_time_colon) +  selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle(getResources().getString(R.string.select_start_time));
                mTimePicker.show();

            }
        });


        addScheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewSchedule();
            }
        });
    }

    private void addNewSchedule(){

        ScheduleObject schedule = new ScheduleObject();
        schedule.setStartTime(startTime);
        schedule.setDays(QuotesContract.ScheduleEntry.FULL_WEEK);

        long id = new QuoteDBHelper(getContext(), null).addSchedule(schedule);

        schedule.setId(id);

        Snackbar.make(getView(), "Added a schedule!", Snackbar.LENGTH_LONG).show();

        NotificationScheduler scheduler = new NotificationScheduler(getContext());
        scheduler.scheduleNotification(schedule);

    }


}
