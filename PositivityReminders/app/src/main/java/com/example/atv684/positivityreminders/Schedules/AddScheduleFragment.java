package com.example.atv684.positivityreminders.Schedules;

import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import com.example.atv684.positivityreminders.R;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

/**
 * Created by atv684 on 10/1/16.
 */
public class AddScheduleFragment extends Fragment {

    private Button startTimeButton;

    private Button endTimeButton;

    private Date endTime;

    private Date startTime;

    private SeekBar frequencySeekBar;

    private int frequencyValue;

    private TextView frequencyValueTextView;

    private Button addScheduleButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_schedule_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        Log.e("TAG", sharedPrefs.getStringSet(ScheduleUtil.SCHEDULE_PREFERENCE, new HashSet<String>()).toString());

        startTimeButton = (Button) view.findViewById(R.id.start_time_button);
        endTimeButton = (Button) view.findViewById(R.id.end_time_button);
        frequencySeekBar = (SeekBar) view.findViewById(R.id.frequency_seekbar);
        frequencyValueTextView = (TextView) view.findViewById(R.id.frequency_value_textview);
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

        endTimeButton = (Button) view.findViewById(R.id.end_time_button);

        endTimeButton.setOnClickListener(new View.OnClickListener() {
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
                        endTimeButton.setText(getResources().getString(R.string.end_time_colon) +  selectedHour + ":" + selectedMinute);
                        endTime = new Date();
                        endTime.setHours(selectedHour);
                        endTime.setMinutes(selectedMinute);
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle(getResources().getString(R.string.select_end_time));
                mTimePicker.show();

            }
        });

        frequencySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                frequencyValue = progress;

                handleFrequencyValueText();

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        addScheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewSchedule();
            }
        });
    }

    private void handleFrequencyValueText() {
        if(frequencyValue < 33){
            frequencyValueTextView.setText("Low");
        }
        else if(frequencyValue < 66){
            frequencyValueTextView.setText("Medium");
        }
        else{
            frequencyValueTextView.setText("High");
        }
    }

    private void addNewSchedule(){

        ScheduleObject schedule = new ScheduleObject();
        schedule.setStartTime(startTime);
        schedule.setEndTime(endTime);
        schedule.setFrequency(frequencyValue);

        ScheduleUtil.addSchedule(getContext(), schedule);

        Snackbar.make(getView(), "Added a schedule!", Snackbar.LENGTH_LONG).show();

    }
}
